package serverComms;

import java.nio.ByteBuffer;

import com.google.gson.Gson;

import physics.network.RaceSetupData;
import physics.network.ShipSetupData;

public class Converter {

	public static ShipSetupData buildShipData(byte[] message) {
		return (new Gson()).fromJson(new String(message), ShipSetupData.class);
	}

	public static ShipSetupData buildShipData(String message) {
		return (new Gson()).fromJson(message, ShipSetupData.class);
	}

	public static byte[] sendRaceData(RaceSetupData data, int i) {
		return (new Gson()).toJson(data.setId((byte) i)).getBytes();
	}

	public static RaceSetupData receiveRaceData(byte[] data) {
		return (new Gson()).fromJson(new String(data), RaceSetupData.class);
	}

	public static byte[] buildLogicData(int ranking, boolean finished, int currrentLap) {
		ByteBuffer buffer = ByteBuffer.allocate(4 + 1 + 4);
		buffer.putInt(ranking);
		buffer.put(finished ? (byte) 0 : (byte) 1);
		buffer.putInt(currrentLap);
		return buffer.array();
	}


}
