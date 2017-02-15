package gameEngine.entities;

import org.joml.Vector3f;
import org.lwjgl.input.Keyboard;

import audioEngine.AudioMaster;
import audioEngine.Source;
import gameEngine.models.TexturedModel;
import gameEngine.renderEngine.DisplayManager;
import gameEngine.terrains.Terrain;
import placeholders.InputController;
import placeholders.InputController.Action;

/**
 * @author rtm592
 *
 */
public class Player extends Ship {
  
  private InputController input;

  public Player(TexturedModel model, Vector3f position, Vector3f rotation, float scale,
      InputController input) {
    super(model, position, rotation, scale);
    this.input = input;
  }

  public void update(float dt) {
    // Check inputs
    thrust = 0;
    if (input.checkAction(Action.FORWARD)) thrust += 1;
    if (input.checkAction(Action.BREAK)) thrust -= 1;
    
    rotThrust = 0;
    if (input.checkAction(Action.TURN_LEFT)) rotThrust += 1;
    if (input.checkAction(Action.TURN_RIGHT)) rotThrust -= 1;
    
    super.update(1f / 60f);
  }

}
