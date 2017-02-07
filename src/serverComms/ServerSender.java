package serverComms;
import java.net.*;
import java.io.*;
public class ServerSender extends Thread {
	private StringQueue queue;
	private PrintStream client;
	public volatile boolean continueSending = true; //Will be set to false when the thread should stop
	
	public ServerSender(StringQueue queue, PrintStream client) {
		this.queue = queue;
		this.client = client;
	}
	
	public void run() {
		while(continueSending) {
			String msg = queue.take();
			client.println(msg);
		}
	}
}
