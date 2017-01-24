package trackDesign.catmullrom;

import java.util.ArrayList;

import trackDesign.TrackPoint;

public class SplineUtils {

	public static ArrayList<TrackPoint> dividePoints(ArrayList<TrackPoint> points, int divisionCount) {
		ArrayList<TrackPoint> subPoints = new ArrayList<TrackPoint>();
		float increments = 1f/(float)divisionCount;
		for(int i = 0; i < points.size(); i++) {
			TrackPoint p0;
			if(i == 0) {
				p0 = points.get(points.size()-1);
			} else {
				p0 = points.get(i-1);
			}
			TrackPoint p1 = points.get(i);
			TrackPoint p2;
			TrackPoint p3;
			if(i == points.size()-1) {
				p2 = points.get(0);
				p3 = points.get(1);
			} else if(i == points.size()-2) {
				p2 = points.get(i+1);
				p3 = points.get(0);
			} else {
				p2 = points.get(i+1);
				p3 = points.get(i+2);
			}
			Spline2D s = new Spline2D(p0, p1, p2, p3);
			subPoints.add(p1);
			for(int j = 0; j <= divisionCount; j++) {
				subPoints.add(s.q(j * increments));
			}
		}
		return subPoints;
	}
	
}
