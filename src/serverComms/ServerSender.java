package serverComms;
import java.io.*;

public class ServerSender extends Thread{
	
	private CommQueue queue;
	private DataOutputStream client;
	public volatile boolean continueSending = true; //Set to false when the thread should stop

	public ServerSender(CommQueue queue, DataOutputStream client) {
		this.queue = queue;
		this.client = client;
	}
	
	public void run() {
		while(continueSending) {
			try {
				Byte[] msg = queue.take();
				byte[] msgSend = new byte[msg.length];
				for(int i = 0; i < msg.length; i++) {
					msgSend[i] = msg[i];
				}
				client.write(msgSend);
			} catch (IOException e) {
				System.err.println("Error passing message to client: " + e.getMessage());
			}
		}
	}
	
	
}
