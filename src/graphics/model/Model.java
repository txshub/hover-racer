package graphics.model;

import static org.lwjgl.opengl.GL15.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

public class Model {
  
  private int vbo;
  private int ibo;
  private int size;
  
  public Model() {
    vbo = glGenBuffers();
    ibo = glGenBuffers();
    size = 0;
  }
  
  public void bufferVertices(Vertex[] vertices, int[] indices) {
    FloatBuffer buffer = BufferUtils.createFloatBuffer(vertices.length * Vertex.SIZE);
    
    for (Vertex vertex : vertices) {
      buffer.put(vertex.getPos().x);
      buffer.put(vertex.getPos().y);
      buffer.put(vertex.getPos().z);
    }
    
    buffer.flip();
    
    glBindBuffer(GL_ARRAY_BUFFER, vbo);
    glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
    glBindBuffer(GL_ARRAY_BUFFER, 0);
    
    IntBuffer ibuffer = BufferUtils.createIntBuffer(indices.length);
    ibuffer.put(indices);
    ibuffer.flip();
    
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, ibuffer, GL_STATIC_DRAW);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    
    size = indices.length;
  }
  
  public int getIBO() {
    return ibo;
  }
  
  public int getVBO() {
    return vbo;
  }
  
  public int getSize() {
    return size;
  }

}
