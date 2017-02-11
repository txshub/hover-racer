package serverComms;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class CommQueue {
	private BlockingQueue<Byte[]> queue = new LinkedBlockingQueue<Byte[]>();
	
	public void offer(Byte[] msg) {
		queue.offer(msg);
	}
	
	public Byte[] take() {
		while(true) {
			try {
				return(queue.take());
			} catch (InterruptedException e) {
				//This catch does nothing as interrupt() isn't present in
				// the code but is needed for compilation.
				//If by some miracle it is reached, just loop back and take from
				//the queue again
			}
		}
	}

}
