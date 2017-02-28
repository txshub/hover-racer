package serverComms;

public class GameSettings {

	final long seed;
	final int maxPlayers;
	final int numAI;
	final int lapCount;
	final String lobbyName;
	final String hostName;
	
	public GameSettings(long seed, int maxPlayers, int numAI, int lapCount, String lobbyName, String hostName) throws IllegalArgumentException {
		this.seed = seed;
		this.maxPlayers = maxPlayers;
		this.numAI = numAI;
		this.lobbyName = lobbyName;
		this.lapCount = lapCount;
		this.hostName = hostName;
		if(lobbyName.contains(System.lineSeparator())) throw new IllegalArgumentException("Name shouldn't contain new line symbol");
	}
	
	public GameSettings(String in) {
		String collected = "";
		while(in.charAt(0)!='|') {
			collected += in.charAt(0);
			in = in.substring(1);
		}
		seed = Long.valueOf(collected);
		in = in.substring(1);
		collected = "";
		while(in.charAt(0)!='|') {
			collected += in.charAt(0);
			in = in.substring(1);
		}
		maxPlayers = Integer.valueOf(collected);
		in = in.substring(1);
		collected = "";
		while(in.charAt(0)!='|') {
			collected += in.charAt(0);
			in = in.substring(1);
		}
		numAI = Integer.valueOf(collected);
		in = in.substring(1);
		collected = "";
		while(in.charAt(0)!='|') {
			collected += in.charAt(0);
			in = in.substring(1);
		}
		lapCount = Integer.valueOf(collected);
		in = in.substring(1);
		collected = "";
		while(in.charAt(0)!='|') {
			collected += in.charAt(0);
			in = in.substring(1);
		}
		lobbyName = collected;
		hostName = in.substring(1);
	}
	
	public String toString() {
		return seed + "|" + maxPlayers + "|" + numAI + "|" + lapCount + "|" + lobbyName + "|" + hostName;
	}
	
	public byte[] toByteArray() {
		return toString().getBytes(ServerComm.charset);
	}
	
}
