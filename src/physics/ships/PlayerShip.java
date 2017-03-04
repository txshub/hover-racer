package physics.ships;

import java.util.ArrayList;
import java.util.Collection;

import org.joml.Vector3f;

import gameEngine.models.TexturedModel;
import input.Action;
import input.KeyboardController;
import physics.core.Ship;
import physics.support.GroundProvider;
import physics.support.ShipSounds;

/**
 * Represents a Ship controlled with inputs from the Player
 * 
 * @author Maciej Bogacki
 */
public class PlayerShip extends Ship {

  KeyboardController input;
  ShipSounds sound;

  public PlayerShip(byte id, TexturedModel model, Vector3f startingPosition,
      Collection<Ship> otherShips, GroundProvider ground, KeyboardController input) {
    super(id, model, startingPosition, ground);
    super.addOtherShips(otherShips);
    this.input = input; // Deal with input
    this.sound = new ShipSounds(this, otherShips != null ? otherShips : new ArrayList<Ship>()); // Create
                                                                                                // ShipSounds
  }

  @Override
  public void update(float delta) {
    float thrust = 0, turn = 0, strafe = 0, jump = 0;
    // Handle inputs
      thrust += input.isDown(Action.FORWARD);
        thrust -= input.isDown(Action.BREAK);
      turn += input.isDown(Action.TURN_RIGHT);
      turn -= input.isDown(Action.TURN_LEFT);
      strafe += input.isDown(Action.STRAFE_RIGHT);
      strafe -= input.isDown(Action.STRAFE_LEFT);
      jump += input.isDown(Action.JUMP);
    // Steer and update ship
    super.steer(thrust, turn, strafe, jump, delta);
    super.updatePhysics(delta);
    sound.update(delta);
  }

  public void cleanUp() {
    sound.cleanUp();
  }

}
