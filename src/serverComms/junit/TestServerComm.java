package serverComms.junit;

import static org.junit.Assert.*;

import org.junit.Test;

import serverComms.Lobby;
import serverComms.ServerComm;

public class TestServerComm {

	@Test
	public void noRunOnUsedSocket() {
		Lobby l = new Lobby(1000);
		ServerComm comm = new ServerComm(5151, l);
		comm.start();
		try {Thread.sleep(1000);} catch (InterruptedException e) {}
		ServerComm comm2 = new ServerComm(5151, l);
		comm2.start();
		try {Thread.sleep(1000);} catch (InterruptedException e) {}
		if (comm2.runThread)
			fail("Listened on in-use port");
	}

	@Test
	public void test() {
		Lobby l = new Lobby(5152); // This creates it's own ServerComm, we don't
									// want this so make another
		ServerComm comm = new ServerComm(5153, l);
		comm.start();
		DummyClient client = new DummyClient("Test", 5153, "localhost");
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
