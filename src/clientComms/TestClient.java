package clientComms;
/**
 * Test client to ensure the client/server connections are working
 * @author simon
 *
 */
public class TestClient {
	
	/**
	 * Creates a client object
	 * @param args Any program arguments to subsequently be ignored
	 */
	public static void main(String[] args) {
		System.out.println("Test");
		Client client = new Client("Bob", 4444, "localhost");
	}
}
