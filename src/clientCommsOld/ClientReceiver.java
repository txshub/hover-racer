package clientCommsOld;
import java.io.*;
import java.net.*;
public class ClientReceiver extends Thread {
	
	private BufferedReader server;
	private PrintStream serverSender;
	
	public ClientReceiver(BufferedReader server, PrintStream serverSender) {
		this.server = server;
		this.serverSender = serverSender;
	}
	
	public void run() {
		try {
			while(true) {
				String s = server.readLine();
				if(s==null) {
					server.close();
					throw new IOException("Got null from server");
				} else if(s.equals("Response")) {
					//Do an action here
				}
			}
		} catch (IOException e) {
			System.out.println("Server seems to have died: " + e.getMessage());
			//What to do here?
		}
	}
}
