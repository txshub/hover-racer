package ai;

import java.util.ArrayList;

import org.joml.Vector2f;

import trackDesign.TrackPoint;

/**
 * 
 * @author Reece Bennett
 *
 */
public class TestAI extends TestShip {

  private ArrayList<TrackPoint> track;
  private int nextPointIndex = 0;

  public TestAI(float x, float y, float rot, ArrayList<TrackPoint> track) {
    super(x, y, rot);
    this.track = track;
  }

  public void doNextInput() {
    TrackPoint np = track.get(nextPointIndex);
    Vector2f nextPoint = new Vector2f(np.x, np.y);
    Vector2f dirToPoint = new Vector2f(nextPoint).sub(pos);
    Vector2f dirVec = new Vector2f((float) Math.sin(Math.toRadians(rot)),
        (float) -Math.cos(Math.toRadians(rot)));

    // If angle is - turn right, if + turn left
    float angle = dirToPoint.angle(dirVec);
    float dist = dirToPoint.length();

    float dTurn = 0;

    if (angle > 0) {
      dTurn = -TestShip.maxTurnSpeed;
    } else if (angle < 0) {
      dTurn = TestShip.maxTurnSpeed;
    }

    setRotV(dTurn);

    // Check the angle between future points, if high slow down
    // Get the next points
    ArrayList<Vector2f> nextEdges = new ArrayList<>();
    for (int i = 0; i < 2; i++) {
      int n = nextPointIndex + i;
      int nextPoint1 = n + i >= track.size() ? n + i - track.size() : n + i;
      int nextPoint2 = n + i + 1 >= track.size() ? (n + i + 1) - track.size() : n + i + 1;

      Vector2f point1 = new Vector2f(track.get(nextPoint1).x, track.get(nextPoint1).y);
      Vector2f point2 = new Vector2f(track.get(nextPoint2).x, track.get(nextPoint2).y);

      nextEdges.add(point2.sub(point1).normalize());
    }

    setAccel(0.02);

    if (dist < 10) {
      nextPointIndex++;
      if (nextPointIndex >= track.size()) {
        nextPointIndex = 0;
      }
    }
  }

}
