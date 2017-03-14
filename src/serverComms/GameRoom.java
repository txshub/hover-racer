package serverComms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.joml.Vector2f;
import org.joml.Vector3f;

import physics.network.RaceSetupData;
import physics.network.ShipSetupData;
import trackDesign.SeedTrack;
import trackDesign.TrackMaker;
import trackDesign.TrackPoint;

public class GameRoom {

	private static final long TIME_TO_START = 4L * 1000000000L; // Time to start race
																// in nanoseconds
	private static final int SIDE_DISTANCES = 10;
	private static final int FORWARD_DISTANCES = 10;
	private static final int STARTING_HEIGHT = 10;
	ArrayList<String> players = new ArrayList<String>();
	ArrayList<ShipSetupData> ships;

	String name;
	public final int id;
	private long seed;
	private int maxPlayers;
	private boolean inGame = false;
	private String hostName;
	private int lapCount;
	private ClientTable table;
	private ArrayList<TrackPoint> trackPoints;

	private ServerShipManager shipManager;
	private UpdateAllUsers updatedUsers;

	private long raceStartsAt = -1;

	public GameRoom(int id, String name, long seed, int maxPlayers, String hostName, int lapCount, ClientTable table) {
		System.out.println(hostName + " created a game room " + name + " with id " + id);
		this.id = id;
		this.name = name;
		this.seed = seed;
		this.maxPlayers = maxPlayers;
		this.hostName = hostName;
		this.lapCount = lapCount;
		this.table = table;
		this.ships = new ArrayList<ShipSetupData>(maxPlayers);
		// Generate the track
		SeedTrack st = TrackMaker.makeTrack(seed);
		for (TrackPoint tp : st.getTrack()) {
			tp.mul(20);
		}
		trackPoints = st.getTrack();
	}

	public GameRoom(String in) {
		String collected = "";
		while (in.charAt(0) != '|') {
			collected += in.charAt(0);
			in = in.substring(1);
		}
		name = collected;
		collected = "";
		in = in.substring(1);
		while (in.charAt(0) != '|') {
			collected += in.charAt(0);
			in = in.substring(1);
		}
		id = Integer.parseInt(collected);
		collected = "";
		in = in.substring(1);
		while (in.charAt(0) != '|') {
			collected += in.charAt(0);
			in = in.substring(1);
		}
		seed = Long.parseLong(collected);
		collected = "";
		in = in.substring(1);
		while (in.charAt(0) != '|') {
			collected += in.charAt(0);
			in = in.substring(1);
		}
		maxPlayers = Integer.parseInt(collected);
		collected = "";
		in = in.substring(1);
		while (in.charAt(0) != '|') {
			collected += in.charAt(0);
			in = in.substring(1);
		}
		hostName = collected;
		collected = "";
		in = in.substring(1);
		while (in.charAt(0) != '|') {
			collected += in.charAt(0);
			in = in.substring(1);
		}
		lapCount = Integer.parseInt(collected);
		collected = "";
		in = in.substring(1);
		players = new ArrayList<String>();
		while (in.length() > 0) {
			while (in.charAt(0) != '|') {
				collected += in.charAt(0);
				in = in.substring(1);
			}
			players.add(collected);
			collected = "";
			in = in.substring(1);
		}
	}

	public boolean isBusy() {
		return (players.size() >= maxPlayers || inGame);
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public void remove(String name) {
		players.remove(name);
		// Add in method to replace with AI?

	}

	public long getSeed() {
		return seed;
	}


	public void addPlayer(ShipSetupData data) {
		if (data == null) throw new IllegalArgumentException("ShipSetupData cannot be null");
		ships.add(data);
		players.add(data.getNickname());
	}

	public void addPlayer(String username) {
		players.add(username);
	}

	public ArrayList<String> getPlayers() {
		return players;
	}

	public String getHostName() {
		return hostName;
	}


	public void startGame(String clientName) {
		if (players.size() == 0) throw new IllegalStateException("Tried starting game with no players");
		if (ships.size() != players.size())
			throw new IllegalStateException("Mismatch between amount of ships and players when staring game.");

		if (clientName.equals(hostName)) {
			inGame = true;
			RaceSetupData setupData = setupRace();
			shipManager = new ServerShipManager(setupData, players.size(), maxPlayers - players.size(), trackPoints);
			ArrayList<CommQueue> allQueues = new ArrayList<CommQueue>();
			for (int i = 0; i < players.size(); i++) {
				table.getReceiver(players.get(i)).setGame(this, i);
				table.getQueue(players.get(i)).offer(new ByteArrayByte(Converter.sendRaceData(setupData, i), ServerComm.RACESETUPDATA));
				allQueues.add(table.getQueue(players.get(i)));
			}
			raceStartsAt = System.nanoTime() + TIME_TO_START;
			updatedUsers = new UpdateAllUsers(allQueues, this);
			updatedUsers.start();
		}
	}

	public void endGame() {
		inGame = false;
	}

	public void updateUser(int gameNum, byte[] msg) {
		shipManager.addPacket(msg);
	}

	public byte[] getShipPositions() {
		return shipManager.getPositionMessage();
	}

	public void addSetupData(int gameNum, byte[] msg) {
		ships.set(gameNum, Converter.buildShipData(msg));
	}

	public void addSetupData(int gameNum, String msg) {
		ships.set(gameNum, Converter.buildShipData(msg));
	}

	public RaceSetupData setupRace() {
		HashMap<Byte, ShipSetupData> resShips = new HashMap<Byte, ShipSetupData>();
		for (int i = 0; i < maxPlayers; i++) {
			if (i < ships.size()) resShips.put((byte) i, ships.get(i)); // Players
			else resShips.put((byte) i, AIBuilder.fakeAIData()); // AIs
		}
		Vector2f startDirection = new Vector2f(trackPoints.get(1)).sub(trackPoints.get(0));
		return new RaceSetupData(resShips, generateStartingPositions(startDirection),
			new Vector3f(0, (float) Math.atan2(startDirection.x, startDirection.y), 0), seed, TIME_TO_START, lapCount);
	}

	// TODO finish this
	private Map<Byte, Vector3f> generateStartingPositions(Vector2f startDirection) {
		// Map<Byte, Vector2f> res = new HashMap<Byte, Vector2f>();
		// float width = trackPoints.get(0).getWidth();
		// int shipsInRow = (int) width / SIDE_DISTANCES;
		// float sidePadding = (width - shipsInRow * SIDE_DISTANCES) / 2;
		// int shipsLeft = maxPlayers;
		// int currentRow = 0;
		// float startAngle = (float) Math.atan2(startDirection.x, startDirection.y);
		// float sin = (float) Math.cos(startAngle);
		// float cos = (float) Math.cos(startAngle);
		// Vector2f firstShip = new Vector2f((float) (trackPoints.get(0).x + cos * trackPoints.get(0).getWidth() / 2),
		// (float) (trackPoints.get(0).y + sin * trackPoints.get(0).getWidth() / 2));
		// while (shipsLeft > shipsInRow) {
		// for (int i = 0; i < shipsInRow; i++) {
		// res.put((byte) (maxPlayers - shipsLeft), new Vector2f(firstShip).add(SIDE_DISTANCES * i * cos, SIDE_DISTANCES * i * sin))
		// .add(FORWARD_DISTANCES * currentRow * sin, FORWARD_DISTANCES * currentRow * cos);
		// }
		// currentRow++;
		// }
		// float extraPadding = shipsLeft % 2 == 0 ? 0.5f : 0f;
		// if (shipsLeft % 2 == 0) {
		// float padding = (width - sidePadding * 2) / shipsLeft;
		// for (int i = 0; i < shipsLeft; i++) {
		// res.put((byte) (maxPlayers - shipsLeft),
		// new Vector2f(firstShip).add(padding * (i + extraPadding) * cos, padding * (i + extraPadding) * sin))
		// .add(FORWARD_DISTANCES * currentRow * sin, FORWARD_DISTANCES * currentRow * cos);
		// }
		// }
		// return res.entrySet().stream()
		// .collect(Collectors.toMap(e -> e.getKey(), e -> new Vector3f(e.getValue().x, STARTING_HEIGHT, e.getValue().y)));

		// TODO temporary thing here:
		Map<Byte, Vector3f> res = new HashMap<>();
		for (int i = 0; i < maxPlayers; i++) {
			System.out.println("Trackpoint gameroom: " + trackPoints.get(0));
			res.put((byte) i, new Vector3f(trackPoints.get(0).x + i * 40, 5, trackPoints.get(0).y));
		}
		return res;
	}

	private float getTrackDirection() {
		Vector2f relative = trackPoints.get(0).sub(trackPoints.get(1));
		return (float) Math.atan2(relative.x, relative.y);
	}

	public String toString() {
		String out = name + "|" + id + "|" + seed + "|" + maxPlayers + "|" + hostName + "|" + lapCount + "|";
		for (String p : players) {
			out += p + "|";
		}
		return out;
	}

	public byte[] toByteArray() {
		return toString().getBytes(ServerComm.charset);
	}

	public int getNoPlayers() {
		return maxPlayers;
	}

	public void update(float delta) {
		if (raceStartsAt == -1) throw new IllegalStateException("Update called before the race was started");
		if (System.nanoTime() >= raceStartsAt) shipManager.startRace();
		shipManager.update(delta);
	}

	public int getLaps() {
		return lapCount;
	}


}