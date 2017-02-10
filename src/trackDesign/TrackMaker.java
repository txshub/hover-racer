package trackDesign;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import trackDesign.catmullrom.SplineUtils;
/**
 * Class to make the track
 * @author sxw588
 *
 */
public class TrackMaker {
	
	/**
	 * Makes the track
	 * @param minTrackPoints Minimum number of points for the initial point generation
	 * @param maxTrackPoints Maximum number of points for the initial point generation
	 * @param minDist Minimum distance between any 2 given points
	 * @param seperateIterations Number of iterations to spread any points that are within this minimum distance
	 * @param difficulty The difficulty for the track between 0 and 100. Closest to 0 is the hardest setting 
	 * @param maxDisp The maximum to displace a point by when applying the difficulty
	 * @param subDivs The number of subdivisions between each point when smoothing is applied
	 * @return A track in the form of an arraylist of points
	 */
	public static ArrayList<TrackPoint> makeTrack(int minTrackPoints, int maxTrackPoints, float minDist, int seperateIterations, float difficulty, float maxDisp, int subDivs) {
		Random temp = new Random();
		return makeTrack(temp.nextLong(), minTrackPoints, maxTrackPoints, minDist, seperateIterations, difficulty, maxDisp, subDivs);
	}
	
	/**
	 * 
	 * @param seed The given seed for this generation
	 * @param minTrackPoints Minimum number of points for the initial point generation
	 * @param maxTrackPoints Maximum number of points for the initial point generation
	 * @param minDist Minimum distance between any 2 given points
	 * @param seperateIterations Number of iterations to spread any points that are within this minimum distance
	 * @param difficulty The difficulty for the track between 0 and 100. Closest to 0 is the hardest setting 
	 * @param maxDisp The maximum to displace a point by when applying the difficulty
	 * @param subDivs The number of subdivisions between each point when smoothing is applied
	 * @return A track in the form of an arraylist of points
	 */
	public static ArrayList<TrackPoint> makeTrack(long seed, int minTrackPoints, int maxTrackPoints, float minDist, int seperateIterations, float difficulty, float maxDisp, int subDivs) {
		Random random = new Random(seed); //Make the random object
		ArrayList<TrackPoint> points = new ArrayList<TrackPoint>();
		ArrayList<TrackPoint> hullPoints = new ArrayList<TrackPoint>();
		int trackPointCount = random.nextInt(maxTrackPoints - minTrackPoints) + minTrackPoints; //Decide the number of points between the minimum and maximum number
		for(int i = 0; i < trackPointCount; i++) { //Create an array of random TrackPoints
			int x = random.nextInt(250); //Pick a random x coordinate
			int y = random.nextInt(250); //Pick a random y coordinate
			points.add(new TrackPoint(x,y)); //Add this new point
		}
		hullPoints = convexHull(points); //Take the outer hull of these points for the initial track
		
		
		hullPoints = new ArrayList<>();
		hullPoints.add(new TrackPoint(50, 50));
		hullPoints.add(new TrackPoint(180, 180));
		hullPoints.add(new TrackPoint(300, 50));
		hullPoints.add(new TrackPoint(300, 300));
		hullPoints.add(new TrackPoint(50, 300));
		
		
		
		for(int i = 0; i < seperateIterations; i++) {
			seperatePoints(points, minDist); //Spread out the points to ensure the minimum distance
		}
		ArrayList<TrackPoint> circuit = doublePoints(hullPoints, difficulty, maxDisp, random); //Double the number of points and offset them
		for(int i = 0; i < seperateIterations * 3; i++) {
			//fixAngles(circuit);
			seperatePoints(circuit, minDist); //Seperate the points again
		}
		ArrayList<TrackPoint> finalCircuit = SplineUtils.dividePoints(circuit, subDivs); //Apply smoothing
		return finalCircuit; //Return this final track after smoothing
	}
	
	//Not currently working - Need to investigate
	public static void fixAngles(ArrayList<TrackPoint> points) {
		for(int i = 0; i < points.size(); i++) {
			int prevPoint;
			if(i == 0) {
				prevPoint = points.size()-1;
			} else {
				prevPoint = i - 1;
			}
			int next = (i+1)% points.size();
			float pdx = points.get(i).getX() - points.get(prevPoint).getX();
			float pdy = points.get(i).getY() - points.get(prevPoint).getY();
			float pl = (float)Math.sqrt(pdx*pdx + pdy*pdy);
			pdx /= pl;
			pdy /= pl;
			float ndx = points.get(i).getX() - points.get(next).getX();
			float ndy = points.get(i).getY() - points.get(next).getY();
			float nl = (float)Math.sqrt(ndx*ndx + ndy*ndy);
			ndx /= nl;
			ndy /= nl;
			float angle = (float)Math.atan2(pdx * ndy - pdy * ndx, pdx * ndx + pdy * ndy);
			if(Math.abs(angle) < Math.toRadians(100)) {
				float newAngle = (float)(Math.signum(angle) * Math.toRadians(100));
				float diff = newAngle - angle;
				float cos = (float)Math.cos(diff);
				float sin = (float)Math.sin(diff);
				float newX = ndx * cos - ndy * sin;
				float newY = ndx * sin + ndy * cos;
				newX *= nl;
				newY *= nl;
				points.get(next).setX(newX);
				points.get(next).setY(newY);
			}
		}
	}
	
	
	/**
	 * Puts a new point in between every set of 2 points and offsets it slightly from the middle
	 * @param points The initial track to add points to
	 * @param difficulty The difficulty rating (The higher the difficulty, the higher the offset)
	 * @param maxDisplacement The maximum to displace the new points from the centre
	 * @param random The random object
	 * @return The track with additional offset points
	 */
	public static ArrayList<TrackPoint> doublePoints(ArrayList<TrackPoint> points, float difficulty, float maxDisplacement, Random random) {
		ArrayList<TrackPoint> newPoints = new ArrayList<TrackPoint>();
		for(int i = 0; i < points.size(); i++) {
			int displacementLength = (int)(Math.pow(random.nextDouble(), ((double)difficulty) / 100d) * (double)maxDisplacement); //Randomise the displacement length
			double randomAngle = (random.nextDouble() * Math.PI * 2) - Math.PI; //Randomise the displacement direction
			float xAdd = (float)(displacementLength * Math.cos(randomAngle));
			float yAdd = (float)(displacementLength * Math.sin(randomAngle));
			float xNew = ((points.get(i).getX() + points.get((i+1)%points.size()).getX())/2) + xAdd;
			float yNew = ((points.get(i).getY() + points.get((i+1)%points.size()).getY())/2) + yAdd;
			newPoints.add(points.get(i)); //Add the original point
			newPoints.add(new TrackPoint(xNew, yNew)); //Add this new point
		}
		return newPoints;
	}
	
	/**
	 * Spreads the points out to keep a minimum distance
	 * @param points The points to spread out
	 * @param minDist The minimum distance to enforce
	 */
	public static void seperatePoints(ArrayList<TrackPoint> points, float minDist) {
		for(int i = 0; i < points.size(); i++) { //For each point
			for(int j = i+1; j < points.size(); j++) { //For each remaining point
				if(points.get(i).dist(points.get(j)) < minDist) { //If the distance is less than the minimum distance
					float dx = points.get(i).getX() - points.get(j).getX();
					float dy = points.get(i).getY() - points.get(j).getY();
					float len = (float)Math.sqrt((dx*dx)+(dy*dy));
					dx /= len;
					dy /= len;
					float dif = minDist - len;
					dx *= dif;
					dy *= dif;
					points.get(i).setX(points.get(i).getX() + dx); //Spread the points out
					points.get(i).setY(points.get(i).getY() + dy);
					points.get(j).setX(points.get(j).getX() + dx);
					points.get(j).setY(points.get(j).getY() + dy);
				}
			}
		}
	}

	/**
	 * Returns all the TrackPoints that form the ConvexHull of all elements of points
	 * @param points The initial ArrayList
	 * @return All the TrackPoints that form the ConvexHull of all elements of points
	 */
	public static ArrayList<TrackPoint> convexHull(ArrayList<TrackPoint> points) {
		Collections.sort(points); //Sort the points
		ArrayList<TrackPoint> lower = new ArrayList<TrackPoint>(); //Lower half of hull
		ArrayList<TrackPoint> upper = new ArrayList<TrackPoint>(); //Upper half of hull
		for(int i = 0; i < points.size(); i++) { //For each point in the array
			while(lower.size() >= 2 && cross(lower.get(lower.size()-2), lower.get(lower.size()-1), points.get(i)) <= 0) { //While the most recent 2 points make a clockwise angle with the new point
				lower.remove(lower.size()-1);  //Remove the middle point out of these three as it isn't part of the upper hull
			}
			lower.add(points.get(i)); //Add the new point to the lower hull
		}
		
		for(int i = points.size() - 1; i >= 0; i--) {
			while(upper.size() >= 2 && cross(upper.get(upper.size()-2), upper.get(upper.size()-1), points.get(i)) <= 0) {
				upper.remove(upper.size()-1);
			}
			upper.add(points.get(i));
		}
		lower.remove(lower.size()-1);
		upper.remove(upper.size()-1);
		lower.addAll(upper);
		return(lower);
	}

	/**
	 * Returns whether a given angle OAB is straight (returns 0), clockwise (returns >0) or anti-clockwise (returns <0)
	 * @param o Point O
	 * @param a Point A
	 * @param b Point B
	 * @return Whether a given angle OAB is straight, clockwise or anti-clockwise
	 */
	public static float cross(TrackPoint o, TrackPoint a, TrackPoint b) {
		return ((a.getX() - o.getX()) * (b.getY() - o.getY())) - ((a.getY() - o.getY()) * (b.getX() - o.getX()));
	}
}
