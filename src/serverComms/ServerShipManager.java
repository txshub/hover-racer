package serverComms;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import physics.core.Ship;
import physics.network.RaceSetupData;
import physics.network.ServerShipProvider;
import physics.placeholders.FlatGroundProvider;
import physics.ships.AIShip;
import physics.ships.RemoteShip;
import physics.support.GroundProvider;

public class ServerShipManager implements ServerShipProvider {

	private static final float GROUND_HEIGHT = 0;
	private static final int SHIP_PACKET_LENGTH = 49;
	private ArrayList<Ship> ships;
	private Map<Byte, byte[]> packets;
	private GroundProvider ground;


	public ServerShipManager(RaceSetupData data, int players, int ais) {
		int amount = data.shipData.values().size();
		this.ground = new FlatGroundProvider(GROUND_HEIGHT);
		if (amount != players + ais) throw new IllegalStateException("Mismatch between ShipSetupData and amount of players");
		ships = new ArrayList<Ship>(amount);
		for (int i = 0; i < amount; i++) {
			if (i <= players) ships.add(new RemoteShip((byte) i, null, data.getStartingPositions().get(i), ground, this));
			else ships.add(new AIShip((byte) i, null, data.getStartingPositions().get(i), null, ground, null));
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



}
