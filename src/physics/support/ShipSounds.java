package physics.support;

import java.util.Collection;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import audioEngine.AudioMaster;
import audioEngine.Sounds;
import audioEngine.Source;
import gameEngine.entities.Camera;
import gameEngine.toolbox.Maths;
import physics.core.Ship;

/**
 * Class that handles all sounds coming from ships, currently limited to sounds
 * of engines with sounds of collisions on the way. Constructed and updated by
 * PlayerShip.
 * 
 * @author Maciej Bogacki (TODOs left for Tudor to fill in)
 */
public class ShipSounds {

	private Ship player;
	private Source playerSource;
	private Map<Ship, Source> otherShips;

	/**
	 * Creates a new ShipSounds class
	 * 
	 * @param player
	 *            The player's ship, where the sound is centred on
	 * @param otherShips
	 *            Other ships that also make sounds
	 */
	public ShipSounds(Ship player, Collection<Ship> otherShips) {
		this.player = player;

		// Create player ship's Source
		playerSource = AudioMaster.createSFXSource();
		playerSource.setLooping(true);
		playerSource.play(Sounds.ENGINE);
		// TODO anything else you want do when creating player's source

		// Create other ship's Sources
		this.otherShips = otherShips.stream()
				.collect(Collectors.toMap(Function.identity(), ship -> AudioMaster.createSFXSource()));
		for (Entry<Ship, Source> entry : this.otherShips.entrySet()) {
			entry.getValue().setLooping(true);
			entry.getValue().play(Sounds.ENGINE);
			// TODO anything else you want to do when creating other ship's
			// Sources.
			// Use entry.getValue() for Source and entry.getKey() for
			// corresponding Ship)
		}
		update(0f);
	}

	/**
	 * Updates all sources with positions, velocities, pitch, volume etc. Called
	 * on each update of physics.
	 * 
	 * @param delta
	 *            Time since last call of this method (may or may not be
	 *            helpful)
	 */
	public void update(float delta) {

		float pitch = 1f + player.getVelocity().length() / (player.getMaxSpeed() / 3f);
		if (pitch > 2f) pitch = 2f;
		playerSource.setPitch(pitch);
		
		// Updates all other ships
		for (Entry<Ship, Source> entry : this.otherShips.entrySet()) {
			Ship ship = entry.getKey();
			Source source = entry.getValue();
//			Vector3f position = new Vector3f(ship.getPosition()).sub(player.getPosition()); 
//			Vector3f velocity = new Vector3f(ship.getVelocity()).sub(player.getVelocity());
//			source.setPosition(position.x, position.y, position.z); 
//			source.setVelocity(velocity.x, velocity.y, velocity.z);

			Matrix4f tm = Maths.createTransformMatrix(new Vector3f(0f, 0f, 0f));
			Vector3f sourcePos = player.getPosition().mulPosition(tm);
			source.setPosition(sourcePos.x(), sourcePos.y(), sourcePos.z());
			
			float p = 1f + ship.getVelocity().length() / (ship.getMaxSpeed() / 3f);
			if (pitch > 2f) pitch = 2f;
			source.setPitch(pitch);
		}
	}

	/**
	 * Called whenever there if a collision between two ships - produces the
	 * sound of this collision (WIP)
	 * 
	 * @param first
	 *            First ship involved
	 * @param second
	 *            Second ship involved
	 */
	public void collision(Ship first, Ship second) {
		Vector3f position = new Vector3f(first.getPosition()).sub(player.getPosition()).div(2);
		float force = new Vector3f(first.getVelocity()).sub(second.getVelocity()).length();
	}

}
