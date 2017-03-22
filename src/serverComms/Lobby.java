package serverComms;

import java.util.ArrayList;

import userInterface.MainMenu;

/**
 * Server to ensure client/server communications are working
 * 
 * @author simon
 *
 */
public class Lobby {

  public ArrayList<GameRoom> games = new ArrayList<GameRoom>();
  public ClientTable clientTable;

  public Lobby(int port) {
    clientTable = new ClientTable(this);
    ServerComm comm = new ServerComm(port, this);
    comm.start();
    if(port == 4445) MainMenu.allThreads.add(0, comm); //Only add the server if it's single player
  }

  public void remove(String clientName) {
    clientTable.remove(clientName);
  }
}
