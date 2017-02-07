package audioEngine;

import java.util.HashMap;

/*
 * Tudor Suruceanu
 * 
 * Class storing the sounds
 */
public class Sounds {
	
	// Music
	public static final String MUSIC = "audioEngine/music.wav";
	
	// SFX
	public static final String SFX = "audioEngine/bounce.wav";
	
	private static HashMap<String, Integer> map;
	
	/*
	 * Load the files
	 */
	public static void init() {
		
		map = new HashMap<String, Integer>();
		
		map.put(MUSIC, AudioMaster.loadSound(MUSIC));
		map.put(SFX, AudioMaster.loadSound(SFX));
		
	}
	
	/*
	 * Get the file to be played
	 */
	public static int get(String s) {
		int buffer = map.get(s);
		return (int)buffer;
	}
}
