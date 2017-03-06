package gameEngine.entities;

import org.joml.Vector3f;

import gameEngine.models.TexturedModel;
import input.Action;
import input.KeyboardController;

/** @author rtm592 */
public class Player extends Ship {

  private KeyboardController input;

  public Player(TexturedModel model, Vector3f position, Vector3f rotation, float scale,
      KeyboardController input) {
    super(model, position, rotation, scale);
    this.input = input;
  }

  public void update(float dt) {
    // Check inputs
    thrust = 0;
      thrust += input.isDown(Action.FORWARD);
      thrust -= input.isDown(Action.BREAK);

    rotThrust = 0;
      rotThrust += input.isDown(Action.TURN_LEFT);
      rotThrust -= input.isDown(Action.TURN_RIGHT);

    super.update(1f / 60f);
  }

}
