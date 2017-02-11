package serverCommsOld;
import java.net.*;
import java.io.*;

public class Server extends Thread {
	private String status;
	private int portNumber;
	private final static boolean DEBUG = true;
	public volatile boolean runThread = true;
	
	public Server(int portNumber) {
		this.portNumber = portNumber;
	}
	
	@Override
	public void run() {
		ClientTable clientTable = new ClientTable();
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(portNumber);
		} catch(IOException e) {
			System.err.println("Couldn't listen on port " + portNumber);
			runThread = false;
		}
		if(DEBUG) System.out.println("Now listening on port " + portNumber);
		try {
			while(runThread) {
				Socket socket = serverSocket.accept();
				BufferedReader fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String request = fromClient.readLine();
				if(request.equals("Status")) { //Server status requested
					PrintStream toClient = new PrintStream(socket.getOutputStream());
					toClient.println(status);
					socket.close();
				} else { //Request must be client's name
					if(clientTable.userExists(request)) {
						PrintStream toClient = new PrintStream(socket.getOutputStream());
						toClient.println("Bad Username");
						socket.close();
					} else { //Valid Username
						clientTable.add(request);
						PrintStream toClient = new PrintStream(socket.getOutputStream());
						ServerComms comms = new ServerComms(request, clientTable.getQueue(request), toClient, fromClient, clientTable);
						socket.close();
					}
				}
			}
		} catch(IOException e) {
			System.err.println("IO error: " + e.getMessage());
		}
	}
}
