package clientComms;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

import audioEngine.AudioMaster;
import game.MainGameLoop;
import javafx.application.Platform;
import physics.network.RaceSetupData;
import physics.ships.MultiplayerShipManager;
import serverComms.ByteArrayByte;
import serverComms.GameRoom;
import serverComms.ServerComm;
import serverLogic.Converter;
import userInterface.MainMenu;

/** Thread to receive any messages passed from the server
 * 
 * @author simon */
public class ClientReceiver extends Thread {

	private DataInputStream server;
	private Client client;
	private MultiplayerShipManager manager;

	/** Creates a ClientReceiver object
	 * 
	 * @param server
	 *        the stream to listen on for any messages from the server
	 * @param client
	 *        The client to send messages to the server through if need be */
	public ClientReceiver(DataInputStream server, Client client) {
		this.server = server;
		this.client = client;
	}

	/** Waits for messages from the server then deals with then appropriately */
	@Override
	public void run() {
		try {
			while (!this.isInterrupted()) {
				byte[] msg = new byte[server.readInt()];
				server.readFully(msg);
				if (msg == null || msg.length == 0) {
					server.close();
					throw new IOException("Got null from the server");
				}
				ByteArrayByte fullMsg = new ByteArrayByte(msg);
				if (fullMsg.getType() == ServerComm.BADUSER) {
					System.out.println("Username not valid, please pick another");
					System.exit(1);
				} else if (fullMsg.getType() == ServerComm.ACCEPTEDUSER) {
					System.out.println("Username valid. Now connected to the server");
				} else if (fullMsg.getType() == ServerComm.BADPACKET) {
					System.out.println("Need To Reconnect");
				} else if (fullMsg.getType() == ServerComm.SENDALLGAMES) {
					String[] allGames = (new String(fullMsg.getMsg(), ServerComm.charset)).split(System.lineSeparator());
					ArrayList<GameRoom> gameList = new ArrayList<GameRoom>();
					for (String s : allGames) {
						if (!s.equals("")) gameList.add(new GameRoom(s));
					}
					client.setGameList(gameList);
				} else if (fullMsg.getType() == ServerComm.INVALIDGAME) {
					client.setCurrentRoom(null);
				} else if (fullMsg.getType() == ServerComm.VALIDGAME) {
					GameRoom gr = new GameRoom(new String(fullMsg.getMsg(), ServerComm.charset));
					client.setCurrentRoom(gr);
				} else if (fullMsg.getType() == ServerComm.RACESETUPDATA) {
					RaceSetupData data = Converter.receiveRaceData(fullMsg.getMsg());
					AudioMaster.stopMusic();
					AudioMaster.cleanUp();
					try {
						Platform.runLater(new Runnable() {

							public void run() {
								MainMenu.hideScene();
							}
						});
					} catch (IllegalStateException e) {
						System.out.println("Game created without GUI");
					}
					MainGameLoop.startMultiplayerGame(data, client);
				} else if (fullMsg.getType() == ServerComm.FULLPOSITIONUPDATE) {
					if (client.getManager() == null)
						throw new IllegalStateException("Position update received but ship manager was not added to ClientReceiver");
					client.getManager().addPacket(fullMsg.getMsg());
				} else if (fullMsg.getType() == ServerComm.ROOMCLOSED) {
					// Go back to multiplayer menu (need to speak to Andreea)
				} else if (fullMsg.getType() == ServerComm.LOGIC_UPDATE) {
					if (client.multiplayerGame != null)
						client.multiplayerGame.updateLogic(Converter.receiveRanking(fullMsg.getMsg()),
							Converter.receiveFinished(fullMsg.getMsg()), Converter.receiveCurrentLap(fullMsg.getMsg()));
				} else if (fullMsg.getType() == ServerComm.FINISH_DATA) {
					if (client.multiplayerGame != null) client.multiplayerGame.updateFinishData(fullMsg.getMsg());
				} else if (fullMsg.getType() == ServerComm.END_GAME) {
					System.out.println("End Game Received");
					if (client.multiplayerGame != null) {
						System.out.println("Not Null");
						client.multiplayerGame.updateFinishData(fullMsg.getMsg());
						client.multiplayerGame.endGame();
					}
				}
			}
		} catch (IOException e) {
			System.err.println("Server seems to have died: " + e.getMessage());
			// What to do here?
		}
	}

	/** Adds the ShipManager, to receive the information about ships */
	public void addManager(MultiplayerShipManager manager) {
		this.manager = manager;
	}
}