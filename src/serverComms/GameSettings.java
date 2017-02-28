package serverComms;

public class GameSettings {

	final long seed;
	final int maxPlayers;
	final int numAI;
	final int lapCount;
	final String name;
	
	public GameSettings(long seed, int maxPlayers, int numAI, int lapCount, String name) throws IllegalArgumentException {
		this.seed = seed;
		this.maxPlayers = maxPlayers;
		this.numAI = numAI;
		this.name = name;
		this.lapCount = lapCount;
		if(name.contains(System.lineSeparator())) throw new IllegalArgumentException("Name shouldn't contain new line symbol");
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
		name = in.substring(1);
	}
	
	public String toString() {
		return seed + "|" + maxPlayers + "|" + numAI + "|" + lapCount + "|" + name;
	}
	
	public byte[] toByteArray() {
		return toString().getBytes(ServerComm.charset);
	}
	
}
