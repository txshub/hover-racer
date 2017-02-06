package upgrades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Shop {

	private static final int DEFAULT_MONEY = 1000;
	private ShipTemplate ship;
	private Collection<UpgradeType> allUpgrades;
	private int money;


	public Shop() {
		this.money = DEFAULT_MONEY;
	}
	public Shop(int money) {
		this.money = money;
	}

	public int getMoney() {
		return money;
	}
	public Collection<UpgradeDisplayData> getUpgradeData() {
		ArrayList<UpgradeDisplayData> res = new ArrayList<UpgradeDisplayData>();
		for (UpgradeType upgrade : allUpgrades) {
			if (ship.getUpgrades().containsKey(upgrade)) {
				res.add(new UpgradeDisplayData(upgrade.name, ship.getCost(upgrade), ship.getUpgrades().get(upgrade), upgrade.maxLevel,
					convertStats(upgrade.stats)));
			} else {
				res.add(new UpgradeDisplayData(upgrade.name, ship.getCost(upgrade), 0, upgrade.maxLevel, convertStats(upgrade.stats)));
			}
		}
		return res;
	}
	public Map<String, Float> getStats() {
		return convertStats(ship.getStats());
	}

	private Map<String, Float> convertStats(Map<Stat, Float> stats) {
		Map<String, Float> res = new HashMap<String, Float>();
		stats.entrySet().stream().forEach(e -> res.put(e.getKey().name(), e.getValue()));
		return res;
	}

	public int buy(String name) {
		UpgradeType upgrade = allUpgrades.stream().filter(u -> u.name.equals(name)).findAny().get(); // Get the upgrade with this name
		if (ship.getCost(upgrade) > money) throw new IllegalStateException("Tried buying an upgrade without sufficient funds");
		return ship.buy(upgrade); // If sufficient funds buy the upgrade
	}

	public ShipTemplate getShip() {
		return ship;
	}

}
