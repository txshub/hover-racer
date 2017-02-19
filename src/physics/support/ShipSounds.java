package physics.support;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.joml.Vector3f;

import audioEngine.AudioMaster;
import audioEngine.Sounds;
import audioEngine.Source;
import physics.core.Ship;

/** Class that handles all sounds coming from ships, currently limited to sounds of engines with sounds of collisions on the way.
 * Constructed and updated by PlayerShip.
 * 
 * @author Maciej Bogacki (TODOs left for Tudor to fill in) */
public class ShipSounds {

	private Ship center;
	private Source engineSource;
	private Map<Ship, Source> otherShips;

	/** Creates a new ShipSounds class
	 * 
	 * @param center The player's ship, where the sound is centred on
	 * @param otherShips Other ships that also make sounds */
	public ShipSounds(Ship center, Collection<Ship> otherShips) {
		this.center = center;

		// Create player ship's Source
		engineSource = AudioMaster.createSFXSource();
		engineSource.setLooping(true);
		engineSource.play(Sounds.ENGINE);
		// TODO anything else you want do when creating player's source

		// Create other ship's Sources
		this.otherShips = otherShips.stream().collect(Collectors.toMap(Function.identity(), ship -> AudioMaster.createSFXSource()));
		for (Entry<Ship, Source> entry : this.otherShips.entrySet()) {
			entry.getValue().setLooping(true);
			entry.getValue().play(Sounds.ENGINE);
			// TODO anything else you want to do when creating other ship's Sources. Use entry.getValue() for Source and entry.getKey() for
			// corresponding Ship)
		}
	}

	public void update(float delta) {
		for (Entry<Ship, Source> entry : this.otherShips.entrySet()) {
			Ship ship = entry.getKey();
			Source source = entry.getValue();
			Vector3f position = ship.getPosition().sub(center.getPosition()); // TODO change to camera's position instead
			Vector3f velocity = ship.getVelocity().sub(center.getVelocity());

			source.setPosition(position.x, position.y, position.z); // Set relative position
			source.setVelocity(velocity.x, velocity.y, velocity.z); // Set relative velocity
			source.setVolume((float) Math.max(1, Math.log10(10 / position.lengthSquared()))); // Set volume based on distance
			source.setPitch(Math.max(2, ship.getVelocity().length() / ship.getMaxSpeed()) + 1f); // Set pitch based on speed/maxSpeed

			// TODO whatever else you want to do each frame
		}
	}

	public void collision(Ship first, Ship second) {
		Vector3f position = first.getPosition().sub(center.getPosition()).div(2); // Position relative to player
		float force = first.getVelocity().sub(second.getVelocity()).length(); // Force of the collision (relative speeds)
		// TODO Make a collision sound happen
	}

	public void cleanUp() {
		engineSource.delete();
		otherShips.values().forEach(s -> s.delete());
		// TODO anything else you need to do when cleaning up
	}



}
