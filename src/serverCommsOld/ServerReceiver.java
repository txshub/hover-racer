package serverCommsOld;
import java.net.*;
import java.io.*;

public class ServerReceiver extends Thread {

	private String clientName;
	private BufferedReader client;
	private ClientTable clientTable;
	private ServerSender sender;

	public ServerReceiver(String name, BufferedReader client, ClientTable table, ServerSender sender) {
		this.clientName = name;
		this.client = client;
		this.clientTable = table;
		this.sender = sender;
	}
	
	public void run() {
		try {
			while(true) {
				String messageIn = client.readLine();
				if(messageIn == null) return;
			}
		} catch(IOException e) {
			System.err.println("Problem with client " + clientName);
			e.printStackTrace();
		}
	}
	
}
