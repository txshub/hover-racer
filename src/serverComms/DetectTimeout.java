package serverComms;

/**
 * Detects when a client disconnects
 * 
 * @author simon
 *
 */
public class DetectTimeout extends Thread {

  public volatile boolean messageReceived = false;
  ClientTable table;
  String user;

  /**
   * Creates the DetectTimeout object
   * 
   * @param table
   *          The ClientTable to remove the user from if they disconnect
   * @param user
   *          The user's name
   */
  public DetectTimeout(ClientTable table, String user) {
    this.table = table;
    this.user = user;
  }

  @Override
  /**
   * Runs the DetectTimeout object (Called with DetectTimeout.start())
   */
  public void run() {
    try {
      Thread.sleep(5000); // Sleep for 5s
      if (!messageReceived) { // If no message is received
        if (ServerComm.DEBUG)
          System.out.println("Client " + user + " disconnected"); // If debug is
                                                                  // on, print a
                                                                  // disconnect
                                                                  // message
        table.remove(user); // Remove the user from the server
      }
    } catch (InterruptedException e) {

    }

  }

}
