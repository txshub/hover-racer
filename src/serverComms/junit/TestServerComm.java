package serverComms.junit;

import static org.junit.Assert.*;

import org.junit.Test;

import serverComms.Lobby;
import serverComms.ServerComm;

public class TestServerComm {

  @Test
  public void test() {
    Lobby l = new Lobby(5152); // This creates it's own ServerComm, we don't
                               // want this so make another
    ServerComm comm = new ServerComm(5153, l);
    comm.start();
    DummyClient client = new DummyClient("Test", 5153, "localhost", null);
    if (!client.serverOn)
      fail("Server wasn't on");
    client.start();
    try {
      Thread.sleep(2000);
      if (!client.testsPassed)
        fail("Tests Failed");
      if (!l.clientTable.userExists("Test"))
        fail("User didn't exist after accepted");
    } catch (InterruptedException e) {
      fail("Interrupted");
    }
  }

}
