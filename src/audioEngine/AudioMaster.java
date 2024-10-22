package audioEngine;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;

import userInterface.MainMenu;

/**
 * Class managing the audio engine
 * 
 * @author Tudor Suruceanu
 */
public class AudioMaster {

  private static List<Integer> buffers = new ArrayList<Integer>();
  private static List<Source> music = new ArrayList<Source>();
  private static List<Source> sfx = new ArrayList<Source>();
  private static List<Source> sources = new ArrayList<Source>();

  private static MusicPlayer player;
  private static InGamePlayer inGame;

  private static float sfxMaster;
  private static float musicMaster;

  private static boolean initialised = false;

  /** Initialise the master */
  public static void init() {
    // Don't reinitialise the engine
    if (initialised)
      return;

    try {
      AL.create();
    } catch (LWJGLException e) {
      e.printStackTrace();
    }
    Sounds.init();

    // The listener will be the actual player
    setListenerData(0, 0, 0);

    player = new MusicPlayer();
    inGame = new InGamePlayer();

    sfxMaster = 0.5f;
    musicMaster = 0.5f;

    setMusicVolume(musicMaster);
    setSFXVolume(sfxMaster);

    initialised = true;
  }

  /**
   * Set the information related to who listens to the sounds Currently not used
   * outside this class
   * 
   * @param x
   *          X coordinate
   * @param y
   *          Y coordinate
   * @param z
   *          Z coordinate
   */
  public static void setListenerData(float x, float y, float z) {
    AL10.alListener3f(AL10.AL_POSITION, x, y, z);
    AL10.alListener3f(AL10.AL_VELOCITY, 0, 0, 0);
  }

  /**
   * Load audio file into the system
   * 
   * @param file
   *          The audio file
   */
  public static int loadSound(String file) {
    int buffer = AL10.alGenBuffers();
    buffers.add(buffer);
    WaveData waveFile = WaveData.create(file);
    AL10.alBufferData(buffer, waveFile.format, waveFile.data, waveFile.samplerate);
    waveFile.dispose();
    return buffer;
  }

  /** Stop engine */
  public static void cleanUp() {
    for (int buffer : buffers) {
      AL10.alDeleteBuffers(buffer);
    }
    for (Source source : sources) {
      source.delete();
    }
    AL.destroy();
    initialised = false;
  }

  /**
   * Create a music source
   * 
   * @return The new music source
   */
  public static Source createMusicSource() {
    Source s = new Source();
    s.setVolume(musicMaster);
    music.add(s);
    sources.add(s);
    return s;
  }

  /**
   * Create a sfx source
   * 
   * @return The new SFX source
   */
  public static Source createSFXSource() {
    Source s = new Source();
    s.setVolume(sfxMaster);
    sfx.add(s);
    sources.add(s);
    return s;
  }

  /**
   * Create a sfx source with a specific initial volume
   * 
   * @param initialVolume
   *          The initial volume
   * @return The new SFX source
   */
  public static Source createSFXSource(float initialVolume) {
    Source s = createSFXSource();
    s.setInitialVolume(initialVolume);
    s.setVolume(sfxMaster);
    return s;
  }

  /**
   * Set the volume of all the music sources
   * 
   * @param master
   *          The value to increase/decrease the volume by
   */
  public static void setMusicVolume(float master) {
    for (Source s : music) {
      s.setVolume(master);
    }
    musicMaster = master;
  }

  // Not really
  public static float getMusicVolume() {
    return musicMaster;
  }

  /**
   * Set the volume of all the sfx sources
   * 
   * @param master
   *          The value to increase/decrease the volume by
   */
  public static void setSFXVolume(float master) {
    for (Source s : sfx) {
      s.setVolume(master);
    }
    sfxMaster = master;
  }

  // Not really
  public static float getSFXVolume() {
    return sfxMaster;
  }

  /** Start the music player */
  public static void playMusic() {
    player.start();
    MainMenu.allThreads.add(0, player);
  }

  /** Stop the music player */
  public static void stopMusic() {
    if (player != null) {
      player.terminate();
    }
  }

  /** Skip the current song */
  public static void skipMusic() {
    player.skip();
  }

  /** Start the in-game music player */
  public static void playInGameMusic() {
    inGame.start();
  }

  /** Stop the in-game music player */
  public static void stopInGameMusic() {
    inGame.stop();
  }

  /** Skip the in-game current song */
  public static void skipInGameMusic() {
    inGame.skip();
  }

  /** Increase the music volume */
  public static void increaseMusicVolume() {
    musicMaster += 0.1f;
    musicMaster = Math.min(musicMaster, 1f);
    for (Source s : music) {
      s.setVolume(musicMaster);
    }
  }

  /** Decrease the music volume */
  public static void decreaseMusicVolume() {
    musicMaster -= 0.1f;
    musicMaster = Math.max(musicMaster, 0f);
    for (Source s : music) {
      s.setVolume(musicMaster);
    }
  }

  /** Increase the sound effects volume */
  public static void increaseSFXVolume() {
    sfxMaster += 0.1f;
    sfxMaster = Math.min(sfxMaster, 1f);
    for (Source s : sfx) {
      s.setVolume(sfxMaster);
    }
  }

  /** Decrease the sound effects volume */
  public static void decreaseSFXVolume() {
    sfxMaster -= 0.1f;
    sfxMaster = Math.max(sfxMaster, 0f);
    for (Source s : sfx) {
      s.setVolume(sfxMaster);
    }
  }

}
