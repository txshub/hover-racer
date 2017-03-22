package upgrades;

public enum Stat {

	ACCELERATION("Acceleration"), BREAK_POWER("Break power"), SIZE("Size"), TURN_SPEED("Turn speed"), DRAG("Drag"), MASS(
		"Mass"), JUMP_POWER("Jumping power"), BOOST_CAPACITY("Boost capacity"), BOOST_GENERATION("Boost generation"), BOOST_COLLECTION(
			"Boost collection bonus"), COLLISION_NEGATION("Collision dampening"), WALL_ELASTICITY(
				"Elasticity of ship-wall collisions"), SHIP_ELASTICITY("Elasticity of ship-ship collision");

	private String name;

	private Stat(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
