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
  
  public void render(HashMap<Model, ArrayList<Entity>> entities) {
    for (Model model : entities.keySet()) {
      loadModel(model);
      
      for (Entity entity : entities.get(model)) {
        loadInstance(entity);
        glDrawElements(GL_TRIANGLES, model.getSize(), GL_UNSIGNED_INT, 0);
      }
      
      unloadModel();
    }
  }
  
  private void loadModel(Model model) {
    glBindBuffer(GL_ARRAY_BUFFER, model.getVBO());
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, model.getIBO());
    
    glEnableVertexAttribArray(0);
    glEnableVertexAttribArray(1);
    
    glVertexAttribPointer(0, 3, GL_FLOAT, false, Vertex.SIZE * 4, 0);
    glVertexAttribPointer(1, 3, GL_FLOAT, false, Vertex.SIZE * 4, 12);
  }
  
  private void unloadModel() {
    glDisableVertexAttribArray(0);
    glDisableVertexAttribArray(1);
    
    glBindBuffer(GL_ARRAY_BUFFER, 0);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
  }
  
  private void loadInstance(Entity entity) {
    shader.updateWorldMatrix(Transform.getTransformation(entity.getPos(), 0.78f, 0.78f, 0.78f, 1f));
  }

}
