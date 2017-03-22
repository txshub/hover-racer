package serverLogic;

import physics.network.ShipSetupData;
import physics.placeholders.DataGenerator;
import upgrades.ShipTemplate;

/** Generates random AI ship data for the server
 * 
 * @author Maciej Bogacki */
public class AIBuilder {

	private static final int AMOUNT_OF_SHIPS = 3;

	public static ShipTemplate makeBasicStats() {
		return new ShipTemplate();
	}

	public static ShipSetupData fakeAIData() {
		switch ((int) (Math.random() * AMOUNT_OF_SHIPS) + 1) {
			case 1 :
				return DataGenerator.basicShipSetup("Light bot", 1);
			case 2 :
				return DataGenerator.basicShipSetup("Heavy bot", 2);
			case 3 :
				return DataGenerator.basicShipSetup("Turbo bot", 3);
			default :
				return DataGenerator.basicShipSetup("Error bot");
		}
	}

}
