package trackDesign.catmullrom;

import trackDesign.TrackPoint;

/**
 * @author Simon Adapted From
 *         http://hawkesy.blogspot.co.uk/2010/05/catmull-rom-spline-curve-implementation.html
 * 
 *
 */
public class Spline2D {
  private Spline splineXs, splineYs;
  private TrackPoint p1, p2;

  /**
   * Creates a Spline2D object
   * @param p0 The point before the two in question
   * @param p1 The first of the two points in question
   * @param p2 The second of the two points in question
   * @param p3 The point after the two in question
   */
  public Spline2D(TrackPoint p0, TrackPoint p1, TrackPoint p2, TrackPoint p3) {
    splineXs = new Spline(p0.getX(), p1.getX(), p2.getX(), p3.getX());
    splineYs = new Spline(p0.getY(), p1.getY(), p2.getY(), p3.getY());
    this.p1 = p1;
    this.p2 = p2;
  }

  /**
   * Returns the coordinates of the point proportion t between p1 & p2 
   * @param t The proportion between the two points
   * @return The coordinates of the point proportion t between p1 & p2
   */
  public TrackPoint q(float t) {
    TrackPoint p = new TrackPoint(splineXs.q(t), splineYs.q(t));
    float dif = (p2.getWidth() - p1.getWidth()) * t;
    p.setWidth(p1.getWidth() - dif);
    return p;
  }
}
