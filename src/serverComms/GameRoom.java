package serverComms;

public class GameRoom {

	String name;
	final int id;
	
	public GameRoom(String name, int id) {
		this.name = name;
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
}
