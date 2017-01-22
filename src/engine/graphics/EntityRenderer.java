package engine.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import engine.camera.Camera;
import engine.entity.Entity;
import engine.graphics.model.Model;
import engine.graphics.model.Texture;
import engine.graphics.shader.BasicShader2;
import engine.graphics.shader.PointLight;

public class EntityRenderer {
  
  private BasicShader2 shader;
  private Transformation transform;
  
  public EntityRenderer(BasicShader2 shader) {
    this.shader = shader;
    this.transform = new Transformation();
  }

  public void render(HashMap<Model, ArrayList<Entity>> entities, Camera camera, 
      Vector3f ambientLight, PointLight pointLight) {
    
    // Update the view Matrix
    Matrix4f viewMatrix = transform.getViewMatrix(camera);
    
    // Update light uniforms
    shader.updateAmbientLight(ambientLight);
    shader.updateSpecularPower(10f);
    
    // Get a copy of the light object and transform its position to view coordinates
    PointLight currPointLight = new PointLight(pointLight);
    Vector3f lightPos = currPointLight.getPosition();
    Vector4f aux = new Vector4f(lightPos, 1);
    aux.mul(viewMatrix);
    lightPos.x = aux.x;
    lightPos.y = aux.y;
    lightPos.z = aux.z;
    shader.updatePointLight(currPointLight);
    
    shader.updateTextureSampler(0);
    
    for (Model model : entities.keySet()) {
      Texture texture = model.getMaterial().getTexture();
      if (texture != null) {
        // Activate the first texture unit and bind the texture to it
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture.getID());
      }
      
      shader.updateMaterial(model.getMaterial());
      
      glBindVertexArray(model.getVAO());
      glEnableVertexAttribArray(0);
      glEnableVertexAttribArray(1);
      glEnableVertexAttribArray(2);
      
      for (Entity entity : entities.get(model)) {
        // Set the model view matrix for this entity
        Matrix4f modelViewMatrix = transform.getModelViewMatrix(entity, viewMatrix);
        shader.updateModelViewMatrix(modelViewMatrix);
        
        glDrawElements(GL_TRIANGLES, model.getVertexCount(), GL_UNSIGNED_INT, 0);
      }

      // Restore state
      glDisableVertexAttribArray(0);
      glDisableVertexAttribArray(1);
      glDisableVertexAttribArray(2);
      glBindVertexArray(0);
      glBindTexture(GL_TEXTURE_2D, 0);
    }
  }

}
