package clientComms;
import java.io.*;
public class ClientSender extends Thread {

	private String name;
	private PrintStream server;
	
	public ClientSender(String name, PrintStream server) {
		this.name = name;
		this.server = server;
	}
	
	public void run() {
		BufferedReader user = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			server.println(name); //Tell server what our name is
			while (true) {
				String text = user.readLine();
				server.println(text);
			}
		} catch (IOException e) {
			System.err.println("Communication broke in ClientSender");
			//What to do here?
		}
	}
}
