package serverLogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.joml.Vector2f;
import org.joml.Vector3f;

import physics.core.Ship;
import physics.network.RaceSetupData;
import physics.network.ServerShipProvider;
import physics.placeholders.FlatGroundProvider;
import physics.ships.AIShip;
import physics.ships.RemoteShip;
import physics.support.GroundProvider;
import serverComms.ServerComm;
import trackDesign.SeedTrack;
import trackDesign.TrackPoint;

public class ServerShipManager implements ServerShipProvider {

  private static final float GROUND_HEIGHT = 0;
  private static final int SHIP_PACKET_LENGTH = 49;
  private ArrayList<Ship> ships;
  private Map<Byte, byte[]> packets;
  private GroundProvider ground;

  public ServerShipManager(RaceSetupData data, int players, int ais,
      ArrayList<TrackPoint> trackPoints, Vector3f startingOrientation) {
    if (ServerComm.DEBUG) {
      System.out.println("Server generated race data:");
      System.out.println(data.toString());
    }

    int amount = data.shipData.values().size();
    packets = new HashMap<>();

    if (amount == 0)
      throw new IllegalArgumentException("ServerShipManager created with no ship data");
    if (amount != players + ais)
      throw new IllegalArgumentException("Mismatch between ShipSetupData and amount of players: "
          + amount + " vs " + players + "+" + ais);

    this.ground = new FlatGroundProvider(GROUND_HEIGHT);
    ships = new ArrayList<Ship>(amount);
    for (int i = 0; i < amount; i++) {
      if (data.getStartingPositions().size() == 0)
        throw new IllegalArgumentException("Not starting positions");
      if (i < players)
        ships.add(new RemoteShip((byte) i, null, data.getStartingPositions().get(i), ground,
            data.getStats().get(i), trackPoints, this));
      else
        ships.add(new AIShip((byte) i, null, data.getStartingPositions().get(i), null, ground,
            trackPoints, data.getStats().get(i)));
    }
    ships.forEach(s -> s.addOtherShips(ships));
    ships.forEach(ship -> ship.setRotation(startingOrientation));

    for (Ship ship : ships) {
      ship.addBarrier(makeBarrierPoints(trackPoints));
    }
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

  public ArrayList<ShipLogicData> getShipsLogics() {
    return ships.stream().map(ship -> new ShipLogicData(ship))
        .collect(Collectors.toCollection(ArrayList::new));
  }

  private ArrayList<Vector3f> makeBarrierPoints(ArrayList<TrackPoint> trackPoints) {
    float trackHeight = SeedTrack.getTrackHeight();
    float barrierHeight = SeedTrack.getBarrierHeight();
    float barrierWidth = SeedTrack.getBarrierWidth();
    ArrayList<Vector3f> barrierPoints = new ArrayList<>();
    for (int i = 0; i <= trackPoints.size(); i++) {
      TrackPoint curPoint = null;
      TrackPoint prevPoint = null;
      TrackPoint nextPoint = null;
      if (i < trackPoints.size()) {
        curPoint = trackPoints.get(i);
        // If we are at the first point the previous is the last point
        int prev = (i == 0) ? trackPoints.size() - 1 : i - 1;
        prevPoint = trackPoints.get(prev);

        // If we are at the last point the next is the first point
        int next = (i == trackPoints.size() - 1) ? 0 : i + 1;
        nextPoint = trackPoints.get(next);
      } else {
        curPoint = trackPoints.get(0);
        prevPoint = trackPoints.get(trackPoints.size() - 1);
        nextPoint = trackPoints.get(1);
      }
      // Find the line between previous and next point for direction of
      // this
      // slice
      Vector2f dirVec = new Vector2f(nextPoint).sub(prevPoint).normalize();

      // Calculate the perpendicular normal vectors
      Vector2f left = new Vector2f(dirVec.y, -dirVec.x).normalize();
      Vector2f right = new Vector2f(-dirVec.y, dirVec.x).normalize();

      // Apply the offsets to the center point
      float w = curPoint.getWidth() / 2;
      Vector3f centerPoint = new Vector3f(curPoint.x, trackHeight, curPoint.y);
      Vector3f leftPoint = new Vector3f(centerPoint).add(left.x * w, 0, left.y * w);
      Vector3f rightPoint = new Vector3f(centerPoint).add(right.x * w, 0, right.y * w);
      barrierPoints.add(leftPoint);
      barrierPoints.add(rightPoint);
    }
    return barrierPoints;
  }

}
