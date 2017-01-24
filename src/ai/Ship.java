package ai;

public class Ship {

  private int x;
  private int y;
  private int rot;
  private double vx;
  private double vy;
  private double vrot;
  
  public Ship() {
    this(0, 0, 0);
  }
  
  public Ship(int x, int y, int rot) {
    this.x = x;
    this.y = y;
    this.rot = rot;
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }
  
  public void changeX(int dx) {
    x += dx;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }
  
  public void changeY(int dy) {
    y += dy;
  }

  public int getRot() {
    return rot;
  }

  public void setRot(int rot) {
    this.rot = rot;
  }
  
  public void changeRot(int drot) {
    rot += drot;
  }

  public double getVX() {
    return vx;
  }

  public void setVX(double vx) {
    this.vx = vx;
  }
  
  public void changeVX(double dvx) {
    vx += dvx;
  }

  public double getVY() {
    return vy;
  }

  public void setVY(double vy) {
    this.vy = vy;
  }
  
  public void changeVY(double dvy) {
    vy += dvy;
  }
  
  public double getVRot() {
    return vrot;
  }
  
  public void setVRot(double vrot) {
    this.vrot = vrot;
  }
  
  public void changeVRot(double dvrot) {
    vrot += dvrot;
  }
  
  public void updatePos() {
    x += vx;
    y += vy;
    rot += vrot;
  }
}
