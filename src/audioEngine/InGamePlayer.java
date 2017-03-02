package audioEngine;

import java.util.Random;

/**
 * @author txs Similar to MusicPlayer. It manages the in-game music
 */
public class InGamePlayer {

  private Source s;
  Random random;
  int index;

  public InGamePlayer() {
    s = AudioMaster.createMusicSource();
    s.setLooping(true);
    random = new Random();
    index = -1;
  }

  /**
   * Start playing the music
   */
  public void start() {
    int r = random.nextInt(Sounds.inGame.length);

    if (index == r) {
      start();
    }

    index = r;
    s.play(Sounds.inGame[index]);
  }

  /**
   * Skip the song that is currently playing
   */
  public void skip() {
    if (s.isPlaying()) {
      s.stop();
    }
    start();
  }

  /**
   * Close the in-game music player
   */
  public void stop() {
    s.delete();
  }

}
