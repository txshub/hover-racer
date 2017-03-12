package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.joml.Intersectionf;
import org.joml.Vector3f;

import physics.core.Ship;
import trackDesign.SeedTrack;
import trackDesign.TrackPoint;

/**
 * @author rtm592
 * @author Tudor Suruceanu
 */
public class GameLogic {

	private float playerDist;
	private Ship player;
	private ArrayList<Ship> opponents;
	private HashMap<Integer, Integer> ranking;
	private HashMap<TrackPoint, Float> pointsDist;
	private ArrayList<TrackPoint> trackPoints;
	private int lastTrackPoint;
	private int laps;
	private int currentLap;
	private boolean finished;
	private boolean wrongWay;
	private int lastValidPoint;

	/**
	 * Constructor
	 * 
	 * @param player
	 *            The player's ship
	 * @param opponents
	 *            Other ships in the race
	 * @param track
	 *            The track seed of the race
	 */
	public GameLogic(Ship player, SeedTrack track, int laps) {

		this.player = player;
		this.laps = laps;

		currentLap = 1;
		trackPoints = track.getTrack();

		playerDist = 0;
		ranking = new HashMap<Integer, Integer>();
		pointsDist = new HashMap<TrackPoint, Float>();

		calculatePointsDist();
		lastTrackPoint = 0;

		finished = false;
		wrongWay = false;
		lastValidPoint = 0;
	}

	/**
	 * Calculate the distance from the first track point to each of the others
	 * 
	 * @param track
	 *            The list of track points
	 */
	private void calculatePointsDist() {
		float distance = 0f;
		for (int i = 1; i < trackPoints.size(); i++) {
			TrackPoint previous = trackPoints.get(i - 1);
			TrackPoint current = trackPoints.get(i);
			distance += previous.distance(current.getX(), current.getY());
			pointsDist.put(trackPoints.get(i), distance);
		}
		if (!trackPoints.isEmpty()) {
			TrackPoint previous = trackPoints.get(trackPoints.size() - 1);
			TrackPoint current = trackPoints.get(0);
			distance += previous.distance(current.getX(), current.getY());
			pointsDist.put(trackPoints.get(0), distance);
		}
	}

	/**
	 * Update the last point the player surpassed
	 */
	private void updateLastPoint() {
		Vector3f playerPos = player.getPosition();

		int previous = lastTrackPoint;

		for (int i = 0; i < trackPoints.size(); i++) {
			TrackPoint tp = trackPoints.get(i);
			float pointWidth = tp.getWidth() / 2f;
			float distanceToNext = tp.distance(playerPos.x, playerPos.z);

			if (distanceToNext <= pointWidth && previous != i) {
				lastTrackPoint = i;
				System.out.println("Last trackpoint: " + lastTrackPoint + " Wrong way: " + wrongWay);
			}
		}

		if (!wrongWay) {
			if (previous == trackPoints.size() - 1 && lastTrackPoint == 0) {
				if (currentLap == laps) {
					System.err.println("CONGRATULATIONS!");
					finished = true;
				} else {
					int left = laps - currentLap;
					if (left > 1)
						System.err.println(left + " MORE LAPS!");
					else
						System.err.println("1 MORE LAP!");
					currentLap++;
				}
				return;
			}
			if (lastTrackPoint < previous) {
				System.err.println("WRONG WAY!");
				wrongWay = true;
				lastValidPoint = previous;
				System.err.println("WRONG WAY: Last valid trackpoint: " + lastTrackPoint);
			}
		} else {
			if (lastTrackPoint == lastValidPoint)
				wrongWay = false;
		}
		
		if (lastTrackPoint - previous > 1 )
			System.err.println("YOU LEFT THE TRACK!");

	}

	/**
	 * Calculate the distance travelled by the player from the start
	 * 
	 * @return The distance travelled by the player
	 */
	private float calculatePlayerDist() {
		float distance; 
		if (lastTrackPoint == 0)
			distance = pointsDist.get(trackPoints.get(0)) * (currentLap - 1);
		else
			distance = pointsDist.get(trackPoints.get(lastTrackPoint)) + pointsDist.get(trackPoints.get(0)) * (currentLap - 1);
		
		Vector3f playerPos = player.getPosition();
		TrackPoint last = trackPoints.get(lastTrackPoint);
		TrackPoint next;
		if (lastTrackPoint + 1 < trackPoints.size())
			next = trackPoints.get(lastTrackPoint + 1);
		else
			next = trackPoints.get(0);
		
		float orth = Intersectionf.distancePointLine(playerPos.x(), playerPos.z(), last.getX(), last.getY(),
				next.getX(), next.getY());
		float ip = last.distance(playerPos.x(), playerPos.z());
		distance += Math.sqrt(ip * ip - orth * orth);
		
		return distance;
	}

	/**
	 * General update method
	 */
	public void update() {

		updateLastPoint();
		playerDist = calculatePlayerDist();

		// get rankings from the server
	}

	/**
	 * Update information about the ranking
	 * 
	 * @param ranking
	 *            The current ranking
	 */
	public void setRankings(HashMap<Integer, Integer> ranking) {
		this.ranking = ranking;
	}

	/**
	 * Get the distance travelled by the player from the start
	 * 
	 * @return The distance a player has travelled from the start
	 */
	public float getPlayerDist() {
		return playerDist;
	}

	/**
	 * Get the current lap the player is completing
	 * 
	 * @return The current lap
	 */
	public int getCurrentLap() {
		return currentLap;
	}

	/**
	 * Get the last track point the player surpassed
	 * 
	 * @return The last track point the player surpassed
	 */
	public int getLastPoint() {
		return lastTrackPoint;
	}

	/**
	 * Get the current ranking in the race
	 * 
	 * @return The current ranking
	 */
	public HashMap<Integer, Integer> getRankings() {
		return ranking;
	}

	/**
	 * Check if the player has finished the race
	 * 
	 * @return Whether the player has finished the race
	 */
	public boolean finishedRace() {
		return finished;
	}

}
