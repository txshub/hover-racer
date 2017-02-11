package serverComms;
import java.util.*;

public class ClientTable {

	private Map<String,CommQueue> queueTable = new TreeMap<String,CommQueue>();
	
	public boolean userExists(String name) {
		for(Map.Entry<String, CommQueue> entry : queueTable.entrySet()) {
			if(entry.getKey().equals(name)) return true;
		}
		return false;
	}

	public void add(String name) {
		queueTable.put(name, new CommQueue());
		
	}

	public CommQueue getQueue(String name) {
		return queueTable.get(name);
	}

}
