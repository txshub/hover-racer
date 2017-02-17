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
	private Socket socket;
	
	public ServerReceiver(Socket socket, String clientName, DataInputStream client, ClientTable table, ServerSender sender) {
		this.clientName = clientName;
		this.client = client;
		this.table = table;
		this.sender = sender;
	}
	
	public void run() {
		try {
			detect = new DetectTimeout(table, clientName);
			while(true) {
				int in = client.readInt();
				byte[] messageIn = new byte[in];
				client.readFully(messageIn);
				Date d = new Date();
				System.out.println("Received Message at " + d.getSeconds());
				detect.messageReceived = true;
				ByteArrayByte fullMsg;
				if(messageIn == null || messageIn.length == 0) {
					fullMsg = new ByteArrayByte(new byte[0], Server.badPacket);
				} else {
					fullMsg = new ByteArrayByte(messageIn);
				}
				if (fullMsg.getType()==Server.badPacket) {
					System.out.println("Got Bad Packet, ignoring it");
					System.out.println(in + messageIn.toString());
					table.getQueue(clientName).offer(new ByteArrayByte(("Socket Closed").getBytes(), Server.badPacket));
				} else if(fullMsg.getType()==Server.clientDisconnect) {
					table.remove(clientName);
					if(Server.DEBUG) System.out.println("Client Disconnected");
				} else if(fullMsg.getType()==Server.userSendingTag) {
					
				} else if(fullMsg.getType()==Server.statusTag) {
					//System.out.println(new String(fullMsg.getMsg(),Server.charset));
				} else if(fullMsg.getType()==Server.dontDisconnect) {
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
