package serverComms;

import java.util.Map;
import java.util.TreeMap;

import physics.network.ShipSetupData;

/**
 * Client table holding information on all clients connected to a single server
 * 
 * @author simon
 *
 */
public class ClientTable {
  private Map<String, CommQueue> queueTable = new TreeMap<String, CommQueue>(); // The
                                                                                // CommQueue
                                                                                // for
                                                                                // each
                                                                                // user
  private Map<String, ServerReceiver> receivers = new TreeMap<String, ServerReceiver>(); // The
                                                                                         // relevant
                                                                                         // receiver
                                                                                         // on
                                                                                         // the
                                                                                         // server
  private Map<String, Integer> games = new TreeMap<String, Integer>(); // Users
                                                                       // connected
                                                                       // to
                                                                       // which
                                                                       // lobbies
  private Map<Integer, GameRoom> allGames = new TreeMap<Integer, GameRoom>(); // GameRooms
                                                                              // and
                                                                              // their
                                                                              // respective
                                                                              // IDs
  private int nextInt = 0; // Next gamelobby id

  private Lobby lobby; // The lobby this was called from

  /**
   * Creates a ClientTable object
   * 
   * @param lobby
   *          The lobby this was called from
   */
  public ClientTable(Lobby lobby) {
    this.lobby = lobby;
  }

  /**
   * Checks if the user exists already
   * 
   * @param name
   *          The name to check for existence on the server
   * @return Whether the user already exists
   */
  public boolean userExists(String name) {
    for (Map.Entry<String, CommQueue> entry : queueTable.entrySet()) {
      if (entry.getKey().equals(name)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Adds a user to the table
   * 
   * @param name
   *          The user to add
   */
  public void add(String name) {
    queueTable.put(name, new CommQueue());
  }

  /**
   * Adds the ServerReceiver for a user
   * 
   * @param name
   *          The user to add this for
   * @param receiver
   *          The receiver to add
   */
  public void addReceiver(String name, ServerReceiver receiver) {
    receivers.put(name, receiver);
  }

  /**
   * Removes a user from the table
   * 
   * @param name
   *          The user to remove
   */
  public void remove(String name) {
    queueTable.remove(name);
    receivers.remove(name);
    int gameId = getGameID(name);
    if (gameId != -1) allGames.get(gameId).remove(name);
    games.remove(name);
  }

  /**
   * Returns the queue associated with a given user
   * 
   * @param name
   *          The given user
   * @return The queue associated with a given user
   */
  public CommQueue getQueue(String name) {
    return queueTable.get(name);
  }

  /**
   * Gets the ServerReceiver for a given user
   * 
   * @param name
   *          The given user
   * @return The ServerReceiver for the given user
   */
  public ServerReceiver getReceiver(String name) {
    return receivers.get(name);
  }

  /**
   * Returns a map of users to queues
   * 
   * @return The map of users to queues
   */
  public Map<String, CommQueue> getQueues() {
    return queueTable;
  }

  /**
   * Gets the Game ID of the game a specified user is in
   * 
   * @param clientName
   *          The specified user
   * @return The GameID for a user (-1 if no game)
   */
  public int getGameID(String clientName) {
    for (Map.Entry<String, Integer> g : games.entrySet()) {
      if (g.getKey().equals(clientName)) {
        return g.getValue();
      }
    }
    return -1;
  }

  /**
   * Gets the gameroom associated with an ID
   * 
   * @param gameID
   *          The ID for the room
   * @return The GameRoom for a given ID
   */
  public GameRoom getGame(int gameID) {
    return allGames.get(gameID);
  }

  /**
   * Adds a game with given settings
   * 
   * @param gameSettings
   *          The given settings
   * @return True if the game was succesfully joined, False if there was a problem making the game
   */
  public boolean addGame(GameSettings gameSettings) {
    allGames.put(nextInt, new GameRoom(nextInt, gameSettings.lobbyName, gameSettings.seed,
        gameSettings.maxPlayers, gameSettings.setupData.nickname, gameSettings.lapCount, this));
    lobby.games.add(getGame(nextInt));
    nextInt++;
    return joinGame(nextInt - 1, gameSettings.setupData);
  }

  /**
   * Joins a game
   * 
   * @param gameNum
   *          The game number to join
   * @param data
   *          The data with which to connect
   * @return True if the user was succesfully joined to the lobby. False if not (i.e the gameroom no
   *         longer exists)
   */
  public boolean joinGame(int gameNum, ShipSetupData data) {
    for (Map.Entry<Integer, GameRoom> g : allGames.entrySet()) {
      if (g.getValue().id == gameNum) {
        games.put(data.nickname, gameNum);
        g.getValue().addPlayer(data);
        return true;
      }
    }
    return false;
  }
}