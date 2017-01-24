package trackDesign;
import java.awt.Color;
import java.awt.Graphics2D;
/**
 * This class exists in order to have a sortable point, as well as a square distance measure
 * @author sxw588
 *
 */
public class TrackPoint implements Comparable<TrackPoint>{
	private int x,y;
	/**
	 * Creates a new TrackPoint object
	 * @param x X coordinate of the point
	 * @param y Y coordinate of the point
	 */
	public TrackPoint(int x, int y) {
		System.out.println("New point at " + x + ", " + y);
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Returns the X coordinate of this point
	 * @return The X coordinate of this point
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Returns the Y coordinate of this point
	 * @return The Y coordinate of this point
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Sets a new X coordinate for this point
	 * @param x New X coordinate for this point
	 */
	public void setX(int x) {
		System.out.println("Changing x from " + this.x + " to " + x);
		this.x = x;
	}
	
	/**
	 * Sets a new Y coordinate for this point
	 * @param y New Y coordinate for this point
	 */
	public void setY(int y) {
		System.out.println("Changing y from " + this.y + " to " + y);
		this.y = y;
	}
	
	/**
	 * Returns the distance between this and point p
	 * @param p The point to measure distance to
	 * @return The distance between this and point p
	 */
	public int dist(TrackPoint p) {
		int dx = this.x - p.getX();
		int dy = this.y - p.getY();
		return (int) Math.sqrt((dx*dx) + (dy*dy));
	}
	
	@Override
	public int compareTo(TrackPoint p) {
		if(this.x == p.x) {
			return this.y - p.y; //If x is the same, return the difference in Y
		} else {
			return this.x - p.x;
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
		g.setColor(Color.RED);
		g.drawOval(x*2, y*2, 2, 2);
//		System.out.println("Drawing point at " + x + ", " + y);
	}
	
	
}
