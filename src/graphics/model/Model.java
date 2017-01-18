package graphics.model;

import static org.lwjgl.opengl.GL15.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;

import math.Vector3f;

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
    bufferVertices(vertices, indices, false);
  }
  
  public void bufferVertices(Vertex[] vertices, int[] indices, boolean calcNormals) {
    if (calcNormals) calcNormals(vertices, indices);
    
    FloatBuffer buffer = BufferUtils.createFloatBuffer(vertices.length * Vertex.SIZE);
    
    for (Vertex vertex : vertices) {
      buffer.put(vertex.getPos().x);
      buffer.put(vertex.getPos().y);
      buffer.put(vertex.getPos().z);

      buffer.put(vertex.getNormal().x);
      buffer.put(vertex.getNormal().y);
      buffer.put(vertex.getNormal().z);
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
  
  public static Model loadModel(String fileName) {
    Model res = new Model();
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
          vertices.add(new Vertex(Float.parseFloat(parts[1]), Float.parseFloat(parts[2]), Float.parseFloat(parts[3]), new Vector3f()));
        
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
    
    res.bufferVertices(v, i, true);
    
    return res;
  }
  
  private void calcNormals(Vertex[] vertices, int[] indices) {
    // We use 3 indices each step so increment += 3
    for (int i = 0; i < indices.length; i += 3) {
      int i0 = indices[i];
      int i1 = indices[i+1];
      int i2 = indices[i+2];
      
      // Calculate the vertex normals
      Vector3f v1 = vertices[i1].getPos().sub(vertices[i0].getPos());
      Vector3f v2 = vertices[i2].getPos().sub(vertices[i0].getPos());
      
      // Cross these to get the face normal
      Vector3f normal = v1.cross(v2).normalized();
      
      vertices[i0].setNormal(vertices[i0].getNormal().add(normal));
      vertices[i1].setNormal(vertices[i1].getNormal().add(normal));
      vertices[i2].setNormal(vertices[i2].getNormal().add(normal));
    }
    
    for (int i = 0; i < vertices.length; i++) {
      vertices[i].setNormal(vertices[i].getNormal().normalized());
    }
  }

}
