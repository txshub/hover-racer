package physics.ships;

import java.util.List;

import org.joml.Vector3f;

import gameEngine.models.TexturedModel;
import physics.core.Ship;
import physics.support.GroundProvider;
import trackDesign.TrackPoint;
import upgrades.ShipTemplate;

/** Ship that does nothing (except following physics and colliding with things).
 * Used for testing.
 * 
 * @author Maciej Bogacki */
public class DummyShip extends Ship {

	public DummyShip(byte id, TexturedModel model, Vector3f startingPosition, GroundProvider ground, ShipTemplate stats,
		List<TrackPoint> track) {
		super(id, model, startingPosition, ground, stats, track);
	}

	@Override
	public void update(float delta) {
		// It does nothing
		super.updatePhysics(delta);
	}

}
