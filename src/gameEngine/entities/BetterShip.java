package gameEngine.entities;

import java.util.Collection;

import org.joml.Vector3f;
import org.lwjgl.input.Keyboard;

import gameEngine.models.TexturedModel;
import placeholders.Action;
import placeholders.ControllerInt;

public class BetterShip extends Entity {
  
  private Vector3f velocity;
  private Vector3f acceleration;
  
  private float thrustInput;
  private float turnInput;
  
  private float maxAcceleration;
  private float maxTurnSpeed;
  private float airCushion;
  private float mass;
  
  private final float GRAVITY = 1;

  public BetterShip(TexturedModel model, int textureIndex, Vector3f position, Vector3f rotation,
      float scale) {
    super(model, textureIndex, position, rotation, scale);
    
    velocity = new Vector3f();
    acceleration = new Vector3f();
    
    thrustInput = 0;
    turnInput = 0;
  }
  
  private void handleControls(float delta) {
    turnInput = 0;
    if (Keyboard.isKeyDown(Keyboard.KEY_A)) turnInput -= 1;
    if (Keyboard.isKeyDown(Keyboard.KEY_D)) turnInput += 1;
    
    thrustInput = 0;
    if (Keyboard.isKeyDown(Keyboard.KEY_W)) thrustInput += 1;
    if(Keyboard.isKeyDown(Keyboard.KEY_S)) thrustInput -= 1;
  }
  
  public void update(float delta) {
    handleControls(delta);
    
    // Apply inputs
    
  }

}
