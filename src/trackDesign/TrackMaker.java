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
	 * Calls the track making function
	 * @param args Any arguments passed to the program
	 */
	public static void main(String[] args) {
		makeTrack(10,20, 15, 3, 50, 20, 4);
		
	}
	
	/**
	 * Makes the track
	 * @param minTrackPoints Minimum number of points to generate 
	 * @param maxTrackPoints Maximum number of points to generate
	 * @param minDist The minimum distance between any two points
	 * @param seperateIterations Number of times to seperate out any close-together points
	 * @param difficulty The difficulty setting when offsetting the centre points
	 * @param maxDisp Max displacement 
	 * @return An arraylist of track points forming the route
	 */

	public static ArrayList<TrackPoint> makeTrack(int minTrackPoints, int maxTrackPoints, float minDist, int seperateIterations, float difficulty, float maxDisp, int subDivs) {
		Random temp = new Random();
		return makeTrack(temp.nextLong(), minTrackPoints, maxTrackPoints, minDist, seperateIterations, difficulty, maxDisp, subDivs);
	}
	
	public static ArrayList<TrackPoint> makeTrack(long seed, int minTrackPoints, int maxTrackPoints, float minDist, int seperateIterations, float difficulty, float maxDisp, int subDivs) {
		Random random = new Random(seed);
		ArrayList<TrackPoint> points = new ArrayList<TrackPoint>();
		ArrayList<TrackPoint> hullPoints = new ArrayList<TrackPoint>();
		int trackPointCount = random.nextInt(maxTrackPoints - minTrackPoints) + minTrackPoints;
		for(int i = 0; i < trackPointCount; i++) { //Create an array of random TrackPoints
			int x = random.nextInt(250);
			int y = random.nextInt(250);
			points.add(new TrackPoint(x,y));
		}
		hullPoints = convexHull(points);
		for(int i = 0; i < seperateIterations; i++) {
			seperatePoints(points, minDist);
		}
		ArrayList<TrackPoint> circuit = new ArrayList<TrackPoint>();//doublePoints(hullPoints, difficulty, maxDisp, random);
		circuit.add(new TrackPoint(100,100));
		circuit.add(new TrackPoint(100,200));
		circuit.add(new TrackPoint(200,200));
		circuit.add(new TrackPoint(200,100));
		ArrayList<TrackPoint> finalCircuit = SplineUtils.dividePoints(circuit, subDivs);
		return finalCircuit;
	}
	
	
	
	public static ArrayList<TrackPoint> doublePoints(ArrayList<TrackPoint> points, float difficulty, float maxDisplacement, Random random) {
		ArrayList<TrackPoint> newPoints = new ArrayList<TrackPoint>();
		for(int i = 0; i < points.size(); i++) {
			int displacementLength = (int)(Math.pow(random.nextDouble(), ((double)difficulty) / 100d) * (double)maxDisplacement);
			double randomAngle = (random.nextDouble() * Math.PI * 2) - Math.PI;
			float xAdd = (float)(displacementLength * Math.cos(randomAngle));
			float yAdd = (float)(displacementLength * Math.sin(randomAngle));
			float xNew = ((points.get(i).getX() + points.get((i+1)%points.size()).getX())/2) + xAdd;
			float yNew = ((points.get(i).getY() + points.get((i+1)%points.size()).getY())/2) + yAdd;
			newPoints.add(points.get(i));
			newPoints.add(new TrackPoint(xNew, yNew));
		}
		return newPoints;
	}
	
	public static void seperatePoints(ArrayList<TrackPoint> points, float minDist) {
		for(int i = 0; i < points.size(); i++) {
			for(int j = i+1; j < points.size(); j++) {
				if(points.get(i).dist(points.get(j)) < minDist) {
					float dx = points.get(i).getX() - points.get(j).getX();
					float dy = points.get(i).getY() - points.get(j).getY();
					float len = (float)Math.sqrt((dx*dx)+(dy*dy));
					dx /= len;
					dy /= len;
					float dif = minDist - len;
					dx *= dif;
					dy *= dif;
					points.get(i).setX(points.get(i).getX() + dx);
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
