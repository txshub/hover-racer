package serverComms.junit;

import static org.junit.Assert.*;

import org.junit.Test;

import serverComms.Lobby;

public class TestLobby {

  @Test
  public void test() {
    String name = "Test";
    Lobby l = new Lobby(5151);
    l.clientTable.add(name);
    l.remove(name);
    if (l.clientTable.userExists(name))
      fail("User existed after delete");
  }

}
