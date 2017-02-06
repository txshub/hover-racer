package audioEngine;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;

public class AudioMaster {

	private static List<Integer> buffers = new ArrayList<Integer>();
	private static List<Source> sources = new ArrayList<Source>();
	
	private static float masterVolume = 1;

	/*
	 * Initialize the master
	 */
	public static void init() {
		try {
			AL.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		Sounds.init();
	}
	
	public static void setListenerData(float x, float y, float z) {
		AL10.alListener3f(AL10.AL_POSITION, x, y, z);
		AL10.alListener3f(AL10.AL_VELOCITY, 0, 0, 0);
	}
	
	/*
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
	
	/*
	 * Stop engine
	 */
	public static void cleanUP() {
		for (int buffer : buffers) {
			AL10.alDeleteBuffers(buffer);
		}
		AL.destroy();
	}
	
	public static void increaseMasterVolume() {
		if (masterVolume < 1) {
			masterVolume += 0.1;
			for (Source s : sources) {
				s.changeVolume(masterVolume);
			}
		}
	}
	
	public static void decreaseMasterVolume() {
		if (masterVolume > 0) {
			masterVolume -= 0.1;
			for (Source s : sources) {
				s.changeVolume(masterVolume);
			}
		}
	}
	
	public static void addSource(Source s) {
		sources.add(s);
	}
	
}
