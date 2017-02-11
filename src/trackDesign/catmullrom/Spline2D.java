package trackDesign.catmullrom;

import trackDesign.TrackPoint;

public class Spline2D {
	private Spline splineXs, splineYs;
	private float z;
	
	public Spline2D(TrackPoint p0, TrackPoint p1, TrackPoint p2, TrackPoint p3) {
		splineXs = new Spline(p0.getX(), p1.getX(), p2.getX(), p3.getX());
		splineYs = new Spline(p0.getY(), p1.getY(), p2.getY(), p3.getY());
		z = (p0.getZ() + p1.getZ() + p2.getZ() + p3.getZ()) / 4;
	}
	
	public TrackPoint q(float t) {
		return new TrackPoint(splineXs.q(t), splineYs.q(t), z);
	}
}
