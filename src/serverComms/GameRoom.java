package serverComms;

import java.util.ArrayList;

public class GameRoom {
	
	ArrayList<String> players = new ArrayList<String>(); 

	String name;
	final int id;
	private long seed;
	private int maxPlayers;
	private int numAI;
	private String hostName;
	private ClientTable table;
	
	public GameRoom(int id, String name, long seed, int maxPlayers, int numAI, String hostName, ClientTable table) {
		this.id = id;
		this.name = name;
		this.seed = seed;
		this.maxPlayers = maxPlayers;
		this.numAI = numAI;
		this.hostName = hostName;
		this.table = table;
		
	}
	
	public String getName() {
		return name;
	}
	
	public int getId() {
		return id;
	}

	public void remove(String name) {
		players.remove(name);
		//Add in method to replace with AI?
		
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
		if(clientName == hostName) {
			for(int i = 0; i < players.size(); i++) {
				table.getReceiver(players.get(i)).setGame(this, i);
			}
		}
	}
	
}
