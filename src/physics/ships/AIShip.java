package physics.ships;

import java.util.ArrayList;
import java.util.Collection;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.input.Keyboard;

import gameEngine.models.TexturedModel;
import physics.core.Ship;
import physics.support.Action;
import physics.support.GroundProvider;
import physics.support.InputController;
import physics.support.ShipSounds;
import trackDesign.TrackPoint;

/**
 * 
 * @author Reece Bennett
 * 
 */
public class AIShip extends Ship {

  ShipSounds sound;
  ArrayList<TrackPoint> track;
  int nextPointIndex;
  InputController input;
  
  private boolean debug = true;
  
  public AIShip(byte id, TexturedModel model, Vector3f startingPosition, Collection<Ship> otherShips, GroundProvider ground, ArrayList<TrackPoint> track, InputController input) {
    this(id, model, startingPosition, otherShips, ground, track);
    this.input = input;
  }

  public AIShip(byte id, TexturedModel model, Vector3f startingPosition,
      Collection<Ship> otherShips, GroundProvider ground, ArrayList<TrackPoint> track) {
    super(id, model, startingPosition, ground);
    super.addOtherShips(otherShips);
    this.sound = new ShipSounds(this, otherShips != null ? otherShips : new ArrayList<Ship>());
    this.track = track;
    nextPointIndex = 1;
  }

  @Override
  public void update(float delta) {
    float thrust = 0, turn = 0, strafe = 0, jump = 0;
    
    Vector2f pos = new Vector2f(getPosition().x, getPosition().z);
    float rot = getRotation().y;
    
    TrackPoint nextPoint = track.get(nextPointIndex);
    Vector2f dirToPoint = new Vector2f(nextPoint).sub(pos);
    Vector2f dirVec = new Vector2f((float) Math.sin(Math.toRadians(rot)), (float) Math.cos(Math.toRadians(rot)));
    
    // If angle is - turn right, if + turn left
    float angle = dirVec.angle(dirToPoint); 
    float dist = dirToPoint.length();
    
    float marginOfError = 0.1f;
    
    if (Keyboard.isKeyDown(Keyboard.KEY_I) || input == null) {
      if (angle > marginOfError) turn += 1f;
      if (angle < -marginOfError) turn -= 1f;
      
      thrust = 0.5f;
    
    } else {
      if (input.checkAction(Action.FORWARD)) thrust++;
      if (input.checkAction(Action.BREAK)) thrust--;
      if (input.checkAction(Action.TURN_RIGHT)) turn++;
      if (input.checkAction(Action.TURN_LEFT)) turn--;
      if (input.checkAction(Action.STRAFE_RIGHT)) strafe++;
      if (input.checkAction(Action.STRAFE_LEFT)) strafe--;
      if (input.checkAction(Action.JUMP)) jump++;
    }
    
    if (debug)
      System.out.println("Point: " + nextPointIndex + " - " + nextPoint + " Angle: " + angle
          + " Dist: " + dist /*+ " Pos: " + pos + " TPoint: " + dirToPoint + " dir: " + dirVec*/);

    // Steer and update ship
    super.steer(thrust, turn, strafe, jump, delta);
    super.updatePhysics(delta);
    sound.update(delta);
    
    if (dist < 100) {
      nextPointIndex++;
      if (nextPointIndex >= track.size()) {
        nextPointIndex = 0;
      }
    }
  }

  public void cleanUp() {
    sound.cleanUp();
  }

}
