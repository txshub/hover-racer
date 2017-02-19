package physics.ships;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.joml.Vector3f;

import gameEngine.models.TexturedModel;
import physics.core.Ship;
import physics.network.ServerShipProvider;
import physics.support.GroundProvider;
import physics.support.InputController;

public class MultiplayerShipManager implements ServerShipProvider {

	private Ship player;
	private Collection<Ship> remotes;
	private Map<Byte, byte[]> packets;


	public MultiplayerShipManager(byte playerId, InputController input, TexturedModel playerTexture,
		Collection<TexturedModel> otherTextures, ArrayList<Vector3f> startingPositions, GroundProvider ground) {
		this.packets = new HashMap<Byte, byte[]>();

		// Create all RemoteShips
		this.remotes = new ArrayList<Ship>();
		byte id = 0;
		for (TexturedModel texturedModel : otherTextures) {
			if (id == player.getId()) id++; // Don't give them player's id
			remotes.add(new RemoteShip(id, texturedModel, startingPositions.get(id), ground, this));
		}
		// Tell ships about each other
		remotes.forEach(r -> r.addOtherShips(remotes));
		remotes.forEach(r -> r.addOtherShip(player));
		// Finally create the player
		this.player = new PlayerShip(playerId, playerTexture, startingPositions.get(playerId), remotes, ground, input);
	}

	public void updateShips(float delta) {
		player.update(delta);
		remotes.forEach(r -> r.update(delta));
	}

	public void addPacket(byte[] packet) {
		packets.remove(packet[0]);
		packets.put(packet[0], packet);
	}
	public byte[] getShipPacket() {
		return player.export();
	}

	@Override
	public Optional<byte[]> getShip(byte id) {
		return Optional.ofNullable(packets.remove(id));
	}



}
