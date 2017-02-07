package audioEngine;

import org.lwjgl.openal.AL10;

import com.sun.jndi.toolkit.ctx.StringHeadTail;

/*
 * Tudor Suruceanu
 * 
 * Class representing a sound source
 */
public class Source {

	private int sourceId;
	private float compVolume;
	
	public Source() {
		
		AudioMaster.addSource(this);
		
		sourceId = AL10.alGenSources();
		compVolume = 1;
		
		setVolume(1);
		setPitch(1);
		setPosition(0, 0, 0);
		setVelocity(0, 0, 0);
		
		
//		AL10.alSourcef(sourceId, AL10.AL_ROLLOFF_FACTOR, 1);
//		AL10.alSourcef(sourceId, AL10.AL_REFERENCE_DISTANCE, 10);
//		AL10.alSourcef(sourceId, AL10.AL_MAX_DISTANCE, 250);
	}
	
	public Source(int compVolume) {
		this();
		this.compVolume = compVolume;
		setVolume(compVolume);
	}
	
	public Source(float x, float y, float z) {
		this();
		setPosition(x, y, z);
	}
	
	public Source(int v, float x, float y, float z) {
		this(v);
		setPosition(x, y, z);
	}
	
	/*
	 * Play the actual sound
	 */
	public void play (String sound) {
		stop();
		int buffer = Sounds.get(sound);
		AL10.alSourcei(sourceId, AL10.AL_BUFFER, buffer);
		AL10.alSourcePlay(sourceId);
	}
	
	/*
	 * Delete the sources
	 */
	public void delete() {
		stop();
		AL10.alDeleteSources(sourceId);
	}
	
	/*
	 * Pause the sound
	 */
	public void pause() {
		AL10.alSourcePause(sourceId);
	}
	
	/*
	 * Continue playing the sound
	 */
	public void continuePlaying() {
		AL10.alSourcePlay(sourceId);
	}
	
	/*
	 * Stop the sound
	 */
	public void stop() {
		AL10.alSourceStop(sourceId);
	}
	
	/*
	 * Set the velocity of the source
	 */
	public void setVelocity (float x, float y, float z) {
		AL10.alSource3f(sourceId, AL10.AL_VELOCITY, x, y, z);
	}
	
	/*
	 * Make the sound loop
	 */
	public void setLooping (boolean loop) {
		AL10.alSourcei(sourceId, AL10.AL_LOOPING, loop ? AL10.AL_TRUE : AL10.AL_FALSE);
	}
	
	/*
	 * Check if the source is playing any sound
	 */
	public boolean isPlaying() {
		return AL10.alGetSourcef(sourceId, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
	}
	
	/*
	 * Set the volume of the source
	 * Currently used only for testing
	 */
	public void setVolume (float volume) {
		AL10.alSourcef(sourceId, AL10.AL_GAIN, volume);
	}
	
	/*
	 * Set the pitch / frequency of the sound 
	 */
	public void setPitch (float pitch) {
		AL10.alSourcef(sourceId, AL10.AL_PITCH, pitch);
	}
	
	/*
	 * Set the position of the source
	 */
	public void setPosition (float x, float y, float z) {
		AL10.alSource3f(sourceId, AL10.AL_POSITION, x, y, z);
	}
	
	/*
	 * Change the volume in accordance to the master volume
	 */
	public void changeVolume(float master) {
		setVolume(master * compVolume);
	}
	
}
