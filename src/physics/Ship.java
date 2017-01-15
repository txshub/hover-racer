package physics;

public class Ship {

	private Vector3 position;
	private Vector3 velocity;
	private Vector3 rotation;


	public Ship() {
		this.position = new Vector3(0, 0, 0);
		this.velocity = new Vector3(0, 0, 0);
		this.rotation = new Vector3(0, 0, 0);
	}
	public Ship(Vector3 v) {
		position = v.copy();
		this.velocity = new Vector3(0, 0, 0);
		this.rotation = new Vector3(0, 0, 0);
	}

	/** Accelerate in any direction within the 2d horizontal plane. The acceleration is instant; it's basically just changing velocities
	 * 
	 * @param force Force to accelerate with. Force 1 means changing velocity by 1 if in angle is along one axis
	 * @param angle Direction of the acceleration in standard notation: 0 is right, 0.5pi it forward etc. */
	public void accelerate2d(double force, double angle) {
		velocity.changeX(x -> x + force * Math.cos(angle));
		velocity.changeZ(z -> z + force * Math.sin(angle));
	}

	/** Applies air resistance, i.e. slows down velocities
	 * 
	 * @param delta Time in seconds that passed since the last call of this function */
	public void airResistance(double delta) {
		final double AIR_RESISTANCE = 10; // to be experimentally found out
		velocity.forEach(v -> Math.signum(v) * (Math.abs(v) - delta * Math.sqrt(Math.abs(v) / AIR_RESISTANCE)));
	}



}
