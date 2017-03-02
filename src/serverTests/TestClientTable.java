package serverTests;

import static org.junit.Assert.*;

import org.junit.Test;

import serverComms.ClientTable;
import serverComms.ServerReceiver;

public class TestClientTable {

  @Test
  public void testUserExists() {
    ClientTable table = new ClientTable();
    if (table.userExists("Testing"))
      fail("User exists before adding");
    table.add("Testing");
    if (!table.userExists("Testing"))
      fail("User doesn't exist after adding");
  }

  @Test
  public void testAdd() {
    ClientTable table = new ClientTable();
    table.add("Testing");
    if (table.getQueue("Testing") == null)
      fail("User CommQueue not found");
  }

  @Test
  public void testAddReceiver() {
    ClientTable table = new ClientTable();
    table.add("Testing");
    ServerReceiver testReceiver = new ServerReceiver(null, null, null, null);
    table.addReceiver("Testing", testReceiver);
    if (table.getReceiver("Testing") != testReceiver)
      fail("Got wrong receiver");
  }

  @Test
  public void testRemove() {
    fail("Not yet implemented");
  }

  @Test
  public void testGetQueue() {
    fail("Not yet implemented");
  }

  @Test
  public void testGetReceiver() {
    fail("Not yet implemented");
  }

  @Test
  public void testGetQueues() {
    fail("Not yet implemented");
  }

  @Test
  public void testGetGameID() {
    fail("Not yet implemented");
  }

  @Test
  public void testGetGame() {
    fail("Not yet implemented");
  }

  @Test
  public void testAddGame() {
    fail("Not yet implemented");
  }

  @Test
  public void testJoinGame() {
    fail("Not yet implemented");
  }

}
