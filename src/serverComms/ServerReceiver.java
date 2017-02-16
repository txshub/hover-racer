package serverComms;
import java.io.*;
import java.net.*;

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
				byte[] messageIn = new byte[client.readInt()];
				client.readFully(messageIn);
				detect.messageReceived = true;
				detect.interrupt();
				if(messageIn == null || messageIn.length == 0) return;
				ByteArrayByte fullMsg = new ByteArrayByte(messageIn);
				if(fullMsg.getType()==Byte.parseByte(Server.clientDisconnect, 2)) {
					table.remove(clientName);
				} else if(fullMsg.getType()==Byte.parseByte(Server.userSendingTag, 2)) {
					
				} else if(fullMsg.getType()==Byte.parseByte(Server.statusTag, 2)) {
					System.out.println(new String(fullMsg.getMsg(),Server.charset));
				}
				detect = new DetectTimeout(table, clientName);
				detect.start();
			}
		} catch (IOException e) {
			//What to do?
		}
	}
	
}
