package audioEngine;

import java.util.Random;

/**
 * Class managing a music player
 * 
 * @author Tudor Suruceanu
 */
public class MusicPlayer extends Thread {

  private Source s;
  private boolean running;
  Random random;

  /** Constructor */
  public MusicPlayer() {
    s = AudioMaster.createMusicSource();
    running = true;
    random = new Random();
  }

  @Override
  public void run() {

	running = true;
    int r = random.nextInt(Sounds.songs.length);
    int index = r;
    s.play(Sounds.songs[index]);

    while (running && !this.isInterrupted()) {
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
    if (s.isPlaying()) {
      s.stop();
    }
  }

  /**
   * Close the music player
   */
  public void terminate() {
    running = false;
  }

}
