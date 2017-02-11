package serverCommsOld;

import java.io.BufferedReader;
import java.io.PrintStream;

public class ServerComms extends Thread {

	private ServerSender sender;
	private ServerReceiver receiver;
	
	public ServerComms(String name, StringQueue queue, PrintStream toClient, BufferedReader fromClient, ClientTable clientTable) {
		sender = new ServerSender(queue, toClient);
		receiver = new ServerReceiver(name, fromClient, clientTable, sender);
	}

}
