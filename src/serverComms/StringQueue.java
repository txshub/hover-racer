package serverComms;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
public class StringQueue {
	private BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
	
	public void offer(String s) {
		queue.offer(s);
	}
	
	public String take() {
		while(true) {
			try {
				return(queue.take());
			} catch (InterruptedException e) {
				//This catch does nothing as interrupt() isn't present in the code
				//Therefore if this is reached, it should loop back and take from the queue
			}
		}
	}
}
