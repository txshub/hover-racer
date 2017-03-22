package clientComms;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import game.MultiplayerGame;
import physics.network.ShipSetupData;
import physics.ships.MultiplayerShipManager;
import serverComms.GameRoom;
import serverComms.GameSettings;
import serverComms.IDShipData;
import serverComms.ServerComm;
import userInterface.MainMenu;

/**
 * Main client class for client/server communications
 * 
 * @author simon
 */
public class Client extends Thread {
  public static final boolean DEBUG = false;
  public boolean serverOn = true;
  protected DataOutputStream toServer;
  public String clientName;
  int portNumber;
  String machineName;
  StopDisconnect serverStop;
  MultiplayerShipManager manager;
  public volatile boolean alreadyAccessed = false;
  private ArrayList<GameRoom> gameList;
  private volatile boolean alreadyAccessedList = true;
  public volatile boolean alreadyAccessedRoom = true;
  private GameRoom currentRoom;
  public MultiplayerGame multiplayerGame;
private boolean alreadyAccessedIP = true;
private String currentIP;

  /**
   * Creates a client object and connects to a given server on a given port
   * automagically
   * 
   * @param clientName
   *          The client's nickname to pass to the server first
   * @param portNumber
   *          The port to send the request on
   * @param machineName
   *          The machinename of the server host (for testing purposes use
   *          localhost)
   * @param gameMenu
   * @param gameMenu
   */
  public Client(String clientName, int portNumber, String machineName) {
    this.clientName = clientName;
    this.portNumber = portNumber;
    this.machineName = machineName;
    Socket testConn = null;
    try {
      testConn = new Socket(machineName, portNumber);
      toServer = new DataOutputStream(new BufferedOutputStream(testConn.getOutputStream()));
      sendByteMessage(new byte[0], ServerComm.TESTCONN);
      testConn.close();
    } catch (Exception e) {
      serverOn = false;
    }
  }

  @Override
  /**
   * Runs the client (called via client.start())
   */
  public void run() {
    DataInputStream fromServer = null;
    Socket server = null;
    try {
      server = new Socket(machineName, portNumber);
      toServer = new DataOutputStream(new BufferedOutputStream(server.getOutputStream()));
      fromServer = new DataInputStream(new BufferedInputStream(server.getInputStream()));
    } catch (Exception e) {
      serverOn = false;
      return;
    }
    ClientReceiver receiver = new ClientReceiver(fromServer, this);
    receiver.start();
    MainMenu.allThreads.add(0, receiver);
    try {
      sendByteMessage(clientName.getBytes(ServerComm.charset), ServerComm.USERSENDING);
      serverStop = new StopDisconnect(this);
      serverStop.start();
      MainMenu.allThreads.add(0, serverStop);
      receiver.join();
      toServer.close();
      fromServer.close();
      server.close();
    } catch (IOException e) {
      System.err.println("Something wrong: " + e.getMessage());
      // What to do here?
    } catch (InterruptedException e) {
      // What to do here?
    } finally {
    	try {
			sendByteMessage(new byte[0], ServerComm.CLIENTDISCONNECT);
		} catch (IOException e) {
		}
    }
  }

  /**
   * Cleans up the client by stopping the thread that constantly pings the
   * server then sends the disconnect message to the server
   */
  public void cleanup() {
    serverStop.interrupt();
    try {
      sendByteMessage(new byte[0], ServerComm.CLIENTDISCONNECT);
    } catch (IOException e) {
      // Closing anyway so oh well
    }
  }

  /**
   * Sends a request to the server to make a game room then returns the
   * resulting room
   * 
   * @param seed
   *          The seed to make the track with
   * @param maxPlayers
   *          Max number of players in the room
   * @param lapCount
   *          Number of laps to be done
   * @param lobbyName
   *          Name of the lobby
   * @param data
   *          Ship data for this user
   * @return The resulting game room
   * @throws IOException
   *           If there is an issue in writing a message to the server
   */
  public GameRoom createGame(String seed, int maxPlayers, int lapCount, String lobbyName,
      ShipSetupData data) throws IOException {
    alreadyAccessedRoom = true;
    GameSettings thisGame = new GameSettings(seed, maxPlayers, lapCount, lobbyName, data);
    sendByteMessage(thisGame.toByteArray(), ServerComm.MAKEGAME);
    return waitForRoom();
  }

  /**
   * Joins a gameroom
   * 
   * @param id
   *          The gameroom id
   * @param data
   *          The ship setup data for this user
   * @return The resulting game room
   * @throws IOException
   *           If there is an issue in writing a message to the server
   */
  public GameRoom joinGame(int id, ShipSetupData data) throws IOException {
    alreadyAccessedRoom = true;
    IDShipData toSend = new IDShipData(id, data);
    sendByteMessage(toSend.toByteArray(), ServerComm.JOINGAME);
    return waitForRoom();
  }

  /**
   * Method called by the host of a game room to start the game
   * 
   * @throws IOException
   *           If there is an issue in writing a message to the server
   */
  public void startGame() throws IOException {
    sendByteMessage(new byte[0], ServerComm.STARTGAME);
  }

  /**
   * Starts a single player game by creating a game then immediately starting it
   * 
   * @param seed
   *          The seed to generate the track with
   * @param numAI
   *          The number of AI to play against
   * @param lapCount
   *          The number of laps to complete
   * @param data
   *          The ship data for this user
   * @throws IOException
   *           If there is an issue in writing a message to the server
   */
  public void startSinglePlayerGame(String seed, int numAI, int lapCount, ShipSetupData data)
      throws IOException {
    GameRoom room = createGame(seed, numAI + 1, lapCount, "1", data);
    if (room == null)
      System.out.println("Null Game");
    startGame();
  }

  /**
   * Returns the updated gameroom from the server
   * 
   * @return The updated gameroom from the server
   * @throws IOException
   *           If there is an issue in writing a message to the server
   */
  public GameRoom getUpdatedRoom() throws IOException {
    alreadyAccessedRoom = true;
    sendByteMessage(new byte[0], ServerComm.REFRESHROOM);
    return waitForRoom();
  }
  
  public String waitForIP() {
	  while(alreadyAccessedIP) {
		  try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}
	  }
	  return currentIP;
  }

  /**
   * Returns the gameroom object when it is received from the server
   * 
   * @return The gameroom object when it is received from the server
   */
  public GameRoom waitForRoom() {
    while (alreadyAccessedRoom) {
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
      }
    }
    alreadyAccessedRoom = true;
    return currentRoom;
  }

  /**
   * Returns a list of all joinable gamerooms for the server you're connected to
   * 
   * @return A list of all joinable gamerooms for the server you're connected to
   * @throws IOException
   *           If there is an issue in writing a message to the server
   */
  public ArrayList<GameRoom> requestAllGames() throws IOException {
    alreadyAccessedList = true;
    sendByteMessage(new byte[0], ServerComm.SENDALLGAMES);
    while (alreadyAccessedList) {
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
      }
    }
    alreadyAccessedList = true;
    return gameList;
  }

  /**
   * Sends the updated position to the server
   * 
   * @param data
   *          The player data to send to the server
   * @throws IOException
   *           If there is an issue in writing a message to the server
   */
  public void updateMe(byte[] data) throws IOException {
    sendByteMessage(data, ServerComm.SENDPLAYERDATA);
  }

  /**
   * Sends a message to the server
   * 
   * @param message
   *          The byte message to send
   * @throws IOException
   *           If there is a problem with writing
   */
  public void sendByteMessage(byte[] message, byte type) throws IOException {
    byte[] out = new byte[message.length + 1];
    out[0] = type;
    for (int i = 0; i < message.length; i++) {
      out[i + 1] = message[i];
    }
    toServer.writeInt(out.length);
    toServer.write(out);
    toServer.flush();
    if (DEBUG)
      System.out.println("Sent message " + new String(message, ServerComm.charset) + " with tag "
          + Byte.toString(type));
  }

  /**
   * Sets the game list when received by ClientReceiver
   * 
   * @param gameList
   *          The game list to update with
   */
  public void setGameList(ArrayList<GameRoom> gameList) {
    this.gameList = gameList;
    alreadyAccessedList = false;
  }

  /**
   * Sets the current room when received by the ClientReceiver
   * 
   * @param gr
   *          The gameroom to update with
   */
  public void setCurrentRoom(GameRoom gr) {
    this.currentRoom = gr;
    alreadyAccessedRoom = false;
  }

  /**
   * Sets the ship manager
   * 
   * @param manager
   *          The ship manager
   */
  public void setManager(MultiplayerShipManager manager) {
    this.manager = manager;
  }

  /**
   * Returns the current ship manager
   * 
   * @return The current ship manager
   */
  public MultiplayerShipManager getManager() {
    return manager;
  }

  public void setMultiplayerGame(MultiplayerGame multiplayerGame) {
    this.multiplayerGame = multiplayerGame;

  }

	public void setIP(String ip) {
		this.currentIP = ip;
		alreadyAccessedIP = false;
	}
}