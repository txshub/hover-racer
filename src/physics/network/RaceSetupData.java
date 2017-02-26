package physics.network;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.joml.Vector3f;

import upgrades.ShipTemplate;

/** Contains data sent by the server to all clients with information about the game. Only the "youId" field is different for each client.
 * 
 * @author Maciej Bogacki */
public class RaceSetupData {

	public long yourId;
	public Map<Byte, ShipSetupData> shipData;
	public Map<Byte, Vector3f> startingPositions;
	public Vector3f startingOrientation;
	public long trackSeed;
	public long timeToStart;


	public RaceSetupData(Map<Byte, ShipSetupData> shipData, Map<Byte, Vector3f> startingPositions, Vector3f startingOrientation,
		long trackSeed, long timeToStart) {
		this.shipData = shipData;
		this.startingPositions = startingPositions;
		this.startingOrientation = startingOrientation;
		this.trackSeed = trackSeed;
		this.timeToStart = timeToStart;
	}
	public RaceSetupData(byte id, Map<Byte, ShipSetupData> shipData, Map<Byte, Vector3f> startingPositions, Vector3f startingOrientation,
		long trackSeed, long timeToStart) {
		this.yourId = id;
		this.shipData = shipData;
		this.startingPositions = startingPositions;
		this.startingOrientation = startingOrientation;
		this.trackSeed = trackSeed;
		this.timeToStart = timeToStart;
	}

	public RaceSetupData setId(byte id) {
		this.yourId = id;
		return this;
	}

	public List<String> getNicknames() {
		return getSorted(shipData, v -> v.nickname);
	}
	public List<String> getModels() {
		return getSorted(shipData, v -> v.model);
	}
	public List<String> getTextures() {
		return getSorted(shipData, v -> v.texture);
	}
	public List<ShipTemplate> getStats() {
		return getSorted(shipData, v -> v.stats);
	}
	public List<Vector3f> getStartingPositions() {
		return getSorted(startingPositions);
	}


	public long getYourId() {
		return yourId;
	}
	public Vector3f getStartingOrientation() {
		return startingOrientation;
	}
	public long getTrackSeed() {
		return trackSeed;
	}
	public long getTimeToStart() {
		return timeToStart;
	}


	private <T1> List<T1> getSorted(Map<Byte, T1> map) {
		return getSorted(map, Function.identity());
	}
	private <T1, T2> List<T2> getSorted(Map<Byte, T1> map, Function<T1, T2> f) {
		return map.entrySet().stream().sorted((a, b) -> a.getKey().compareTo(b.getKey())).map(e -> e.getValue()).map(f)
			.collect(Collectors.toList());
	}


}
