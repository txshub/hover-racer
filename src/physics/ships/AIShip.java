package physics.ships;

import java.util.ArrayList;
import java.util.Collection;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.input.Keyboard;

import gameEngine.models.TexturedModel;
import input.Action;
import input.InputController;
import physics.core.Ship;
import physics.support.GroundProvider;
import trackDesign.TrackPoint;
import upgrades.ShipTemplate;

/**
 * 
 * @author Reece Bennett
 * 
 */
public class AIShip extends Ship {

  private ArrayList<TrackPoint> track;
  private int nextPointIndex;
  private InputController input;

  private boolean debug = false;

  private final float turnMargin = 0.3f;
  private final float strafeMargin = 0.3f;

  public AIShip(byte id, TexturedModel model, Vector3f startingPosition,
      Collection<Ship> otherShips, GroundProvider ground, ArrayList<TrackPoint> track,
      ShipTemplate stats, InputController input) {
    this(id, model, startingPosition, otherShips, ground, track, stats);
    this.input = input;
  }

  public AIShip(byte id, TexturedModel model, Vector3f startingPosition,
      Collection<Ship> otherShips, GroundProvider ground, ArrayList<TrackPoint> track,
      ShipTemplate stats) {
    super(id, model, startingPosition, ground, stats, track);
    super.addOtherShips(otherShips);
    this.track = track;
    nextPointIndex = 1;
  }

  @Override
  public void update(float delta) {
    if (debug)
      System.out.print("AI: " + getId() + " ");

    float thrust = 0, turn = 0, strafe = 0;

    Vector2f pos = new Vector2f(getPosition().x, getPosition().z);
    float rot = getRotation().y;

    TrackPoint nextPoint = track.get(nextPointIndex);
//    TrackPoint nextNextPoint = nextPointIndex + 1 >= track.size() ? track.get(0)
//        : track.get(nextPointIndex + 1);
    TrackPoint prevPoint = nextPointIndex == 0 ? track.get(track.size() - 1)
        : track.get(nextPointIndex - 1);

    Vector2f dirNP = new Vector2f(nextPoint).sub(pos);
    Vector2f dirSeg = new Vector2f(nextPoint).sub(prevPoint);
    Vector2f dirVec = new Vector2f((float) Math.sin(Math.toRadians(rot)),
        (float) Math.cos(Math.toRadians(rot)));

    // If angle < 0 turn right, > 0 turn left
    float turnAngle = dirVec.angle(dirNP) * 2;

    float dist = dirNP.length();

    float x0 = pos.x;
    float y0 = pos.y;
    float x1 = prevPoint.x;
    float y1 = prevPoint.y;
    float x2 = nextPoint.x;
    float y2 = nextPoint.y;
    
    // -1 on left of track, +1 on right of track 
    float perpSide = dirNP.angle(dirSeg);
    perpSide /= -Math.abs(perpSide);

    float perpDistance = Math.abs((y2 - y1) * x0 - (x2 - x1) * y0 + x2 * y1 - y2 * x1)
        / dirSeg.length() * perpSide;
    if (debug)
      System.out.print("pd: " + (perpDistance / track.get(nextPointIndex).getWidth() * 2) + " ");

    try {
      if (input == null || Keyboard.isKeyDown(Keyboard.KEY_I)) {
        if (turnAngle > turnMargin) {
          turn = Math.min(1f, turnAngle);
        } else if (turnAngle < -turnMargin) {
          turn = Math.max(-1f, turnAngle);
        }

         if (perpDistance > strafeMargin) {
         strafe = Math.min(1, perpDistance / track.get(nextPointIndex).getWidth() * 2);
         } else if (perpDistance < -strafeMargin) {
         strafe = Math.max(-1, perpDistance / track.get(nextPointIndex).getWidth() * 2);
         }

        thrust = 1f;

      } else {

        thrust += input.isDown(Action.FORWARD);
        thrust -= input.isDown(Action.BREAK);
        turn += input.isDown(Action.TURN_RIGHT);
        turn -= input.isDown(Action.TURN_LEFT);
        strafe += input.isDown(Action.STRAFE_RIGHT);
        strafe -= input.isDown(Action.STRAFE_LEFT);
      }

    } catch (IllegalStateException e) {
      // I'm on the server and I don't have access to Keyboard
    }

    if (debug)
      System.out.println("Point: " + nextPointIndex + " - " + nextPoint + " Angle: " + turnAngle
          + " Dist: " + dist + " Thrust: " + thrust + " Turn: " + turn + " Strafe: " + strafe);

    // Steer and update ship
    super.steer(thrust, turn, strafe, delta); // TODO temporary thing
    super.updatePhysics(delta);

    if (dist < track.get(nextPointIndex).getWidth() / 2) {
      nextPointIndex++;
      if (nextPointIndex >= track.size()) {
        nextPointIndex = 0;
      }
    }
  }

  public void cleanUp() {
    // sound.cleanUp();
  }

}
