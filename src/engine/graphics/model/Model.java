package engine.graphics.model;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;

public class Model {
  
  private int vertexCount;
  
  private int vaoID;
  private ArrayList<Integer> vboList;

  private Material material;
  
  public Model(float[] vertices, int[] indices, float[] normals, float[] texCoords) {
    vertexCount = 0;
    
    vaoID = glGenVertexArrays();
    vboList = new ArrayList<>();
    
    bufferVertices(vertices, indices, normals, texCoords);
  }
  
//  public void bufferVertices(Vertex[] vertices, int[] indices) {
//    bufferVertices(vertices, indices, false);
//  }
  
  public void bufferVertices(float[] vertices, int[] indices, float[] normals, float[] texCoords) {
    vertexCount = indices.length;
    
    glBindVertexArray(vaoID);
    
    // Vertex VBO
    int vboID = glGenBuffers();
    vboList.add(vboID);
    FloatBuffer vBuffer = BufferUtils.createFloatBuffer(vertices.length * Vertex.SIZE);
    vBuffer.put(vertices);
    vBuffer.flip();
    
    glBindBuffer(GL_ARRAY_BUFFER, vboID);
    glBufferData(GL_ARRAY_BUFFER, vBuffer, GL_STATIC_DRAW);
    glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
    
    // Index VBO
    vboID = glGenBuffers();
    vboList.add(vboID);
    IntBuffer iBuffer = BufferUtils.createIntBuffer(indices.length);
    iBuffer.put(indices);
    iBuffer.flip();
    
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID);
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, iBuffer, GL_STATIC_DRAW);
    
    // Vertex normals VBO
    vboID = glGenBuffers();
    vboList.add(vboID);
    FloatBuffer nBuffer = BufferUtils.createFloatBuffer(normals.length);
    nBuffer.put(normals);
    nBuffer.flip();
    
    glBindBuffer(GL_ARRAY_BUFFER, vboID);
    glBufferData(GL_ARRAY_BUFFER, nBuffer, GL_STATIC_DRAW);
    glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);
    
    // Colour VBO
//    vboID = glGenBuffers();
//    FloatBuffer cBuffer = BufferUtils.createFloatBuffer(colors.length);
//    cBuffer.put(colors);
//    cBuffer.flip();
//    
//    glBindBuffer(GL_ARRAY_BUFFER, vboID);
//    glBufferData(GL_ARRAY_BUFFER, cBuffer, GL_STATIC_DRAW);
//    glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
    
    // Texture coordinates VBO
    vboID = glGenBuffers();
    vboList.add(vboID);
    FloatBuffer tBuffer = BufferUtils.createFloatBuffer(texCoords.length);
    tBuffer.put(texCoords);
    tBuffer.flip();
    
    glBindBuffer(GL_ARRAY_BUFFER, vboID);
    glBufferData(GL_ARRAY_BUFFER, tBuffer, GL_STATIC_DRAW);
    glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
    
    // Unbind buffers
    glBindBuffer(GL_ARRAY_BUFFER, 0);
    glBindVertexArray(0);
  }
  
  public Material getMaterial() {
    return material;
  }

  public void setMaterial(Material material) {
    this.material = material;
  }

  public int getVAO() {
    return vaoID;
  }

  public int getVertexCount() {
    return vertexCount;
  }
    
  /**
   * Free up buffers used by the model
   */
  public void cleanUp() {
    glDisableVertexAttribArray(0);
    
    // Delete VBOs
    glBindBuffer(GL_ARRAY_BUFFER, 0);
    for (int vboID : vboList) {
      glDeleteBuffers(vboID);
    }
    
    // Delete VAO
    glBindVertexArray(0);
    glDeleteVertexArrays(vaoID);
  }

}
