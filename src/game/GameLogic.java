package game;

import java.util.ArrayList;
import java.util.HashMap;

import org.joml.Intersectionf;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector3f;

import gameEngine.entities.Player;
import gameEngine.toolbox.Maths;
import physics.core.Ship;
import trackDesign.SeedTrack;
import trackDesign.TrackPoint;

/**
 * @author rtm592
 *
 */
public class GameLogic {

	private float playerDist;
	private Player player;
	private ArrayList<Ship> opponents;
	private SeedTrack track;
	private HashMap<Integer,Integer> ranking;
	
	private HashMap<TrackPoint, Float> pointsDist;
	private ArrayList<TrackPoint> trackPoints;
	private int lastTrackPoint;
	
	public GameLogic(Player player, ArrayList<Ship> opponents, SeedTrack track) {

		this.player = player;
		this.opponents = opponents;
		this.track = track;
		playerDist = 0;
		ranking = new HashMap<Integer, Integer>();
		
		trackPoints = this.track.getTrack();
		calculatePointsDist();
		lastTrackPoint = 0;
	}
	
	/**
	 * Calculate the distance from the first track point to each of the others
	 * @param track The list of track points
	 */
	private void calculatePointsDist() {
		if (!trackPoints.isEmpty())
			pointsDist.put(trackPoints.get(0), 0f);
		float distance = 0f;
		for (int i = 1; i < trackPoints.size(); i++) {
			TrackPoint previous = trackPoints.get(i-1);
			TrackPoint current = trackPoints.get(i);
			distance += previous.distance(current.getX(), current.getY());
			pointsDist.put(trackPoints.get(i), distance);
		}
	}
	
	/**
	 * Update the last point the player surpassed
	 */
	private void updateLastPoint() {
		Vector3f playerPos = player.getPosition();
		for (int i = 0; i < trackPoints.size(); i++) {
			TrackPoint tp = trackPoints.get(i);
			float pointWidth = tp.getWidth() / 2f;
			float distanceToNext = playerPos.distance(tp.getX(), tp.getY(), playerPos.z());
			if (distanceToNext <= pointWidth)
				lastTrackPoint = i;
		}
	}
	
	/**
	 * Calculate the distance travelled by the player from the start
	 * @return The distance travelled by the player
	 */
	private float calculatePlayerDist() {
		float distance = pointsDist.get(trackPoints.get(lastTrackPoint));
		Vector3f playerPos = player.getPosition();
		TrackPoint last = trackPoints.get(lastTrackPoint);
		TrackPoint next;
		if (lastTrackPoint + 1 < trackPoints.size())
			next = trackPoints.get(lastTrackPoint + 1);
		else
			next = trackPoints.get(0);
		float orth = Intersectionf.distancePointLine(playerPos.x(), playerPos.y(), last.getX(), last.getY(), next.getX(), next.getY());
		float ip = playerPos.distance(last.getX(), last.getY(), playerPos.z());
		distance += Math.sqrt(ip * ip - orth * orth);
		return distance;
	}

	public void update(){
		
		updateLastPoint();
		playerDist = calculatePlayerDist();
		
		// get rankings from the server
	}

	public void setRankings(HashMap<Integer,Integer> ranking){
		this.ranking = ranking;
	}
	
	public float getPlayerDist(){
		return playerDist;
	}
	
	public HashMap<Integer, Integer> getRankings(){
		return ranking;
	}
	
}
