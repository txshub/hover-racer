package serverComms;

import java.util.ArrayList;

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
  }

  public void remove(String clientName) {
    clientTable.remove(clientName);
  }

  /**
   * Creates a server object and starts it
   * 
   * @param args
   *          Any program arguments to subsequently be ignored
   */
  public static void main(String[] args) {
    Lobby l = new Lobby(4444);
  }
}
