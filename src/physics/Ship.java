package physics;

import java.util.ArrayList;
import java.util.Collection;

import org.joml.Vector3f;

import gameEngine.entities.Entity;
import gameEngine.models.TexturedModel;
import gameEngine.toolbox.VecCon;
import placeholders.Action;
import placeholders.ControllerInt;
import placeholders.ExportedShip;
import placeholders.FakeController;
import placeholders.FakeServerProvider;
import placeholders.FlatGroundProvider;
import placeholders.GroundProvider;
import placeholders.ServerShipProvider;

/** A single ship entity. Can be controlled by an AI or the player. Handles all the physics, namely (currently):
 * -Keeping track of velocity and position
 * -Applying air resistance
 * -Applying accelerations based on user's input
 * -Keeping track of rotation
 * -Handles gravity and forces of the air cushion, on flat terrain only (so far)
 * -Detecting and reacting to collision with other ships (untested!)
 * Can also export itself to an array of floats and update itself with such an array received from server (used for ships controlled by
 * other players/server's AIs)
 * TODO:
 * - Integrate with track system to detect and react to track collisions
 * - Test and polish collisions with other ships
 * - Upgrade air cushion to work with curved terrain
 * - Smarter reaction if the ship ends up underground for some reason (super rare but still should be addressed)
 * - Panning physics (matching ground below/reacting to accelerations)
 * 
 * @author Maciej Bogacki */
public class Ship extends Entity {

	private static final float SCALE = 3;
	private static final float VERTICAL_SCALE = 10;
	private static final float ACCELERATION = 20 * SCALE; // How fast does the ship accelerate
	private static final float BREAK_POWER = 10; // How fast does it break
	private static final float TURN_SPEED = 4f; // How fast does it turn
	private static final float AIR_RESISTANCE = 10; // How fast do ships slow down (this and acceleration determines the max speed)
	private static final float ROTATIONAL_RESISTANCE = 5; // How fast does rotating slow down

	private static final float GRAVITY = 15 * SCALE; // The force of gravity affecting the ship
	private static final float AIR_CUSHION = 50 * SCALE; // The base force of the ait cushion keeping the hovercraft in the air
	private static final double CUSHION_SCALE = 0.8f;
	private static final float JUMP_POWER = 30 * SCALE; // Jumping, for science! (testing vertical stuff)
	// 15, 100, 2, 30: magnet-like
	// 15, 50, 0.8, 30: nicely cushiony

	private static final float DEFAULT_MASS = 1;
	private static final float DEFAULT_SIZE = 1;
	private Vector3f position;
	private Vector3f velocity;
	private Vector3f rotation;
	private Vector3f rotationalMomentum;
	private float mass;
	private float size;
	private ControllerInt controller;
	private Collection<Ship> otherShips;
	private ServerShipProvider server;
	private ExportedShip fromServer;
	private GroundProvider ground;


	/** Creates a ship with position (0,0,0), no inputs an no other ships. For testing only */
	public Ship() {
		this(new Vector3f(0, 0, 0), new FakeController());
	}
	public Ship(Vector3f startingPosition, ControllerInt controller) {
		this(null, startingPosition, new ArrayList<>(), controller, new FlatGroundProvider(0));
	}
	/** Creates a new server-controlled ship
	 * 
	 * @param startingPosition Vector describing this ship's starting position.
	 * @param otherShips Other ships to possibly collide with
	 * @param server Object providing data about the ship, as described in the interface */
	public Ship(TexturedModel model, Vector3f startingPosition, Collection<Ship> otherShips, ServerShipProvider server,
		GroundProvider ground) {
		this(model, startingPosition, otherShips, new FakeController(), server, ground);
	}
	/** Creates a player-controlled ship
	 * 
	 * @param startingPosition Vector describing this ship's starting position
	 * @param otherShips Other ships to possibly collide with
	 * @param controller Controlled providing player's desired actions, as described in the interface */
	public Ship(TexturedModel model, Vector3f startingPosition, Collection<Ship> otherShips, ControllerInt controller,
		GroundProvider ground) {
		this(model, startingPosition, otherShips, controller, new FakeServerProvider(), ground);
	}


	private Ship(TexturedModel model, Vector3f startingPosition, Collection<Ship> otherShips, ControllerInt controller,
		ServerShipProvider server, GroundProvider ground) {
		super(model, VecCon.toLWJGL(startingPosition), 0, 0, 0, 1);
		position = startingPosition;
		this.velocity = new Vector3f(0, 0, 0);
		this.rotation = new Vector3f(0, 0, 0);
		this.rotationalMomentum = new Vector3f(0, 0, 0);
		this.mass = DEFAULT_MASS;
		this.size = DEFAULT_SIZE;
		this.controller = controller;
		this.otherShips = otherShips != null ? otherShips : new ArrayList<Ship>(); // If null set to an empty ArrayList
		this.server = server;
		this.ground = ground;
		position.add(0,0,0);
		position.changeY(y -> y / 20f); // TODO this is a temporary fix
	}

	/** Accelerate in any direction within the 2d horizontal plane. The acceleration is instant; it's basically just changing velocities.
	 * Made public for testing purposes.
	 * 
	 * @param force Force to accelerate with. Force 1 means changing velocity by 1 if in angle is along one axis
	 * @param angle Direction of the acceleration in standard notation: 0 is right, 0.5pi it forward etc. */
	public void accelerate2d(float force, float angle) {
		final float angle2 = correctAngle(rotation.getY() + angle); // Adjust the angle for ship's own rotation
		velocity.changeX(x -> x + force * Math.cos(angle2));
		velocity.changeZ(z -> z - force * Math.sin(angle2));
	}

	/** Applies air resistance, i.e. slows down velocities
	 * 
	 * @param delta Time in seconds that passed since the last call of this function */
	private void airResistance(float delta) {
		velocity.forEach(v -> Math.signum(v) * Math.max(0, (Math.abs(v) - delta * Math.sqrt(Math.abs(v) * AIR_RESISTANCE))));
	}

	/** Apply the force of gravity */
	private void gravity(float delta) {
		velocity.changeY(y -> y - GRAVITY * delta * VERTICAL_SCALE);
	}

	/** Apply the forces of the air cushion (also bounce off ground if it ever happens) */
	private void airCushion(float delta) {
		float distance = ground.distanceToGround(position.as3f(), rotation.getDownDirection()) / VERTICAL_SCALE;
		if (distance <= 0 && velocity.getY() < 0) velocity.changeY(y -> -.3 * y); // If hit the ground do this
		else if (distance > 0) velocity.changeY(y -> y + delta * AIR_CUSHION * VERTICAL_SCALE / Math.pow(distance, CUSHION_SCALE));
	}

	/** Changes the position by given velocity
	 * 
	 * @param delta Time in seconds that passed since the last call of this function */
	private void updatePosition(float delta) {
		position.add(velocity.copy().multiply(delta));
	}

	private void updateRotation(float delta) {
		float before = rotation.getY();
		rotation.forEach(rotationalMomentum, (rot, vel) -> correctAngle(rot + delta * vel)); // Add momentum
		rotationalMomentum
			.forEach(v -> Math.signum(v) * Math.max(0, (Math.abs(v) - delta * Math.sqrt(Math.abs(v) * ROTATIONAL_RESISTANCE))));
		// System.out.println(rotation.getY() - before);
		// System.out.println(delta);
	}

	/** Handle controls from the player.
	 * Once we have server controlled ships, this method (or even whole update) could be abstract and differently implemented by PlayerShip
	 * and ServerShip.
	 * 
	 * @param controller2 */
	private void handleControls(float delta, ControllerInt conn) {
		if (conn == null) return; // Safeguard against first few frames before receiving any data
		Collection<Action> keys = conn.getPressedKeys();
		/* if (keys.contains(Action.TURN_RIGHT)) rotation.changeY(y -> correctAngle(y - delta * TURN_SPEED));
		 * if (keys.contains(Action.TURN_LEFT)) rotation.changeY(y -> correctAngle(y + delta * TURN_SPEED)); */
		if (keys.contains(Action.TURN_RIGHT)) rotationalMomentum.changeY(y -> y - delta * TURN_SPEED);
		if (keys.contains(Action.TURN_LEFT)) rotationalMomentum.changeY(y -> y + delta * TURN_SPEED);
		if (keys.contains(Action.FORWARD)) accelerate2d(delta * ACCELERATION, (float) Math.PI * 1.5f);
		if (keys.contains(Action.BREAK)) airResistance(delta * BREAK_POWER); // Breaking slows you down, no matter how you're moving
		// if (keys.contains(Action.BREAK)) accelerate2d(delta * ACCELERATION, Math.PI * 1.5); // Breaking accelerates backwards
		if (keys.contains(Action.STRAFE_RIGHT)) accelerate2d(delta * ACCELERATION / 2, (float) Math.PI);
		if (keys.contains(Action.STRAFE_LEFT)) accelerate2d(delta * ACCELERATION / 2, 0);
		if (keys.contains(Action.JUMP)) velocity.changeY(y -> y + delta * JUMP_POWER * VERTICAL_SCALE);
	}

	private void doCollisions() {
		otherShips.stream().filter(ship -> ship.getInternalPosition().distanceTo(this.position) <= ship.getSize() + this.size)
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
	public void update(float preDelta) {
		// if (preDelta != 1) System.out.println((preDelta - 1) / 60 + " sec");
		while (preDelta > 1.2) {
			update(1);
			preDelta--;
		}
		float delta = preDelta / 60f;
		if (server.getShip().isPresent()) { // If received data from server, just update and don't do physics
			this.fromServer = server.getShip().get();
			this.position = fromServer.getPosition();
			this.velocity = fromServer.getVelocity();
			return;
		}

		if (controller != null) handleControls(delta, controller); // Steer the ship with user controls
		else handleControls(delta, fromServer); // Steer the ship with controls from server (only when missed packets)
		// Do physics
		airResistance(delta);
		doCollisions();
		gravity(delta);
		airCushion(delta);
		updateRotation(delta);
		updatePosition(delta);

		// Update parent
		// super.setPosition(position.copy().changeY(y -> y * 10).as3f()); // TODO fix this
		super.setPosition(position.as3f());
		super.setRotx((float) Math.toDegrees(rotation.getX()));
		super.setRoty((float) Math.toDegrees(rotation.getY()));
		super.setRotz((float) Math.toDegrees(rotation.getZ()));

	}

	/** @return Position of this ship's centre */
	public Vector3f getInternalPosition() {
		return position.copy();
	}

	/** @return The ship's rotation in all three dimensions (x,y,z), in radians. Values (0,0,0) mean the ship is horizontal and facing
	 *         towards positive x. */
	public Vector3f getRotation() {
		return rotation.copy();
	}
	/** @return This ship's current velocities, separately in all dimensions */
	public Vector3f getVelocity() {
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
