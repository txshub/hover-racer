package audioEngine;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import javax.print.attribute.standard.DateTimeAtCompleted;

import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;

/**
 * @author Tudor Suruceanu 
 * Class managing the audio engine 
 */
public class AudioMaster {

	private static List<Integer> buffers = new ArrayList<Integer>();
	private static List<Source> music = new ArrayList<Source>();
	private static List<Source> sfx = new ArrayList<Source>();
	private static List<Source> sources = new ArrayList<Source>();
	
	private static MusicPlayer player;
	private static InGamePlayer inGame;
	
	private static boolean initialised = false;
	

	/**
	 * Initialise the master
	 */
	public static void init() {
	  // Don't reinitialise the engine
	  if (initialised) return;
		
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
		
		initialised = true;
	}
	
	/**
	 * Set the information related to who listens to the sounds
	 * Currently not used outside this class
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param z Z coordinate
	 */
	public static void setListenerData(float x, float y, float z) {
		AL10.alListener3f(AL10.AL_POSITION, x, y, z);
		AL10.alListener3f(AL10.AL_VELOCITY, 0, 0, 0);
	}
	
	/**
	 * Load audio file into the system
	 * @param file The audio file
	 */
	public static int loadSound(String file) {
		int buffer = AL10.alGenBuffers();
		buffers.add(buffer);
		WaveData waveFile = WaveData.create(file);
		AL10.alBufferData(buffer, waveFile.format, waveFile.data, waveFile.samplerate);
		waveFile.dispose();
		return buffer;
	}
	
	/**
	 * Stop engine
	 */
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
	 * @return The new music source
	 */
	public static Source createMusicSource() {
		Source s = new Source();
		music.add(s);
		sources.add(s);
		return s;
	}
	
	/**
	 * Create a sfx source
	 * @return The new SFX source
	 */
	public static Source createSFXSource() {
		Source s = new Source();
		sfx.add(s);
		sources.add(s);
		return s;
	}
	
	/**
	 * Set the volume of all the music sources
	 * @param master The value to increase/decrease the volume by
	 */
	public static void setMusicVolume(float master) {
		for (Source s : music) {
			s.setVolume(master);
		}
	}
	
	/**
	 * Set the volume of all the sfx sources
	 * @param master The value to increase/decrease the volume by
	 */
	public static void setSFXVolume(float master) {
		for (Source s : sfx) {
			s.setVolume(master);
		}
	}
	
	/**
	 * Start the music player
	 */
	public static void playMusic() {
		player.start();
	}
	
	/**
	 * Stop the music player
	 */
	public static void stopMusic() {
		player.terminate();
	}
	
	/**
	 * Skip the current song
	 */
	public static void skipMusic() {
		player.skip();
	}
	
	/**
	 * Start the in-game music player
	 */
	public static void playInGameMusic() {
		inGame.start();
	}
	
	/**
	 * Stop the in-game music player
	 */
	public static void stopInGameMusic() {
		inGame.stop();
	}
	
	/**
	 * Skip the in-game current song
	 */
	public static void skipInGameMusic() {
		inGame.skip();
	}
	
	public static void increaseMusicVolume() {
		for (Source s : music) {
			s.setCurrentVolume(s.getCurrentVolume() + 0.1f);
		}
	}
	
	public static void decreaseMusicVolume() {
		for (Source s : music) {
			s.setCurrentVolume(s.getCurrentVolume() - 0.1f);
		}	
	}
	
	public static void increaseSFXVolume() {
		for (Source s : sfx) {
			s.setCurrentVolume(s.getCurrentVolume() + 0.1f);
		}
	}
	
	public static void decreaseSFXVolume() {
		for (Source s : sfx) {
			s.setCurrentVolume(s.getCurrentVolume() - 0.1f);
		}
	}
	
	public static void deleteSource(Source s) {
		sources.remove(s);
		sfx.remove(s);
		music.remove(s);
	}
}
