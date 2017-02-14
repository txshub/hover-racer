package ai;

import org.joml.Vector2f;

/**
 * 
 * @author Reece Bennett
 *
 */
public class Ship {

  protected Vector2f pos;
  protected Vector2f vel;
  protected Vector2f acl;
  protected double accel;
  protected double rot;
  protected double rotV;
  
  public static float maxAcceleration = 0.1f;
  public static float maxTurnSpeed = 5;
  
  public Ship() {
    this(0, 0, 0);
  }
  
  public Ship(float x, float y, float rot) {
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
    rot = (rot + rotV);
    if (rot >= 360) {
      rot = rot % 360;
    } else if (rot < 0) {
      rot = 360 + rot;
    }
    
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
