package clientComms;

import java.io.IOException;

import serverComms.ServerComm;

/**
 *  Sends a message to the server every second to avoid being disconnected
 * @author simon
 *
 */
public class StopDisconnect extends Thread {

  Client client;

  /**
   * Creates the StopDisconnect object
   * @param client The client to send messages to
   */
  public StopDisconnect(Client client) {
    this.client = client;
  }

  /**
   * Pings the server every second
   * (called via StopDisconnect.start())
   */
  public void run() {
    try {
      while (true) {
    	  try {
    		  client.sendByteMessage(new byte[0], ServerComm.DONTDISCONNECT);
    		  Thread.sleep(1000);
    	  } catch (IOException e) {
    		  //Don't do anything
    	  }
      }
    } catch (InterruptedException e) {
      // Don't do anything - Needed to compile though
    }
  }

}
