package physics.ships;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.joml.Vector3f;

import gameEngine.entities.Entity;
import gameEngine.models.TexturedModel;
import gameEngine.objConverter.OBJFileLoader;
import gameEngine.renderEngine.Loader;
import gameEngine.textures.ModelTexture;
import input.InputController;
import physics.core.Ship;
import physics.network.RaceSetupData;
import physics.network.ServerShipProvider;
import physics.support.GroundProvider;
import physics.support.ShipSounds;
import trackDesign.TrackPoint;
import upgrades.ShipTemplate;

/** A class that manages ship on client side while in multiplayer mode. Handles
 * creating them, updating and communicating with the server.
 * 
 * @author Maciej Bogacki */
public class MultiplayerShipManager implements ServerShipProvider {

	private static final int SHIP_PACKET_LENGTH = 49;
	private PlayerShip player;
	private List<Ship> remotes;
	private Map<Byte, byte[]> packets;
	private ShipSounds sounds;
	private ArrayList<TrackPoint> track;

	/** Creates a new ship manager.
	 * 
	 * @param data Data received from the server during setup
	 * @param ground Ground for ships to levitate over
	 * @param input Input for the player
	 * @param loader Loader object for loading textures
	 * @param track Track to generate the barriers from */
	public MultiplayerShipManager(RaceSetupData data, GroundProvider ground, InputController input, Loader loader,
		ArrayList<TrackPoint> track) {
		this(data.getYourId(), input, makeModels(data.getModels(), data.getTextures(), loader), data.getStartingPositions(),
			data.getStartingOrientation(), data.getStats(), ground, track);
	}

	private MultiplayerShipManager(byte playerId, InputController input, List<TexturedModel> models, List<Vector3f> startingPositions,
		Vector3f startingOrientation, List<ShipTemplate> stats, GroundProvider ground, ArrayList<TrackPoint> track) {
		this.packets = new HashMap<Byte, byte[]>();
		this.track = track;

		// Create all RemoteShips
		this.remotes = new ArrayList<Ship>();
		for (byte id = 0; id < models.size(); id++) {
			if (id == playerId) continue; // Don't give them player's id
			RemoteShip remoteShip = new RemoteShip(id, models.get(id), startingPositions.get(id), ground, stats.get(id), track, this);
			remoteShip.setRotation(startingOrientation);
			remotes.add(remoteShip);
			System.out.println("Created: " + remoteShip);
		}
		// Tell ships about each other
		remotes.forEach(r -> r.addOtherShips(remotes));
		// Finally create the player
		this.player = new PlayerShip(playerId, models.get(playerId), startingPositions.get(playerId), remotes, ground, stats.get(playerId),
			track, input);
		player.setRotation(startingOrientation);
		remotes.forEach(r -> r.addOtherShip(player));
		// Create sounds
		sounds = new ShipSounds(player, remotes);
		player.setCollisionListener(sounds);
	}

	@Deprecated
	public MultiplayerShipManager(byte playerId, InputController input, TexturedModel playerTexture,
		Collection<TexturedModel> otherTextures, ArrayList<Vector3f> startingPositions, GroundProvider ground) {
		this.packets = new HashMap<Byte, byte[]>();

		// Create all RemoteShips
		this.remotes = new ArrayList<Ship>();
		byte id = 0;
		for (TexturedModel texturedModel : otherTextures) {
			if (id == playerId) id++; // Don't give them player's id
			remotes.add(new RemoteShip(id, texturedModel, startingPositions.get(id++), ground, null, null, this));
		}
		// Tell ships about each other
		remotes.forEach(r -> r.addOtherShips(remotes));
		// Finally create the player
		this.player = new PlayerShip(playerId, playerTexture, startingPositions.get(playerId), remotes, ground, null, null, input);
		remotes.forEach(r -> r.addOtherShip(player));
	}

	public void updateShips(float delta) {
		player.update(delta);
		remotes.forEach(r -> r.update(delta));
		sounds.update(delta);
	}

	public void addShipsTo(Collection<Entity> entities) {
		entities.addAll(remotes);
		entities.add(player);
	}

	public PlayerShip getPlayerShip() {
		return player;
	}

	/** Starts the race, allowing input through. This is when racing actually
	 * starts, so probably a few seconds after the game is loaded. */
	public void startRace() {
		player.start();
		remotes.forEach(r -> r.start());
	}

	/** Adds a client-side AI to a multiplayer game, only to be used for testing.
	 * The server won't know about it, but it will collide with things. Call
	 * before addShipsTo(entities) or it won't be rendered.
	 * 
	 * @param aiShip
	 *        The AIShip to be added */
	public void addAI(AIShip aiShip) {
		remotes.forEach(r -> r.addOtherShip(aiShip));
		player.addOtherShip(aiShip);
		remotes.add(aiShip);
		aiShip.addOtherShips(remotes);
		aiShip.addOtherShip(player);
	}

	/** Add a new position packet received from the server. This will update all remote ships.
	 * 
	 * @param packet Packet from message of type FULLPOSITIONUPDATE */
	public void addPacket(byte[] packet) {
		int j = 0;
		if (packet.length % SHIP_PACKET_LENGTH != 0) throw new IllegalArgumentException();
		while (j < packet.length) {
			byte[] oneShip = new byte[SHIP_PACKET_LENGTH];
			for (int i = 0; i < SHIP_PACKET_LENGTH; i++) {
				oneShip[i] = packet[j++];
			}
			packets.put(oneShip[0], oneShip);
		}
		// packets.remove(packet[0]);
		// packets.put(packet[0], packet);
	}

	/** @return The position packet for the player, to be sent to the server as a message of type SENDPLAYERDATA */
	public byte[] getShipPacket() {
		return player.export();
	}

	@Override
	public Optional<byte[]> getShip(byte id) {
		return Optional.ofNullable(packets.remove(id));
	}

	private static List<TexturedModel> makeModels(List<String> models, List<String> textures, Loader loader) {
		List<TexturedModel> res = new ArrayList<TexturedModel>();
		for (int i = 0; i < textures.size(); i++) {
			res.add(makeModel(models.get(i), textures.get(i), loader));
		}
		return res;
	}

	private static TexturedModel makeModel(String model, String texture, Loader loader) {
		TexturedModel model1 = new TexturedModel(OBJFileLoader.loadOBJ(model, loader), new ModelTexture(loader.loadTexture(texture)));
		model1.getTexture().setReflectivity(4f);
		model1.getTexture().setShineDamper(10f);
		return model1;
	}

	public void addBarrier(ArrayList<Vector3f> barrierPoints) {

		player.addBarrier(barrierPoints);
		for (Ship ship : remotes) {
			ship.addBarrier(barrierPoints);
		}
	}

}
