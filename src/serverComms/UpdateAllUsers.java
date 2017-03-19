package serverComms;

import java.util.ArrayList;

/**
 * Sends a message out to all clients every frame
 * 
 * @author simon
 *
 */
public class UpdateAllUsers extends Thread {

  public volatile boolean run = true;

  final float refreshRate = 1f / 60f; // Refresh Rate
  private ArrayList<CommQueue> queues;
  private GameRoom room;

  /**
   * Creates an UpdateAllUsers object
   * 
   * @param queues
   *          All of the queues to send data to
   * @param room
   *          The GameRoom in question
   */
  public UpdateAllUsers(ArrayList<CommQueue> queues, GameRoom room) {
    this.queues = queues;
    this.room = room;
  }

  @Override
  /**
   * Runs the updater (Called via UpdateAllUsers.start())
   */
  public void run() {
    while (run) {
      long timeStarting = System.nanoTime();
      room.update(refreshRate);
      byte[] data = room.getShipPositions();
      for (CommQueue queue : queues) {
        queue.offer(new ByteArrayByte(data, ServerComm.FULLPOSITIONUPDATE));
      }
      long timePassed = System.nanoTime() - timeStarting;
      try {
        // System.out.println(timeStarting + ", " + timeAfter);
        Thread.sleep(Math.max(0, (int) (refreshRate * 1000) - (timePassed / 1000000))); // Sleep
        // for
        // 1/60th
        // of a
        // second
      } catch (InterruptedException e) {
        // Needed for compilation, won't actually happen
      }
    }
  }

}
