package physics.junit;

import java.util.ArrayList;

import org.joml.Vector3f;
import org.junit.Before;
import org.junit.Test;

import physics.core.ImVector2f;
import physics.placeholders.FlatGroundProvider;
import physics.ships.AIShip;
import trackDesign.TrackPoint;

public class TestAI {

  private AIShip ship;
  private ArrayList<TrackPoint> track;

  @Before
  public void setUp() {
    track = new ArrayList<>();
  }

  @Test
  public void Turn1() {
    track = new ArrayList<>();
    track.add(new TrackPoint(-70, -1000));
    track.add(new TrackPoint(0, 0));
    track.add(new TrackPoint(-50, 1000));

    ship = new AIShip(new Byte("0"), null, new Vector3f(), null,
        new FlatGroundProvider(0), track, null);
    // Create some fake barrier points
    ArrayList<Vector3f> barrierPoints = new ArrayList<>();
    barrierPoints.add(new Vector3f(1000, 0, 0));
    barrierPoints.add(new Vector3f(1001, 0, 0));
    ship.addBarrier(barrierPoints);

    ship.setPosition(new Vector3f(-100, 0, -10));
    ship.setRotation(new Vector3f(0, 0.6f, 0));

    Vector3f rot = ship.getRotation();
    System.out.println(rot);

    for (int i = 0; i < 600; i++) {
      ship.update(1f / 60f);
    }

    Vector3f newRot = ship.getRotation();

    System.out.println(rot + " " + newRot);
  }

}
