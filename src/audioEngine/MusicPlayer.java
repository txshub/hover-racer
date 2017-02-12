package audioEngine;

import java.util.Random;

/**
 * @authorTudor Suruceanu
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
	
	@Override
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
	
	/**
	 * Skip the song that is currently playing
	 */
	public void skip() {
		if (s.isPlaying()){
			s.stop();
		}
	}
	
	/**
	 * Close the music player
	 */
	public void terminate() {
		s.delete();
		running = false;
	}
	
}