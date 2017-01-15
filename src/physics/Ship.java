package physics;

import java.util.Collection;

import placeholders.Action;
import placeholders.ControllerInt;

public class Ship {

	private static final int ACCELERATION = 1; // How fast does the ship accelerate
	private static final double BREAK_POWER = 5; // How fast does it break
	private static final double TURN_SPEED = 0.5; // How fast does it turn
	private Vector3 position;
	private Vector3 velocity;
	private Vector3 rotation;
	private double mass;
	private final double DEFAULT_MASS = 1;
	private ControllerInt controller;


	public Ship(ControllerInt controller) {
		this.position = new Vector3(0, 0, 0);
		this.velocity = new Vector3(0, 0, 0);
		this.rotation = new Vector3(0, 0, 0);
		this.mass = DEFAULT_MASS;
		this.controller = controller;
	}
	public Ship(Vector3 v, ControllerInt controller) {
		position = v.copy();
		this.velocity = new Vector3(0, 0, 0);
		this.rotation = new Vector3(0, 0, 0);
		this.mass = DEFAULT_MASS;
		this.controller = controller;
	}

	/** Accelerate in any direction within the 2d horizontal plane. The acceleration is instant; it's basically just changing velocities
	 * 
	 * @param force Force to accelerate with. Force 1 means changing velocity by 1 if in angle is along one axis
	 * @param angle Direction of the acceleration in standard notation: 0 is right, 0.5pi it forward etc. */
	public void accelerate2d(double force, double angle) {
		final double angle2 = correctAngle(rotation.getY() + angle); // Adjust the angle for ship's own rotation
		velocity.changeX(x -> x + force * Math.cos(angle2));
		velocity.changeZ(z -> z + force * Math.sin(angle2));
	}

	/** Applies air resistance, i.e. slows down velocities
	 * 
	 * @param delta Time in seconds that passed since the last call of this function */
	private void airResistance(double delta) {
		final double AIR_RESISTANCE = 10; // to be experimentally found out
		velocity.forEach(v -> Math.signum(v) * (Math.abs(v) - delta * Math.sqrt(Math.abs(v) / AIR_RESISTANCE)));
	}

	/** Changes the position by given velocity
	 * 
	 * @param delta Time in seconds that passed since the last call of this function */
	private void updatePosition(double delta) {
		position.add(velocity);
	}

	/** Updates all physics
	 * 
	 * @param delta Time in seconds that passed since the last call of this function */
	public void update(double delta) {
		airResistance(delta);
		updatePosition(delta);
		Collection<Action> keys = controller.getPressedKeys();
		if (keys.contains(Action.FORWARD)) accelerate2d(delta * ACCELERATION, Math.PI / 2);
		if (keys.contains(Action.BREAK)) airResistance(delta * BREAK_POWER); // Breaking slows you down, no matter how you're moving
		// if (keys.contains(Action.BREAK)) accelerate2d(delta * ACCELERATION, Math.PI * 1.5); // Breaking accelerates backwards
		if (keys.contains(Action.STRAFE_RIGHT)) accelerate2d(delta * ACCELERATION / 2, 0);
		if (keys.contains(Action.STRAFE_LEFT)) accelerate2d(delta * ACCELERATION / 2, Math.PI);
		if (keys.contains(Action.TURN_RIGHT)) rotation.changeY(y -> correctAngle(y - TURN_SPEED));
		if (keys.contains(Action.TURN_LEFT)) rotation.changeY(y -> correctAngle(y + TURN_SPEED));
	}


	/** Corrects an angle to fit between 0 and 2pi (e.g. 7.13pi->1.13pi, -0.13pi->1.87pi) */
	private double correctAngle(double angle) {
		while (angle < 0)
			angle += Math.PI * 2;
		while (angle >= Math.PI * 2)
			angle -= Math.PI * 2;
		return angle;
	}



}
