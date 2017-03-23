package physics.junit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import physics.placeholders.FlatGroundProvider;
import physics.ships.AIShip;
import trackDesign.TrackPoint;

@RunWith(Parameterized.class)
public class TestAITurn {

  private Vector2f point;
  private float rotationY;
  private int expectedTurn;

  public TestAITurn(Vector2f point, float rotationY, int expectedTurn) {
    this.point = point;
    this.rotationY = rotationY;
    this.expectedTurn = expectedTurn;
  }

  @Parameterized.Parameters
  public static Collection<Object[]> inputs() {
    return Arrays.asList(new Object[][] {
        // Data: point1 position, ship rotation, expected turn 0/-1/1
        { new Vector2f(0, 1000), 0, 0 }, { new Vector2f(1000, 0), 0, 1 },
        { new Vector2f(0, 1000), -90, 1 }, { new Vector2f(0, 1000), 90, -1 } });
  }

  @Test
  public void TurnTest() {
    ArrayList<TrackPoint> track = new ArrayList<>();
    track.add(new TrackPoint(0, 0));
    track.add(new TrackPoint(point.x, point.y));

    AIShip ship = new AIShip(new Byte("0"), null, new Vector3f(), null, new FlatGroundProvider(0),
        track, null);

    // Create some fake barrier points
    ArrayList<Vector3f> barrierPoints = new ArrayList<>();
    barrierPoints.add(new Vector3f(1000, 0, 0));
    barrierPoints.add(new Vector3f(1001, 0, 0));
    ship.addBarrier(barrierPoints);

    ship.start();

    ship.setPosition(new Vector3f());
    ship.setRotation(new Vector3f(0, rotationY, 0));

    Vector3f oldRot = ship.getRotation();

    for (int i = 0; i < 60; i++) {
      ship.update(1f / 60f);
    }

    Vector3f newRot = ship.getRotation();
    float actualTurn = newRot.y - oldRot.y;
    if (actualTurn != 0) {
      actualTurn /= Math.abs(actualTurn);
    }

    System.out.println(oldRot + " - " + newRot + " : " + actualTurn);

    assert (actualTurn == expectedTurn);
  }

}
