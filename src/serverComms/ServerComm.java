package serverComms;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;

import physics.core.Ship;



/**
 * The server to handle a game
 * @author simon
 *
 */
public class ServerComm extends Thread {
	public final static Charset charset = StandardCharsets.UTF_8;
	private String status;
	private int portNumber;
	private Lobby lobby;
	public final static boolean DEBUG = true;
	public volatile boolean runThread = true;
	public static final byte BADPACKET        = Byte.parseByte("0" );
	public static final byte USERSENDING      = Byte.parseByte("1" );
	public static final byte BADUSER          = Byte.parseByte("2" );
	public static final byte STATUS           = Byte.parseByte("3" );
	public static final byte ACCEPTEDUSER     = Byte.parseByte("4" );
	public static final byte CLIENTDISCONNECT = Byte.parseByte("5" );
	public static final byte DONTDISCONNECT   = Byte.parseByte("6" );
	public static final byte POSITIONUPDATE   = Byte.parseByte("7" );
	public static final byte SENDALLGAMES     = Byte.parseByte("8" );
	public static final byte MAKEGAME         = Byte.parseByte("9" );
	public static final byte JOINGAME         = Byte.parseByte("10");
	public static final byte VALIDGAME        = Byte.parseByte("11");
	public static final byte INVALIDGAME      = Byte.parseByte("12");
	
	/**
	 * Creates a Server object
	 * @param portNumber The port to listen on for incoming connections
	 */
	public ServerComm(int portNumber, Lobby lobby) {
		this.portNumber = portNumber;
		this.lobby = lobby;
	}
	
	/**
	 * Runs the server
	 */
	public void run() {
		ServerSocket serverSocket = null;
		//Open the server socket and start listening
		try {
			serverSocket = new ServerSocket(portNumber);
		//If it can't listen on the port, throw an error
		} catch(IOException e) {
			System.err.println("Couldn't listen on port " + portNumber);
			runThread = false;
		}
		if(runThread && DEBUG) System.out.println("Now listening on port " + portNumber);
		
		try {
			while(runThread) {
				//Wait for a client to connect
				Socket socket = serverSocket.accept();
				if(DEBUG) System.out.println("Socket Accepted");
				//Get a buffered input stream from the client to receive the message
				DataInputStream fromClient = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
				//Read the data from the client
				byte[] data = new byte[fromClient.readInt()];
				fromClient.readFully(data);
				//Create a new ByteArrayByte with this data
				ByteArrayByte msg = new ByteArrayByte(data);
				if(DEBUG) System.out.println("Request to server: " + new String(msg.getMsg(),charset));
				
				//Get the status of this server if requested
				if(msg.getType()==STATUS) { //Server status requested
					DataOutputStream toClient = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
					writeByteMessage((status).getBytes(charset), STATUS, toClient);
					if(DEBUG) System.out.println("Sent status to client");
					
				//Requesting to join - The message is the client's username
				} else {
					//Get the client's name
					String name = new String(msg.getMsg(), charset);
					if(lobby.clientTable.userExists(name)) { //Can't have that username
						DataOutputStream toClient = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
						writeByteMessage(("Bad Username").getBytes(charset), BADUSER, toClient);
						if(DEBUG) System.out.println("Sent Bad Username to client");
					} else { //Valid Username
						DataOutputStream toClient = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
						ServerSender sender = new ServerSender(lobby.clientTable.getQueue(name), toClient);
						sender.start();
						lobby.clientTable.getQueue(name).offer(new ByteArrayByte(("Valid").getBytes(charset),ACCEPTEDUSER));
						if(DEBUG) System.out.println("Sent Accepted User to client");
						ServerReceiver receiver = new ServerReceiver(socket, name, fromClient, lobby);
						lobby.clientTable.add(name, receiver);
						receiver.start();
						ArrayList<GameNameNumber> rooms = new ArrayList<GameNameNumber>();
						for(GameRoom room : lobby.games) {
							rooms.add(new GameNameNumber(room.name, room.id));
						}
						String out = "";
						for(GameNameNumber g : rooms) {
							out += g.toString() + System.lineSeparator();
						}
						lobby.clientTable.getQueue(name).offer(new ByteArrayByte(out.getBytes(charset),SENDALLGAMES));
						
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
