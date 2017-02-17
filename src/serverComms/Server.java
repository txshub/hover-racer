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
	public final static byte badPacket        = Byte.parseByte("0");
	public final static byte userSendingTag   = Byte.parseByte("1");
	public final static byte badUserTag       = Byte.parseByte("2");
	public final static byte statusTag        = Byte.parseByte("3");
	public final static byte acceptedUserTag  = Byte.parseByte("4");
	public final static byte clientDisconnect = Byte.parseByte("5");
	public final static byte dontDisconnect   = Byte.parseByte("6");
	public final static byte positionUpdate   = Byte.parseByte("7");
	
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
				DataInputStream fromClient = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
				byte[] data = new byte[fromClient.readInt()];
				fromClient.readFully(data);
				ByteArrayByte msg = new ByteArrayByte(data);
				if(DEBUG) System.out.println("Request to server: " + new String(msg.getMsg(),charset));
				if(msg.getType()==statusTag) { //Server status requested
					DataOutputStream toClient = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
					writeByteMessage((status).getBytes(charset), statusTag, toClient);
					if(DEBUG) System.out.println("Sent status to client");
				} else { //Request is the client's username
					String name = new String(msg.getMsg(), charset);
					if(clientTable.userExists(name)) { //Can't have that username
						DataOutputStream toClient = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
						writeByteMessage(("Bad Username").getBytes(charset), badUserTag, toClient);
						if(DEBUG) System.out.println("Sent Bad Username to client");
					} else { //Valid Username
						clientTable.add(name);
						DataOutputStream toClient = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
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
	
	
	public static void writeByteMessage(byte[] msg, byte type, DataOutputStream client) throws IOException {
		client.writeInt(msg.length + 1);
		byte[] out = new byte[msg.length+1];
		out[0] = type;
		for(int i = 0; i < msg.length; i++) {
			out[i+1] = msg[i];
		}
		client.write(out);
		client.flush();
		
	}
}
