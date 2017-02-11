package clientComms;
import java.io.*;

public class ClientReceiver extends Thread {
	private DataInputStream server;
	private DataOutputStream serverSender;
	
	public ClientReceiver(DataInputStream server, DataOutputStream toServer) {
		this.server = server;
		this.serverSender = serverSender;
	}
	
	@Override
	public void run() {
		try {
			while(true) {
				byte[] msg = new byte[server.readInt()];
				server.readFully(msg);
				if(msg == null || msg.length==0) {
					server.close();
					throw new IOException("Got null from the server");
				}
				switch(new String(msg, Client.charset)) {
				default:
				}
			}
		} catch (IOException e) {
			System.err.println("Server seems to have died: " + e.getMessage());
			//What to do here?
		}
	}
}
