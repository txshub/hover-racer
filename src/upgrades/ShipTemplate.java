package upgrades;

import java.util.HashMap;
import java.util.Map;

public class ShipTemplate {

	Map<Stat, Float> stats;
	Map<UpgradeType, Integer> upgrades;

	// For reference: ACCELERATION, BREAK_POWER, SIZE, TURN_SPEED, AERODYNAMICS, MASS, JUMP_POWER, BOOST_CAPACITY, BOOST_GENERATION,
	// BOOST_COLLECTION, COLLISION_NEGATION;


	public ShipTemplate() {
		stats = new HashMap<Stat, Float>();
		upgrades = new HashMap<UpgradeType, Integer>();
		setDefault();
	}

	public int getCost(UpgradeType upgrade) {
		if (upgrades.containsKey(upgrade)) return roundUp(upgrade.getCost(upgrades.get(upgrade) + 1));
		else return roundUp(upgrade.getCost(1));
	}

	public int buy(UpgradeType upgrade) {
		int res = getCost(upgrade);
		// Raise level by one
		if (upgrades.containsKey(upgrade)) upgrades.put(upgrade, upgrades.get(upgrade) + 1);
		else upgrades.put(upgrade, 1);
		// Change the stats
		upgrade.stats.entrySet().stream().forEach(e -> add(e.getKey(), e.getValue()));
		return res; // Return back cost for convenience
	}

	private int roundUp(double x) {
		return (int) Math.ceil(x);
	}
	private void add(Stat stat, float value) {
		stats.put(stat, stats.get(stat) + value);
	}

	// TODO make it read content from JSON
	private void setDefault() {
		stats.put(Stat.ACCELERATION, 20f);
		stats.put(Stat.BREAK_POWER, 10f);
		stats.put(Stat.SIZE, 1f);
		stats.put(Stat.TURN_SPEED, 2.5f);
		stats.put(Stat.AERODYNAMICS, 10f);
		stats.put(Stat.MASS, 1f);
		stats.put(Stat.JUMP_POWER, 30f);
		stats.put(Stat.BOOST_CAPACITY, 100f);
		stats.put(Stat.BOOST_GENERATION, 0f);
		stats.put(Stat.BOOST_COLLECTION, 100f);
		stats.put(Stat.COLLISION_NEGATION, 0f);
	}



}
