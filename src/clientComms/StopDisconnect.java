package clientComms;

import java.io.IOException;

import serverComms.Server;

public class StopDisconnect extends Thread {

	Client client;
	
	public StopDisconnect(Client client) {
		this.client = client;
	}
	
	public void run() {
		try {
			while(true) {
				client.sendByteMessage(new byte[0], Server.dontDisconnect);
				Thread.sleep(1000);
			}
		} catch (IOException e) {
			//Don't do anything -you'll be disconnected anyway
		} catch (InterruptedException e) {
			//Won't actually happen but this needs to be here to compile
		}
	}
	
}
