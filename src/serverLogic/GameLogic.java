package serverLogic;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import org.joml.Intersectionf;
import org.joml.Vector3f;

import serverComms.GameRoom;
import trackDesign.TrackPoint;

/** Class implementing the game logic
 * 
 * @author Tudor Suruceanu */
public class GameLogic {

	private ArrayList<ShipLogicData> players;
	private HashMap<TrackPoint, Float> pointsDist;
	private HashMap<ShipLogicData, Integer> lastTrackPoints;
	private ArrayList<TrackPoint> trackPoints;
	private int laps;
	private int amountOfPlayers;
	private int finished;
	private GameRoom gameRoom;

	/** Constructor
	 * 
	 * @param players
	 *        All the players in the race
	 * @param trackPoints
	 *        The track
	 * @param laps
	 *        The number of laps of the race
	 * @param amountOfPlayers
	 *        The number of non-AI players
	 * @param gameRoom
	 *        The game room */
	public GameLogic(ArrayList<ShipLogicData> players, ArrayList<TrackPoint> trackPoints, int laps, int amountOfPlayers,
		GameRoom gameRoom) {
		if (players == null || trackPoints == null || laps == 0 || gameRoom == null) throw new IllegalArgumentException();
		this.players = players;
		this.laps = Math.max(laps, 1);
		this.amountOfPlayers = amountOfPlayers;
		this.gameRoom = gameRoom;
		this.trackPoints = trackPoints;
		pointsDist = new HashMap<TrackPoint, Float>();
		calculatePointsDist();

		finished = 0;

		lastTrackPoints = new HashMap<ShipLogicData, Integer>();
		for (ShipLogicData player : players) {
			player.setFinished(false);
			lastTrackPoints.put(player, 0);
		}
	}

	/** Calculate the position in the race for every player */
	private void updateRankings() {
		ArrayList<ShipLogicData> racingPlayers = new ArrayList<ShipLogicData>();
		for (ShipLogicData player : players) {
			if (!player.finished()) racingPlayers.add(player);
		}
		racingPlayers.sort(new Comparator<ShipLogicData>() {

			@Override
			public int compare(ShipLogicData p1, ShipLogicData p2) {
				float d1 = getPlayerDist(p1);
				float d2 = getPlayerDist(p2);
				if (d1 > d2) return -1;
				if (d1 < d2) return 1;
				return 0;
			}
		});
		for (int i = 0; i < racingPlayers.size(); i++) {
			racingPlayers.get(i).setRanking(finished + 1 + i);
		}
	}

	/** Calculate the distance between track points */
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

	/** Update the last track point a player surpassed and do the logic
	 * 
	 * @param player
	 *        A specific player */
	private void updateLastPoint(ShipLogicData player) {
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
			if (currentLap == laps && !player.finished()) {
				System.out.println("PLAYER " + player.getId() + " FINISHED THE RACE");
				player.setFinished(true);
				finished++;
			} else {
				player.setCurrentLap(currentLap + 1);
			}
		} else {
			if (previous == 0 && lastTrackPoint == trackPoints.size() - 1) {
				if (currentLap > 0) player.setCurrentLap(currentLap - 1);
			}
		}

	}

	/** Calculate the distance on the track a player has travelled from the
	 * begining of the race
	 * 
	 * @param player
	 *        A specific player
	 * @return The distance it has travelled */
	private float getPlayerDist(ShipLogicData player) {
		float distance;
		int lastTrackPoint = lastTrackPoints.get(player);
		if (lastTrackPoint == 0) distance = pointsDist.get(trackPoints.get(0)) * (player.getCurrentLap() - 1);
		else distance = pointsDist.get(trackPoints.get(lastTrackPoint)) + pointsDist.get(trackPoints.get(0)) * (player.getCurrentLap() - 1);

		Vector3f playerPos = player.getPosition();
		TrackPoint last = trackPoints.get(lastTrackPoint);
		TrackPoint next;
		if (lastTrackPoint + 1 < trackPoints.size()) next = trackPoints.get(lastTrackPoint + 1);
		else next = trackPoints.get(0);

		float orth = Intersectionf.distancePointLine(playerPos.x(), playerPos.z(), last.getX(), last.getY(), next.getX(), next.getY());
		float ip = last.distance(playerPos.x(), playerPos.z());
		distance += Math.sqrt(ip * ip - orth * orth);

		return distance;
	}

	/** Method called every frame, updating the ranking and sending the data to
	 * clients */
	public void update() {
		for (ShipLogicData player : players) {
			updateLastPoint(player);
		}
		updateRankings();

		for (ShipLogicData player : players) {
			if (player.getId() < amountOfPlayers) {
				gameRoom.sendLogicUpdate(player.getId(), player.getRanking(), player.finished(), player.getCurrentLap());
				if (player.finished()) gameRoom.sendFinishData(player.getId(), getRanking());
			}
		}
	}

	/** Get the total number of laps of the race
	 * 
	 * @return The total number of laps of the race */
	public int getTotalLaps() {
		return laps;
	}

	/** Check if the race has finished
	 * 
	 * @return Whether the race has finished */
	public boolean raceFinished() {
		return finished == players.size();
	}

	/** Get the ranking of the players that finished the race as data to be sent
	 * to clients
	 * 
	 * @return The ranking of the players that finished the race */
	public byte[] getRanking() {
		byte[] ranking = new byte[finished];
		for (ShipLogicData player : players) {
			if (player.finished()) {
				try {
					// TODO actually fix whatever is causeing it
					ranking[player.getRanking() - 1] = player.getId();
				} catch (ArrayIndexOutOfBoundsException e) {
					System.err.println(e.getMessage());
				}

			}
		}
		return ranking;
	}
}
