package serverComms.junit;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import clientComms.Client;
import serverComms.Lobby;

public class TestServerReceiver {

  @Test
  public void test() {
    String name = "Test";
    Lobby l = new Lobby(5154);
    DummyGameMenu m = null;
    try {
      m = new DummyGameMenu();
    } catch (IOException e) {
      e.printStackTrace();
    }
    Client c = new Client(name, 5154, "localhost", m);
    c.start();
    try {
      Thread.sleep(1000);
      if (!l.clientTable.userExists(name))
        fail("User wasn't added");
      c.cleanup();
      Thread.sleep(1000);
      if (l.clientTable.userExists(name))
        fail("User wasn't removed after disconnect");
    } catch (InterruptedException e) {
      fail("Interrupted");
    }
  }

}
