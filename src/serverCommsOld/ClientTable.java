package serverCommsOld;
import java.util.*;

public class ClientTable {

	private Map<String,StringQueue> queueTable = new TreeMap<String, StringQueue>();
	
	public boolean userExists(String name) {
		for(Map.Entry<String, StringQueue> entry : queueTable.entrySet()) {
			if(entry.getKey().equals(name)) return true;
		}
		return false;
	}

	public void add(String name) {
		queueTable.put(name, new StringQueue());
		
	}

	public StringQueue getQueue(String name) {
		return queueTable.get(name);
	}

}
