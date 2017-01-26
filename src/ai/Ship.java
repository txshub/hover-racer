package ai;

import org.lwjgl.util.vector.Vector2f;

public class Ship {

  private Vector2f pos;
  private Vector2f vel;
  private Vector2f acl;
  private double accel;
  private double rot;
  private double rotV;
  
  public Ship() {
    this(0, 0, 0);
  }
  
  public Ship(int x, int y, int rot) {
    pos = new Vector2f(x, y);
    vel = new Vector2f();
    acl = new Vector2f();
    accel = 0;
    this.rot = rot;
    rotV = 0;
  }
  
  public Vector2f getPos() {
    return pos;
  }
  
  public void setPos(Vector2f pos) {
    this.pos = pos;
  }
  
  public void setPos(float x, float y) {
    pos.x = x;
    pos.y = y;
  }
  
  public Vector2f getVel() {
    return vel;
  }
  
  public Vector2f getAcl() {
    return acl;
  }
  
  public double getAccel() {
    return accel;
  }
  
  public void setAccel(double accel) {
    this.accel = accel;
  }

  public double getRot() {
    return rot;
  }

  public void setRot(double rot) {
    this.rot = rot;
  }

  public double getRotV() {
    return rotV;
  }
  
  public void setRotV(double rotV) {
    this.rotV = rotV;
  }
  
  public void updatePos() {
    rot = (rot + rotV) % 360;
    
    acl.x = (float) (accel * Math.sin(Math.toRadians(rot)));
    acl.y = (float) (accel * -Math.cos(Math.toRadians(rot)));
    
    // Basic drag
    double b = 0.05;
    acl.x -= b * vel.x;
    acl.y -= b * vel.y;
    
    vel.x += acl.x;
    vel.y += acl.y;
    
    pos.x += vel.x;
    pos.y += vel.y;
  }
}
