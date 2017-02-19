package physics.core;

import java.util.ArrayList;
import java.util.Collection;

import org.joml.Vector3f;

import audioEngine.AudioMaster;
import audioEngine.Sounds;
import audioEngine.Source;
import gameEngine.entities.Entity;
import gameEngine.models.TexturedModel;
import physics.network.ExportedShip;
import physics.network.ServerShipProvider;
import physics.placeholders.FakeServerProvider;
import physics.placeholders.FlatGroundProvider;
import physics.support.Action;
import physics.support.GroundProvider;
import physics.support.InputController;

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
public abstract class Ship extends Entity {

	private static final float SCALE = 3;
	private static final float VERTICAL_SCALE = 10;
	private static final float ACCELERATION = 50 * SCALE; // How fast does the ship accelerate
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
	private static final float LEVELLING_SPEED = 0.2f;
	private static final float SPEED_OF_ROTATION_WHILE_TURNING = 1.25f;

	private static final float MAX_SPEED = ACCELERATION * ACCELERATION / AIR_RESISTANCE; // Used for the sound engine

	private static boolean ACTUALLY_BREAK = true; // Whether the ship actually breaks when braking (and not accelerates backwards)
	private Vector3 position;
	private Vector3 rotation;
	private Vector3 velocity;
	private Vector3 rotationalVelocity;
	private float mass;
	private float size;
	private InputController controller;
	private Collection<Ship> otherShips;
	private ServerShipProvider server;
	private ExportedShip fromServer;
	private GroundProvider ground;

	long lastPrint = 0;
	double deltaSum = 0;


	// Tudor
	private Source engineSource;

	/** Creates a ship with position (0,0,0), no inputs an no other ships. For testing only */
	// public Ship() {
	// this(new Vector3f(0, 0, 0), new FakeController());
	//
	// // Tudor
	// engineSource = AudioMaster.createSFXSource();
	// engineSource.setLooping(true);
	// engineSource.play(Sounds.ENGINE);
	// }
	// public Ship(Vector3f startingPosition, ControllerInt controller) {
	// this(null, startingPosition, new ArrayList<>(), controller, new FlatGroundProvider(0));
	//
	// // Tudor
	// engineSource = AudioMaster.createSFXSource();
	// engineSource.setLooping(true);
	// engineSource.play(Sounds.ENGINE);
	// }

	/** Creates a new server-controlled ship
	 * 
	 * @param startingPosition Vector describing this ship's starting position.
	 * @param otherShips Other ships to possibly collide with
	 * @param server Object providing data about the ship, as described in the interface */
	// public Ship(TexturedModel model, Vector3f startingPosition, Collection<Ship> otherShips, ServerShipProvider server,
	// GroundProvider ground) {
	// this(model, startingPosition, otherShips, new FakeController(), server, ground);
	//
	// // Tudor
	// engineSource = AudioMaster.createSFXSource();
	// engineSource.setLooping(true);
	// engineSource.play(Sounds.ENGINE);
	// }

	public Ship(TexturedModel model, Vector3f position, InputController controller) {
		this(model, position, new ArrayList<Ship>(), controller, new FlatGroundProvider(0));
	}

	/** Creates a player-controlled ship
	 * 
	 * @param startingPosition Vector describing this ship's starting position
	 * @param otherShips Other ships to possibly collide with
	 * @param controller Controlled providing player's desired actions, as described in the interface */
	public Ship(TexturedModel model, Vector3f startingPosition, Collection<Ship> otherShips, InputController controller,
		GroundProvider ground) {
		this(model, startingPosition, otherShips, controller, new FakeServerProvider(), ground);
	}

	// TODO remove this constructor after Ship subclasses are created
	private Ship(TexturedModel model, Vector3f startingPosition, Collection<Ship> otherShips, InputController controller,
		ServerShipProvider server, GroundProvider ground) {
		super(model, startingPosition, new Vector3(0, 0, 0), 1);
		this.position = new Vector3(startingPosition);
		super.position = this.position;
		this.velocity = new Vector3(0, 0, 0);
		this.rotation = new Vector3(0, 0, 0);
		this.rotationalVelocity = new Vector3(0, 0, 0);
		this.mass = DEFAULT_MASS;
		this.size = DEFAULT_SIZE;
		this.controller = controller;
		this.otherShips = otherShips != null ? otherShips : new ArrayList<Ship>(); // If null set to an empty ArrayList
		this.server = server;
		this.ground = ground;

		// Tudor
		engineSource = AudioMaster.createSFXSource();
		engineSource.setLooping(true);
		engineSource.play(Sounds.ENGINE);
	}

	protected Ship(TexturedModel model, Vector3f startingPosition, Collection<Ship> otherShips, GroundProvider ground) {
		super(model, startingPosition, new Vector3(0, 0, 0), 1);
		this.position = new Vector3(startingPosition);
		super.position = this.position;
		this.velocity = new Vector3(0, 0, 0);
		this.rotation = new Vector3(0, 0, 0);
		this.rotationalVelocity = new Vector3(0, 0, 0);
		this.mass = DEFAULT_MASS;
		this.size = DEFAULT_SIZE;
		this.otherShips = otherShips != null ? otherShips : new ArrayList<Ship>(); // If null set to an empty ArrayList
		this.ground = ground;
	}

	/** Accelerate in any direction within the 2d horizontal plane. The acceleration is instant; it's basically just changing velocities.
	 * Made public for testing purposes.
	 * 
	 * @param force Force to accelerate with. Force 1 means changing velocity by 1 if in angle is along one axis
	 * @param angle Direction of the acceleration in standard notation: 0 is right, 0.5pi it forward etc. */
	public void accelerate2d(float force, float angle) {
		final float angle2 = correctAngle(rotation.y + angle); // Adjust the angle for ship's own rotation
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
		float distance = ground.distanceToGround(position, rotation) / VERTICAL_SCALE;
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
		// Add momentum (doing it first to be more responsive)
		rotation.forEach(rotationalVelocity, (rot, vel) -> correctAngle(rot + delta * vel));
		// Try levelling z with the ground
		rotationalVelocity.changeZ(z -> (z - relativeAngle(rotation.z) * LEVELLING_SPEED));

		// Air resistance for rotation
		rotationalVelocity
			.forEach(v -> Math.signum(v) * Math.max(0, (Math.abs(v) - delta * Math.sqrt(Math.abs(v) * ROTATIONAL_RESISTANCE))));

	}

	private float relativeAngle(double angle) {
		if (angle <= Math.PI) return (float) angle;
		else return (float) (-2 * Math.PI + angle);
	}

	/** Handle controls from the player.
	 * Once we have server controlled ships, this method (or even whole update) could be abstract and differently implemented by PlayerShip
	 * and ServerShip.
	 * 
	 * @param controller2 */
	private void handleControls(float delta) {
		/* if (keys.contains(Action.TURN_RIGHT)) rotation.changeY(y -> correctAngle(y - delta * TURN_SPEED));
		 * if (keys.contains(Action.TURN_LEFT)) rotation.changeY(y -> correctAngle(y + delta * TURN_SPEED)); */
		if (controller.checkAction(Action.TURN_RIGHT)) {
			rotationalVelocity.changeY(y -> y - delta * TURN_SPEED);
			rotationalVelocity.changeZ(z -> z + delta * TURN_SPEED * SPEED_OF_ROTATION_WHILE_TURNING);
		}
		if (controller.checkAction(Action.TURN_LEFT)) {
			rotationalVelocity.changeY(y -> y + delta * TURN_SPEED);
			rotationalVelocity.changeZ(z -> z - delta * TURN_SPEED * SPEED_OF_ROTATION_WHILE_TURNING);
		}
		if (controller.checkAction(Action.FORWARD)) accelerate2d(delta * ACCELERATION, (float) Math.PI * 1.5f);
		if (controller.checkAction(Action.BREAK)) airResistance(delta * BREAK_POWER); // Breaking slows you down, no matter how you're
																						// moving
		// if (keys.contains(Action.BREAK)) accelerate2d(delta * ACCELERATION, Math.PI * 1.5); // Breaking accelerates backwards
		if (controller.checkAction(Action.STRAFE_RIGHT)) accelerate2d(delta * ACCELERATION, (float) Math.PI);
		if (controller.checkAction(Action.STRAFE_LEFT)) accelerate2d(delta * ACCELERATION, 0);
		if (controller.checkAction(Action.JUMP)) velocity.changeY(y -> y + delta * JUMP_POWER * VERTICAL_SCALE);
	}

	protected void steer(float thrust, float turn, float brokenDelta) {
		steer(thrust, turn, 0f, 0f, brokenDelta);
	}
	protected void steer(float thrust, float turn, float strafe, float brokenDelta) {
		steer(thrust, turn, strafe, 0f, brokenDelta);
	}
	protected void steer(float thrust, float turn, float strafe, float jump, float brokenDelta) {
		float delta = (float) 1 / 60; // TODO fix deltas
		// Checking parameters - TODO probably remove this for production code
		if (Math.abs(thrust) > 1 || Math.abs(turn) > 1 || Math.abs(strafe) > 1 || Math.abs(jump) > 1) {
			throw new IllegalArgumentException(
				"Ship's steer method called with bad parameters. Only values between -1 and 1 are accepted.");
		}
		// Turning
		if (turn != 0) {
			rotationalVelocity.changeY(y -> y - delta * turn * TURN_SPEED);
			rotationalVelocity.changeZ(z -> z + delta * turn * TURN_SPEED * SPEED_OF_ROTATION_WHILE_TURNING);
		}
		// Accelerating/breaking
		if (!ACTUALLY_BREAK && thrust != 0) accelerate2d(delta * thrust * ACCELERATION, (float) Math.PI * 1.5f);
		else if (ACTUALLY_BREAK && thrust > 0) accelerate2d(delta * thrust * ACCELERATION, (float) Math.PI * 1.5f);
		else if (ACTUALLY_BREAK && thrust < 0) airResistance(-delta * thrust * BREAK_POWER);
		// Strafing
		if (strafe != 0) accelerate2d(delta * strafe * ACCELERATION, (float) Math.PI); // TODO change strafing to an instant boost
		// Jumping
		if (jump != 0) velocity.changeY(y -> y + delta * jump * JUMP_POWER * VERTICAL_SCALE);
	}

	private void doCollisions() {
		otherShips.stream().filter(ship -> ship.getInternalPosition().distanceTo(this.position) <= ship.getSize() + this.size)
			.forEach(s -> collideWith(s));
	}

	protected Vector3 getInternalPosition() {
		return position.copy();
	}

	/** Changes the velocity to account for a collision with a different ship */
	private void collideWith(Ship ship) {
		velocity.add(ship.getVelocity().sub(this.velocity).mul(ship.getMass()).mul(1 / this.getMass()).mul(0.5f));
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
	public boolean updatePhysics(float preDelta) {
		float delta = (float) 1 / 60; // TODO fix deltas
		/* if (server.getShip().isPresent()) { // If received data from server, just update and don't do physics
		 * this.fromServer = server.getShip().get();
		 * this.position = fromServer.getPosition();
		 * this.velocity = fromServer.getVelocity();
		 * return true;
		 * }
		 * 
		 * if (controller != null) handleControls(delta); // Steer the ship with user controls
		 * else handleControls(delta); // Steer the ship with controls from server (only when missed packets) */
		// Do physics
		airResistance(delta);
		doCollisions();
		gravity(delta);
		airCushion(delta);
		updateRotation(delta);
		updatePosition(delta);

		// Update parent
		super.setRotation(rotation.copy().forEach(r -> Math.toDegrees(r)));

		// Update sound TODO encapsulate sound
		/* float pitch = (velocity.length() / MAX_SPEED) + 1f;
		 * if (pitch > 2f) {
		 * pitch = 2f;
		 * }
		 * engineSource.setPitch(pitch); */
		return true;
	}

	public void updateFromPacket(ExportedShip remote) {
		this.position = remote.getPosition();
		this.velocity = remote.getVelocity();
		this.rotation = remote.getRotation();
		this.rotationalVelocity = remote.getRotationalVelocity();
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

	public void cleanUp() {
		engineSource.delete();
	}

	/** Updates this ship. This includes receiving packets from server for RemoteShip, taking input from the player for PlayerShip and
	 * making decisions what to do for AIship. Excluding RemoteShip, this method also updates physics.
	 * 
	 * @param delta Time since last call of this function (TODO specify units) */
	public abstract void update(float delta);

}
