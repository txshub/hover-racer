package physics;

import java.util.ArrayList;
import java.util.Collection;

import placeholders.Action;
import placeholders.ControllerInt;
import placeholders.ExportedShip;
import placeholders.FakeController;
import placeholders.FakeServerProvider;
import placeholders.FlatGroundProvider;
import placeholders.GroundProvider;
import placeholders.ServerShipProvider;

public class Ship {

	private static final float SCALE = 3;
	private static final float ACCELERATION = 20 * SCALE; // How fast does the ship accelerate
	private static final float BREAK_POWER = 10; // How fast does it break
	private static final float TURN_SPEED = 2.5f; // How fast does it turn
	private static final float AIR_RESISTANCE = 10; // How fast do ships slow down (this and acceleration determines the max speed)

	private static final float GRAVITY = 15 * SCALE; // The force of gravity affecting the ship
	private static final float AIR_CUSHION = 50 * SCALE; // The base force of the ait cushion keeping the hovercraft in the air
	private static final double CUSHION_SCALE = 0.8f;
	private static final float JUMP_POWER = 30 * SCALE; // Jumping, for science! (testing vertical stuff)
	// 15, 100, 2, 30: magnet-like

	private static final float DEFAULT_MASS = 1;
	private static final float DEFAULT_SIZE = 1;
	private Vector3 position;
	private Vector3 velocity;
	private Vector3 rotation;
	private float mass;
	private float size;
	private ControllerInt controller;
	private Collection<Ship> otherShips;
	private ServerShipProvider server;
	private ExportedShip fromServer;
	private GroundProvider ground;


	/** Creates a ship with position (0,0,0), no inputs an no other ships. For testing only */
	public Ship() {
		this(new Vector3(0, 0, 0), new FakeController());
	}
	public Ship(Vector3 startingPosition, ControllerInt controller) {
		this(startingPosition, new ArrayList<>(), controller, new FlatGroundProvider(0));
	}
	/** Creates a new server-controlled ship
	 * 
	 * @param startingPosition Vector describing this ship's starting position.
	 * @param otherShips Other ships to possibly collide with
	 * @param server Object providing data about the ship, as described in the interface */
	public Ship(Vector3 startingPosition, Collection<Ship> otherShips, ServerShipProvider server, GroundProvider ground) {
		this(startingPosition, otherShips, new FakeController(), server, ground);
	}
	/** Creates a player-controlled ship
	 * 
	 * @param startingPosition Vector describing this ship's starting position
	 * @param otherShips Other ships to possibly collide with
	 * @param controller Controlled providing player's desired actions, as described in the interface */
	public Ship(Vector3 startingPosition, Collection<Ship> otherShips, ControllerInt controller, GroundProvider ground) {
		this(startingPosition, otherShips, controller, new FakeServerProvider(), ground);
	}


	private Ship(Vector3 startingPosition, Collection<Ship> otherShips, ControllerInt controller, ServerShipProvider server,
		GroundProvider ground) {
		position = startingPosition.copy();
		this.velocity = new Vector3(0, 0, 0);
		this.rotation = new Vector3(0, 0, 0);
		this.mass = DEFAULT_MASS;
		this.size = DEFAULT_SIZE;
		this.controller = controller;
		this.otherShips = otherShips != null ? otherShips : new ArrayList<Ship>(); // If null set to an empty ArrayList
		this.server = server;
		this.ground = ground;
	}

	/** Accelerate in any direction within the 2d horizontal plane. The acceleration is instant; it's basically just changing velocities.
	 * Made public for testing purposes.
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
		velocity.forEach(v -> Math.signum(v) * Math.max(0, (Math.abs(v) - delta * Math.sqrt(Math.abs(v) * AIR_RESISTANCE))));
	}

	/** Apply the force of gravity */
	private void gravity(float delta) {
		velocity.changeY(y -> y - GRAVITY * delta);
	}

	/** Apply the forces of the air cushion (also bounce off ground if it ever happens) */
	private void airCushion(float delta) {
		float distance = ground.distanceToGround(position.as3f(), rotation.getDownDirection());
		if (distance <= 0 && velocity.getY() < 0) velocity.changeY(y -> -y);
		else if (distance > 0) velocity.changeY(y -> y + delta * AIR_CUSHION / Math.pow(distance, CUSHION_SCALE));
	}

	/** Changes the position by given velocity
	 * 
	 * @param delta Time in seconds that passed since the last call of this function */
	private void updatePosition(float delta) {
		position.add(velocity.copy().multiply(delta));
	}

	/** Handle controls from the player.
	 * Once we have server controlled ships, this method (or even whole update) could be abstract and differently implemented by PlayerShip
	 * and ServerShip.
	 * 
	 * @param controller2 */
	private void handleControls(float delta, ControllerInt conn) {
		if (conn == null) return; // Safeguard against first few frames before receiving any data
		Collection<Action> keys = conn.getPressedKeys();
		if (keys.contains(Action.FORWARD)) accelerate2d(delta * ACCELERATION, (float) Math.PI * 1.5f);
		if (keys.contains(Action.BREAK)) airResistance(delta * BREAK_POWER); // Breaking slows you down, no matter how you're moving
		// if (keys.contains(Action.BREAK)) accelerate2d(delta * ACCELERATION, Math.PI * 1.5); // Breaking accelerates backwards
		if (keys.contains(Action.STRAFE_RIGHT)) accelerate2d(delta * ACCELERATION / 2, 0);
		if (keys.contains(Action.STRAFE_LEFT)) accelerate2d(delta * ACCELERATION / 2, (float) Math.PI);
		if (keys.contains(Action.TURN_RIGHT)) rotation.changeY(y -> correctAngle(y + delta * TURN_SPEED));
		if (keys.contains(Action.TURN_LEFT)) rotation.changeY(y -> correctAngle(y - delta * TURN_SPEED));
		if (keys.contains(Action.JUMP)) velocity.changeY(y -> y + delta * JUMP_POWER);
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



	/** Updates all physics
	 * 
	 * @param delta Time in seconds that passed since the last call of this function */
	public void update(float delta) {
		if (server.getShip().isPresent()) { // If received data from server, just update and don't do physics
			this.fromServer = server.getShip().get();
			this.position = fromServer.getPosition();
			this.velocity = fromServer.getVelocity();
			return;
		}

		if (controller != null) handleControls(delta, controller); // Steer the ship with user controls
		else handleControls(delta, fromServer); // Steer the ship with controls from server (only when missed packets)

		airResistance(delta);
		doCollisions();
		gravity(delta);
		airCushion(delta);
		updatePosition(delta);
	}

	public void setX(float amount) {
		this.position.setX(amount);
	}

	/** @return Position of this ship's centre */
	public Vector3 getPosition() {
		return position.copy();
	}
	/** @return The ship's rotation in all three dimensions (x,y,z), in radians. Values (0,0,0) mean the ship is horizontal and facing
	 *         towards positive x. */
	public Vector3 getRotation() {
		return rotation.copy();
	}
	/** @return This ship's current velocities, separately in all dimensions */
	public Vector3 getVelocity() {
		return velocity.copy();
	}
	/** @return Size, or radius of this ship's hitbox */
	public float getSize() {
		return size;
	}
	/** @return This ship's mass */
	public float getMass() {
		return mass;
	}

	public float[] export() {
		return (new ExportedShip(position, velocity, controller.getPressedKeys())).toNumbers();
	}

}
