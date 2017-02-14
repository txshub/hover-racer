package clientComms;
import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

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
			sendByteMessage(name.getBytes());
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
	public void sendByteMessage(byte[] message) throws IOException {
		toServer.writeInt(message.length);
		toServer.write(message);
	}
}
