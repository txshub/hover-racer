package audioEngine;

import java.util.HashMap;


public class Sounds {
	public static final String MUSIC = "audioEngine/music.wav";
	public static final String SFX = "audioEngine/bounce.wav";
	
	private static HashMap<String, Integer> map;
	
	public static void init() {
		
		map = new HashMap<String, Integer>();
		
		map.put(MUSIC, AudioMaster.loadSound(MUSIC));
		map.put(SFX, AudioMaster.loadSound(SFX));
		
	}
	
	public static int get(String s) {
		int buffer = map.get(s);
		return (int)buffer;
	}
}
