package serverComms;

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


}
