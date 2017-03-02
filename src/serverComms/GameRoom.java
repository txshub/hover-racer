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


	private static final long TIME_TO_START = 3000000000L; // To to start race in nanoseconds
	private static final int SIDE_DISTANCES = 10;
	private static final int FORWARD_DISTANCES = 10;
	private static final int STARTING_HEIGHT = 10;
	ArrayList<String> players = new ArrayList<String>();
	ArrayList<ShipSetupData> ships;

	String name;
	final int id;
	private long seed;
	private int maxPlayers;
	private boolean inGame = false;
	private String hostName;
	private ClientTable table;
	private ArrayList<TrackPoint> trackPoints;

	private ServerShipManager shipManager;

	public GameRoom(int id, String name, long seed, int maxPlayers, String hostName, ClientTable table) {
		this.id = id;
		this.name = name;
		this.seed = seed;
		this.maxPlayers = maxPlayers;
		this.hostName = hostName;
		this.table = table;
		this.ships = new ArrayList<ShipSetupData>(maxPlayers);
		// Generate the track
		SeedTrack st = TrackMaker.makeTrack(seed, 10, 20, 30, 1, 40, 40, 4);
		trackPoints = st.getTrack();
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

	public void addPlayer(String clientName) {
		players.add(clientName);
	}

	public ArrayList<String> getPlayers() {
		return players;
	}

	public void startGame(String clientName) {
		if (clientName == hostName) {
			inGame = true;
			RaceSetupData setupData = setupRace();
			shipManager = new ServerShipManager(setupData, players.size(), maxPlayers - players.size());
			for (int i = 0; i < players.size(); i++) {
				table.getReceiver(players.get(i)).setGame(this, i);
				table.getQueue(players.get(i)).offer(new ByteArrayByte(Converter.sendRaceData(setupData, i), ServerComm.RACESETUPDATA));
			}
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

	public RaceSetupData setupRace() {
		HashMap<Byte, ShipSetupData> resShips = new HashMap<Byte, ShipSetupData>();
		for (int i = 0; i < ships.size(); i++) {
			resShips.put((byte) i, ships.get(i));
		}
		Vector2f startDirection = trackPoints.get(0).sub(trackPoints.get(1));
		return new RaceSetupData(resShips, generateStartingPositions(startDirection),
			new Vector3f(startDirection.x, STARTING_HEIGHT, startDirection.y), seed, TIME_TO_START);
	}

	// TODO Those are not finished
	private Map<Byte, Vector3f> generateStartingPositions(Vector2f startDirection) {
		Map<Byte, Vector2f> res = new HashMap<Byte, Vector2f>();
		float width = trackPoints.get(0).getWidth();
		int shipsInRow = (int) width / SIDE_DISTANCES;
		float sidePadding = (width - shipsInRow * SIDE_DISTANCES) / 2;
		int shipsLeft = maxPlayers;
		int currentRow = 0;
		float startAngle = (float) Math.atan2(startDirection.x, startDirection.y);
		float sin = (float) Math.cos(startAngle);
		float cos = (float) Math.cos(startAngle);
		Vector2f firstShip = new Vector2f((float) (trackPoints.get(0).x + cos * trackPoints.get(0).getWidth() / 2),
			(float) (trackPoints.get(0).y + sin * trackPoints.get(0).getWidth() / 2));
		while (shipsLeft > shipsInRow) {
			for (int i = 0; i < shipsInRow; i++) {
				res.put((byte) (maxPlayers - shipsLeft), new Vector2f(firstShip).add(SIDE_DISTANCES * i * cos, SIDE_DISTANCES * i * sin))
					.add(FORWARD_DISTANCES * currentRow * sin, FORWARD_DISTANCES * currentRow * cos);
			}
		}
		return null; // TODO
	}

	private float getTrackDirection() {
		Vector2f relative = trackPoints.get(0).sub(trackPoints.get(1));
		return (float) Math.atan2(relative.x, relative.y);
	}

}