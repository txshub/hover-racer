package audioEngine;

import org.lwjgl.openal.AL10;

public class Source {

	private int sourceId;
	private int compVolume;
	
	public Source() {
		AudioMaster.addSource(this);
		sourceId = AL10.alGenSources();
		compVolume = 3;
		AL10.alSourcef(sourceId, AL10.AL_GAIN, 3);
		AL10.alSourcef(sourceId, AL10.AL_PITCH, 1);
		AL10.alSource3f(sourceId, AL10.AL_POSITION, 0, 0, 0);
		AL10.alSource3f(sourceId, AL10.AL_VELOCITY, 0, 0, 0);
		AL10.alSourcef(sourceId, AL10.AL_ROLLOFF_FACTOR, 1);
		AL10.alSourcef(sourceId, AL10.AL_REFERENCE_DISTANCE, 10);
		AL10.alSourcef(sourceId, AL10.AL_MAX_DISTANCE, 250);
	}
	
	public Source(int compVolume) {
		this();
		this.compVolume = compVolume;
		setVolume(compVolume);
	}
	
	/*
	 * Play the actual sound
	 */
	public void play (int buffer) {
		stop();
		AL10.alSourcei(sourceId, AL10.AL_BUFFER, buffer);
		AL10.alSourcePlay(sourceId);
	}
	
	public void delete() {
		stop();
		AL10.alDeleteSources(sourceId);
	}
	
	public void pause() {
		AL10.alSourcePause(sourceId);
	}
	
	public void continuePlaying() {
		AL10.alSourcePlay(sourceId);
	}
	
	public void stop() {
		AL10.alSourceStop(sourceId);
	}
	
	public void setVelocity (float x, float y, float z) {
		AL10.alSource3f(sourceId, AL10.AL_VELOCITY, x, y, z);
	}
	
	public void setLooping (boolean loop) {
		AL10.alSourcei(sourceId, AL10.AL_LOOPING, loop ? AL10.AL_TRUE : AL10.AL_FALSE);
	}
	
	public boolean isPlaying() {
		return AL10.alGetSourcef(sourceId, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
	}
	
	public void setVolume (float volume) {
		AL10.alSourcef(sourceId, AL10.AL_GAIN, volume);
	}
	
	public void setPitch (float pitch) {
		AL10.alSourcef(sourceId, AL10.AL_PITCH, pitch);
	}
	
	public void setPosition (float x, float y, float z) {
		AL10.alSource3f(sourceId, AL10.AL_POSITION, x, y, z);
	}
	
	public void changeVolume(float master) {
		setVolume(master * compVolume);
	}
	
}
