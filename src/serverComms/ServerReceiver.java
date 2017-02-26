package serverComms;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;

public class ServerReceiver extends Thread {
	private DetectTimeout detect;
	private String clientName;
	private DataInputStream client;
	private ClientTable table;
	private Lobby lobby;
	
	public ServerReceiver(Socket socket, String clientName, DataInputStream client, Lobby lobby) {
		this.clientName = clientName;
		this.client = client;
		this.lobby = lobby;
	}
	
	public void run() {
		try {
			detect = new DetectTimeout(table, clientName);
			while(true) {
				int in = client.readInt();
				byte[] messageIn = new byte[in];
				client.readFully(messageIn);
				detect.messageReceived = true;
				ByteArrayByte fullMsg;
				if(messageIn == null || messageIn.length == 0) {
					fullMsg = new ByteArrayByte(new byte[0], ServerComm.BADPACKET);
				} else {
					fullMsg = new ByteArrayByte(messageIn);
				}
				if (fullMsg.getType()==ServerComm.BADPACKET) {
					System.out.println("Got Bad Packet, ignoring it");
					System.out.println(in + messageIn.toString());
					table.getQueue(clientName).offer(new ByteArrayByte(("Socket Closed").getBytes(), ServerComm.BADPACKET));
				} else if(fullMsg.getType()==ServerComm.CLIENTDISCONNECT) {
					lobby.remove(clientName);
				} else if(fullMsg.getType()==ServerComm.USERSENDING) {
					
				} else if(fullMsg.getType()==ServerComm.STATUS) {
					//System.out.println(new String(fullMsg.getMsg(),ServerComm.charset));
				} else if(fullMsg.getType()==ServerComm.DONTDISCONNECT) {
					//Do Nothing - It's just making sure we don't disconnect
				} else if(fullMsg.getType()==ServerComm.MAKEGAME) {
					lobby.clientTable.addGame(new GameSettings(new String(fullMsg.getMsg(), ServerComm.charset)));
				} else if(fullMsg.getType()==ServerComm.JOINGAME) {
					if(!lobby.clientTable.joinGame(clientName,Integer.valueOf(new String(fullMsg.getMsg(), ServerComm.charset)))) {
						lobby.clientTable.getQueue(clientName).offer(new ByteArrayByte(("").getBytes(ServerComm.charset), ServerComm.INVALIDGAME));
					} else {
						long gameSeed = lobby.clientTable.getGame(Integer.valueOf(new String(fullMsg.getMsg(), ServerComm.charset))).getSeed();
						ArrayList<String> players = lobby.clientTable.getGame(Integer.valueOf(new String(fullMsg.getMsg(), ServerComm.charset))).getPlayers();
						lobby.clientTable.getQueue(clientName).offer(new ByteArrayByte(new SeedPlayers(gameSeed, players).toByteArray(), ServerComm.VALIDGAME));
					}
				} else {
					System.out.println("Unknown Message Type: " + fullMsg.getType());
				}
				detect = new DetectTimeout(table, clientName);
				detect.start();
			}
		} catch (IOException e) {
			//What to do?
		}
	}
	
}
