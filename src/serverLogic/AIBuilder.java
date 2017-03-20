package serverLogic;

import physics.network.ShipSetupData;
import upgrades.ShipTemplate;

/** Currently a stub, will generate AI ships for the server
 * 
 * @author Maciej Bogacki */
public class AIBuilder {

	public static ShipTemplate makeBasicStats() {
		return new ShipTemplate();
	}

	public static ShipSetupData fakeAIData() {
		if (Math.random() > .5) {
			return new ShipSetupData("Bot" + (int) (Math.random() * 100), "newShip", "newShipTexture", makeBasicStats());
		} else {
			return new ShipSetupData("Bot" + (int) (Math.random() * 100), "hovercraft", "hover2Texture", makeBasicStats());
		}
	}

}
