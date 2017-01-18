package graphics.model;

import math.Vector3f;

public class Vertex {

  public static final int SIZE = 6;
  
  private Vector3f pos;
  private Vector3f normal;
  
  public Vertex(float x, float y, float z, Vector3f normal) {
    this(new Vector3f(x, y, z), normal);
  }
  
  public Vertex(Vector3f pos, Vector3f normal) {
    this.setPos(pos);
    this.normal = normal;
  }

  public Vector3f getPos() {
    return pos;
  }

  public void setPos(Vector3f pos) {
    this.pos = pos;
  }

  public Vector3f getNormal() {
    return normal;
  }

  public void setNormal(Vector3f normal) {
    this.normal = normal;
  }
  
}
