package camera;

import math.Vector3f;

public class Camera {
  
  private static final Vector3f yAxis = new Vector3f(0f, 1f, 0f);
  
  private Vector3f pos;
  private Vector3f forward;
  private Vector3f up;
  
  public Camera() {
    this(new Vector3f());
  }
  
  public Camera(Vector3f pos) {
    this.pos = pos;
    forward = new Vector3f(0f, 0f, 1f);
    up = new Vector3f(0f, 1f, 0f);
  }
  
  public void move(float dx, float dy, float dz) {
    // Axes relative to the forward axis of the camera
    Vector3f relXAxis = yAxis.cross(forward).normalized();
    Vector3f relZAxis = forward.normalized();
    
    // Convert the relative movement to the global space
    float x = relXAxis.x * dx + relZAxis.x * dz;
    float y = dy;
    float z = relXAxis.z * dx + relZAxis.z * dz;
    
    pos.x += x;
    pos.y += y;
    pos.z += z;
  }
  
  public void rotateY(float angle) {
    Vector3f horAxis = yAxis.cross(forward.normalized());
    forward = forward.rotate(yAxis, angle);
    up = forward.cross(horAxis).normalized();
  }
  
  public void rotateX(float angle) {
    Vector3f horAxis = yAxis.cross(forward.normalized());
    forward = forward.rotate(horAxis, angle);
    up = forward.cross(horAxis).normalized();
  }

  public Vector3f getForward() {
    return forward;
  }

  public Vector3f getPos() {
    return pos;
  }

  public void setPos(Vector3f pos) {
    this.pos = pos;
  }

  public Vector3f getUp() {
    return up;
  }
  
}
