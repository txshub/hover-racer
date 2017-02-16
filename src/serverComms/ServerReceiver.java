package serverComms;
import java.io.*;
import java.net.*;

public class ServerReceiver extends Thread {

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
			while(true) {
				byte[] messageIn = new byte[client.readInt()];
				client.readFully(messageIn);
				if(messageIn.length == 0) return;
				ByteArrayByte fullMsg = new ByteArrayByte(messageIn);
				if(fullMsg.getType()==Byte.parseByte(Server.userSendingTag, 2)) {
					
				} else if(fullMsg.getType()==Byte.parseByte(Server.statusTag, 2)) {
					System.out.println(new String(fullMsg.getMsg(),Server.charset));
				}
			}
		} catch (IOException e) {
			//What to do?
		}
	}
	
}
