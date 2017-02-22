package clientComms;

import java.nio.ByteBuffer;
import java.util.HashMap;

/**
 * @author rtm592 converts classes into compact bytes messages and back this
 *         method allows for 256 different message types to be sent along the
 *         network. first byte is the messageId
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
	 * converts a byte[] to a float. messageid = 0
	 * 
	 * @param bytes
	 *            the sent message
	 * @return the distance of the player
	 */
	public static float getDistancefloat(byte[] bytes) {
		return ByteBuffer.wrap(bytes).getFloat();
	}

	/**
	 * converts sent byte message into a hashmap leaderboard. messageID = 1
	 * 
	 * @param bytes
	 *            the sent message in the form of ship ID, place, ship ID, place
	 *            etc...
	 * @return the hashmap leaderboard
	 */
	public static HashMap<Integer, Integer> getRankings(byte[] bytes) {

		if (bytes.length - 1 % 2 == 1 || bytes[0] != (byte) 1) {
			// error should not occur
			System.err.println("error something is wrong");
			return new HashMap<>();
		}
		HashMap<Integer, Integer> leaderboard = new HashMap<>();

		for (int i = 0; i < bytes.length / 2; i++) {
			int shipid = (int) bytes[i * 2 + 1];
			int place = (int) bytes[i * 2 + 2];
			leaderboard.put(place, shipid);
		}

		return leaderboard;

	}

	/**
	 * converts the leaderboard into a byte message messageID = 1
	 * 
	 * @param leaderboard
	 *            the leaderboard
	 * @return the message
	 */
	public static byte[] getRankingsMessage(HashMap<Integer, Integer> leaderboard) {

		// under the assumption that ships have incrementing ids in the game
		// starting at 0
		byte[] message = new byte[leaderboard.size() * 2 + 1];
		message[0] = (byte) 1;

		for (Integer place : leaderboard.keySet()) {
			message[place * 2 + 1] = (byte) place.intValue();
			message[place * 2 + 2] = (byte) leaderboard.get(place).intValue();
		}

		return message;

	}

	/**
	 * for testing the class need J-units
	 */
	public static void main(String[] args) {
		HashMap<Integer, Integer> map = new HashMap<>();
		map.put(0, 7);
		map.put(1, 30);
		map.put(2, 56);
		map.put(3, 87);

		map = getRankings(getRankingsMessage(map));

		for (Integer place : map.keySet()) {
			System.out.println(place + ", " + map.get(place));
		}

		float out = getDistancefloat(getDistanceMessage(10f));
		System.out.println("should be 10.0: " + out);
	}

}
