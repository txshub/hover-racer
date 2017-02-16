package clientComms;
import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import serverComms.Server;

/**
 * Main client class for client/server communications
 * @author simon
 *
 */
public class Client {
	public final static Charset charset = StandardCharsets.UTF_8;
	private DataOutputStream toServer;
	
	/**
	 * Creates a client object and connects to a given server on a given port automagically
	 * @param name The client's nickname to pass to the server first
	 * @param portNumber The port to send the request on
	 * @param machineName The machinename of the server host (for testing purposes use localhost)
	 */
	public Client(String name, int portNumber, String machineName) {
		DataInputStream fromServer = null;
		Socket server = null;
		try {
			server = new Socket(machineName, portNumber);
			toServer = new DataOutputStream(server.getOutputStream());
			fromServer = new DataInputStream(server.getInputStream());
		} catch (UnknownHostException e) {
			System.err.println("Unknown host: " + machineName);
			//What to do here?
		} catch (IOException e) {
			System.err.println("Server doesn't seem to be running " + e.getMessage());
			//What to do here
		}
		
		ClientReceiver receiver = new ClientReceiver(fromServer, this);
		receiver.start();
		try {
			sendByteMessage(name.getBytes(charset), Server.userSendingTag);
			receiver.join();
			toServer.close();
			fromServer.close();
			server.close();
		} catch (IOException e) {
			System.err.println("Something wrong: " + e.getMessage());
			//What to do here?
		} catch (InterruptedException e) {
			System.err.println("Unexpected interruption: " + e.getMessage());
			//What to do here?
		}
		
	}
	
	/**
	 * Sends a message to the server
	 * @param message The byte message to send
	 * @throws IOException If there is a problem with writing
	 */
	public void sendByteMessage(byte[] message, String type) throws IOException {
		toServer.writeInt(message.length+1);
		byte[] out = new byte[message.length+1];
		out[0] = Byte.parseByte(type, 2);
		for(int i = 0; i < message.length; i++) {
			out[i+1] = message[i];
		}
		toServer.write(out);
		if(Server.DEBUG) System.out.println("Sent message " + new String(message, charset) + " with tag " + type);
	}
}
