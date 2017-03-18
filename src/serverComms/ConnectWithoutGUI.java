package serverComms;

import java.io.IOException;

import clientComms.Client;
import physics.network.ShipSetupData;
import physics.placeholders.DataGenerator;

public class ConnectWithoutGUI {

	private static final int PORT = 4444;
	private static final String MACHINE_NAME = "2001:0:9d38:78cf:5e:b9d:f5eb:edce";// "10.20.18.49";
	private static Client client;
	private static GameRoom gameRoom;
	private static final int GAME_ID = 0;
	private static final String SEED = "5555";
	private static final int MAX_PLAYERS = 3;
	private static final int LAPS = 2;
	private static final String LOBBY_NAME = "Testonium";

	public static void main(String[] args) throws IOException, InterruptedException {

		// Start the server
		// Lobby serverLobby = new Lobby(4444);
		// ServerComm server = new ServerComm(4444, serverLobby);
		// server.start();

		// Create and connect the user
		String usr = "Mac";
		int portNo = PORT;
		String machineName = MACHINE_NAME;

		client = new Client(usr, PORT, MACHINE_NAME);
		client.start();

		// Create a game room

		ShipSetupData data = DataGenerator.basicShipSetup("Mac");
		client.joinGame(GAME_ID, data);

		// Start the game
		// client.startGame();

		/* long closeAt = System.nanoTime() + 10000000000l; while (System.nanoTime()
		 * < closeAt) { Thread.sleep(100); }
		 * System.out.println("---------------EXITING-----------------"); */

	}

}