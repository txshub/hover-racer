package clientComms;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import physics.network.ShipSetupData;
import physics.ships.MultiplayerShipManager;
import serverComms.GameRoom;
import serverComms.GameSettings;
import serverComms.IDShipData;
import serverComms.ServerComm;

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
  Socket server;
  DataInputStream fromServer;

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
    } catch (UnknownHostException e) {
      serverOn = false;
    } catch (IOException e) {
      serverOn = false;
    }
  }

  @Override
  public void run() {
	setupConnection();
    ClientReceiver receiver = new ClientReceiver(fromServer, this);
    receiver.start();
    try {
      sendByteMessage(clientName.getBytes(ServerComm.charset), ServerComm.USERRECCONECT);
      serverStop = new StopDisconnect(this);
      serverStop.start();
      receiver.join();
      toServer.close();
      fromServer.close();
      server.close();
    } catch (IOException e) {
      System.err.println("Something wrong: " + e.getMessage());
      // What to do here?
    } catch (InterruptedException e) {
      System.err.println("Unexpected interruption: " + e.getMessage());
      // What to do here?
    }
  }

  public void cleanup() {
    serverStop.interrupt();
    try {
      sendByteMessage(new byte[0], ServerComm.CLIENTDISCONNECT);
    } catch (IOException e) {
      // Closing anyway so oh well
    }
  }
  

  public GameRoom createGame(String seed, int maxPlayers, int lapCount, String lobbyName, ShipSetupData data)
      throws IOException {
	  alreadyAccessedRoom = true;
    GameSettings thisGame = new GameSettings(seed, maxPlayers, lapCount, lobbyName, data);
    sendByteMessage(thisGame.toByteArray(), ServerComm.MAKEGAME);
    return waitForRoom();
  }

  public GameRoom joinGame(int id, ShipSetupData data) throws IOException {
	  alreadyAccessedRoom = true;
    IDShipData toSend = new IDShipData(id, data);
    sendByteMessage(toSend.toByteArray(), ServerComm.JOINGAME);
    return waitForRoom();
  }

  public void startGame() throws IOException{
	  sendByteMessage(new byte[0], ServerComm.STARTGAME);
  }
  
  public void startSinglePlayerGame(String seed, int numAI, int lapCount, ShipSetupData data) throws IOException {
	 createGame(seed, numAI+1, lapCount, "1", data);
	 startGame();
  }
  
  public GameRoom getUpdatedRoom() throws IOException {
	  alreadyAccessedRoom = true;
    sendByteMessage(new byte[0], ServerComm.REFRESHROOM);
    return waitForRoom();
  }

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

  public ArrayList<GameRoom> requestAllGames() throws IOException {
	alreadyAccessedList = true;
    sendByteMessage(("").getBytes(ServerComm.charset), ServerComm.SENDALLGAMES);
    while (alreadyAccessedList) {
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
      }
    }
    alreadyAccessedList = true;
    return gameList;
  }

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

  public void setGameList(ArrayList<GameRoom> gameList) {
    this.gameList = gameList;
    alreadyAccessedList = false;

  }

  public void setCurrentRoom(GameRoom gr) {
    this.currentRoom = gr;
    alreadyAccessedRoom = false;
  }

  public void setManager(MultiplayerShipManager manager) {
    this.manager = manager;
  }

  public MultiplayerShipManager getManager() {
    return manager;
  }

public void reopenPort() {
	server = new Socket();
}

public void setupConnection() {

    fromServer = null;
    server = null;
    try {
      server = new Socket(machineName, portNumber);
      toServer = new DataOutputStream(new BufferedOutputStream(server.getOutputStream()));
      fromServer = new DataInputStream(new BufferedInputStream(server.getInputStream()));
    } catch (UnknownHostException e) {
      System.err.println("Unknown host: " + machineName);
      serverOn = false;
    } catch (IOException e) {
      serverOn = false;
    }
}

}
