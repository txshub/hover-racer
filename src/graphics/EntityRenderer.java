package graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.joml.Matrix4f;

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

  public void render(HashMap<Model, ArrayList<Entity>> entities) {
    for (Model model : entities.keySet()) {
      glBindVertexArray(model.getVAO());
      glEnableVertexAttribArray(0);
      glEnableVertexAttribArray(1);
      
      // Activate the first texture unit and bind to it
      glActiveTexture(GL_TEXTURE0);
      glBindTexture(GL_TEXTURE_2D, model.getTexture().getID());
      
      for (Entity entity : entities.get(model)) {
        // Set the world matrix for this entity
        Matrix4f worldMatrix = 
            transform.getWorldMatrix(
                entity.getPosition(), 
                entity.getRotation(), 
                entity.getScale());
        shader.updateWorldMatrix(worldMatrix);
        
        glDrawElements(GL_TRIANGLES, model.getVertexCount(), GL_UNSIGNED_INT, 0);
      }

      glDisableVertexAttribArray(0);
      glDisableVertexAttribArray(1);
      glBindVertexArray(0);
    }
  }

}
