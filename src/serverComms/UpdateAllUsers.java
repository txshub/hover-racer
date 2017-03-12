package serverComms;

import java.util.ArrayList;

public class UpdateAllUsers extends Thread {

	public volatile boolean run = true;

	final long refreshRate = (long) ((float) 1.0 / 60.0) * 1000000; // Refresh Rate in nanos

	private ArrayList<CommQueue> queues;
	private GameRoom room;

	public UpdateAllUsers(ArrayList<CommQueue> queues, GameRoom room) {
		this.queues = queues;
		this.room = room;
	}

	@Override
	public void run() {
		while (run) {
			long timeStarting = System.nanoTime();
			byte[] data = room.getShipPositions();
			for (CommQueue queue : queues) {
				queue.offer(new ByteArrayByte(data, ServerComm.FULLPOSITIONUPDATE));
			}
			long timeAfter = System.nanoTime();
			try {
				// System.out.println(timeStarting + ", " + timeAfter);
				Thread.sleep(Math.max(0, (refreshRate - timeAfter + timeStarting) / 1000)); // Sleep
				// for
				// 1/60th
				// of a
				// second
			} catch (InterruptedException e) {
				// Needed for compilation, won't actually happen
			}
		}
	}

}
