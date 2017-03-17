package serverComms.junit;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import physics.placeholders.DataGenerator;
import serverComms.ClientTable;
import serverComms.GameSettings;
import serverComms.Lobby;
import serverComms.ServerReceiver;

public class TestClientTable {

  static Lobby lobby;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    lobby = new Lobby(1234);
  }

  @Test
  public void testUserExists() {
    ClientTable table = new ClientTable(lobby);
    if (table.userExists("Testing"))
      fail("User exists before adding");
    table.add("Testing");
    if (!table.userExists("Testing"))
      fail("User doesn't exist after adding");
  }

  @Test
  public void testAdd() {
    ClientTable table = new ClientTable(lobby);
    table.add("Testing");
    if (table.getQueue("Testing") == null)
      fail("User CommQueue not found");
  }

  @Test
  public void testAddReceiver() {
    ClientTable table = new ClientTable(lobby);
    String name = "Testing";
    ServerReceiver testReceiver = new ServerReceiver(null, null, null);
    table.add(name);
    table.addReceiver(name, testReceiver);
    if (table.getReceiver(name) != testReceiver)
      fail("Got wrong receiver");
  }

  @Test
  public void testRemove() {
    ClientTable table = new ClientTable(lobby);
    String name = "Testing";
    ServerReceiver testReceiver = new ServerReceiver(null, null, null);
    table.add(name);
    table.addReceiver(name, testReceiver);
    table.remove(name);
    if (table.userExists(name))
      fail("Name remained after remove");
    if (table.getReceiver(name) != null)
      fail("Receiver remained after remove");
  }

  @Test
  public void testGetQueue() {
    ClientTable table = new ClientTable(lobby);
    String name = "Testing";
    ServerReceiver testReceiver = new ServerReceiver(null, null, null);
    table.add(name);
    table.addReceiver(name, testReceiver);
    if (table.getQueue(name) == null)
      fail("CommQueue not initialised");
  }

  @Test
  public void testGetReceiver() {
    ClientTable table = new ClientTable(lobby);
    String name = "Testing";
    ServerReceiver testReceiver = new ServerReceiver(null, null, null);
    table.add(name);
    table.addReceiver(name, testReceiver);
    if (table.getReceiver(name) != testReceiver)
      fail("Got wrong receiver");
  }

  @Test
  public void testGetGameID() {
    ClientTable table = new ClientTable(lobby);
    String name = "Testing";
    table.add(name);
    if (table.getGameID(name) != -1)
      fail("Game ID didn't initialise as -1");
    if (table.joinGame(0, DataGenerator.basicShipSetup(name)))
      fail("Game was present before being made");
    table.addGame(new GameSettings("0", 1, 0, "lobby", DataGenerator.basicShipSetup(name)));
    if (!table.joinGame(0, DataGenerator.basicShipSetup(name)))
      fail("Game wasn't present after being made");
    if (table.getGameID(name) == -1)
      fail("Couldn't get Game ID after being made");
  }

  @Test
  public void testGetGame() {
    ClientTable table = new ClientTable(lobby);
    String name = "Testing";
    table.add(name);
    if (table.getGameID(name) != -1)
      fail("Game ID didn't initialise as -1");
    if (table.joinGame(0, DataGenerator.basicShipSetup(name)))
      fail("Game was present before being made");
    table.addGame(new GameSettings("0", 1, 0, "lobby", DataGenerator.basicShipSetup(name)));
    if (!table.joinGame(0, DataGenerator.basicShipSetup(name)))
      fail("Game wasn't present after being made");
    int id = table.getGameID(name);
    if (id == -1)
      fail("Couldn't get Game ID after being made");
    if (table.getGame(id) == null)
      fail("Game wasn't got after being initialised");
  }

  @Test
  public void testAddGame() {
    ClientTable table = new ClientTable(lobby);
    String name = "Testing";
    table.add(name);
    if (table.getGameID(name) != -1)
      fail("Game ID didn't initialise as -1");
    if (table.joinGame(0, DataGenerator.basicShipSetup(name)))
      fail("Game was present before being made");
    table.addGame(new GameSettings("0", 1, 0, "lobby", DataGenerator.basicShipSetup(name)));
    if (table.getGame(0) == null)
      fail("Game wasn't got after being initialised");
  }

  @Test
  public void testJoinGame() {
    ClientTable table = new ClientTable(lobby);
    String name = "Testing";
    table.add(name);
    if (table.getGameID(name) != -1)
      fail("Game ID didn't initialise as -1");
    if (table.joinGame(0, DataGenerator.basicShipSetup(name)))
      fail("Game was present before being made");
    table.addGame(new GameSettings("0", 1, 0, "lobby", DataGenerator.basicShipSetup(name)));
    if (!table.joinGame(0, DataGenerator.basicShipSetup(name)))
      fail("Game wasn't present after being made");
    if (table.getGameID(name) == -1)
      fail("Couldn't get Game ID after being made");
  }

}
