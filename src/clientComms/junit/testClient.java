package clientComms.junit;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import clientComms.Client;
import physics.network.ShipSetupData;
import physics.placeholders.DataGenerator;
import serverComms.GameRoom;
import serverComms.Lobby;

public class testClient {
  static String local = "localhost";
  static String seed = "114553";
  static int maxPlayers = 3;
  static int lapCount = 5;
  static String lobbyName = "Test Lobby";

  @Test
  public void testServerOff() {
    System.out.println("-----Start testServerOff method-----");
    String name = "Bob";
    int port = 1111;
    Client test = new Client(name, port, local);
    if (!test.clientName.equals(name))
      fail("Name given not equal to actual name");
    if (test.serverOn)
      fail("Server reported on when it was off");
    test.serverOn = true;
    test.start();
    try {
      Thread.sleep(2000);
    } catch (Exception e) {
    }
    if (test.serverOn)
      fail("Server reported on when it was off");
  }

  @Test
  public void testServerOnConnect() {
    System.out.println("-----Start testServerOnConnect method-----");
    int port = 1112;
    Lobby lobby = new Lobby(port);
    String name = "Mike";
    Client test = new Client(name, port, local);
    if (!test.clientName.equals(name))
      fail("Name given not equal to actual name");
    if (!test.serverOn)
      fail("Server reported off when it was on");
    test.start();
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      fail("InterruptedException");
    }
    if (!lobby.clientTable.userExists(name))
      fail("Name not in server after joining");
  }

  @Test
  public void testCreateGameRequestAllGames() {
    System.out.println("-----Start testCreateGameRequestAllGames method-----");
    int port = 1113;
    new Lobby(port);
    String name = "Pablo";
    ShipSetupData data = DataGenerator.basicShipSetup(name);
    try {
      Client test = new Client(name, port, local);
      test.start();
      Thread.sleep(1000);
      ArrayList<GameRoom> rooms = test.requestAllGames();
      if (rooms.size() != 0)
        fail("Game exists when none were created");
      test.createGame(seed, maxPlayers, lapCount, lobbyName, data);
      Thread.sleep(1000);
      rooms = test.requestAllGames();
      if (rooms.size() != 1)
        fail("Wrong number of games when 1 was created");
      GameRoom room = rooms.get(0);
      if (!room.getHostName().equals(name))
        fail("Hostname incorrect");
      if (!room.getSeed().equals(seed))
        fail("Seed incorrect");
      if (room.getNoPlayers() != maxPlayers)
        fail("Max players incorrect");
      if (room.getLaps() != lapCount)
        fail("Lap count incorrect");
      if (!room.getName().equals(lobbyName))
        fail("Lobby name incorrect");
    } catch (IOException e) {
      fail("IOException");
    } catch (InterruptedException e) {
      fail("InterruptedException");
    }

  }

  @Test
  public void testJoinGame() {
    System.out.println("-----Start testJoinGame method-----");
    int port = 1114;
    new Lobby(port);
    String name1 = "George";
    ShipSetupData data1 = DataGenerator.basicShipSetup(name1);
    String name2 = "Sunny";
    ShipSetupData data2 = DataGenerator.basicShipSetup(name2);
    try {
      Client test1 = new Client(name1, port, local);
      test1.start();
      Thread.sleep(1000);
      test1.createGame(seed, maxPlayers, lapCount, lobbyName, data1);
      Client test2 = new Client(name2, port, local);
      test2.start();
      Thread.sleep(1000);
      test2.joinGame(0, data2);
      Thread.sleep(1000);
      GameRoom room1 = test1.getUpdatedRoom();
      GameRoom room2 = test2.getUpdatedRoom();
      if (!room1.toString().equals(room2.toString()))
        fail("Rooms not the same");
    } catch (IOException e) {
      fail("IOException");
    } catch (InterruptedException e) {
      fail("InterruptedException");
    }

  }

}
