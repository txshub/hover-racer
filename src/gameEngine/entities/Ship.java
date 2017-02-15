package gameEngine.entities;

import org.joml.Vector3f;

import audioEngine.Source;
import gameEngine.models.TexturedModel;

/**
 * @author rtm592
 *
 */
public class Ship extends Entity{

//	protected final float TURN_SPEED = 0.05f;
//	protected final float GRAVITY = -50;
//	protected final float JUMP_POWER = 30;
//	protected float TERRAIN_HEIGHT = 0;
//
//	protected float acceleration = 0.2f;
//	protected float maxSpeed = 30;
//	protected float currentRunSpeed = 0;
//	protected float currentStrafeSpeed = 0;
//	protected float currentTurn = 0;
//	protected float maxTurn = 1;
//	protected float upwardsSpeed = 0;
//
//	protected boolean isInAir = false;
//	
//	protected Source source = null;
//	protected int jumpBuffer;

  private Vector3f velocity;
  private Vector3f acceleration;
  private Vector3f rotVelocity;
  
  protected float thrust;
  protected float rotThrust; 
  protected float mass;
  protected float maxAcceleration = 1000f;
  protected float maxTurn = 2f;
  
  private final float DRAG = 1f; // Drag constant
  private final Vector3f G = new Vector3f(0, -9.81f, 0); // Acceleration due to gravity

	public Ship(TexturedModel model, Vector3f position, Vector3f rotation, float scale) {
		this(model, 0, position, rotation, scale);
	}

  public Ship(TexturedModel model, int textureIndex, Vector3f position, Vector3f rotation,
      float scale) {
    super(model, textureIndex, position, rotation, scale);
    
    velocity = new Vector3f();
    acceleration = new Vector3f();
    rotVelocity = new Vector3f();
    
    thrust = 0;
    rotThrust = 0;
    mass = 1;
  }

	protected void update(float dt) {
	  // Update rotation
	  rotVelocity.y = rotThrust * maxTurn;
	  rotation.add(rotVelocity);
	  
	  // Update acceleration
	  acceleration = new Vector3f();
	  
	  // Apply input
	  float rotY = (float) Math.toRadians(rotation.y);
	  Vector3f power = new Vector3f(
	      (float) ((thrust * maxAcceleration * Math.sin(rotY)) / mass), 
	      0f, 
	      (float) ((thrust * maxAcceleration * Math.cos(rotY)) / mass));
	  
	  acceleration.add(power);
    
    // Apply gravity
    //acceleration.add(G.x, G.y, G.z);
	
    // Apply drag
    Vector3f dragForce = new Vector3f(
        -velocity.x * DRAG,
        -velocity.y * DRAG,
        -velocity.z * DRAG);
    
    acceleration.add(dragForce);
    
    // Update velocity (again)
    velocity.add(new Vector3f(acceleration).mul(dt));
	  
	  // Update position with velocity
	  position.add(new Vector3f(velocity).mul(dt));
	  
	  System.out.println("dt: " + dt + " T: " + thrust + " RT: " + rotThrust + " P: " + power + " DF: " + dragForce + " R: " + rotation + " A: " + acceleration + " V: " + velocity + " P: " + position);
	}
}
