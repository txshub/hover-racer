package audioEngine;

import java.util.HashMap;

/**
 * @author Tudor Suruceanu
 * Class storing the sounds
 */
public class Sounds {
  
  private static String PATH = "resources/audio/";
	
	// Music
	public static String MUSIC_1 = PATH + "the-northern-lights.wav";
	public static String MUSIC_2 = PATH + "sacred-motion.wav";
	public static String MUSIC_3 = PATH + "scorpio-moon.wav";
	
	// In-game music
	public static String IN_GAME_1 = PATH + "infinity.wav";
	public static String IN_GAME_2 = PATH + "mathrix.wav";
	public static String IN_GAME_3 = PATH + "night-pulse.wav";
	public static String IN_GAME_4 = PATH + "restricted-area.wav";
	
	// SFX
	public static String ENGINE = PATH + "futuristic-machine.wav";
	public static String BUTTON_CLICK = PATH + "button-spacey.wav";
	public static String BUTTON_HOVER = PATH + "button-hover.wav";
	public static String BACKGROUND_NOISE = PATH + "hover-craft.wav";
	
	// The list of songs
	public static String[] songs = { MUSIC_1, MUSIC_2, MUSIC_3 };
	public static String[] inGame = { IN_GAME_1, IN_GAME_2, IN_GAME_3, IN_GAME_4 };
	
	// Mapping from file to buffer
	private static HashMap<String, Integer> map;
	
	/**
	 * Load the files
	 */
	public static void init() {
		
		map = new HashMap<String, Integer>();
		
		map.put(MUSIC_1, AudioMaster.loadSound(MUSIC_1));
		map.put(MUSIC_2, AudioMaster.loadSound(MUSIC_2));
		map.put(MUSIC_3, AudioMaster.loadSound(MUSIC_3));
		
		map.put(IN_GAME_1, AudioMaster.loadSound(IN_GAME_1));
		map.put(IN_GAME_2, AudioMaster.loadSound(IN_GAME_2));
		map.put(IN_GAME_3, AudioMaster.loadSound(IN_GAME_3));
		map.put(IN_GAME_4, AudioMaster.loadSound(IN_GAME_4));
		
		map.put(ENGINE, AudioMaster.loadSound(ENGINE));
		map.put(BUTTON_CLICK, AudioMaster.loadSound(BUTTON_CLICK));
		map.put(BUTTON_HOVER, AudioMaster.loadSound(BUTTON_HOVER));
		map.put(BACKGROUND_NOISE, AudioMaster.loadSound(BACKGROUND_NOISE));
		
	}
	
	/**
	 * Get the buffer storing a certain audio file
	 * @param s - The path to the audio file (example: Sounds.SOMETHING)
	 * @return - The buffer to be played
	 */
	public static int get(String s) {
		int buffer = map.get(s);
		return (int)buffer;
	}
}
