package trackDesign.catmullrom;

import trackDesign.TrackPoint;

/**
 * @author Simon
 * Adapted From http://hawkesy.blogspot.co.uk/2010/05/catmull-rom-spline-curve-implementation.html
 * 
 *
 */
public class Spline2D {
	private Spline splineXs, splineYs;
	private TrackPoint p1,p2;
	
	public Spline2D(TrackPoint p0, TrackPoint p1, TrackPoint p2, TrackPoint p3) {
		splineXs = new Spline(p0.getX(), p1.getX(), p2.getX(), p3.getX());
		splineYs = new Spline(p0.getY(), p1.getY(), p2.getY(), p3.getY());
		this.p1 = p1;
		this.p2 = p2;
	}
	
	public TrackPoint q(float t) {
		TrackPoint p = new TrackPoint(splineXs.q(t),splineYs.q(t));
		float dif = (p2.getWidth()-p1.getWidth())*t;
		p.setWidth(p1.getWidth()-dif);
		return p;
	}
}
