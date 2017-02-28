package clientComms;
import java.io.*;
import java.net.*;

import serverComms.GameSettings;
import serverComms.ServerComm;

/**
 * Main client class for client/server communications
 * @author simon
 *
 */
public class Client extends Thread {
	public static final boolean DEBUG = false;
	public boolean serverOn = true;
	private DataOutputStream toServer;
	String clientName;
	int portNumber;
	String machineName;
	StopDisconnect serverStop;
	
	/**
	 * Creates a client object and connects to a given server on a given port automagically
	 * @param clientName The client's nickname to pass to the server first
	 * @param portNumber The port to send the request on
	 * @param machineName The machinename of the server host (for testing purposes use localhost)
	 * @param gameMenu 
	 */
	public Client(String clientName, int portNumber, String machineName) {
		this.clientName = clientName;
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
			serverOn = false;
		} catch (IOException e) {
			serverOn = false;
		}
		
		ClientReceiver receiver = new ClientReceiver(fromServer, this);
		receiver.start();
		try {
			sendByteMessage(clientName.getBytes(ServerComm.charset), ServerComm.USERSENDING);
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
			sendByteMessage(new byte[0],ServerComm.CLIENTDISCONNECT);
		} catch (IOException e) {
			//Closing anyway so oh well
		}
	}
	
	public void createGame(long seed, int maxPlayers, int numAI, int lapCount, String lobbyName) throws IOException {
		GameSettings thisGame = new GameSettings(seed, maxPlayers, numAI, lapCount, lobbyName, clientName);
		sendByteMessage(thisGame.toByteArray(), ServerComm.MAKEGAME);
	}
	
	public void joinGame(int id) throws IOException {
		sendByteMessage(String.valueOf(id).getBytes(ServerComm.charset), ServerComm.JOINGAME);
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
		if(ServerComm.DEBUG) System.out.println("Sent message " + new String(message, ServerComm.charset) + " with tag " + Byte.toString(type));
	}
}
