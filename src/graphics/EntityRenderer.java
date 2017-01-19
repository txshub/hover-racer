package graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import java.util.ArrayList;
import java.util.HashMap;

import entity.Entity;
import graphics.model.Model;
import graphics.model.Vertex;
import graphics.shader.BasicShader;
import math.Transform;

public class EntityRenderer {
  
  private BasicShader shader;
  
  public EntityRenderer(BasicShader shader) {
    this.shader = shader;
  }
  
  /**
   * Loads the model information.
   * 
   * @param model
   */
  private void loadModel(Model model) {
    glBindBuffer(GL_ARRAY_BUFFER, model.getVBO());
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, model.getIBO());
    
    glEnableVertexAttribArray(0);
    glEnableVertexAttribArray(1);
    
    glVertexAttribPointer(0, 3, GL_FLOAT, false, Vertex.SIZE * 4, 0);
    glVertexAttribPointer(1, 3, GL_FLOAT, false, Vertex.SIZE * 4, 12);
  }

  /**
   * Loads position and rotation for a specific entity, updating the
   * world matrix.
   * 
   * @param entity
   */
  private void loadInstance(Entity entity) {
    shader.updateWorldMatrix(Transform.getTransformation(entity.getPos(), entity.getRot().x, entity.getRot().y, entity.getRot().z, entity.getScale()));
  }
  
  private void unloadModel() {
    glDisableVertexAttribArray(0);
    glDisableVertexAttribArray(1);
    
    glBindBuffer(GL_ARRAY_BUFFER, 0);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
  }

  public void render(HashMap<Model, ArrayList<Entity>> entities) {
    for (Model model : entities.keySet()) {
      loadModel(model);
      
      for (Entity entity : entities.get(model)) {
        loadInstance(entity);
        glDrawElements(GL_TRIANGLES, model.getVertexCount(), GL_UNSIGNED_INT, 0);
      }
      
      unloadModel();
    }
  }

}
