package physics.ships;

import java.util.ArrayList;
import java.util.Collection;

import org.joml.Vector3f;

import gameEngine.models.TexturedModel;
import input.Action;
import input.InputController;
import physics.core.Ship;
import physics.support.GroundProvider;
import physics.support.ShipSounds;

/**
 * Represents a Ship controlled with inputs from the Player
 * 
 * @author Maciej Bogacki
 */
public class PlayerShip extends Ship {

  InputController input;
  ShipSounds sound;

  public PlayerShip(byte id, TexturedModel model, Vector3f startingPosition,
      Collection<Ship> otherShips, GroundProvider ground, InputController input) {
    super(id, model, startingPosition, ground);
    super.addOtherShips(otherShips);
    this.input = input; // Deal with input
    this.sound = new ShipSounds(this, otherShips != null ? otherShips : new ArrayList<Ship>()); // Create
                                                                                                // ShipSounds
  }

  @Override
  public void update(float delta) {
    float thrust = 0f, turn = 0f, strafe = 0f, jump = 0f;
    // Handle inputs
    thrust += input.isDown(Action.FORWARD);
    thrust += input.isDown(Action.BREAK);
    turn += input.isDown(Action.TURN_RIGHT);
    turn += input.isDown(Action.TURN_LEFT);
    strafe += input.isDown(Action.STRAFE_RIGHT);
    strafe += input.isDown(Action.STRAFE_LEFT);
    jump += input.isDown(Action.JUMP);
    // System.out.println(thrust +", "+turn+", "+strafe);
    // Steer and update ship
    super.steer(thrust, turn, strafe, jump, delta);
    super.updatePhysics(delta);
    sound.update(delta);
  }

  public void cleanUp() {
    sound.cleanUp();
  }

}
