package serverComms;

import java.io.*;
import java.net.*;


public class Server extends Thread {
	private int status; //Current server status.
	private final int portNumber;
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
			if(DEBUG) System.err.println("Couldn't listen on port " + portNumber);
			runThread = false;
		}
		if(serverSocket != null && DEBUG) System.out.println("Now listening on port " + portNumber);
		try {
			while(runThread) {
				Socket socket = serverSocket.accept();
				BufferedInputStream fromClient = new BufferedInputStream(socket.getInputStream());
				int request = fromClient.read();
				if (request==0) { //Status Query
					socket.getOutputStream().write(status); //Send the status
					socket.close();
				} else {
					clientTable.add();
				}
			}
		}
	}
}
