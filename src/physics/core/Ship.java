package physics.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.joml.Vector3f;

import gameEngine.entities.Entity;
import gameEngine.models.TexturedModel;
import physics.network.ExportedShip;
import physics.support.CollisionListener;
import physics.support.GroundProvider;
import trackDesign.TrackPoint;
import upgrades.ShipTemplate;

/** A single ship entity. Can be controlled by an AI or the player. Handles all
 * the physics, namely (currently): -Keeping track of velocity and position
 * -Applying air resistance -Applying accelerations based on user's input
 * -Keeping track of rotation -Handles gravity and forces of the air cushion, on
 * flat terrain only (so far) -Detecting and reacting to collision with other
 * ships. Can also export itself to an array of floats and update
 * itself with such an array received from server (used for ships controlled by
 * other players/server's AIs)
 * 
 * @author Maciej Bogacki */
public abstract class Ship extends Entity {

	private static final float VERTICAL_SCALE = 10;
	// How fast does rotating slow down
	private static final float ROTATIONAL_RESISTANCE = 5;
	// How much speed is retained during a wall collision
	private static final float WALL_ELASTICITY = 0.35f;
	// How much energy is retained during ship collisions
	private static final float SHIP_COLLISION_ELASTICITY = 0.8f;

	// 15, 100, 2, 30: magnet-like
	// 15, 50, 0.8, 30: nicely cushiony

	private static final float LEVELLING_SPEED = 0.2f;
	private static final float SPEED_OF_ROTATION_WHILE_TURNING = 1.25f;
	private static final float SPEED_OF_ROTATION_WHILE_STRAFING = 0.75f;
	// Whether the ship actually breaks when braking (and not accelerates
	// backwards)
	private static boolean ACTUALLY_BREAK = true;

	transient private Vector3 position;
	private Vector3 rotation;
	private Vector3 velocity;
	private Vector3 rotationalVelocity;
	private float mass = 1;
	private float size = 5;
	private Collection<Ship> otherShips;
	private GroundProvider ground;
	private Barriers barriers;
	private StatManager stats;
	private CollisionListener collisionListener;

	private byte id;
	private boolean started;
	private boolean finished;

	long lastPrint = 0;
	double deltaSum = 0;

	protected Ship(byte id, TexturedModel model, Vector3f startingPosition, GroundProvider ground, ShipTemplate stats,
		List<TrackPoint> track) {
		super(model, startingPosition, new Vector3(0, 0, 0), 1);
		if (stats == null) stats = ShipTemplate.getDefault();
		this.id = id;
		this.position = new Vector3(startingPosition);
		super.position = this.position;
		this.velocity = new Vector3(0, 0, 0);
		this.rotation = new Vector3(0, 0, 0);
		this.rotationalVelocity = new Vector3(0, 0, 0);
		this.stats = new StatManager(stats);
		this.ground = ground;
		// this.barriers = new Barriers(track);
		this.otherShips = new ArrayList<Ship>();

		this.started = false;
		this.finished = false;
	}

	/** Adds ships to the list of ships this one can collide with */
	public void addOtherShips(Collection<Ship> ships) {
		if (ships == null) return;
		ships.forEach(this::addOtherShip);
	}

	/** Adds another ship to the list of ships this one can collide with */
	public void addOtherShip(Ship ship) {
		if (ship.getId() != id) otherShips.add(ship);
	}

	public void addBarrier(ArrayList<Vector3f> barrierPoints) {
		this.barriers = new Barriers(barrierPoints);
	}

	/** Sets the listener for this ship colliding with other ships. Used for the sound engine to make collision sounds.
	 * 
	 * @param collisionListener Listener to be called whenever a collision with another ship happens */
	public void setCollisionListener(CollisionListener collisionListener) {
		this.collisionListener = collisionListener;
	}

	/** Accelerate in any direction within the 2d horizontal plane. The
	 * acceleration is instant; it's basically just changing velocities. Made
	 * public for testing purposes.
	 * 
	 * @param force
	 *        Force to accelerate with. Force 1 means changing velocity by 1 if
	 *        in angle is along one axis
	 * @param angle
	 *        Direction of the acceleration in standard notation: 0 is right,
	 *        0.5pi it forward etc. */
	public void accelerate2d(float force, float angle) {
		final float relativeAngle = correctAngle(rotation.y + angle);

		velocity.changeX(x -> x + force * Math.cos(relativeAngle));
		velocity.changeZ(z -> z - force * Math.sin(relativeAngle));
	}

	/** Applies air resistance, i.e. slows down velocities
	 * 
	 * @param delta
	 *        Time in seconds that passed since the last call of this function */
	private void airResistance(float delta) {
		velocity.forEach(v -> Math.signum(v) * Math.max(0, (Math.abs(v) - delta * Math.sqrt(Math.abs(v) * stats.AIR_RESISTANCE))));
	}

	/** Handle colliding with other ships */
	private void shipCollisions() {
		otherShips.stream().filter(ship -> ship.getInternalPosition().distanceTo(this.position) <= ship.getSize() + this.size)
			.forEach(s -> collideWith(s));
	}

	/** @return The Ship's position as Vector3 instead of Vector3f. Used for internal collision logic */
	protected Vector3 getInternalPosition() {
		return position.copy();
	}

	/** Changes the velocity to account for a collision with a different ship */
	private void collideWith(Ship ship) {
		if (collisionListener != null) collisionListener.addCollision(this, ship); // TODO fix sounds
		Vector3 pos = ship.getInternalPosition().copy();
		float expectedDistance = ship.getSize() + this.getSize();
		// Apply momentum
		velocity.add(ship.getVelocity().sub(this.velocity).mul(ship.getMass()).mul(1 / this.getMass()).mul(SHIP_COLLISION_ELASTICITY));
		// Get out of collision zone
		position.add(this.position.copy().sub(pos).mul((expectedDistance - position.distance(pos)) / expectedDistance));
	}

	/** Apply the force of gravity */
	private void gravity(float delta) {
		velocity.changeY(y -> y - stats.GRAVITY * delta * VERTICAL_SCALE);
	}

	/** Apply the forces of the air cushion (also bounce off ground if it ever
	 * happens) */
	private void airCushion(float delta) {
		float modifier = finished ? 3f : 1f;
		float distance = ground.distanceToGround(position, rotation) / VERTICAL_SCALE;
		if (distance <= 0 && velocity.getY() < 0) {
			position.changeY(y -> y - distance + 1); // Get above ground
			velocity.changeY(y -> -.3 * y); // Change velocity to upward
		} else if (distance > 0)
			velocity.changeY(
				y -> y + delta * modifier * stats.AIR_CUSHION * VERTICAL_SCALE / Math.pow(Math.max(distance, 1), stats.CUSHION_SCALE));
	}

	/** Handles collisions with the track edges */
	private void trackCollision() {
		barriers.allCollisions(this).forEach(v -> velocity.bounceOff(v, WALL_ELASTICITY));
	}



	/** Changes the position by given velocity
	 * 
	 * @param delta Time in seconds that passed since the last call of this function */
	private void updatePosition(float delta) {
		position.add(velocity.copy().multiply(delta));
	}

	/** Updates the ships's rotation -
	 * 
	 * @param delta */
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

	/** Steers the ships, telling it exactly what to do. All parameters (except for delta) should be between -1 and 1.
	 * 
	 * @param thrust How fast should the ship accelerate. Negative values do breaking insted.
	 * @param turn How fast (and in which direction) should the ship turn. Positive values turn right, negative turn left.
	 * @param strafe How fast should the ship strafe; positive values strafe right.
	 * @param brokenDelta Time in seconds since the last call of this method. TODO brokenDelta */
	protected void steer(float thrust, float turn, float strafe, float brokenDelta) {
		steer(Math.max(0, thrust), -thrust, turn, strafe, brokenDelta);
	}

	protected void steer(float thrust, float breaking, float turn, float strafe, float brokenDelta) {
		if (!started) return; // Don't allow input until the race has started
		float delta = (float) 1 / 60; // TODO fix deltas
		// Checking parameters
		if (Math.abs(thrust) > 1 || Math.abs(turn) > 1 || Math.abs(strafe) > 1 || Math.abs(breaking) > 1) {
			throw new IllegalArgumentException(
				"Ship's steer method called with bad parameters. Only values between -1 and 1 are accepted.");
		}
		// Turning
		if (turn != 0) {
			rotationalVelocity.changeY(y -> y - delta * turn * stats.TURN_SPEED);
			rotationalVelocity.changeZ(z -> z + delta * turn * stats.TURN_SPEED * SPEED_OF_ROTATION_WHILE_TURNING);
		}
		// Accelerating
		accelerate2d(delta * thrust * stats.ACCELERATION, (float) Math.PI * 1.5f);
		// Breaking
		if (breaking > 0) airResistance(delta * breaking * stats.BREAK_POWER);
		// Strafing TODO change to an instant boost (?)
		if (strafe != 0) {
			accelerate2d(delta * strafe * stats.ACCELERATION, (float) Math.PI);
			rotationalVelocity.changeZ(z -> z + delta * strafe * stats.TURN_SPEED * SPEED_OF_ROTATION_WHILE_STRAFING);
		}
	}


	/** Corrects an angle to fit between 0 and 2pi (e.g. 7.13pi->1.13pi,
	 * -0.13pi->1.87pi) */
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
	 * @param delta
	 *        Time in seconds that passed since the last call of this function */
	public void updatePhysics(float preDelta) {
		float delta = (float) 1 / 60; // TODO fix deltas

		// Do physics
		airResistance(delta);
		shipCollisions();
		if (!finished) trackCollision();
		gravity(delta);
		airCushion(delta);
		updateRotation(delta);
		updatePosition(delta);

		// Update parent
		super.setRotation(this.rotation.copy().forEach(r -> Math.toDegrees(r)));
	}

	/** Allows the player to control the ship from now on. Call when the race
	 * starts. */
	public void start() {
		this.started = true;
	}

	/** Finished the race for this ship, causing it to move up and not interfere with ships that are still racing */
	public void finish() {
		this.finished = true;
	}

	public void resetTo(Vector3f checkpoint, Vector3f facing) {
		this.velocity.set(0, 0, 0);
		this.rotationalVelocity.set(0, 0, 0);
		float angle = (float) Math.atan2(facing.z - checkpoint.z, facing.x - checkpoint.x);
		this.rotation.set(0, angle, 0);
		this.position.set(checkpoint);
	}

	@Override
	public void setRotation(Vector3f rot) {
		this.rotation.set(rot);
		this.rotation.forEach(Math::toRadians).forEach(this::correctAngle);
		super.setRotation(rotation.copy().forEach(r -> Math.toDegrees(r)));
	}

	public void updateFromPacket(byte[] bytes) {
		ExportedShip remote = new ExportedShip(bytes);
		this.position.set(remote.getPosition());
		this.velocity.set(remote.getVelocity());
		this.rotation.set(remote.getRotation());
		super.setRotation(rotation.copy().forEach(Math::toDegrees));
		this.rotationalVelocity.set(remote.getRotationalVelocity());
		// if (this.getId() == 1) System.out.println("got: " + rotation.y);
	}

	public byte[] export() {
		if (this.getId() == 1) {
			// System.out.println("Sending data: " + rotation.y);
		}
		return new ExportedShip(id, position, velocity, rotation, rotationalVelocity).toNumbers();

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

	/** @return The Ship's unique id */
	public byte getId() {
		return id;
	}

	/** @return Approximated max speed. This is the point where air resistance and
	 *         acceleration would average out. Actual speed might be more due to
	 *         other factors. Currently only used by the sound engine. */
	public float getMaxSpeed() {
		return stats.MAX_SPEED;
	}

	/** Updates this ship. This includes receiving packets from server for
	 * RemoteShip, taking input from the player for PlayerShip and making
	 * decisions what to do for AIship. Excluding RemoteShip, this method also
	 * updates physics.
	 * 
	 * @param delta Time since last call of this function in seconds */
	public abstract void update(float delta);

	@Override
	public String toString() {
		return "Ship No " + id + " at " + position + " facing " + rotation + "\nLinear velocity is " + velocity + " and angular velocity is"
			+ rotationalVelocity;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		try {
			Ship casted = (Ship) obj;
			return casted.getId() == this.id;
		} catch (ClassCastException e) {
			return false;
		}
	}

}
