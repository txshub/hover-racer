package serverComms;

public class DetectTimeout extends Thread{

	public volatile boolean messageReceived = false;
	ClientTable table;
	String user;
	
	public DetectTimeout(ClientTable table, String user) {
		this.table = table;
		this.user = user;
	}
	
	public void run() {
		try {
			Thread.sleep(5000);
			if(!messageReceived) table.remove(user);
		} catch (InterruptedException e) {
			
		}
		
	}
	
}
