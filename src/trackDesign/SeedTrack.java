package trackDesign;

import java.util.ArrayList;

/**
 * Track to hold a seed for a random object and the track that resulted
 * @author simon
 *
 */
public class SeedTrack {
	private final long seed;
	private final ArrayList<TrackPoint> track;
	
	/**
	 * Creates a SeedTrack object
	 * @param seed The random seed
	 * @param track The track this seed produces
	 */
	public SeedTrack(long seed, ArrayList<TrackPoint> track) {
		this.seed = seed;
		this.track = track;
	}
	
	/**
	 * Returns the seed used to make this track
	 * @return The seed used to make this track
	 */
	public long getSeed() {
		return seed;
	}
	
	/**
	 * Returns the track
	 * @return The track
	 */
	public ArrayList<TrackPoint> getTrack() {
		return track;
	}
}
