package clientComms;
import java.io.*;
import java.net.*;
public class Client {
	public Client(String name, int portNumber, String machineName) {
		PrintStream toServer = null;
		BufferedReader fromServer = null;
		Socket server = null;
		try {
			server = new Socket(machineName, portNumber);
			toServer = new PrintStream(server.getOutputStream());
			fromServer = new BufferedReader(new InputStreamReader(server.getInputStream()));
		} catch (UnknownHostException e) {
			System.err.println("Unknown host: " + machineName);
			//What to do here?
		} catch (IOException e) {
			System.err.println("Server doesn't seem to be running " + e.getMessage());
			//What to do here?
		}
		
		ClientReceiver receiver = new ClientReceiver(fromServer, toServer);
		ClientSender sender = new ClientSender(name, toServer);
	}
}
