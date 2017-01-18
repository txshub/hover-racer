package graphics.model;

import static org.lwjgl.opengl.GL15.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

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
    
    res.bufferVertices(v, i);
    
    return res;
  }

}
