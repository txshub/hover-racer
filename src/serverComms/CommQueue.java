package serverComms;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Holds the queue for a user
 * 
 * @author simon
 *
 */
public class CommQueue {
  private BlockingQueue<ByteArrayByte> queue = new LinkedBlockingQueue<ByteArrayByte>();

  /**
   * Offer a new message to the queue
   * 
   * @param msg
   *          The new message to add
   */
  public void offer(ByteArrayByte msg) {
    queue.offer(msg);
  }

  /**
   * Waits for a message then takes it
   * 
   * @return The next message in the queue
   */
  public ByteArrayByte take() {
    while (true) {
      try {
        return (queue.take());
      } catch (InterruptedException e) {
        // This catch does nothing as interrupt() isn't present in
        // the code but is needed for compilation.
        // If by some miracle it is reached, just loop back and take from
        // the queue again
      }
    }
  }

}
