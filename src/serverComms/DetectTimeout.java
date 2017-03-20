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
//    try {
//      Thread.sleep(20000); // Sleep for 20s
//      if (!messageReceived) { // If no message is received
//        if (ServerComm.DEBUG)
//          System.out.println("Client " + user + " disconnected"); // If debug is
//                                                                  // on, print a
//                                                                  // disconnect
//                                                                  // message
//        table.remove(user); // Remove the user from the server
//      }
//    } catch (InterruptedException e) {
//
//    }
	  try {
		  while(messageReceived) {
			  messageReceived = false;
			  Thread.sleep(10000); //Sleep for 10s
		  }
		  table.remove(user);
	  } catch (InterruptedException e) {
		  
	  }

  }

}
