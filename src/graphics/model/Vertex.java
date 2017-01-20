package graphics.model;

import org.joml.Vector3f;

public class Vertex {

  /**
   * The number of floats in the vertex (3 for position)
   */
  public static final int SIZE = 3;
  
  private Vector3f pos;
  
  public Vertex(float x, float y, float z) {
    this(new Vector3f(x, y, z));
  }
  
  public Vertex(Vector3f pos) {
    this.setPos(pos);
  }

  public Vector3f getPos() {
    return pos;
  }

  public void setPos(Vector3f pos) {
    this.pos = pos;
  }
  
}
