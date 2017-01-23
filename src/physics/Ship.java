package physics;

import java.util.ArrayList;
import java.util.Collection;

import placeholders.Action;
import placeholders.ControllerInt;
import placeholders.ExportedShip;
import placeholders.FakeController;

public class Ship {

	private static final int ACCELERATION = 1; // How fast does the ship accelerate
	private static final float BREAK_POWER = 5; // How fast does it break
	private static final float TURN_SPEED = 0.5f; // How fast does it turn
	private static final float AIR_RESISTANCE = 10; // How fast do ships slow down (this and acceleration determines the max speed)
	private static final float DEFAULT_MASS = 1;
	private static final float DEFAULT_SIZE = 1;
	private Vector3 position;
	private Vector3 velocity;
	private Vector3 rotation;
	private float mass;
	private float size;
	private ControllerInt controller;
	private Collection<Ship> otherShips;



	/** Creates a ship with position (0,0,0), no inputs an no other ships. For testing only */
	public Ship() {
		this(new Vector3(0, 0, 0), new FakeController(), new ArrayList<>());
	}
	/** Creates a new player-controlled ship
	 * 
	 * @param startingPosition Vector describing this ship's starting position.
	 * @param controller Controller object for steering the ship
	 * @param otherShips Other ships to possibly collide with */
	public Ship(Vector3 startingPosition, ControllerInt controller, Collection<Ship> otherShips) {
		position = startingPosition.copy();
		this.velocity = new Vector3(0, 0, 0);
		this.rotation = new Vector3(0, 0, 0);
		this.mass = DEFAULT_MASS;
		this.size = DEFAULT_SIZE;
		this.controller = controller;
		this.otherShips = otherShips;
	}
	public Ship(float[] numbers) {
		// TODO
	}

	/** Accelerate in any direction within the 2d horizontal plane. The acceleration is instant; it's basically just changing velocities
	 * 
	 * @param force Force to accelerate with. Force 1 means changing velocity by 1 if in angle is along one axis
	 * @param angle Direction of the acceleration in standard notation: 0 is right, 0.5pi it forward etc. */
	public void accelerate2d(float force, float angle) {
		final float angle2 = correctAngle(rotation.getY() + angle); // Adjust the angle for ship's own rotation
		velocity.changeX(x -> x + force * Math.cos(angle2));
		velocity.changeZ(z -> z + force * Math.sin(angle2));
	}

	/** Applies air resistance, i.e. slows down velocities
	 * 
	 * @param delta Time in seconds that passed since the last call of this function */
	private void airResistance(float delta) {
		velocity.forEach(v -> Math.signum(v) * (Math.abs(v) - delta * Math.sqrt(Math.abs(v) / AIR_RESISTANCE)));
	}

	/** Changes the position by given velocity
	 * 
	 * @param delta Time in seconds that passed since the last call of this function */
	private void updatePosition(float delta) {
		position.add(velocity);
	}

	/** Updates all physics
	 * 
	 * @param delta Time in seconds that passed since the last call of this function */
	public void update(float delta) {
		airResistance(delta);
		handleControls(delta);
		doCollisions();
		updatePosition(delta);
	}

	/** Handle controls from the player.
	 * Once we have server controlled ships, this method (or even whole update) could be abstract and differently implemented by PlayerShip
	 * and ServerShip. */
	private void handleControls(float delta) {
		Collection<Action> keys = controller.getPressedKeys();
		if (keys.contains(Action.FORWARD)) accelerate2d(delta * ACCELERATION, (float) Math.PI / 2);
		if (keys.contains(Action.BREAK)) airResistance(delta * BREAK_POWER); // Breaking slows you down, no matter how you're moving
		// if (keys.contains(Action.BREAK)) accelerate2d(delta * ACCELERATION, Math.PI * 1.5); // Breaking accelerates backwards
		if (keys.contains(Action.STRAFE_RIGHT)) accelerate2d(delta * ACCELERATION / 2, 0);
		if (keys.contains(Action.STRAFE_LEFT)) accelerate2d(delta * ACCELERATION / 2, (float) Math.PI);
		if (keys.contains(Action.TURN_RIGHT)) rotation.changeY(y -> correctAngle(y - TURN_SPEED));
		if (keys.contains(Action.TURN_LEFT)) rotation.changeY(y -> correctAngle(y + TURN_SPEED));
	}

	private void doCollisions() {
		otherShips.stream().filter(ship -> ship.getPosition().distanceTo(this.position) <= ship.getSize() + this.size)
			.forEach(s -> collideWith(s));
	}

	/** Changes the velocity to account for a collision with a different ship */
	private void collideWith(Ship ship) {
		velocity.add(ship.getVelocity().substract(this.velocity).multiply(ship.getMass()).multiply(1 / this.getMass()).multiply(0.5));
	}

	/** Corrects an angle to fit between 0 and 2pi (e.g. 7.13pi->1.13pi, -0.13pi->1.87pi) */
	private float correctAngle(float angle) {
		while (angle < 0)
			angle += Math.PI * 2;
		while (angle >= Math.PI * 2)
			angle -= Math.PI * 2;
		return angle;
	}
	private float correctAngle(double angle) {
		return correctAngle((float) angle);
	}



	/** @return Position of this ship's centre */
	public Vector3 getPosition() {
		return position.copy();
	}
	/** @return Size, or radius of this ship's hitbox */
	public float getSize() {
		return size;
	}
	/** @return This ship's current velocities, separately in all dimensions */
	public Vector3 getVelocity() {
		return velocity.copy();
	}
	/** @return This ship's mass */
	public float getMass() {
		return mass;
	}

	public float[] export() {
		return (new ExportedShip(position, velocity, controller.getPressedKeys())).toNumbers();
	}

}
