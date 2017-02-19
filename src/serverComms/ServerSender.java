package serverComms;
import java.io.*;
/**
 * Class to send 
 * @author simon
 *
 */
public class ServerSender extends Thread{
	
	private DataOutputStream toClient;
	private CommQueue queue;
	public volatile boolean continueSending = true; //Set to false when the thread should stop

	/**
	 * Creates a ServerSender object
	 * @param queue The queue to listen for messages on
	 * @param toClient The stream for any messages to be sent on
	 */
	public ServerSender(CommQueue queue, DataOutputStream toClient) {
		this.queue = queue;
		this.toClient = toClient;
	}
	
	/**
	 * Runs the sender
	 */
	public void run() {
		while(continueSending) {
			try {
				ByteArrayByte msg = queue.take();
				Server.writeByteMessage(msg.getMsg(),msg.getType(),toClient);
			} catch (IOException e) {
				System.err.println("Error passing message to client: " + e.getMessage());
			}
		}
	}
	
	
}
