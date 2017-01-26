package ai;

import java.util.ArrayList;

import org.joml.Vector2f;

import trackDesign.TrackPoint;

public class AIShip extends Ship {
  
  private ArrayList<TrackPoint> track;
  private int nextPointIndex = 0;
  
  public AIShip(int x, int y, int rot, ArrayList<TrackPoint> track) {
    super(x, y, rot);
    this.track = track;
  }
  
  public void doNextInput() {
    TrackPoint np = track.get(nextPointIndex);
    Vector2f nextPoint = new Vector2f(np.getX(), np.getY());
    Vector2f dirToPoint = nextPoint.sub(pos);
    Vector2f dirVec = new Vector2f((float) Math.sin(Math.toRadians(rot)), (float) -Math.cos(Math.toRadians(rot)));
    
    // If angle is - turn right, if + turn left
    float angle = dirToPoint.angle(dirVec); 
    float dist = dirToPoint.length();
    
    System.out.println("Next: " + nextPointIndex + " angle: " + angle + " dist: " + dist);
    
    float dTurn = 0;
    float dAcel = 0;
    
    if (angle > 0) {
      dTurn = -Ship.maxTurnSpeed;
    } else if (angle < 0) {
      dTurn = Ship.maxTurnSpeed;
    }
    
    setRotV(dTurn);
    setAccel(0.1);
    
    if (dist < 2) {
      nextPointIndex++;
      if (nextPointIndex >= track.size()) {
        nextPointIndex = 0;
      }
    }
  }

}
