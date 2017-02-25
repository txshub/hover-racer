package serverComms;
import java.io.*;
import java.net.*;
import java.util.Date;

public class ServerReceiver extends Thread {
	private DetectTimeout detect;
	private String clientName;
	private DataInputStream client;
	private ClientTable table;
	private ServerSender sender;
	private Lobby lobby;
	
	public ServerReceiver(Socket socket, String clientName, DataInputStream client, ClientTable table, ServerSender sender, Lobby lobby) {
		this.clientName = clientName;
		this.client = client;
		this.table = table;
		this.sender = sender;
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
					fullMsg = new ByteArrayByte(new byte[0], ServerComm.badPacket);
				} else {
					fullMsg = new ByteArrayByte(messageIn);
				}
				if (fullMsg.getType()==ServerComm.badPacket) {
					System.out.println("Got Bad Packet, ignoring it");
					System.out.println(in + messageIn.toString());
					table.getQueue(clientName).offer(new ByteArrayByte(("Socket Closed").getBytes(), ServerComm.badPacket));
				} else if(fullMsg.getType()==ServerComm.clientDisconnect) {
					lobby.remove(clientName);
				} else if(fullMsg.getType()==ServerComm.userSendingTag) {
					
				} else if(fullMsg.getType()==ServerComm.statusTag) {
					//System.out.println(new String(fullMsg.getMsg(),ServerComm.charset));
				} else if(fullMsg.getType()==ServerComm.dontDisconnect) {
					//Do Nothing - It's just making sure we don't disconnect
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
