package serverLogic;

import serverComms.ClientTable;
import serverComms.GameRoom;

public class DummyGameRoom extends GameRoom {

	public DummyGameRoom(int id, String name, String seed, int maxPlayers, String hostName, int lapCount,
			ClientTable table) {
		super(id, name, seed, maxPlayers, hostName, lapCount, table);
	}

	public void sendLogicUpdate(byte id, int ranking, boolean finished, int currrentLap) {
		// Do nothing
	}
}
