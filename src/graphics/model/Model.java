package graphics.model;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;

import graphics.texture.Texture;

public class Model {
  
  private int vertexCount;
  
  private int vaoID;
  private int vboID;
  private int iboID;
  private int cboID;
  private int tboID;
  
  private Texture texture;
  
  public Model(Texture texture) {
    vertexCount = 0;
    vaoID = glGenVertexArrays();
    vboID = glGenBuffers();
    iboID = glGenBuffers();
    cboID = glGenBuffers();
    tboID = glGenBuffers();
    
    this.texture = texture;
  }
  
//  public void bufferVertices(Vertex[] vertices, int[] indices) {
//    bufferVertices(vertices, indices, false);
//  }
  
  public void bufferVertices(Vertex[] vertices, int[] indices, float[] texCoords) {
    vertexCount = indices.length;
    
    glBindVertexArray(vaoID);
    
    // Vertex VBO
    FloatBuffer vBuffer = BufferUtils.createFloatBuffer(vertices.length * Vertex.SIZE);
    for (Vertex vertex : vertices) {
      vBuffer.put(vertex.getPos().x);
      vBuffer.put(vertex.getPos().y);
      vBuffer.put(vertex.getPos().z);
    }
    vBuffer.flip();
    
    glBindBuffer(GL_ARRAY_BUFFER, vboID);
    glBufferData(GL_ARRAY_BUFFER, vBuffer, GL_STATIC_DRAW);
    glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
    
    // Index VBO
    IntBuffer iBuffer = BufferUtils.createIntBuffer(indices.length);
    iBuffer.put(indices);
    iBuffer.flip();
    
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboID);
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, iBuffer, GL_STATIC_DRAW);
    
    // Colour VBO
//    FloatBuffer cBuffer = BufferUtils.createFloatBuffer(colors.length);
//    cBuffer.put(colors);
//    cBuffer.flip();
//    
//    glBindBuffer(GL_ARRAY_BUFFER, cboID);
//    glBufferData(GL_ARRAY_BUFFER, cBuffer, GL_STATIC_DRAW);
//    glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
    
    // Texture coordinates VBO
    FloatBuffer tBuffer = BufferUtils.createFloatBuffer(texCoords.length);
    tBuffer.put(texCoords);
    tBuffer.flip();
    
    glBindBuffer(GL_ARRAY_BUFFER, tboID);
    glBufferData(GL_ARRAY_BUFFER, tBuffer, GL_STATIC_DRAW);
    glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
    
    // Unbind buffers
    glBindBuffer(GL_ARRAY_BUFFER, 0);
    glBindVertexArray(0);
  }
  
  public int getVertexCount() {
    return vertexCount;
  }
  
  public int getVAO() {
    return vaoID;
  }

  public Texture getTexture() {
    return texture;
  }

  public static Model loadModel(String fileName, Texture texture) {
    Model res = new Model(texture);
    ArrayList<Vertex> vertices = new ArrayList<>();
    ArrayList<Integer> indices = new ArrayList<>();
    
    BufferedReader reader = null;
    
    // Read the model file
    try {
      
      reader = new BufferedReader(new FileReader("./res/models/" + fileName));
      
      String line;
      while ((line = reader.readLine()) != null) {
        
        if (line.startsWith("v ")) {
          String[] parts = line.split(" ");
          vertices.add(new Vertex(Float.parseFloat(parts[1]), Float.parseFloat(parts[2]), Float.parseFloat(parts[3])));
        
        } else if (line.startsWith("f ")) {
          String[] parts = line.split(" ");
          // OBJ files increment their indices by 1 so -1 to get the index for openGL
          indices.add(Integer.parseInt(parts[1]) - 1);
          indices.add(Integer.parseInt(parts[2]) - 1);
          indices.add(Integer.parseInt(parts[3]) - 1);
        }
        
      }
      
      reader.close();
      
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
    
    Vertex[] v = new Vertex[vertices.size()];
    for (int n = 0; n < v.length; n++) v[n] = vertices.get(n);

    int[] i = new int[indices.size()];
    for (int n = 0; n < i.length; n++) i[n] = indices.get(n);
    
//    res.bufferVertices(v, i, true);
    
    return res;
  }
  
//  private void calcNormals(Vertex[] vertices, int[] indices) {
//    // We use 3 indices each step so increment += 3
//    for (int i = 0; i < indices.length; i += 3) {
//      int i0 = indices[i];
//      int i1 = indices[i+1];
//      int i2 = indices[i+2];
//      
//      // Calculate the vertex normals
//      Vector3f v1 = vertices[i1].getPos().sub(vertices[i0].getPos());
//      Vector3f v2 = vertices[i2].getPos().sub(vertices[i0].getPos());
//      
//      // Cross these to get the face normal
//      Vector3f normal = v1.cross(v2).normalized();
//      
//      vertices[i0].setNormal(vertices[i0].getNormal().add(normal));
//      vertices[i1].setNormal(vertices[i1].getNormal().add(normal));
//      vertices[i2].setNormal(vertices[i2].getNormal().add(normal));
//    }
//    
//    for (int i = 0; i < vertices.length; i++) {
//      vertices[i].setNormal(vertices[i].getNormal().normalized());
//    }
//  }
  
  /**
   * Free up buffers used by the model
   */
  public void cleanUp() {
    glDisableVertexAttribArray(0);
    
    // Delete VBOs
    glBindBuffer(GL_ARRAY_BUFFER, 0);
    glDeleteBuffers(vboID);
    glDeleteBuffers(iboID);
    glDeleteBuffers(cboID);
    
    // Delete VAO
    glBindVertexArray(0);
    glDeleteVertexArrays(vaoID);
  }

}
