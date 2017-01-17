package graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import graphics.model.Model;
import graphics.model.Vertex;
import graphics.shader.BasicShader;
import graphics.shader.Shader;
import math.Transform;
import math.Vector3f;

public class MasterRenderer {
  
  private BasicShader basicShader;
  private Model model;

  public MasterRenderer() {
    init();
    
    basicShader = new BasicShader();
    model = new Model();
    
    Vertex[] vertices = {
        new Vertex(-0.5f, -0.5f, -0.5f),
        new Vertex( 0.5f, -0.5f, -0.5f),
        new Vertex( 0.5f, -0.5f,  0.5f),
        new Vertex(-0.5f, -0.5f,  0.5f),
        new Vertex(-0.5f,  0.5f, -0.5f),
        new Vertex( 0.5f,  0.5f, -0.5f),
        new Vertex( 0.5f,  0.5f,  0.5f),
        new Vertex(-0.5f,  0.5f,  0.5f)
    };
    
    int[] indices = { 
        0,1,2, 
        2,3,0,
        
        0,1,5,
        5,4,0,
        
        1,2,6,
        6,5,1,
        
        2,3,7,
        7,6,2,
        
        3,0,4,
        4,7,3
    };
    
    model.bufferVertices(vertices, indices);
  }
  
  private void init() {
    glEnable(GL_DEPTH_TEST);
  }
  
  private void prepare() {
    glClearColor(0.5f, 0.3f, 1f, 1f);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
  }
  
  public void render() {
    prepare();
    
    basicShader.bind();
    basicShader.updateWorldMatrix(Transform.getTransformation(new Vector3f(0f, 0f, 0f), 0f, 0f, 0f, 1f));
    
    glBindBuffer(GL_ARRAY_BUFFER, model.getVBO());
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, model.getIBO());
    
    glEnableVertexAttribArray(0);
    glVertexAttribPointer(0, 3, GL_FLOAT, false, Vertex.SIZE*4, 0);
    
    glDrawElements(GL_TRIANGLES, model.getSize(), GL_UNSIGNED_INT, 0);
    
    glDisableVertexAttribArray(0);
    glBindBuffer(GL_ARRAY_BUFFER, 0);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    
    Shader.unbind();
  }
  
  public void cleanUp() {
    
  }
  
}
