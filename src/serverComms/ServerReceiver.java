package serverComms;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;

public class ServerReceiver extends Thread {
	private DetectTimeout detect;
	private String clientName;
	private DataInputStream client;
	private Lobby lobby;
	private GameRoom gameRoom = null;
	private int gameNum = -1;
	
	public ServerReceiver(Socket socket, String clientName, DataInputStream client, Lobby lobby) {
		this.clientName = clientName;
		this.client = client;
		this.lobby = lobby;
	}
	
	public void run() {
		try {
			detect = new DetectTimeout(lobby.clientTable, clientName);
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
					
					
				} else if(fullMsg.getType()==ServerComm.USERSENDING) {
					//Not expected at this stage so print error
					System.err.println("UserSending tag used when not expected");
					
				} else if(fullMsg.getType()==ServerComm.CLIENTDISCONNECT) {
					lobby.remove(clientName);
					
				} else if(fullMsg.getType()==ServerComm.DONTDISCONNECT) {
					//Do Nothing - It's just making sure we don't disconnect
					
				} else if(fullMsg.getType()==ServerComm.SENDALLGAMES) {
					ArrayList<GameNameNumber> rooms = new ArrayList<GameNameNumber>();
					for(GameRoom room : lobby.games) {
						if(!room.isBusy()) rooms.add(new GameNameNumber(room.name, room.id));
					}
					String out = "";
					for(GameNameNumber g : rooms) {
						out += g.toString() + System.lineSeparator();
					}
					lobby.clientTable.getQueue(clientName).offer(new ByteArrayByte(out.getBytes(ServerComm.charset),ServerComm.SENDALLGAMES));
									
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
					
				} else if(fullMsg.getType()==ServerComm.SENDPLAYERDATA) {
					if(gameRoom!=null) gameRoom.updateUser(gameNum,fullMsg.getMsg());
				} else if (fullMsg.getType()==ServerComm.STARTGAME) {
					int gameID = lobby.clientTable.getGameID(clientName);
					if(gameID == -1) {
						lobby.clientTable.getQueue(clientName).offer(new ByteArrayByte(("Can't start invalid game").getBytes(ServerComm.charset), ServerComm.INVALIDGAME));
					} else { //Valid Game
						lobby.clientTable.getGame(gameID).startGame(clientName);
					}
				} else {
					System.out.println("Unknown Message Type: " + fullMsg.getType());
				}
				detect = new DetectTimeout(lobby.clientTable, clientName);
				detect.start();
			}
		} catch (IOException e) {
			//What to do?
		}
	}

	public void setGame(GameRoom gameRoom, int gameNum) {
		this.gameRoom = gameRoom;
		this.gameNum = gameNum;
		
	}	
}
