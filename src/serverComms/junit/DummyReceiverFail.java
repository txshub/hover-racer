package serverComms.junit;

import java.io.DataInputStream;
import java.io.IOException;

import clientComms.Client;
import clientComms.ClientReceiver;
import serverComms.ByteArrayByte;
import serverComms.ServerComm;

public class DummyReceiverFail extends ClientReceiver {

	DataInputStream server;
	public boolean testsPassed;

	public DummyReceiverFail(DataInputStream server, Client client) {
	    super(server, client);
	    this.server = server;
	  }
	
	public void run() {
	    try {
	      byte[] msg = new byte[server.readInt()];
	      server.readFully(msg);
	      if (msg == null || msg.length == 0) {
	        server.close();
	      }
	      ByteArrayByte fullMsg = new ByteArrayByte(msg);
	      if (fullMsg.getType() == ServerComm.BADUSER);
	        testsPassed = true;
	    } catch (IOException e) {

	    }
	  }
	
}