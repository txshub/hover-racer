package clientComms;
import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Client {
	public final static Charset charset = StandardCharsets.UTF_8;
	
	public Client(String name, int portNumber, String machineName) {
		DataOutputStream toServer = null;
		DataInputStream fromServer = null;
		Socket server = null;
		try {
			server = new Socket(machineName, portNumber);
			toServer = new DataOutputStream(server.getOutputStream());
			fromServer = new DataInputStream(server.getInputStream());
		} catch (UnknownHostException e) {
			System.err.println("Unknown host: " + machineName);
			//What to do here?
		} catch (IOException e) {
			System.err.println("Server doesn't seem to be running " + e.getMessage());
			//What to do here
		}
		
		ClientReceiver receiver = new ClientReceiver(fromServer, toServer);
		receiver.start();
		try {
			toServer.write(name.getBytes());
			receiver.join();
			toServer.close();
			fromServer.close();
			server.close();
		} catch (IOException e) {
			System.err.println("Something wrong: " + e.getMessage());
			//What to do here?
		} catch (InterruptedException e) {
			System.err.println("Unexpected interruption: " + e.getMessage());
			//What to do here?
		}
		
	}
}
