package physics.ships;

import java.util.Collection;
import java.util.Optional;

import org.joml.Vector3f;

import gameEngine.models.TexturedModel;
import physics.core.Ship;
import physics.network.ExportedShip;
import physics.network.ServerShipProvider;
import physics.support.GroundProvider;

public class RemoteShip extends Ship {


	private ServerShipProvider remote;

	public RemoteShip(TexturedModel model, Vector3f startingPosition, Collection<Ship> otherShips, GroundProvider ground,
		ServerShipProvider remote) {
		super(model, startingPosition, otherShips, ground);
		this.remote = remote;
	}

	@Override
	public void update(float delta) {
		Optional<ExportedShip> newShip = remote.getShip(); // Grab a packet if available
		if (newShip.isPresent()) { // If received update Ships's parameters
			super.updateFromPacket(newShip.get());
		} else { // Otherwise
			super.updatePhysics(delta);
		}

	}

}
