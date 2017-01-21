package graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.joml.Matrix4f;

import camera.Camera;
import entity.Entity;
import graphics.model.Model;
import graphics.shader.BasicShader2;

public class EntityRenderer {
  
  private BasicShader2 shader;
  private Transformation transform;
  
  public EntityRenderer(BasicShader2 shader) {
    this.shader = shader;
    this.transform = new Transformation();
  }

  public void render(HashMap<Model, ArrayList<Entity>> entities, Camera camera) {
    // Update the view Matrix
    Matrix4f viewMatrix = transform.getViewMatrix(camera);
    
    shader.updateTextureSampler(0);
    
    for (Model model : entities.keySet()) {
      glBindVertexArray(model.getVAO());
      glEnableVertexAttribArray(0);
      glEnableVertexAttribArray(1);
      glEnableVertexAttribArray(2);
      
      shader.setColor(model.getColor());
      shader.setUseColor(model.isTextured());
      
      if (model.getTexture() != null) {
        // Activate the first texture unit and bind to it
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, model.getTexture().getID());
      }
      
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
