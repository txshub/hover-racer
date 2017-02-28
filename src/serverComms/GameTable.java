package serverComms;

import java.util.*;

public class GameTable {
	Map<Integer, CommQueue> queueTable = new TreeMap<Integer, CommQueue>();
	Map<Integer, String> userIds = new TreeMap<Integer, String>();
	
	public int getId(String name) {
		for(Map.Entry<Integer, String> user: userIds.entrySet()) {
			if(user.getValue()==name) return user.getKey();
		}
		return -1;
	}
	
	public CommQueue getQueue(String name) {
		int id = getId(name);
		return queueTable.get(id);
	}
	
	public String getUsername(int id) {
		return userIds.get(id);
	}
}
