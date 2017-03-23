package serverComms;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Class to send
 * 
 * @author simon
 */
public class ServerSender extends Thread {

  private DataOutputStream toClient;
  private CommQueue queue;
  public volatile boolean continueSending = true; // Set to false when the
  // thread should stop

  /**
   * Creates a ServerSender object
   * 
   * @param queue
   *          The queue to listen for messages on
   * @param toClient
   *          The stream for any messages to be sent on
   */
  public ServerSender(CommQueue queue, DataOutputStream toClient) {
    this.queue = queue;
    this.toClient = toClient;
  }

  /**
   * Runs the sender (Called via ServerSender.start())
   */
  public void run() {
    while (continueSending && !this.isInterrupted()) {
      try {
        ByteArrayByte msg = queue.take();
        ServerComm.writeByteMessage(msg.getMsg(), msg.getType(), toClient);
      } catch (IOException e) {
        // System.err.println("Error passing message to client: " + e.getMessage());
      }
    }
  }

}
