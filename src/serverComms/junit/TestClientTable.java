package serverComms.junit;

import static org.junit.Assert.*;

import org.junit.Test;

import physics.placeholders.DataGenerator;
import serverComms.ClientTable;
import serverComms.GameSettings;
import serverComms.Lobby;
import serverComms.ServerReceiver;

public class TestClientTable {

  @Test
  public void testUserExists() {
    Lobby lobby = new Lobby(1120);
    ClientTable table = new ClientTable(lobby);
    if (table.userExists("Testing")) fail("User exists before adding");
    table.add("Testing");
    if (!table.userExists("Testing")) fail("User doesn't exist after adding");
  }

  @Test
  public void testAdd() {
    Lobby lobby = new Lobby(1121);
    ClientTable table = new ClientTable(lobby);
    table.add("Testing");
    if (table.getQueue("Testing") == null) fail("User CommQueue not found");
  }

  @Test
  public void testAddReceiver() {
    Lobby lobby = new Lobby(1122);
    ClientTable table = new ClientTable(lobby);
    String name = "Testing";
    ServerReceiver testReceiver = new ServerReceiver(name, null, lobby);
    table.add(name);
    table.addReceiver(name, testReceiver);
    if (table.getReceiver(name) != testReceiver) fail("Got wrong receiver");
  }

  @Test
  public void testRemove() {
    Lobby lobby = new Lobby(1123);
    ClientTable table = new ClientTable(lobby);
    String name = "Testing";
    ServerReceiver testReceiver = new ServerReceiver(name, null, lobby);
    table.add(name);
    table.addReceiver(name, testReceiver);
    table.remove(name);
    if (table.userExists(name)) fail("Name remained after remove");
    if (table.getReceiver(name) != null) fail("Receiver remained after remove");
  }

  @Test
  public void testGetQueue() {
    Lobby lobby = new Lobby(1124);
    ClientTable table = lobby.clientTable;
    String name = "Testing";
    ServerReceiver testReceiver = new ServerReceiver(name, null, lobby);
    table.add(name);
    table.addReceiver(name, testReceiver);
    if (table.getQueue(name) == null) fail("CommQueue not initialised");
  }

  @Test
  public void testGetReceiver() {
    Lobby lobby = new Lobby(1125);
    ClientTable table = new ClientTable(lobby);
    String name = "Testing";
    ServerReceiver testReceiver = new ServerReceiver(name, null, lobby);
    table.add(name);
    table.addReceiver(name, testReceiver);
    if (table.getReceiver(name) != testReceiver) fail("Got wrong receiver");
  }

  @Test
  public void testGetGameID() {
    Lobby lobby = new Lobby(1126);
    ClientTable table = new ClientTable(lobby);
    String name = "Testing";
    table.add(name);
    if (table.getGameID(name) != -1) fail("Game ID didn't initialise as -1");
    if (table.joinGame(0, DataGenerator.basicShipSetup(name)))
      fail("Game was present before being made");
    table.addGame(new GameSettings("0", 1, 0, "lobby", DataGenerator.basicShipSetup(name)));
    if (!table.joinGame(0, DataGenerator.basicShipSetup(name)))
      fail("Game wasn't present after being made");
    if (table.getGameID(name) == -1) fail("Couldn't get Game ID after being made");
  }

  @Test
  public void testGetGame() {
    Lobby lobby = new Lobby(1127);
    ClientTable table = new ClientTable(lobby);
    String name = "Testing";
    table.add(name);
    if (table.getGameID(name) != -1) fail("Game ID didn't initialise as -1");
    if (table.joinGame(0, DataGenerator.basicShipSetup(name)))
      fail("Game was present before being made");
    table.addGame(new GameSettings("0", 1, 0, "lobby", DataGenerator.basicShipSetup(name)));
    if (!table.joinGame(0, DataGenerator.basicShipSetup(name)))
      fail("Game wasn't present after being made");
    int id = table.getGameID(name);
    if (id == -1) fail("Couldn't get Game ID after being made");
    if (table.getGame(id) == null) fail("Game wasn't got after being initialised");
  }

  @Test
  public void testAddGame() {
    Lobby lobby = new Lobby(1128);
    ClientTable table = new ClientTable(lobby);
    String name = "Testing";
    table.add(name);
    if (table.getGameID(name) != -1) fail("Game ID didn't initialise as -1");
    if (table.joinGame(0, DataGenerator.basicShipSetup(name)))
      fail("Game was present before being made");
    table.addGame(new GameSettings("0", 1, 0, "lobby", DataGenerator.basicShipSetup(name)));
    if (table.getGame(0) == null) fail("Game wasn't got after being initialised");
  }

  @Test
  public void testJoinGame() {
    Lobby lobby = new Lobby(1129);
    ClientTable table = new ClientTable(lobby);
    String name1 = "Testing";
    String name2 = "Name";
    table.add(name1);
    if (table.getGameID(name1) != -1) fail("Game ID didn't initialise as -1");
    if (table.joinGame(0, DataGenerator.basicShipSetup(name1)))
      fail("Game was present before being made");
    if (!table.addGame(new GameSettings("0", 1, 2, "lobby", DataGenerator.basicShipSetup(name1))))
      fail("Game wasn't present after being made for player 1");
    if (!table.addGame(new GameSettings("0", 1, 2, "lobby", DataGenerator.basicShipSetup(name2))))
      fail("game wasn't present after being made for player 2");
    if (table.getGameID(name1) == -1) fail("Couldn't get Game ID after being made");
  }

  @Test
  public void testGetQueues() {
    Lobby lobby = new Lobby(1130);
    ClientTable table = new ClientTable(lobby);
    if (table.getQueues().size() != 0) fail("Expected size 0 queues");
    table.add("Testing");
    if (table.getQueues().size() != 1) fail("Expected size 1 queues");
  }

}
