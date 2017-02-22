package clientComms;

import java.nio.ByteBuffer;
import java.util.HashMap;

/**
 * @author rtm592 converts classes into compact bytes messages and back
 * this method allows for 256 different message types to be sent along the network
 */
public class ClassConvertor {

	/**
	 * converts the player distance into a byte message ready to be sent
	 * messageID = 0
	 * 
	 * @param distance
	 *            the distance to be converted
	 * @return the byte array
	 */
	public static byte[] getDistanceMessage(float distance) {
		return ByteBuffer.allocate(4).putFloat(distance).array();
	}

	/**
	 * converts a byte[] to a float 
	 * messageid = 0
	 * 
	 * @param bytes
	 *            the sent message
	 * @return the distance of the player
	 */
	public static float getDistancefloat(byte[] bytes) {
		return ByteBuffer.wrap(bytes).getFloat();
	}

	/**
	 * converts sent byte message into a hashmap leaderboard
	 * messageID = 1
	 * 
	 * @param bytes
	 *            the sent message in the form of ship ID, place, ship ID, place
	 *            etc...
	 * @return the hashmap leaderboard
	 */
	public static HashMap<Integer, Integer> getRankings(byte[] bytes) {

		if (bytes.length % 2 == 1) {
			// error should not occur
			System.err.println("error something is wrong");
			return new HashMap<>();
		}
		HashMap<Integer, Integer> leaderboard = new HashMap<>();

		for (int i = 0; i < bytes.length / 2; i++) {
			int shipid = (int) bytes[i * 2];
			int place = (int) bytes[i * 2];
			leaderboard.put(place, shipid);
		}

		return new HashMap<>();

	}

	/**
	 * converts the leaderboard into a byte message
	 * messageID = 1
	 * 
	 * @param leaderboard the leaderboard
	 * @return the message
	 */
	public static byte[] getRankingsMessage(HashMap<Integer, Integer> leaderboard) {

		byte[] message = new byte[leaderboard.size() * 2 + 1];
		message[1] = (byte) 1;

		for (Integer place : leaderboard.keySet()) {

		}

		return message;

	}

}
