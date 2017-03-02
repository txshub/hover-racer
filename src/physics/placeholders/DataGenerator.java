package physics.placeholders;

import com.google.gson.Gson;

import physics.network.ShipSetupData;
import upgrades.ShipTemplate;

public class DataGenerator {

	public static ShipSetupData basicShipSetup(String nickname) {
		return new ShipSetupData(nickname, "newShip", "newShipTexture", new ShipTemplate());
	}

	public static ShipSetupData fromJson(String json) {
		return (new Gson()).fromJson(json, ShipSetupData.class);
	}

}
