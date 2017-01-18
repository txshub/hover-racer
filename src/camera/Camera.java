package camera;

import math.Vector3f;

public class Camera {
  
  private Vector3f pos;
  
  public Camera() {
    this(new Vector3f());
  }
  
  public Camera(Vector3f pos) {
    this.pos = pos;
  }
  
  public void move(float dx, float dy, float dz) {
    pos.x += dx;
    pos.y += dy;
    pos.z += dz;
  }

  public Vector3f getPos() {
    return pos;
  }

  public void setPos(Vector3f pos) {
    this.pos = pos;
  }
  
}
