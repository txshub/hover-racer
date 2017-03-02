package physics.placeholders;

import physics.network.ShipSetupData;
import upgrades.ShipTemplate;

public class DataGenerator {

	public static ShipSetupData basicShipSetup(String nickname) {
		return new ShipSetupData(nickname, "newShip", "newShipTexture", new ShipTemplate());
	}



}
