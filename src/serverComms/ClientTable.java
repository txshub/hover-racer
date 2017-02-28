package serverComms;
import java.util.*;
/**
 * Client table holding information on all clients connected to a single server
 * @author simon
 *
 */
public class ClientTable {

	private Map<String,CommQueue> queueTable = new TreeMap<String,CommQueue>(); //The CommQueue for each user
	private Map<String,ServerReceiver> receivers = new TreeMap<String,ServerReceiver>(); //The relevant receiver on the server
	private Map<String,Integer> games = new TreeMap<String,Integer>(); //Users connected to which lobbies
	private Map<Integer,GameRoom> allGames = new TreeMap<Integer,GameRoom>(); //GameRooms and their respective IDs
	private int nextInt = 0;
	
	/**
	 * Checks if the user exists already
	 * @param name The name to check for existence on the server
	 * @return Whether the user already exists
	 */
	public boolean userExists(String name) {
		for(Map.Entry<String, CommQueue> entry : queueTable.entrySet()) {
			if(entry.getKey().equals(name)) return true;
		}
		return false;
	}

	/**
	 * Adds a user to the table
	 * @param name The user to add
	 */
	public void add(String name) {
		queueTable.put(name, new CommQueue());	
	}
	
	public void addReceiver(String name, ServerReceiver receiver) {
		receivers.put(name, receiver);
	}
	
	public void remove(String name) {
		queueTable.remove(name);
		int gameId = getGameID(name);
		if(gameId !=-1) allGames.get(gameId).remove(name);
	}

	/**
	 * Returns the queue associated with a given user
	 * @param name The given user
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
		for(Map.Entry<String, Integer> g : games.entrySet()) {
			if(g.getKey()==clientName) return g.getValue();
		}
		return -1;
	}

	public GameRoom getGame(int gameID) {
		return allGames.get(gameID);
	}
	
	

	public void addGame(GameSettings gameSettings) {
		allGames.put(nextInt, new GameRoom(nextInt, gameSettings.lobbyName, gameSettings.seed, gameSettings.maxPlayers,gameSettings.hostName, this));
		nextInt++;
	}

	public boolean joinGame(String clientName, int gameNum) {
		if(allGames.get(gameNum)==null) return false; //Show that no game exists
		games.put(clientName, gameNum);
		allGames.get(gameNum).addPlayer(clientName);
		return true;
		
	}

}