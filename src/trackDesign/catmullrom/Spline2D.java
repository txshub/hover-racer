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
	
	public Spline2D(TrackPoint p0, TrackPoint p1, TrackPoint p2, TrackPoint p3) {
		splineXs = new Spline(p0.getX(), p1.getX(), p2.getX(), p3.getX());
		splineYs = new Spline(p0.getY(), p1.getY(), p2.getY(), p3.getY());
	}
	
	public TrackPoint q(float t) {
		return new TrackPoint(splineXs.q(t), splineYs.q(t));
	}
}
