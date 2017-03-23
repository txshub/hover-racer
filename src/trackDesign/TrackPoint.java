package trackDesign;

import java.awt.Graphics2D;

import org.joml.Vector2f;

/**
 * This class exists in order to have a sortable point, as well as a distance measure
 * 
 * @author simon
 *
 */
public class TrackPoint extends Vector2f implements Comparable<TrackPoint> {

  float width = 200f;

  /**
   * Creates a new TrackPoint object
   * 
   * @param x
   *          X coordinate of the point
   * @param y
   *          Y coordinate of the point
   */
  public TrackPoint(float x, float y) {
    super(x, y);
  }

  /**
   * Returns the X coordinate of this point
   * 
   * @return The X coordinate of this point
   */
  public float getX() {
    return x;
  }

  /**
   * Returns the Y coordinate of this point
   * 
   * @return The Y coordinate of this point
   */
  public float getY() {
    return y;
  }

  /**
   * Returns the width of the track at this point
   * 
   * @return The width of the track at this point
   */
  public float getWidth() {
    return width;
  }

  /**
   * Sets a new X coordinate for this point
   * 
   * @param x
   *          New X coordinate for this point
   */
  public void setX(float x) {
    this.x = x;
  }

  /**
   * Sets a new Y coordinate for this point
   * 
   * @param y
   *          New Y coordinate for this point
   */
  public void setY(float y) {
    this.y = y;
  }

  /**
   * Sets a new width for this point
   * 
   * @param width
   *          New width for this point
   */
  public void setWidth(float width) {
    this.width = width;
  }

  /**
   * Returns the distance between this and point p
   * 
   * @param p
   *          The point to measure distance to
   * @return The distance between this and point p
   */
  @Deprecated
  public float dist(TrackPoint p) {
    float dx = this.x - p.getX();
    float dy = this.y - p.getY();
    return (float) Math.sqrt((dx * dx) + (dy * dy));
  }

  @Override
  public int compareTo(TrackPoint p) {
    if (this.x == p.x) {
      return (int) (this.y - p.y); // If x is the same, return the
      // difference in
      // Y
    } else {
      return (int) (this.x - p.x);
    }
  }

  /**
   * Returns whether 2 given TrackPoint objects are equal
   * 
   * @param p
   *          The other TrackPoint object to compare with
   * @return Whether 2 given TrackPoint objects are equal
   */
  public boolean equals(TrackPoint p) {
    return ((this.x == p.x) && (this.y == p.y));
  }

  @Override
  public String toString() {
    return "(" + x + ", " + y + ")";
  }

  public void draw(Graphics2D g) {
    g.drawOval((int) x * 2, (int) y * 2, 2, 2);
  }

}