package audioEngine;

import java.util.HashMap;

/*
 * Tudor Suruceanu
 * 
 * Class storing the sounds
 */
public class Sounds {
	
	// Music
	public static String MUSIC_1 = "audioEngine/the-northern-lights.wav";
	public static String MUSIC_2 = "audioEngine/sacred-motion.wav";
	public static String MUSIC_3 = "audioEngine/scorpio-moon.wav";
	
	// SFX
	public static String ENGINE = "audioEngine/futuristic-machine.wav";
	public static String BUTTON_CLICK = "audioEngine/lasertwang.wav";
	public static String BACKGROUND_NOISE = "audioEngine/hover-craft.wav";
	
	// The list of songs
	public static String[] songs = { MUSIC_1, MUSIC_2, MUSIC_3 };
	
	// Mapping from file to buffer
	private static HashMap<String, Integer> map;
	
	/*
	 * Load the files
	 */
	public static void init() {
		
		map = new HashMap<String, Integer>();
		
		map.put(MUSIC_1, AudioMaster.loadSound(MUSIC_1));
		map.put(MUSIC_2, AudioMaster.loadSound(MUSIC_2));
		map.put(MUSIC_3, AudioMaster.loadSound(MUSIC_3));
		
		map.put(ENGINE, AudioMaster.loadSound(ENGINE));
		map.put(BUTTON_CLICK, AudioMaster.loadSound(BUTTON_CLICK));
		map.put(BACKGROUND_NOISE, AudioMaster.loadSound(BACKGROUND_NOISE));
		
	}
	
	/*
	 * Get the file to be played
	 */
	public static int get(String s) {
		int buffer = map.get(s);
		return (int)buffer;
	}
}
