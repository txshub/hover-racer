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
public class Client extends Thread {
	public final static Charset charset = StandardCharsets.UTF_8;
	public static final boolean DEBUG = false;
	private DataOutputStream toServer;
	String name;
	int portNumber;
	String machineName;
	StopDisconnect serverStop;
	
	/**
	 * Creates a client object and connects to a given server on a given port automagically
	 * @param name The client's nickname to pass to the server first
	 * @param portNumber The port to send the request on
	 * @param machineName The machinename of the server host (for testing purposes use localhost)
	 */
	public Client(String name, int portNumber, String machineName) {
		this.name = name;
		this.portNumber = portNumber;
		this.machineName = machineName;
	}
	
	@Override
	public void run() {
		DataInputStream fromServer = null;
		Socket server = null;
		try {
			server = new Socket(machineName, portNumber);
			toServer = new DataOutputStream(new BufferedOutputStream(server.getOutputStream()));
			fromServer = new DataInputStream(new BufferedInputStream(server.getInputStream()));
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
			serverStop = new StopDisconnect(this);
			serverStop.start();
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
	
	public void cleanup() {
		serverStop.interrupt();
		try {
			sendByteMessage(new byte[0],Server.clientDisconnect);
		} catch (IOException e) {
			//Closing anyway so oh well
		}
	}
	
	/**
	 * Sends a message to the server
	 * @param message The byte message to send
	 * @throws IOException If there is a problem with writing
	 */
	public void sendByteMessage(byte[] message, byte type) throws IOException {
		byte[] out = new byte[message.length+1];
		out[0] = type;
		for(int i = 0; i < message.length; i++) {
			out[i+1] = message[i];
		}
		toServer.writeInt(out.length);
		toServer.write(out);
		toServer.flush();
		if(Server.DEBUG) System.out.println("Sent message " + new String(message, charset) + " with tag " + Byte.toString(type));
	}
}
