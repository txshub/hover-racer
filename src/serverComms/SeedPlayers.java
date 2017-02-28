package serverComms;

import java.util.ArrayList;

public class SeedPlayers {
	
	final long seed;
	final ArrayList<String> players;
	
	public SeedPlayers(long seed, ArrayList<String> players) {
		this.seed = seed;
		this.players = players;
	}
	
	public SeedPlayers(String in) {
		String collected = "";
		while(in.charAt(0)!='|') {
			collected += in.charAt(0);
			in = in.substring(1);
		}
		seed = Long.parseLong(collected);
		collected = "";
		in = in.substring(1);
		players = new ArrayList<String>();
		while(in.length() > 0) {
			while(in.charAt(0) != '|') {
				collected += in.charAt(0);
				in = in.substring(1);
			}
			players.add(collected);
			collected = "";
			in = in.substring(1);
		}
	}
	
	public String toString() {
		String out = String.valueOf(seed) + "|";
		for(String name : players) {
			out += name + "|"; 
		}
		return out;
	}
	
	public byte[] toByteArray() {
		return toString().getBytes(ServerComm.charset);
	}

}
