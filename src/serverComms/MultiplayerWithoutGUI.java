package serverComms;

import java.io.IOException;

import clientComms.Client;
import physics.placeholders.DataGenerator;

public class MultiplayerWithoutGUI {

  private static final int PORT = 4444;
  private static final String MACHINE_NAME = "localhost";
  private static Client client;
  private static final String SEED = "12983";
  private static final int MAX_PLAYERS = 1;
  private static final int LAPS = 2;
  private static final int SHIP_TYPE = 1;
  private static final String LOBBY_NAME = "Testonium";

  public static void main(String[] args) throws IOException, InterruptedException {

    // Start the server
    Lobby serverLobby = new Lobby(4444);
    ServerComm server = new ServerComm(4444, serverLobby);
    server.start();

    // Create and connect the user
    String usr = "Teston";

    client = new Client(usr, PORT, MACHINE_NAME);
    client.start();

    // Create a game room
    client.createGame(SEED, MAX_PLAYERS, LAPS, LOBBY_NAME,
        DataGenerator.basicShipSetup(client.clientName, SHIP_TYPE));

    // Create another user
    Client bob = new Client("Bob", PORT, MACHINE_NAME);
    bob.start();

    // bob.joinGame(GAME_ID, DataGenerator.basicShipSetup("Bob"));

    // Start the game
    client.startGame();

    /*
     * long closeAt = System.nanoTime() + 10000000000l; while (System.nanoTime() < closeAt) {
     * Thread.sleep(100); } System.out.println("---------------EXITING-----------------");
     */

  }

}
