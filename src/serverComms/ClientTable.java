package serverComms;
import java.util.*;
/**
 * Client table holding information on all clients connected to a single server
 * @author simon
 *
 */
public class ClientTable {

	private Map<String,CommQueue> queueTable = new TreeMap<String,CommQueue>();
	private Map<String,Lobby> games = new TreeMap<String,Lobby>();
	private Map<Integer,Lobby> allLobbies = new TreeMap<Integer,Lobby>(); 
	
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
	
	public void remove(String name) {
		queueTable.remove(name);
		int game = getGame(name);
		if(game!=-1) allLobbies.get(games).remove(name);
	}

	/**
	 * Returns the queue associated with a given user
	 * @param name The given user
	 * @return The queue associated with a given user
	 */
	public CommQueue getQueue(String name) {
		return queueTable.get(name);
	}

	public Map<String, CommQueue> getQueues() {
		return queueTable;
	}

	public int getGame(String clientName) {
		for(Map.Entry<String, Lobby> g : games.entrySet()) {
			if(g.getKey()==clientName) return g.getValue().getID();
		}
		return -1;
	}

}
