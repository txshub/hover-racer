package upgrades;


public enum Stat {

	ACCELERATION("Acceleration"), BREAK_POWER("Break power"), SIZE("Size"), TURN_SPEED("Turn speed"), AERODYNAMICS("Aerodynamics"), MASS(
		"Mass"), JUMP_POWER("Jumping power"), BOOST_CAPACITY("Boost capacity"), BOOST_GENERATION(
			"Boost generation"), BOOST_COLLECTION("Boost collection bonus"), COLLISION_NEGATION("Collision dampening");

	private String name;

	private Stat(String name) {
		this.name = name;
	}

}
