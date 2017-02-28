package serverComms;

public class GameNameNumber {

	private final String name;
	private final int id;
	
	public GameNameNumber(String name, int id) {
		this.name = name;
		this.id = id;
	}
	
	public GameNameNumber(String create) {
		String collected = "";
		while(create.charAt(0)!='|') {
			collected += create.charAt(0);
			create = create.substring(1);
		}
		id = Integer.parseInt(collected);
		name = create.substring(1);
	}
	
	public String toString() {
		return id + "|" + name;
	}
	
	public byte[] toByteArray() {
		return toString().getBytes(ServerComm.charset);
	}
	
}
