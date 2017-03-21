package trackDesign;

import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.joml.Vector2f;
import org.joml.Vector3f;

import trackDesign.catmullrom.SplineUtils;

/**
 * Class to make the track
 * 
 * @author sxw588
 *
 */

// 567ujnb
public class TrackMaker {

	public static SeedTrack makeTrack() {
		return makeTrack(10, 20, 30, 1, 30, 40, 3, 120, 260);
	}

	public static SeedTrack makeTrack(String seed) {
		long hash = 0;
		for (char c : seed.toCharArray()) {
			hash = 36L * hash + c;
		}
		return makeTrack(hash, 10, 20, 30, 3, 30, 40, 6, 200, 260);
	}

	/**
	 * Makes the track
	 * 
	 * @param minTrackPoints
	 *            Minimum number of points for the initial point generation
	 * @param maxTrackPoints
	 *            Maximum number of points for the initial point generation
	 * @param minDist
	 *            Minimum distance between any 2 given points
	 * @param seperateIterations
	 *            Number of iterations to spread any points that are within this
	 *            minimum distance
	 * @param difficulty
	 *            The difficulty for the track between 0 and 100. Closest to 0
	 *            is the hardest setting
	 * @param maxDisp
	 *            The maximum to displace a point by when applying the
	 *            difficulty
	 * @param subDivs
	 *            The number of subdivisions between each point when smoothing
	 *            is applied
	 * @return A track in the form of an arraylist of points
	 */
	public static SeedTrack makeTrack(int minTrackPoints, int maxTrackPoints, float minDist, int seperateIterations,
			float difficulty, float maxDisp, int subDivs, int minTrackWidth, int maxTrackWidth) {
		Random temp = new Random(); // Create a new random object
		return makeTrack(temp.nextLong(), minTrackPoints, maxTrackPoints, minDist, seperateIterations, difficulty,
				maxDisp, subDivs, minTrackWidth, maxTrackWidth); // Return
																	// the made
																	// track
																	// with a
																	// random
		// seed
	}

	/**
	 * 
	 * @param seed
	 *            The given seed for this generation
	 * @param minTrackPoints
	 *            Minimum number of points for the initial point generation
	 * @param maxTrackPoints
	 *            Maximum number of points for the initial point generation
	 * @param minDist
	 *            Minimum distance between any 2 given points
	 * @param seperateIterations
	 *            Number of iterations to spread any points that are within this
	 *            minimum distance
	 * @param difficulty
	 *            The difficulty for the track between 0 and 100. Closest to 0
	 *            is the hardest setting
	 * @param maxDisp
	 *            The maximum to displace a point by when applying the
	 *            difficulty
	 * @param subDivs
	 *            The number of subdivisions between each point when smoothing
	 *            is applied
	 * @return A track in the form of an arraylist of points
	 */
	public static SeedTrack makeTrack(long seed, int minTrackPoints, int maxTrackPoints, float minDist,
			int seperateIterations, float difficulty, float maxDisp, int subDivs, int minTrackWidth,
			int maxTrackWidth) {
		if (seed == 0)
			return makeTrack((new Random(seed)).nextLong(), minTrackPoints, maxTrackPoints, minDist, seperateIterations,
					difficulty, maxDisp, subDivs, minTrackWidth, maxTrackWidth);
		// return makeStraightTrack(250);
		Random random = new Random(seed); // Make the random object
		ArrayList<TrackPoint> points = new ArrayList<TrackPoint>();
		ArrayList<TrackPoint> hullPoints = new ArrayList<TrackPoint>();
		// Decide the number of points between the minimum and maximum number
		int trackPointCount = random.nextInt(maxTrackPoints - minTrackPoints) + minTrackPoints;
		// Create an array of random TrackPoints
		for (int i = 0; i < trackPointCount; i++) {
			int x = random.nextInt(250); // Pick a random x coordinate
			int y = random.nextInt(250); // Pick a random y coordinate
			points.add(new TrackPoint(x, y)); // Add this new point
		}
		// Take the outer hull of these points for the initial track
		hullPoints = convexHull(points);
		for (int i = 0; i < seperateIterations; i++) {
			// Spread out the points to ensure the minimum distance
			seperatePoints(points, minDist);
		}
		// Double the number of points and offset them to account for track
		// difficulty
		ArrayList<TrackPoint> circuit = doublePoints(hullPoints, difficulty, maxDisp, random);
		for (int i = 0; i < 10; i++) {
			// Ensure all angles are greater than 100 degrees to prevent sudden
			// turns
			seperatePoints(circuit, minDist); // Separate the points again
			fixAngles(circuit);
		}
		mergeClosePoints(circuit, minDist * 2);
		spreadAngles(circuit);
		if (circuit.size() < 5)
			return makeTrack(random.nextLong(), minTrackPoints, maxTrackPoints, minDist, seperateIterations, difficulty,
					maxDisp, subDivs, minTrackWidth, maxTrackWidth);
		// If the track generation has failed spectacularly then try again

		makeWidths(circuit, random, minTrackWidth, maxTrackWidth);
		// Apply smoothing
		ArrayList<TrackPoint> finalCircuit = SplineUtils.dividePoints(circuit, subDivs);
		// Centre the track so it doesn't go off screen at all
		centreTrack(finalCircuit);
		// Return this final track after smoothing and centring
		for(TrackPoint tp: finalCircuit) {
			tp.mul(20);
		}
			ArrayList<Vector2f> outer = new ArrayList<Vector2f>();
		ArrayList<Vector2f> inner = new ArrayList<Vector2f>();

		for (int i = 0; i < finalCircuit.size(); i++) {
			TrackPoint curPoint = null;
			TrackPoint prevPoint = null;
			TrackPoint nextPoint = null;
			if (i < finalCircuit.size()) {
				curPoint = finalCircuit.get(i);
				// If we are at the first point the previous is the last point
				int prev = (i == 0) ? finalCircuit.size() - 1 : i - 1;
				prevPoint = finalCircuit.get(prev);

				// If we are at the last point the next is the first point
				int next = (i == finalCircuit.size() - 1) ? 0 : i + 1;
				nextPoint = finalCircuit.get(next);
			} else {
				curPoint = finalCircuit.get(0);
				prevPoint = finalCircuit.get(finalCircuit.size() - 1);
				nextPoint = finalCircuit.get(1);
			}

			// Find the line between previous and next point for direction of
			// this
			// slice
			Vector2f dirVec = new Vector2f(nextPoint).sub(prevPoint).normalize();

			// Calculate the perpendicular normal vectors
			Vector2f left = new Vector2f(dirVec.y, -dirVec.x).normalize();
			Vector2f right = new Vector2f(-dirVec.y, dirVec.x).normalize();

			// Apply the offsets to the center point
			float w = curPoint.getWidth() / 2f;
			Vector2f centerPoint = new Vector2f(curPoint.x, curPoint.y);
			outer.add(new Vector2f(centerPoint).add(left.x * w, left.y * w));
			inner.add(new Vector2f(centerPoint).add(right.x * w, right.y * w));
		}
		if (intersects(outer) || intersects(inner))
			return makeTrack(random.nextLong(), minTrackPoints, maxTrackPoints, minDist, seperateIterations, difficulty,
					maxDisp, subDivs, minTrackWidth, maxTrackWidth);
		for(int i = 0; i < outer.size(); i++) {
			for(int j = 0; j < inner.size(); j++) {
				Vector2f l1a = outer.get(i);
				Vector2f l1b = outer.get((i+1)%outer.size());
				Vector2f l2a = inner.get(j);
				Vector2f l2b = inner.get((j+1)%inner.size());
				Line2D l1 = new Line2D.Float(l1a.x, l1a.y, l1b.x, l1b.y);
				Line2D l2 = new Line2D.Float(l2a.x, l2a.y, l2b.x, l2b.y);
				if(l1.intersectsLine(l2)) return makeTrack(random.nextLong(), minTrackPoints, maxTrackPoints, minDist, seperateIterations, difficulty,
						maxDisp, subDivs, minTrackWidth, maxTrackWidth);
			}
		}

		if (random.nextBoolean())
			return new SeedTrack(seed, finalCircuit);
		Collections.reverse(finalCircuit);
		return new SeedTrack(seed, finalCircuit);
	}

	private static boolean intersects(ArrayList<Vector2f> line) {
		for(int i = 0; i < line.size(); i++) {
			for(int j = i+2; j < line.size() - 1; j++) {
				Vector2f l1a = line.get(i);
				Vector2f l1b = line.get((i+1)%line.size());
				Vector2f l2a = line.get(j);
				Vector2f l2b = line.get((j+1)%line.size());
				Line2D l1 = new Line2D.Float(l1a.x, l1a.y, l1b.x, l1b.y);
				Line2D l2 = new Line2D.Float(l2a.x, l2a.y, l2b.x, l2b.y);
				if(l1.intersectsLine(l2))  {
					System.out.println("Track intersects");
					return true;
				}
			}
		}
			return false;
	}

	/**
	 * Make the widths of all of the tracks
	 * 
	 * @param points
	 *            The points to set widths for
	 * @param random
	 *            The random object
	 * @param minWidth
	 *            The minimum track width
	 * @param maxWidth
	 *            The maximum track width
	 */
	private static void makeWidths(ArrayList<TrackPoint> points, Random random, int minWidth, int maxWidth) {
		int secondMin = ((maxWidth - minWidth) / 4) + minWidth;
		int mid = (maxWidth + minWidth) / 2;
		int secondMax = ((maxWidth - minWidth) / 4) + minWidth;

		points.get(0).setWidth(mid);

		for (int i = 1; i < points.size(); i++) {
			int rand = random.nextInt(100);
			if (rand < 10) {
				points.get(i).setWidth(minWidth);
			} else if (rand < 30) {
				points.get(i).setWidth(secondMin);
			} else if (rand < 70) {
				points.get(i).setWidth(mid);
			} else if (rand < 90) {
				points.get(i).setWidth(secondMax);
			} else {
				points.get(i).setWidth(maxWidth);
			}
		}
	}

	/**
	 * <<<<<<< HEAD Ensures all angles are a minimum of 30 degrees
	 * 
	 * @param points
	 *            The points of the track ======= Ensures all angles are a
	 *            minimum of 40 degrees
	 * 
	 * @param points
	 *            The points of the track >>>>>>> branch 'dev' of
	 *            https://git-teaching.cs.bham.ac.uk/mod-team-proj-2016/e1.git
	 */
	private static void spreadAngles(ArrayList<TrackPoint> points) {
		boolean changed = true;
		final float threshold = (float) Math.toRadians(30);
		while (changed) {
			changed = false;
			for (int i = 0; i < points.size(); i++) {
				int next = (i + 1) % points.size();
				int after = (i + 2) % points.size();
				float angle = (float) (Math.atan2(points.get(i).getY() - points.get(next).getY(),
						points.get(i).getX() - points.get(next).getX())
						- Math.atan2(points.get(after).getY() - points.get(next).getY(),
								// Find the angle between the previous and new
								// point centred on
								// this point
								points.get(after).getX() - points.get(next).getX()));
				if (angle < threshold) {
					points.remove(after);
				}
			}
		}
	}

	/**
	 * Merge close points into one
	 * 
	 * @param points
	 *            The list of all trackpoints
	 * @param minDist
	 *            Minimum distance for two points to NOT be joined together
	 */
	private static void mergeClosePoints(ArrayList<TrackPoint> points, float minDist) {
		boolean changed = true;
		while (changed) {
			changed = false;
			for (int i = 0; i < points.size() - 1; i++) {
				for (int j = i + 1; j < points.size(); j++) {
					if (points.get(i).distance(points.get(i + 1)) < minDist) {
						points.get(i).setX((points.get(i).getX() + points.get(j).getX()) / 2);
						points.get(i).setY((points.get(i).getY() + points.get(j).getY()) / 2);
						points.remove(j);
						changed = true;
						i = points.size();
						j = points.size();
					}
				}
			}
		}

	}

	/**
	 * Takes a track and ensures no point is out of the 250*250 area
	 * 
	 * @param points
	 *            The track
	 */
	public static void centreTrack(ArrayList<TrackPoint> points) {
		float minX = 0; // Minimum X so far
		float maxX = 250; // Maximum X so far
		float minY = 0; // Minimum Y so far
		float maxY = 250; // Maximum Y so far
		for (TrackPoint point : points) { // For each of the points
			if (point.getX() < minX) { // If the x is less than the minimum X
				minX = point.getX(); // Update the minimum X to reflect this
			} else if (point.getX() > maxX) { // Else if the X is larger than
												// the
												// maximum X
				maxX = point.getX(); // Update the maximum X to reflect this
			}
			if (point.getY() < minY) { // If the y is less than the minimum Y
				minY = point.getY(); // Update the minimum Y to reflect this
			} else if (point.getY() > maxY) { // Else if the Y is larger than
												// the
												// maximum Y
				maxY = point.getY(); // Update the maximum Y to reflect this
			}
		}
		float factorX = 250f / (maxX - minX); // The factor needed for scaling
												// so it
												// fits within the 0-250 on X
		float factorY = 250f / (maxY - minY); // The factor needed for scaling
												// so it
												// fits within the 0-250 on Y
		for (TrackPoint point : points) { // For each point
			point.setX(point.getX() - minX); // If any X points are negative,
												// increase
												// all points to prevent this
			point.setX(point.getX() * factorX); // Scale the X so it fits
												// perfectly
												// between 0 & 250
			point.setY(point.getY() - minY); // If any Y points are negative,
												// increase
												// all points to prevent this
			point.setY(point.getY() * factorY); // Scale the Y so it fits
												// perfectly
												// between 0 & 250
		}
	}

	/**
	 * Fixes all angles that are less than 60 degrees
	 * 
	 * @param points
	 *            List of all track points
	 */
	public static void fixAngles(ArrayList<TrackPoint> points) {
		final float angle30 = (float) Math.toRadians(30); // 30 degrees in
															// radians
		for (int i = 0; i < points.size(); i++) { // For each point
			TrackPoint currentPoint = points.get(i); // Get this point
			TrackPoint prevPoint; // Get the previous point
			if (i == 0) {
				prevPoint = points.get(points.size() - 1);
			} else {
				prevPoint = points.get(i - 1);
			}
			TrackPoint nextPoint = points.get((i + 1) % points.size()); // Get
																		// the
																		// next
																		// point
			float angle = (float) (Math.atan2(prevPoint.getY() - currentPoint.getY(),
					prevPoint.getX() - currentPoint.getX())
					- Math.atan2(nextPoint.getY() - currentPoint.getY(), nextPoint.getX() - currentPoint.getX())); // Find
																													// the
																													// angle
																													// between
																													// the
																													// previous
																													// and
																													// next
																													// point
																													// centred
																													// on
																													// this
																													// point
			if (Math.abs(angle) < angle30) { // If the angle is less than 100
												// degrees
												// (i.e needs to be increased)
				if (cross(prevPoint, currentPoint, nextPoint) < 0) { // If the
																		// angle
																		// is
																		// Anti-Clockwise
					TrackPoint belowCentre = new TrackPoint(currentPoint.getX(), currentPoint.getY() - 1); // Get
																											// a
																											// point
																											// such
																											// that
																											// the
																											// current
																											// point
																											// to
																											// this
																											// point
																											// is
																											// parallel
																											// to
																											// the
																											// negative
																											// Y
																											// axis
					float angleToNegativeY = (float) (Math.atan2(prevPoint.getY() - currentPoint.getY(),
							prevPoint.getX() - currentPoint.getX())
							- Math.atan2(belowCentre.getY() - currentPoint.getY(),
									belowCentre.getX() - currentPoint.getX())); // Find
																				// the
																				// angle
																				// to
																				// the
																				// negative
																				// Y
																				// axis
					float angleNeeded = (angle30 * -1f) + angleToNegativeY; // Get
																			// the
																			// angle
																			// needed
																			// on
																			// the
																			// other
																			// side
																			// of
																			// the
																			// negative
																			// X
																			// to
																			// make
																			// this
																			// angle
																			// equal
																			// to
																			// 100
																			// degrees
					float length = currentPoint.distance(nextPoint); // Calculate
																		// the
																		// next
					// point's new location
					// so it's at the same
					// distance from this
					// point and at 100
					// degrees
					float newY = (float) Math.cos(angleNeeded) * length; // Get
																			// the
																			// X&Y
																			// for
																			// the
																			// new
																			// location
					float nexX = (float) Math.sin(angleNeeded) * length;
					nextPoint.setX(currentPoint.getX() + nexX); // Set the new
																// X&Y
																// coordinates
					nextPoint.setY(currentPoint.getY() + newY);
				} else { // Clockwise (Same as above but reversed)
					TrackPoint aboveCentre = new TrackPoint(currentPoint.getX(), currentPoint.getY() + 1);
					float angleToPositiveY = (float) (Math.atan2(prevPoint.getY() - currentPoint.getY(),
							prevPoint.getX() - currentPoint.getX())
							- Math.atan2(aboveCentre.getY() - currentPoint.getY(),
									aboveCentre.getX() - currentPoint.getX()));
					float angleNeeded = angle30 - angleToPositiveY;
					float length = currentPoint.distance(nextPoint);
					float newY = (float) Math.cos(angleNeeded) * length;
					float nexX = (float) Math.sin(angleNeeded) * length;
					nextPoint.setX(currentPoint.getX() + nexX);
					nextPoint.setY(currentPoint.getY() + newY);
				}
			}
		}
	}

	/**
	 * Puts a new point in between every set of 2 points and offsets it slightly
	 * from the middle
	 * 
	 * @param points
	 *            The initial track to add points to
	 * @param difficulty
	 *            The difficulty rating (The higher the difficulty, the higher
	 *            the offset)
	 * @param maxDisplacement
	 *            The maximum to displace the new points from the centre
	 * @param random
	 *            The random object
	 * @return The track with additional offset points
	 */
	public static ArrayList<TrackPoint> doublePoints(ArrayList<TrackPoint> points, float difficulty,
			float maxDisplacement, Random random) {
		ArrayList<TrackPoint> newPoints = new ArrayList<TrackPoint>();
		for (int i = 0; i < points.size(); i++) {
			int displacementLength = (int) (Math.pow(random.nextDouble(), ((double) difficulty) / 100d)
					* (double) maxDisplacement); // Randomise the displacement
													// length
			double randomAngle = (random.nextDouble() * Math.PI * 2) - Math.PI; // Randomise
																				// the
																				// displacement
																				// direction
			float xAdd = (float) (displacementLength * Math.cos(randomAngle));
			float yAdd = (float) (displacementLength * Math.sin(randomAngle));
			float xNew = ((points.get(i).getX() + points.get((i + 1) % points.size()).getX()) / 2) + xAdd;
			float yNew = ((points.get(i).getY() + points.get((i + 1) % points.size()).getY()) / 2) + yAdd;
			newPoints.add(points.get(i)); // Add the original point
			newPoints.add(new TrackPoint(xNew, yNew)); // Add this new point
		}
		return newPoints;
	}

	/**
	 * Spreads the points out to keep a minimum distance
	 * 
	 * @param points
	 *            The points to spread out
	 * @param minDist
	 *            The minimum distance to enforce
	 */
	public static void seperatePoints(ArrayList<TrackPoint> points, float minDist) {
		for (int i = 0; i < points.size(); i++) { // For each point
			for (int j = i + 1; j < points.size(); j++) { // For each remaining
															// point
				if (points.get(i).distance(points.get(j)) < minDist) { // If the
																		// distance
																		// is
					// less than the
					// minimum distance
					float dx = points.get(i).getX() - points.get(j).getX();
					float dy = points.get(i).getY() - points.get(j).getY();
					float len = (float) Math.sqrt((dx * dx) + (dy * dy));
					dx /= len;
					dy /= len;
					float dif = (minDist - len) / 2;
					dx *= dif;
					dy *= dif;
					points.get(i).setX(points.get(i).getX() + dx); // Spread the
																	// points
																	// out
					points.get(i).setY(points.get(i).getY() + dy);
					points.get(j).setX(points.get(j).getX() - dx);
					points.get(j).setY(points.get(j).getY() - dy);
				}
			}
		}
	}

	/**
	 * Returns all the TrackPoints that form the ConvexHull of all elements of
	 * points
	 * 
	 * @param points
	 *            The initial ArrayList
	 * @return All the TrackPoints that form the ConvexHull of all elements of
	 *         points
	 */
	public static ArrayList<TrackPoint> convexHull(ArrayList<TrackPoint> points) {
		Collections.sort(points); // Sort the points
		ArrayList<TrackPoint> lower = new ArrayList<TrackPoint>(); // Lower half
																	// of
																	// hull
		ArrayList<TrackPoint> upper = new ArrayList<TrackPoint>(); // Upper half
																	// of
																	// hull
		for (int i = 0; i < points.size(); i++) { // For each point in the array
			while (lower.size() >= 2
					&& cross(lower.get(lower.size() - 2), lower.get(lower.size() - 1), points.get(i)) <= 0) { // While
																												// the
																												// most
																												// recent
																												// 2
																												// points
																												// make
																												// a
																												// clockwise
																												// angle
																												// with
																												// the
																												// new
																												// point
				lower.remove(lower.size() - 1); // Remove the middle point out
												// of these
												// three as it isn't part of the
												// upper
												// hull
			}
			lower.add(points.get(i)); // Add the new point to the lower hull
		}

		for (int i = points.size() - 1; i >= 0; i--) {
			while (upper.size() >= 2
					&& cross(upper.get(upper.size() - 2), upper.get(upper.size() - 1), points.get(i)) <= 0) {
				upper.remove(upper.size() - 1);
			}
			upper.add(points.get(i));
		}
		lower.remove(lower.size() - 1);
		upper.remove(upper.size() - 1);
		lower.addAll(upper);
		return (lower);
	}

	/**
	 * Returns whether a given angle OAB is straight (returns 0), clockwise
	 * (returns >0) or anti-clockwise (returns <0)
	 * 
	 * @param o
	 *            Point O
	 * @param a
	 *            Point A
	 * @param b
	 *            Point B
	 * @return Whether a given angle OAB is straight, clockwise or
	 *         anti-clockwise
	 */
	public static float cross(TrackPoint o, TrackPoint a, TrackPoint b) {
		return ((a.getX() - o.getX()) * (b.getY() - o.getY())) - ((a.getY() - o.getY()) * (b.getX() - o.getX()));
	}
}
