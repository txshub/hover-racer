package serverComms;

import java.util.ArrayList;

public class GameRoom {
	
	ArrayList<String> players = new ArrayList<String>(); 

	String name;
	final int id;
	private long seed;
	private int maxPlayers;
	private boolean inGame = false;
	private String hostName;
	private ClientTable table;
	
	public GameRoom(int id, String name, long seed, int maxPlayers, String hostName, ClientTable table) {
		this.id = id;
		this.name = name;
		this.seed = seed;
		this.maxPlayers = maxPlayers;
		this.hostName = hostName;
		this.table = table;
	}
	
	public boolean isBusy() {
		return(players.size()>=maxPlayers || inGame);
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
			inGame = true;
			for(int i = 0; i < players.size(); i++) {
				table.getReceiver(players.get(i)).setGame(this, i);
				table.getQueue(players.get(i)).offer(new ByteArrayByte(String.valueOf(i).getBytes(ServerComm.charset), ServerComm.STARTGAME));
			}
		}
	}
	
	public void endGame() {
		inGame = false;
	}

	public void updateUser(int gameNum, byte[] msg) {
		// TODO What to do here?
		
	}
	
}
