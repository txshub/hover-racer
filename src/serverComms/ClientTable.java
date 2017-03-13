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
  private int nextInt = 0;

  /**
   * Checks if the user exists already
   * 
   * @param name
   *          The name to check for existence on the server
   * @return Whether the user already exists
   */
  public boolean userExists(String name) {
    for (Map.Entry<String, CommQueue> entry : queueTable.entrySet()) {
      if (entry.getKey().equals(name))
        return true;
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

  public void addReceiver(String name, ServerReceiver receiver) {
    receivers.put(name, receiver);
  }

  public void remove(String name) {
    queueTable.remove(name);
    receivers.remove(name);
    int gameId = getGameID(name);
    if (gameId != -1)
      allGames.get(gameId).remove(name);
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

  public ServerReceiver getReceiver(String name) {
    return receivers.get(name);
  }

  public Map<String, CommQueue> getQueues() {
    return queueTable;
  }

  public int getGameID(String clientName) {
    for (Map.Entry<String, Integer> g : games.entrySet()) {
    	if (g.getKey().equals(clientName)) {
    		return g.getValue();
    	}
    }
    return -1;
  }

  public GameRoom getGame(int gameID) {
    return allGames.get(gameID);
  }

  public boolean addGame(GameSettings gameSettings) {
    allGames.put(nextInt, new GameRoom(nextInt, gameSettings.lobbyName, gameSettings.seed,
        gameSettings.maxPlayers, gameSettings.hostName, gameSettings.lapCount, this));
    nextInt++;
    return joinGame(nextInt-1, gameSettings.setupData);
  }

  public boolean joinGame(int gameNum, ShipSetupData data) {
	for(Map.Entry<Integer, GameRoom> g : allGames.entrySet()) {
		if (g.getValue().id==gameNum) {
			games.put(data.nickname, gameNum);
			g.getValue().addPlayer(data);
			return true;
		}
    }
    return false;

  }

}
