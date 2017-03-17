package trackDesign.catmullrom;

/**
 * @author Simon Adapted From
 *         http://hawkesy.blogspot.co.uk/2010/05/catmull-rom-spline-curve-implementation.html
 * 
 *         Used to get further points between two points for smoother curves
 */
public class Spline {

  private float p0, p1, p2, p3;

  /**
   * Creates a spline object
   * 
   * @param p0
   *          The point before the two in question
   * @param p1
   *          The first point of the two in question
   * @param p2
   *          The second point of the two in question
   * @param p3
   *          The point after the two in question
   */
  public Spline(float p0, float p1, float p2, float p3) {
    this.p0 = p0;
    this.p1 = p1;
    this.p2 = p2;
    this.p3 = p3;
  }

  /**
   * Returns a new trackpoint a specified proportion t between p1 and p2
   * 
   * @param t
   *          The specified proportion through the turn
   * @return
   */
  public float q(float t) {
    return 0.5f * ((2 * p1) + (p2 - p0) * t + (2 * p0 - 5 * p1 + 4 * p2 - p3) * t * t
        + (3 * p1 - p0 - 3 * p2 + p3) * t * t * t);
  }

  /**
   * Returns p0
   * 
   * @return p0
   */
  public float getP0() {
    return p0;
  }

  /**
   * Sets p0
   * 
   * @param p0
   *          The new p0
   */
  public void setP0(float p0) {
    this.p0 = p0;
  }

  /**
   * Returns p1
   * 
   * @return p1
   */
  public float getP1() {
    return p1;
  }

  /**
   * Sets p1
   * 
   * @param p1
   *          The new p1
   */
  public void setP1(float p1) {
    this.p1 = p1;
  }

  /**
   * Returns p2
   * 
   * @return p2
   */
  public float getP2() {
    return p2;
  }

  /**
   * Sets p2
   * 
   * @param p2
   *          The new p2
   */
  public void setP2(float p2) {
    this.p2 = p2;
  }

  /**
   * Returns p3
   * 
   * @return p3
   */
  public float getP3() {
    return p3;
  }

  /**
   * Sets p3
   * 
   * @param p3
   *          The new p3
   */
  public void setP3(float p3) {
    this.p3 = p3;
  }

}
