package clientComms;
import java.io.*;

/**
 * Thread to receive any messages passed from the server
 * @author simon
 *
 */
public class ClientReceiver extends Thread {
	private DataInputStream server;
	private Client client;
	
	/**
	 * Creates a ClientReceiver object
	 * @param server the stream to listen on for any messages from the server
	 * @param client The client to send messages to the server through if need be
	 */
	public ClientReceiver(DataInputStream server, Client client) {
		this.server = server;
		this.client = client;
	}
	
	/**
	 * Waits for messages from the server then deals with then appropriately
	 */
	@Override
	public void run() {
		try {
			while(true) {
				byte[] msg = new byte[server.readInt()];
				server.readFully(msg);
				if(msg == null || msg.length==0) {
					server.close();
					throw new IOException("Got null from the server");
				}
				switch(new String(msg, Client.charset)) {
				default:
				}
			}
		} catch (IOException e) {
			System.err.println("Server seems to have died: " + e.getMessage());
			//What to do here?
		}
	}
}
