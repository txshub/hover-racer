package trackDesign;
import java.awt.geom.Ellipse2D;
import java.awt.Graphics2D;
import java.awt.Color;
/**
 * This class exists in order to have a sortable point, as well as a square distance measure
 * @author sxw588
 *
 */
public class TrackPoint implements Comparable<TrackPoint>{
<<<<<<< HEAD
	private float x,y;
=======
	private int x,y;
>>>>>>> f37925c46b61b7de7344634a6046994d80e41aaf
	/**
	 * Creates a new TrackPoint object
	 * @param x X coordinate of the point
	 * @param y Y coordinate of the point
	 */
<<<<<<< HEAD
	public TrackPoint(float x, float y) {
=======
	public TrackPoint(int x, int y) {
		System.out.println("New point at " + x + ", " + y);
>>>>>>> f37925c46b61b7de7344634a6046994d80e41aaf
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Returns the X coordinate of this point
	 * @return The X coordinate of this point
	 */
<<<<<<< HEAD
	public float getX() {
=======
	public int getX() {
>>>>>>> f37925c46b61b7de7344634a6046994d80e41aaf
		return x;
	}
	
	/**
	 * Returns the Y coordinate of this point
	 * @return The Y coordinate of this point
	 */
<<<<<<< HEAD
	public float getY() {
=======
	public int getY() {
>>>>>>> f37925c46b61b7de7344634a6046994d80e41aaf
		return y;
	}
	
	/**
	 * Sets a new X coordinate for this point
	 * @param x New X coordinate for this point
	 */
<<<<<<< HEAD
	public void setX(float x) {
=======
	public void setX(int x) {
		System.out.println("Changing x from " + this.x + " to " + x);
>>>>>>> f37925c46b61b7de7344634a6046994d80e41aaf
		this.x = x;
	}
	
	/**
	 * Sets a new Y coordinate for this point
	 * @param y New Y coordinate for this point
	 */
<<<<<<< HEAD
	public void setY(float y) {
=======
	public void setY(int y) {
		System.out.println("Changing y from " + this.y + " to " + y);
>>>>>>> f37925c46b61b7de7344634a6046994d80e41aaf
		this.y = y;
	}
	
	/**
	 * Returns the distance between this and point p
	 * @param p The point to measure distance to
	 * @return The distance between this and point p
	 */
<<<<<<< HEAD
	public float dist(TrackPoint p) {
		float dx = this.x - p.getX();
		float dy = this.y - p.getY();
=======
	public int dist(TrackPoint p) {
		int dx = this.x - p.getX();
		int dy = this.y - p.getY();
>>>>>>> f37925c46b61b7de7344634a6046994d80e41aaf
		return (int) Math.sqrt((dx*dx) + (dy*dy));
	}
	
	@Override
	public int compareTo(TrackPoint p) {
		if(this.x == p.x) {
			return (int)(this.y - p.y); //If x is the same, return the difference in Y
		} else {
			return (int)(this.x - p.x);
		}
	}
	
	/**
	 * Returns whether 2 given TrackPoint objects are equal
	 * @param p The other TrackPoint object to compare with
	 * @return Whether 2 given TrackPoint objects are equal
	 */
	public boolean equals(TrackPoint p) {
		return((this.x == p.x) && (this.y == p.y));
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
	
	public void draw(Graphics2D g) {
		g.drawOval((int)x*2, (int)y*2, 2, 2);
	}
	
	
}
