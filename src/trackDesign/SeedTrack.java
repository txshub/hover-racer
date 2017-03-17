package trackDesign;

import java.util.ArrayList;

import org.joml.Vector3f;

/**
 * Track to hold a seed for a random object and the track that resulted
 * 
 * @author simon
 *
 */
public class SeedTrack {

	private final long seed;
	private final ArrayList<TrackPoint> track;
	private static float trackHeight = 1;
	private static float barrierHeight = 20;
	private static float barrierWidth = 10;

	/**
	 * Creates a SeedTrack object
	 * 
	 * @param seed
	 *            The random seed
	 * @param track
	 *            The track this seed produces
	 */
	public SeedTrack(long seed, ArrayList<TrackPoint> track) {
		this.seed = seed;
		this.track = track;
	}

	/**
	 * Returns the seed used to make this track
	 * 
	 * @return The seed used to make this track
	 */
	public long getSeed() {
		return seed;
	}
	public static float getTrackHeight() {
		return trackHeight;
	}

	public static float getBarrierHeight() {
		return barrierHeight;
	}

	public static float getBarrierWidth() {
		return barrierWidth;
	}

	/**
	 * Returns the track
	 * 
	 * @return The track
	 */
	public ArrayList<TrackPoint> getTrack() {
		return track;
	}

	public Vector3f getStart() {
		return new Vector3f(track.get(1).x, 0f, track.get(1).y);
	}
}
