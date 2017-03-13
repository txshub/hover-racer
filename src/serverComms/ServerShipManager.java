package serverComms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import physics.core.Ship;
import physics.network.RaceSetupData;
import physics.network.ServerShipProvider;
import physics.placeholders.FlatGroundProvider;
import physics.ships.AIShip;
import physics.ships.RemoteShip;
import physics.support.GroundProvider;
import trackDesign.TrackPoint;

public class ServerShipManager implements ServerShipProvider {

	private static final float GROUND_HEIGHT = 0;
	private static final int SHIP_PACKET_LENGTH = 49;
	private ArrayList<Ship> ships;
	private Map<Byte, byte[]> packets;
	private GroundProvider ground;

	public ServerShipManager(RaceSetupData data, int players, int ais, ArrayList<TrackPoint> trackPoints) {
		System.out.println("Server generated race data:");
		System.out.println(data.toString());

		int amount = data.shipData.values().size();
		packets = new HashMap<>();

		if (amount == 0) throw new IllegalArgumentException("ServerShipManager created with no ship data");
		if (amount != players + ais)
			throw new IllegalArgumentException(
				"Mismatch between ShipSetupData and amount of players: " + amount + " vs " + players + "+" + ais);

		this.ground = new FlatGroundProvider(GROUND_HEIGHT);
		ships = new ArrayList<Ship>(amount);
		for (int i = 0; i < amount; i++) {
			if (data.getStartingPositions().size() == 0) throw new IllegalArgumentException("Not starting positions");
			if (i < players) ships.add(new RemoteShip((byte) i, null, data.getStartingPositions().get(i), ground, this));
			else ships.add(new AIShip((byte) i, null, data.getStartingPositions().get(i), null, ground, trackPoints));
		}
		ships.forEach(s -> s.addOtherShips(ships));
	}

	public void update(float delta) {
		ships.forEach(s -> s.update(delta));
	}

	public void addPacket(byte[] packet) {
		packets.put(packet[0], packet);
	}

	public byte[] getPositionMessage() {
		byte[] res = new byte[SHIP_PACKET_LENGTH * ships.size()];
		for (int i = 0; i < ships.size(); i++) {
			byte[] oneShip = ships.get(i).export();
			for (int j = 0; j < oneShip.length; j++) {
				res[SHIP_PACKET_LENGTH * i + j] = oneShip[j];
			}
		}
		return res;
	}

	@Override
	public Optional<byte[]> getShip(byte id) {
		return Optional.ofNullable(packets.remove(id));
	}

	public void startRace() {
		ships.forEach(s -> s.start());
	}

}
