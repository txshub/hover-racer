package serverComms;

import java.io.BufferedReader;
import java.io.PrintStream;

public class ServerComms extends Thread {

	private ServerSender sender = null;
	private ServerReceiver receiver = null;
	
	public ServerComms(String name, StringQueue queue, PrintStream toClient, BufferedReader fromClient, ClientTable clientTable) {
		
	}

}
