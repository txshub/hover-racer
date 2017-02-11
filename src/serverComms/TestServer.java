package serverComms;

public class TestServer {
	
	public static void main(String[] args) {
		Server server = new Server(4444);
		server.start();
	}
}
