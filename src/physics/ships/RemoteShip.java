package physics.ships;

import java.util.Optional;

import org.joml.Vector3f;

import gameEngine.models.TexturedModel;
import physics.core.Ship;
import physics.network.ServerShipProvider;
import physics.support.GroundProvider;

public class RemoteShip extends Ship {


	private ServerShipProvider remote;

	public RemoteShip(byte id, TexturedModel model, Vector3f startingPosition, GroundProvider ground, ServerShipProvider remote) {
		super(id, model, startingPosition, ground);
		this.remote = remote;
	}

	@Override
	public void update(float delta) {
		Optional<byte[]> newShip = remote.getShip(super.getId()); // Grab a packet if available
		if (newShip.isPresent()) { // If received update Ships's parameters
			super.updateFromPacket(newShip.get());
		} else { // Otherwise interpolate
			super.updatePhysics(delta);
		}

	}

}
