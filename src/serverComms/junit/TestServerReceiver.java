package serverComms.junit;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import clientComms.Client;
import physics.placeholders.DataGenerator;
import serverComms.Lobby;

public class TestServerReceiver {

  @Test
  public void test() {
    String name = "Test";
    Lobby l = new Lobby(5154);
    Client c = new Client(name, 5154, "localhost");
    c.start();
    try {
      Thread.sleep(1000);
      if (!l.clientTable.userExists(name)) fail("User wasn't added");
      c.createGame("1234", 4, 4, "Test Lobby", DataGenerator.basicShipSetup(name));
      if (l.clientTable.getGameID(name) == -1) fail("Game wasn't created/joined");
      c.cleanup();
      Thread.sleep(1000);
      if (l.clientTable.userExists(name)) fail("User wasn't removed after disconnect");
    } catch (InterruptedException e) {
      fail("Interrupted");
    } catch (IOException e) {
      fail("IOException");
    }
  }

}
