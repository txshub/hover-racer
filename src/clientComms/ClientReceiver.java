package clientComms;
import java.io.*;

import serverComms.ByteArrayByte;
import serverComms.ServerComm;

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
				ByteArrayByte fullMsg = new ByteArrayByte(msg);
				if(fullMsg.getType()==ServerComm.badUserTag) {
					System.out.println("Username not valid, please pick another");
					System.exit(1);
				} else if(fullMsg.getType()==ServerComm.acceptedUserTag) {
					System.out.println("Username valid. Now connected to the server");
				} else if(fullMsg.getType()==ServerComm.badPacket) {
					System.out.println("Need To Reconnect");
				} else if(fullMsg.getType()==ServerComm.sendAllGames) {
					String[] allGames = (new String(fullMsg.getMsg(), ServerComm.charset)).split("~");
					//Where to send this to?
				}
			}
		} catch (IOException e) {
			System.err.println("Server seems to have died: " + e.getMessage());
			//What to do here?
		}
	}
}
