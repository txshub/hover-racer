package serverComms;

/**
 * Test Server to ensure client/server communications are working
 * @author simon
 *
 */
public class TestServer {
	
	/**
	 * Creates a server object and starts it
	 * @param args Any program arguments to subsequently be ignored
	 */
	public static void main(String[] args) {
		Server server = new Server(4444);
		server.start();
	}
}
