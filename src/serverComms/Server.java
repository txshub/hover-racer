package serverComms;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * The server to handle a game
 * @author simon
 *
 */
public class Server extends Thread {
	public final static Charset charset = StandardCharsets.UTF_8;
	private String status;
	private int portNumber;
	public final static boolean DEBUG = true;
	public volatile boolean runThread = true;
	public final static String userSendingTag   = "00000000";
	public final static String badUserTag       = "00000001";
	public final static String statusTag        = "00000010";
	public final static String acceptedUserTag  = "00000011";
	public final static String clientDisconnect = "00000100";
	public final static String dontDisconnect   = "00000101";
	public final static String positionUpdate   = "00000110";
	
	/**
	 * Creates a Server object
	 * @param portNumber The port to listen on for incoming connections
	 */
	public Server(int portNumber) {
		this.portNumber = portNumber;
	}
	
	/**
	 * Runs the server
	 */
	public void run() {
		ClientTable clientTable = new ClientTable();
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(portNumber);
		} catch(IOException e) {
			System.err.println("Couldn't listen on port " + portNumber);
			runThread = false;
		}
		if(runThread && DEBUG) System.out.println("Now listening on port " + portNumber);
		try {
			while(runThread) {
				Socket socket = serverSocket.accept();
				if(DEBUG) System.out.println("Socket Accepted");
				DataInputStream fromClient = new DataInputStream(socket.getInputStream());
				byte[] data = new byte[fromClient.readInt()];
				fromClient.readFully(data);
				ByteArrayByte msg = new ByteArrayByte(data);
				if(DEBUG) System.out.println("Request to server: " + new String(msg.getMsg(),charset));
				if(msg.getType()==Byte.parseByte(statusTag, 2)) { //Server status requested
					DataOutputStream toClient = new DataOutputStream(socket.getOutputStream());
					writeByteMessage((status).getBytes(charset), statusTag, toClient);
					if(DEBUG) System.out.println("Sent status to client");
				} else { //Request is the client's username
					String name = new String(msg.getMsg(), charset);
					if(clientTable.userExists(name)) { //Can't have that username
						DataOutputStream toClient = new DataOutputStream(socket.getOutputStream());
						writeByteMessage(("Bad Username").getBytes(charset), badUserTag, toClient);
						if(DEBUG) System.out.println("Sent Bad Username to client");
					} else { //Valid Username
						clientTable.add(name);
						DataOutputStream toClient = new DataOutputStream(socket.getOutputStream());
						ServerSender sender = new ServerSender(clientTable.getQueue(name), toClient);
						sender.start();
						clientTable.getQueue(name).offer(new ByteArrayByte(("Valid").getBytes(charset),acceptedUserTag));
						if(DEBUG) System.out.println("Sent Accepted User to client");
						(new ServerReceiver(socket, name, fromClient, clientTable, sender)).start();
					}
				}
			}
		} catch(IOException e) {
			System.err.println("IO error: " + e.getMessage());
		}
	}

	/**
	 * Passes the given message to the given client
	 * @param msg the given message
	 * @param client the given client
	 * @throws IOException If any errors occur during write
	 */
	public static void writeByteMessage(byte[] msg, String type, DataOutputStream client) throws IOException {
		client.writeInt(msg.length + 1);
		byte[] out = new byte[msg.length+1];
		out[0] = Byte.parseByte(type,2);
		for(int i = 0; i < msg.length; i++) {
			out[i+1] = msg[i];
		}
		client.write(out);
		
	}

	public static void writeByteMessage(byte[] msg, byte type, DataOutputStream client) throws IOException {
		client.writeInt(msg.length + 1);
		byte[] out = new byte[msg.length+1];
		out[0] = type;
		for(int i = 0; i < msg.length; i++) {
			out[i+1] = msg[i];
		}
		client.write(out);
		
	}
}
