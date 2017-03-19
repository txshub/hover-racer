package serverLogic;

import serverComms.ClientTable;
import serverComms.GameRoom;

/**
 * Dummy class used for testing
 * 
 * @author Tudor Suruceanu
 */
public class DummyGameRoom extends GameRoom {

	/** Constructor */
	public DummyGameRoom(int id, String name, String seed, int maxPlayers, String hostName, int lapCount,
			ClientTable table) {
		super(id, name, seed, maxPlayers, hostName, lapCount, table);
	}

	@Override
	public void sendLogicUpdate(byte id, int ranking, boolean finished, int currrentLap) {
		// Do nothing
	}
}
