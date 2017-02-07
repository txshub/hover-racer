package audioEngine;

/*
 * Tudor Suruceanu
 * 
 * Class managing a music player
 */
public class MusicPlayer extends Thread {
	
	private Source s;
	private boolean running;
	
	// The list of songs
	private String[] songs = {
			Sounds.MUSIC	
	};
	
	public MusicPlayer() {
		s = new Source();
		running = true;
	}
	
	public void run() {
		
		s.play(songs[0]);
		int index = 0;
		
		while (running) {
			if (!s.isPlaying()) {
				index++;
				s.play(songs[songs.length % index]);
			}
		}
		
	}
	
	public void terminate() {
		s.delete();
		running = false;
	}
	
}
