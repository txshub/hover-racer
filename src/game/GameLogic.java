package game;

import java.util.ArrayList;
import java.util.HashMap;

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
  private HashMap<Integer, Integer> ranking;
  private HashMap<TrackPoint, Float> pointsDist;
  private ArrayList<TrackPoint> trackPoints;
  private int lastTrackPoint;
  private int laps;
  private int currentLap;
  private boolean finished;

  /**
   * Constructor
   * 
   * @param player
   *          The player's ship
   * @param opponents
   *          Other ships in the race
   * @param track
   *          The track seed of the race
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
  }

  /**
   * Calculate the distance from the first track point to each of the others
   * 
   * @param track
   *          The list of track points
   */
  private void calculatePointsDist() {
    if (!trackPoints.isEmpty()) {
      pointsDist.put(trackPoints.get(0), 0f);
    }
    float distance = 0f;
    for (int i = 1; i < trackPoints.size(); i++) {
      TrackPoint previous = trackPoints.get(i - 1);
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

    int last = lastTrackPoint;

    for (int i = 0; i < trackPoints.size(); i++) {
      TrackPoint tp = trackPoints.get(i);
      float pointWidth = tp.getWidth() / 2f;
      float distanceToNext = tp.distance(playerPos.x, playerPos.z);

      if (distanceToNext <= pointWidth) {
        lastTrackPoint = i;
      }
    }

    if (last == trackPoints.size() - 1 && lastTrackPoint == 0) {
      if (currentLap == laps) {
        System.err.println("CONGRATULATIONS!");
        finished = true;
      } else {
        int left = laps - currentLap;
        if (left > 1)
          System.err.println((laps - currentLap) + " MORE LAPS!");
        else if (left < 1)
          System.out.println("Finished!!!!!");
        else
          System.err.println("1 MORE LAP!");
        currentLap++;
      }
      return;
    }
    if (lastTrackPoint < last)
      System.err.println("WRONG WAY!");
    if (lastTrackPoint - last > 1)
      System.err.println("PENALTY: You left the track!");

  }

  /**
   * Calculate the distance travelled by the player from the start
   * 
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
    float orth = Intersectionf.distancePointLine(playerPos.x(), playerPos.z(), last.getX(),
        last.getY(), next.getX(), next.getY());
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
   *          The current ranking
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
