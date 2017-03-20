package serverComms;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/** The server to handle a game
 * 
 * @author simon */
public class ServerComm extends Thread {

	public final static Charset charset = StandardCharsets.UTF_8;
	private int portNumber;
	private Lobby lobby;
	public final static boolean DEBUG = true;
	public volatile boolean runThread = true;
	// BADPACKET: Error while sending, to be ignored
	public static final byte BADPACKET = Byte.parseByte("0");
	// Client->Server Connecting for the first time and sending their username
	public static final byte USERSENDING = Byte.parseByte("1");
	// Server->Client Username isn't allowed
	public static final byte BADUSER = Byte.parseByte("2");
	// Server->Client Username is valid
	public static final byte ACCEPTEDUSER = Byte.parseByte("4");
	// Client->Server Username should be removed as client has closed the game
	public static final byte CLIENTDISCONNECT = Byte.parseByte("5");
	// Client->Server Ping every second to prevent detecting a client timeout
	public static final byte DONTDISCONNECT = Byte.parseByte("6");
	// Client->Server Request all games be sent to the client
	// Server->Client Send the name of all gamerooms currently open
	public static final byte SENDALLGAMES = Byte.parseByte("7");
	// Client->Server Make a new GameRoom with a GameSettings object passed
	public static final byte MAKEGAME = Byte.parseByte("8");
	// Client->Server Join an existing GameRoom with the id passed
	public static final byte JOINGAME = Byte.parseByte("9");
	// Server->Client Lobby connecting to is valid, gets passed the seed & player
	// names
	public static final byte VALIDGAME = Byte.parseByte("10");
	// Server->Client Lobby they're connecting to doesn't exist
	public static final byte INVALIDGAME = Byte.parseByte("11");
	// Client->Server Send ship data during the race
	public static final byte SENDPLAYERDATA = Byte.parseByte("12");
	// Client->Server NOT YET IMPLEMENTED Host sends their name and starts the
	// game
	public static final byte STARTGAME = Byte.parseByte("13");
	// Server->Client NOT YET IMPLEMENTED Sends position of all ships to clients
	public static final byte FULLPOSITIONUPDATE = Byte.parseByte("14");
	// Client->Server Establish connection to see if server is running
	public static final byte TESTCONN = Byte.parseByte("15");
	// Data about client's ship sent during setup
	public static final byte CLIENTSETUPDATA = Byte.parseByte("16");
	// Race setup data sent to all clients
	public static final byte RACESETUPDATA = Byte.parseByte("17");
	// Client->Server Request an updated copy of the room
	public static final byte REFRESHROOM = Byte.parseByte("18");
	// Server->Client Host has left the game so room closed
	public static final byte ROOMCLOSED = Byte.parseByte("19");
	// Server->Client Updating rank, current lap and whether the game has finished
	public static final byte LOGIC_UPDATE = Byte.parseByte("20");
	// Server->Client Sending the leaderboard after the game has finished
	public static final byte FINISH_DATA = Byte.parseByte("21");
	// Server->Client Sent to all clients when the game ends
	public static final byte END_GAME = Byte.parseByte("21");

	/** Creates a Server object
	 * 
	 * @param portNumber
	 *        The port to listen on for incoming connections */
	public ServerComm(int portNumber, Lobby lobby) {
		this.portNumber = portNumber;
		this.lobby = lobby;
	}

	/** Runs the server */
	public void run() {
		ServerSocket serverSocket = null;
		// Open the server socket and start listening
		try {
			serverSocket = new ServerSocket(portNumber);
			// If it can't listen on the port, throw an error
		} catch (IOException e) {
			System.err.println("Couldn't listen on port " + portNumber);
			runThread = false;
		}
		if (runThread && DEBUG) System.out.println("Now listening on port " + portNumber);
		try {
			while (runThread) {
				// Wait for a client to connect
				Socket socket = serverSocket.accept();
				if (DEBUG) System.out.println("Socket Accepted");
				// Get a buffered input stream from the client to receive the message
				DataInputStream fromClient = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
				// Read the data from the client
				byte[] data = new byte[fromClient.readInt()];
				fromClient.readFully(data);
				// Create a new ByteArrayByte with this data
				ByteArrayByte msg = new ByteArrayByte(data);
				if (DEBUG) System.out.println("Request to server: " + new String(msg.getMsg(), charset));
				if (msg.getType() == TESTCONN) {
					// Requesting to join - The message is the client's username
				} else {
					// Get the client's name
					String name = new String(msg.getMsg(), charset);
					if (lobby.clientTable.userExists(name)) { // Can't have that username
						DataOutputStream toClient = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
						writeByteMessage(("Bad Username").getBytes(charset), BADUSER, toClient);
						if (DEBUG) System.out.println("Sent Bad Username to client");
					} else { // Valid Username
						lobby.clientTable.add(name);
						DataOutputStream toClient = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
						ServerSender sender = new ServerSender(lobby.clientTable.getQueue(name), toClient);
						sender.start();
						lobby.clientTable.getQueue(name).offer(new ByteArrayByte(("Valid").getBytes(charset), ACCEPTEDUSER));
						if (DEBUG) System.out.println("Sent Accepted User to client");
						ServerReceiver receiver = new ServerReceiver(name, fromClient, lobby);
						lobby.clientTable.addReceiver(name, receiver);
						receiver.start();
						ArrayList<GameRoom> rooms = new ArrayList<GameRoom>();
						for (GameRoom room : lobby.games) {
							if (!room.isBusy()) rooms.add(room);
						}
						String out = "";
						for (GameRoom r : rooms) {
							out += r.toString() + System.lineSeparator();
						}
						lobby.clientTable.getQueue(name).offer(new ByteArrayByte(out.getBytes(charset), SENDALLGAMES));
					}
				}
			}
		} catch (IOException e) {
			System.err.println("IO error: " + e.getMessage());
		}
	}

	/** Sends a byte message to the specified output stream
	 * 
	 * @param msg
	 *        The message being sent
	 * @param type
	 *        The type of message
	 * @param client
	 *        The outputstream to send the message to
	 * @throws IOException
	 *         If there is a problem with writing */
	public static void writeByteMessage(byte[] msg, byte type, DataOutputStream client) throws IOException {
		client.writeInt(msg.length + 1);
		byte[] out = new byte[msg.length + 1];
		out[0] = type;
		for (int i = 0; i < msg.length; i++) {
			out[i + 1] = msg[i];
		}
		client.write(out);
		client.flush();
	}
}