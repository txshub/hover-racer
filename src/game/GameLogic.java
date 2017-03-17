package game;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import org.joml.Intersectionf;
import org.joml.Vector3f;

import physics.core.Ship;
import serverComms.GameRoom;
import trackDesign.SeedTrack;
import trackDesign.TrackPoint;

/**
 * @author Tudor Suruceanu
 */
public class GameLogic {

  private ArrayList<Ship> players;
  private HashMap<TrackPoint, Float> pointsDist;
  private HashMap<Ship, Integer> lastTrackPoints;
  private ArrayList<TrackPoint> trackPoints;
  private int laps;
  private int finished;
  private GameRoom gameRoom;

  public GameLogic(ArrayList<Ship> players, SeedTrack track, int laps, GameRoom gameRoom) {
    this.players = players;
    this.laps = Math.max(laps, 1);
    this.gameRoom = gameRoom;
    trackPoints = track.getTrack();
    pointsDist = new HashMap<TrackPoint, Float>();
    calculatePointsDist();
    finished = 0;

    lastTrackPoints = new HashMap<Ship, Integer>();
    for (Ship player : players) {
      player.setFinished(false);
      lastTrackPoints.put(player, 0);
    }
  }

  private void updateRankings() {
    ArrayList<Ship> racingPlayers = new ArrayList<Ship>();
    for (Ship player : players) {
      if (!player.finished())
        racingPlayers.add(player);
    }
    racingPlayers.sort(new Comparator<Ship>() {
      @Override
      public int compare(Ship p1, Ship p2) {
        float d1 = getPlayerDist(p1);
        float d2 = getPlayerDist(p2);
        if (d1 > d2)
          return -1;
        if (d1 < d2)
          return 1;
        return 0;
      }
    });
    for (int i = 0; i < racingPlayers.size(); i++) {
      racingPlayers.get(i).setRanking(finished + 1 + i);
    }
  }

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

  private void updateLastPoint(Ship player) {
    int lastTrackPoint = lastTrackPoints.get(player);
    int currentLap = player.getCurrentLap();

    Vector3f playerPos = player.getPosition();
    int previous = lastTrackPoints.get(player);
    for (int i = 0; i < trackPoints.size(); i++) {
      TrackPoint tp = trackPoints.get(i);
      float pointWidth = tp.getWidth() / 2f;
      float distanceToNext = tp.distance(playerPos.x, playerPos.z);

      if (distanceToNext <= pointWidth && previous != i) {
        lastTrackPoint = i;
        lastTrackPoints.put(player, lastTrackPoint);
      }
    }

    if (previous == trackPoints.size() - 1 && lastTrackPoint == 0) {
      if (currentLap == laps) {
        System.out.println("PLAYER " + player.getId() + " FINISHED THE RACE");
        player.setFinished(true);
        finished++;
      } else {
        player.setCurrentLap(currentLap + 1);
      }
    } else {
      if (previous == 0 && lastTrackPoint == trackPoints.size() - 1) {
        if (currentLap > 0)
          player.setCurrentLap(currentLap - 1);
      }
    }
  }

  private float getPlayerDist(Ship player) {
    float distance;
    int lastTrackPoint = lastTrackPoints.get(player);
    if (lastTrackPoint == 0)
      distance = pointsDist.get(trackPoints.get(0)) * (player.getCurrentLap() - 1);
    else
      distance = pointsDist.get(trackPoints.get(lastTrackPoint))
          + pointsDist.get(trackPoints.get(0)) * (player.getCurrentLap() - 1);

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

  public void update() {
    for (Ship player : players) {
      updateLastPoint(player);
    }
    updateRankings();

    for (Ship player : players) {
      gameRoom.sendLogicUpdate(player.getId(), player.getRanking(), player.finished(),
          player.getCurrentLap());
    }
  }

  public int getTotalLaps() {
    return laps;
  }

  public boolean raceFinished() {
    return finished == players.size();
  }

  public Ship getWinner() {
    for (Ship player : players) {
      if (player.getRanking() == 1 && player.finished())
        return player;
    }
    return null;
  }

}
