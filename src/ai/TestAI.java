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
  
  private float turnMargin = 0.01f;

  public TestAI(float x, float y, float rot, ArrayList<TrackPoint> track) {
    super(x, y, rot);
    this.track = track;
  }

  public void doNextInput() {
    TrackPoint np = track.get(nextPointIndex);
    TrackPoint nnp = nextPointIndex + 1 >= track.size() ? track.get(0)
        : track.get(nextPointIndex + 1);
    TrackPoint pp = nextPointIndex == 0 ? track.get(track.size() - 1)
        : track.get(nextPointIndex - 1);
    
    System.out.println("P: " + pos.x + ", " + pos.y + " R: " + rot);
    
    Vector2f dirToNP = new Vector2f(np).sub(pos);
    Vector2f facing = new Vector2f((float) Math.sin(Math.toRadians(rot)),
        (float) -Math.cos(Math.toRadians(rot)));
    
    float angle = facing.angle(dirToNP);
    float dist = dirToNP.length();
    
    // Do controls
    if (angle > turnMargin) {
      setRotV(TestShip.maxTurnSpeed);
    } else if (angle < turnMargin) {
      setRotV(-TestShip.maxTurnSpeed);
    }
    
    setAccel(0.1f);

    // Check if we passed the point yet
    if (dist < 5) {
      nextPointIndex++;
      if (nextPointIndex >= track.size()) {
        nextPointIndex = 0;
      }
    }
  }
  
  public int getNextPointIndex() {
    return nextPointIndex;
  }

}
