package graphics.model;

import math.Vector3f;

public class Vertex {

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
