package serverComms;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Server extends Thread {
	public final static Charset charset = StandardCharsets.UTF_8;
	private String status;
	private int portNumber;
	public final static boolean DEBUG = true;
	public volatile boolean runThread = true;
	
	public Server(int portNumber) {
		this.portNumber = portNumber;
	}
	
	public void run() {
		ClientTable clientTable = new ClientTable();
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(portNumber);
		} catch(IOException e) {
			System.err.println("Couldn't listen on port " + portNumber);
			runThread = false;
		}
		if(runThread && DEBUG) System.out.println("Now listening on port " + portNumber);
		try {
			while(runThread) {
				Socket socket = serverSocket.accept();
				System.out.println("Socket Accepted");
				DataInputStream fromClient = new DataInputStream(socket.getInputStream());
				System.out.println("1");
				byte[] data = new byte[fromClient.readInt()];
				System.out.println("2");
				fromClient.readFully(data);
				System.out.println("3");
				String request = new String(data, charset);
				if(DEBUG) System.out.println("Request to server: " + request);
				if(request.equals("#Status")) { //Server status requested
					DataOutputStream toClient = new DataOutputStream(socket.getOutputStream());
					toClient.write(status.getBytes(charset));
				} else { //Request is the client's username
					if(clientTable.userExists(request)) { //Can't have that username
						DataOutputStream toClient = new DataOutputStream(socket.getOutputStream());
						toClient.write(("Bad Username").getBytes(charset));
					} else { //Valid Username
						clientTable.add(request);
						DataOutputStream toClient = new DataOutputStream(socket.getOutputStream());
						ServerSender sender = new ServerSender(clientTable.getQueue(request), toClient);
						sender.start();
						(new ServerReceiver(socket, request, fromClient, clientTable, sender)).start();
					}
				}
			}
		} catch(IOException e) {
			System.err.println("IO error: " + e.getMessage());
		}
	}
}
