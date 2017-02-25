package serverComms;

public class GameRoom {

	String name;
	final int id;
	private long seed;
	private int maxPlayers;
	private int numAI;
	
	public GameRoom(int id, String name, long seed, int maxPlayers, int numAI) {
		this.id = id;
		this.name = name;
		this.seed = seed;
		this.maxPlayers = maxPlayers;
		this.numAI = numAI;
		
	}
	
	public String getName() {
		return name;
	}
	
	public int getId() {
		return id;
	}

	public void remove(String name) {
		// TODO
		
	}

	public long getSeed() {
		return seed;
	}
	
}
