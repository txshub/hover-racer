package serverComms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.joml.Vector2f;
import org.joml.Vector3f;

import physics.network.RaceSetupData;
import physics.network.ShipSetupData;
import serverLogic.AIBuilder;
import serverLogic.Converter;
import serverLogic.GameLogic;
import serverLogic.ServerShipManager;
import trackDesign.SeedTrack;
import trackDesign.TrackMaker;
import trackDesign.TrackPoint;
import userInterface.MainMenu;

/**
 * The GameRoom clients can join
 * 
 * @author simon mostly, specified where not
 */
public class GameRoom {

  // Time between sending the RaceSetupData and start of the race, in
  // nanoseconds
  // TODO It's currently low for easy resting, might increase it for the actual
  // game
  private static final long TIME_TO_START = 15L * 1000000000L; // 15 sec

  // Time between the first player (not AI) finishing the race and the race
  // ending, in nanoseconds.
  private static final long TIME_TO_END = 30L * 1000000000L; // 30 sec

  private static final int SIDE_DISTANCES = 10;
  private static final int FORWARD_DISTANCES = 10;
  private static final int STARTING_HEIGHT = 10;
  ArrayList<String> players = new ArrayList<String>();
  ArrayList<ShipSetupData> ships;
  String name;
  public final int id;
  private String seed;
  private int maxPlayers;
  private boolean inGame = false;
  private String hostName;
  private int lapCount;
  private ClientTable table;
  private ArrayList<TrackPoint> trackPoints;
  private ServerShipManager shipManager;
  private GameLogic logic;
  private UpdateAllUsers updatedUsers;
  private long raceStartsAt = -1;
  private long raceEndsAt = -1;

  private ArrayList<CommQueue> allQueues = null;

  private boolean endGameSent = false;

  /**
   * Makes a GameRoom object
   * 
   * @param id
   *          The GameRoom's id
   * @param name
   *          The GameRoom's name
   * @param seed
   *          The seed to make the track with
   * @param maxPlayers
   *          The max number of players
   * @param hostName
   *          The host's name
   * @param lapCount
   *          The number of laps to complete
   * @param table
   *          The client table for communications
   */
  public GameRoom(int id, String name, String seed, int maxPlayers, String hostName, int lapCount,
      ClientTable table) {
    if (ServerComm.DEBUG)
      System.out.println(
          hostName + " created a game room " + name + " with id " + id + " and seed " + seed);
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
    trackPoints = st.getTrack();
  }

  /**
   * Reconstructs a server from a GameRoom string sent across the network
   * 
   * @param in
   *          The GameRoom string
   */
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
    seed = collected;
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

  /**
   * Returns whether the room is currently busy (max players or in a game)
   * 
   * @return Whether the room is currently busy
   */
  public boolean isBusy() {
    return (players.size() >= maxPlayers || inGame);
  }

  /**
   * Gets the name of the lobby
   * 
   * @return The name of the lobby
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the ID of the lobby
   * 
   * @return The ID of the lobby
   */
  public int getId() {
    return id;
  }

  /**
   * Removes a user from the lobby
   * 
   * @param name
   *          The user to remove
   */
  public void remove(String name) {
    players.remove(name);
    // Add in method to replace with AI?
  }

  /**
   * Gets the seed for track generation
   * 
   * @return The seed for track generation
   */
  public String getSeed() {
    return seed;
  }

  /**
   * Adds a player to the lobby
   * 
   * @param data
   *          The ship data for the user
   */
  public void addPlayer(ShipSetupData data) {
    if (data == null)
      throw new IllegalArgumentException("ShipSetupData cannot be null");
    ships.add(data);
    players.add(data.getNickname());
  }

  /**
   * Gets a list of all connected players
   * 
   * @return A list of all connected players
   */
  public ArrayList<String> getPlayers() {
    return players;
  }

  /**
   * Gets the name of the host
   * 
   * @return The host's name
   */
  public String getHostName() {
    return hostName;
  }

  /**
   * @author Mac Starts the game
   * @param clientName
   *          The name of the caller (to check it is the host calling this)
   */
  public void startGame(String clientName) {
    if (players.size() == 0)
      throw new IllegalStateException("Tried starting game with no players");
    if (ships.size() != players.size())
      throw new IllegalStateException(
          "Mismatch between amount of ships and players when staring game.");
    if (clientName.equals(hostName)) {
      inGame = true;
      RaceSetupData setupData = setupRace();
      shipManager = new ServerShipManager(setupData, players.size(), maxPlayers - players.size(),
          trackPoints, setupData.startingOrientation);
      logic = new GameLogic(shipManager.getShipsLogics(), trackPoints, lapCount, players.size(),
          this);
      allQueues = new ArrayList<CommQueue>();
      for (int i = 0; i < players.size(); i++) {
        table.getReceiver(players.get(i)).setGame(this, i);
        // sendMessage((byte) i, Converter.sendRaceData(setupData, i),
        // ServerComm.RACESETUPDATA);
        table.getQueue(players.get(i)).offer(
            new ByteArrayByte(Converter.sendRaceData(setupData, i), ServerComm.RACESETUPDATA));
        allQueues.add(table.getQueue(players.get(i)));
      }
      raceStartsAt = System.nanoTime() + TIME_TO_START;
      updatedUsers = new UpdateAllUsers(allQueues, this);
      updatedUsers.start();
      MainMenu.allThreads.add(0, updatedUsers);
    }
  }

  /** @author Mac Called when the game ends */
  // public void endGame() {
  // //inGame = false;
  // // If the host is still in the room, don't end the game
  // if (players.contains(hostName)) return;
  // // Otherwise send the closed methods to all currently connected clients
  // for (int i = 0; i < players.size(); i++) {
  // table.getQueue(players.get(i)).offer(new ByteArrayByte(new byte[0],
  // ServerComm.ROOMCLOSED));
  // }
  // }

  /**
   * @author Mac
   * @param gameNum
   *          The user's ID
   * @param msg
   *          The msg to update with
   */
  public void updateUser(int gameNum, byte[] msg) {
    shipManager.addPacket(msg);
  }

  /**
   * @author Mac Gets all the ship positions
   * @return all the ship positions
   */
  public byte[] getShipPositions() {
    return shipManager.getPositionMessage();
  }

  /**
   * @author Mac Adds setupdata for a user
   * @param gameNum
   *          The user's ID
   * @param msg
   *          The msg to update with
   */
  public void addSetupData(int gameNum, byte[] msg) {
    ships.set(gameNum, Converter.buildShipData(msg));
  }

  /**
   * @author Mac Adds setupdata for a user
   * @param gameNum
   *          The user's ID
   * @param msg
   *          The msg to update with
   */
  public void addSetupData(int gameNum, String msg) {
    ships.set(gameNum, Converter.buildShipData(msg));
  }

  /**
   * @author Mac Returns the setup data for a race
   * @return The setup data for a race
   */
  public RaceSetupData setupRace() {
    HashMap<Byte, ShipSetupData> resShips = new HashMap<Byte, ShipSetupData>();
    for (int i = 0; i < maxPlayers; i++) {
      if (i < ships.size())
        resShips.put((byte) i, ships.get(i)); // Players
      else
        resShips.put((byte) i, AIBuilder.fakeAIData()); // AIs
    }
    Vector2f startDirection = new Vector2f(trackPoints.get(1)).sub(trackPoints.get(0));
    float facingAngle = (float) Math.toDegrees(Math.atan2(startDirection.x, startDirection.y));
    return new RaceSetupData(resShips, generateStartingPositions(startDirection),
        new Vector3f(0, facingAngle, 0), seed, TIME_TO_START, lapCount);
  }

  // TODO finish this
  private Map<Byte, Vector3f> generateStartingPositions(Vector2f startDirection) {
    float offset = 40;
    Map<Byte, Vector3f> res = new HashMap<>();

    for (int i = 0; i < maxPlayers; i++) {
      float xOffset = (i % 4 - 2) * offset;
      float yOffset = (float) (Math.floor(i / 4)) * offset;

      Vector2f forward = new Vector2f(trackPoints.get(0))
          .sub(trackPoints.get(trackPoints.size() - 1)).normalize();
      Vector2f left = new Vector2f(-forward.y, forward.x).normalize();

      left.mul(xOffset);
      forward.mul(-yOffset);

      Vector2f combined = new Vector2f(left).add(forward);

      res.put((byte) i,
          new Vector3f(trackPoints.get(0).x + combined.x, 5, trackPoints.get(0).y + combined.y));
    }
    return res;
  }

  /**
   * @author Mac Returns the direction the track starts in
   * @return The direction the track starts in
   */
  private float getTrackDirection() {
    Vector2f relative = trackPoints.get(0).sub(trackPoints.get(1));
    return (float) Math.atan2(relative.x, relative.y);
  }

  /** Returns a string representing this room */
  public String toString() {
    String out = name + "|" + id + "|" + seed + "|" + maxPlayers + "|" + hostName + "|" + lapCount
        + "|";
    for (String p : players) {
      out += p + "|";
    }
    return out;
  }

  /**
   * Returns a byte array formed by toString()
   * 
   * @return a byte array formed by toString()
   */
  public byte[] toByteArray() {
    return toString().getBytes(ServerComm.charset);
  }

  /**
   * Returns the max number of players
   * 
   * @return The max number of players
   */
  public int getNoPlayers() {
    return maxPlayers;
  }

  /**
   * @author Mac Updates all users
   * @param delta
   *          The time passed since the last update
   */
  public void update(float delta) {
    if (raceStartsAt == -1)
      throw new IllegalStateException("Update called before the race was started");
    if (System.nanoTime() >= raceStartsAt)
      shipManager.startRace();
    if (raceEndsAt != -1 && (logic.raceFinished() || System.nanoTime() >= raceEndsAt)) {
      if (!endGameSent) {
        updatedUsers.interrupt();
        for (CommQueue queue : allQueues) {
          queue.offer(new ByteArrayByte(logic.getRanking(), ServerComm.END_GAME));
        }
        endGameSent = true;
      }
    }
    shipManager.update(delta);
    logic.update();
  }

  /**
   * @author Mac Sends a game logic update to a single client
   * @param id
   *          Client's (and ship's) id
   */
  public void sendLogicUpdate(byte id, int ranking, boolean finished, int currrentLap) {
    sendMessage(id, Converter.buildLogicData(ranking, finished, currrentLap),
        ServerComm.LOGIC_UPDATE);
    // table.getQueue(players.get(id))
    // .offer(new ByteArrayByte(Converter.buildLogicData(ranking, finished,
    // currrentLap),
    // ServerComm.LOGIC_UPDATE));
  }

  /**
   * Sends information about player who finished the race (i.e. the leaderboard)
   * to players who already finished to race.
   * 
   * @param id
   *          Id of the client to send the message to
   * @param data
   *          Data being sent - an array of IDs in the order of finishing (index
   *          0 is first place). Should only contain IDs of players who already
   *          finished
   */
  public void sendFinishData(byte id, byte[] data) {
    sendMessage(id, data, ServerComm.FINISH_DATA);
    // table.getQueue(players.get(id)).offer(new ByteArrayByte(data,
    // ServerComm.FINISH_DATA));
    // Sending this message means someone has finished the race
    if (raceEndsAt == -1)
      raceEndsAt = System.nanoTime() + TIME_TO_END;
  }

  private void sendMessage(byte id, byte[] message, byte type) {
    try {
      table.getQueue(players.get(id)).offer(new ByteArrayByte(message, type));
    } catch (Exception e) {
    }
  }

  /**
   * Returns the number of laps in this race
   * 
   * @return The number of laps in this race
   */
  public int getLaps() {
    return lapCount;
  }
}