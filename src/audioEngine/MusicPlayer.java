package audioEngine;

import java.util.Random;

/*
 * Tudor Suruceanu
 * 
 * Class managing a music player
 */
public class MusicPlayer extends Thread {
	
	private Source s;
	private boolean running;
	Random random;
	
	public MusicPlayer() {
		s = AudioMaster.createMusicSource();
		running = true;
		random = new Random();
	}
	
	public void run() {
		
		int r = random.nextInt(Sounds.songs.length);
		int index = r;
		s.play(Sounds.songs[index]);
		
		while (running) {
			if (!s.isPlaying()) {
				r = random.nextInt(Sounds.songs.length);
				if (r != index) {
					index = r; 
					s.play(Sounds.songs[index]);
				}
			}
		}
		
	}
	
	public void skip() {
		if (s.isPlaying()){
			s.stop();
		}
	}
	
	public void terminate() {
		s.delete();
		running = false;
	}
	
}
