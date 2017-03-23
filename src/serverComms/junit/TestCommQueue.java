package serverComms.junit;

import static org.junit.Assert.*;

import org.junit.Test;

import serverComms.ByteArrayByte;
import serverComms.CommQueue;
import serverComms.ServerComm;

public class TestCommQueue {

  @Test
  public void testQueue() {
    CommQueue queue = new CommQueue();
    byte[] message = ("Test").getBytes(ServerComm.charset);
    ByteArrayByte msg = new ByteArrayByte(message, ServerComm.TESTCONN);
    queue.offer(msg);
    ByteArrayByte out = queue.take();
    if (out != msg) fail("Message wasn't the same one put in");
  }

}
