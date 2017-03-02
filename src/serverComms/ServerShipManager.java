package serverComms;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import physics.core.Ship;
import physics.network.RaceSetupData;
import physics.network.ServerShipProvider;
import physics.placeholders.FlatGroundProvider;
import physics.ships.DummyShip;
import physics.ships.RemoteShip;

public class ServerShipManager implements ServerShipProvider {

	private static final float GROUND_HEIGHT = 0;
	private static final int SHIP_PACKET_LENGTH = 49;
	private ArrayList<Ship> ships;
	private Map<Byte, byte[]> packets;


	public ServerShipManager(RaceSetupData data, int players, int ais) {
		int amount = data.shipData.values().size();
		if (amount != players + ais) throw new IllegalStateException("Mismatch between ShipSetupData and amount of players");
		ships = new ArrayList<Ship>(amount);
		for (int i = 0; i < amount; i++) {
			if (i <= players)
				ships.add(new RemoteShip((byte) i, null, data.getStartingPositions().get(i), new FlatGroundProvider(GROUND_HEIGHT), this));
			// TODO change DummyShip to AIShip
			else ships.add(new DummyShip((byte) i, null, data.getStartingPositions().get(i), new FlatGroundProvider(GROUND_HEIGHT)));
		}
	}

	public void update(float delta) {
		ships.forEach(s -> s.update(delta));
	}

	public void addPacket(byte[] packet) {
		int j = 0;
		while (j < packet.length) {
			byte[] oneShip = new byte[SHIP_PACKET_LENGTH];
			for (int i = 0; i < SHIP_PACKET_LENGTH; i++) {
				oneShip[i] = packet[j++];
			}
			packets.put(oneShip[0], oneShip);
		}
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
