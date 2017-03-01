package clientComms;
import java.io.*;
import java.util.ArrayList;

import serverComms.*;

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
				if(fullMsg.getType()==ServerComm.BADUSER) {
					System.out.println("Username not valid, please pick another");
					System.exit(1);
				} else if(fullMsg.getType()==ServerComm.ACCEPTEDUSER) {
					System.out.println("Username valid. Now connected to the server");
				} else if(fullMsg.getType()==ServerComm.BADPACKET) {
					System.out.println("Need To Reconnect");
				} else if(fullMsg.getType()==ServerComm.SENDALLGAMES) {
					String[] allGames = (new String(fullMsg.getMsg(), ServerComm.charset)).split(System.lineSeparator());
					ArrayList<GameNameNumber> gameList = new ArrayList<GameNameNumber>();
					for(String s : allGames) {
						if(!s.equals("")) gameList.add(new GameNameNumber(s));
					}
					client.gameMenu.passRooms(gameList);
				} else if(fullMsg.getType()==ServerComm.INVALIDGAME) {
					//What to do if game doesn't exist?
				} else if(fullMsg.getType()==ServerComm.VALIDGAME) {
					SeedPlayers sp = new SeedPlayers(new String(fullMsg.getMsg(), ServerComm.charset));
					//What to do with seed & players?
				}
			}
		} catch (IOException e) {
			System.err.println("Server seems to have died: " + e.getMessage());
			//What to do here?
		}
	}
}
