package audioEngine;

import org.lwjgl.openal.AL10;

import com.sun.jndi.toolkit.ctx.StringHeadTail;

/**
 * @author Tudor Suruceanu
 * Class representing a sound source
 */
public class Source {

	private int sourceId;
	private float initVolume;
	private float currentVolume;
	
	public Source() {
		
		sourceId = AL10.alGenSources();
		initVolume = 0.5f;
		currentVolume = 0.5f;
		
		setCurrentVolume(0.5f * initVolume);
		setPitch(1);
		setPosition(0, 0, 0);
		setVelocity(0, 0, 0);
		
//		AL10.alSourcef(sourceId, AL10.AL_ROLLOFF_FACTOR, 1);
//		AL10.alSourcef(sourceId, AL10.AL_REFERENCE_DISTANCE, 10);
//		AL10.alSourcef(sourceId, AL10.AL_MAX_DISTANCE, 250);
	}
	
	/**
	 * Set the initial volume of a source. It will be altered when changing the master volume.
	 * @param initVolume The initial volume
	 */
	public void setInitialVolume(float initVolume) {
		this.initVolume = initVolume;
	}
	
	/**
	 * Play the actual sound
	 * @param sound The path to the audio file to be played (example: Sounds.SOMETHING)
	 */
	public void play(String sound) {
		stop();
		int buffer = Sounds.get(sound);
		AL10.alSourcei(sourceId, AL10.AL_BUFFER, buffer);
		AL10.alSourcePlay(sourceId);
	}
	
	/**
	 * Delete the sources
	 */
	public void delete() {
		stop();
		AL10.alDeleteSources(sourceId);
	}
	
	/**
	 * Pause the sound
	 */
	public void pause() {
		AL10.alSourcePause(sourceId);
	}
	
	/**
	 * Continue playing the sound
	 */
	public void continuePlaying() {
		AL10.alSourcePlay(sourceId);
	}
	
	/**
	 * Stop the sound
	 */
	public void stop() {
		AL10.alSourceStop(sourceId);
	}
	
	/**
	 * Set the velocity of the source
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param z Z coordinate
	 */
	public void setVelocity(float x, float y, float z) {
		AL10.alSource3f(sourceId, AL10.AL_VELOCITY, x, y, z);
	}
	
	/**
	 * Make the sound loop
	 * @param loop Whether the sound loops
	 */
	public void setLooping(boolean loop) {
		AL10.alSourcei(sourceId, AL10.AL_LOOPING, loop ? AL10.AL_TRUE : AL10.AL_FALSE);
	}
	
	/**
	 * Check if the source is playing any sound
	 * @return Whether the sound is playing
	 */
	public boolean isPlaying() {
		return AL10.alGetSourcef(sourceId, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
	}
	
	/**
	 * Set the volume of the source
	 * Currently used only for testing
	 * @param volume The new value for the volume
	 */
	public void setCurrentVolume(float volume) {
		if (volume > 1) {
			volume = 1;
		}
		if (volume < 0) {
			volume = 0;
		}
		
		AL10.alSourcef(sourceId, AL10.AL_GAIN, volume);
		currentVolume = volume;
	}
	
	public float getCurrentVolume() {
		return currentVolume;
	}
	
	/**
	 * Set the pitch / frequency of the sound 
	 * @param pitch The new volume for the pitch (1.0 = default pitch)
	 */
	public void setPitch(float pitch) {
		AL10.alSourcef(sourceId, AL10.AL_PITCH, pitch);
	}
	
	/**
	 * Set the position of the source
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param z Z coordinate
	 */
	public void setPosition(float x, float y, float z) {
		AL10.alSource3f(sourceId, AL10.AL_POSITION, x, y, z);
	}
	
	/**
	 * Change the volume in accordance to the master volume
	 * @param master The value to increase/decrease the value by (between 0.0 - 1.0)
	 */
	public void setVolume(float master) {
		setCurrentVolume(master * initVolume);
	}
	
}
