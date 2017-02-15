package gameEngine.entities;

import org.joml.Vector3f;

import audioEngine.Source;
import gameEngine.models.TexturedModel;

/**
 * @author rtm592
 *
 */
public class Ship extends Entity{

	protected final float TURN_SPEED = 0.05f;
	protected final float GRAVITY = -50;
	protected final float JUMP_POWER = 30;
	protected float TERRAIN_HEIGHT = 0;

	protected Vector3f velocity;

	protected float acceleration = 0.2f;
	protected float maxSpeed = 30;
	protected float currentRunSpeed = 0;
	protected float currentStrafeSpeed = 0;
	protected float currentTurn = 0;
	protected float maxTurn = 1;
	protected float upwardsSpeed = 0;

	protected boolean isInAir = false;
	
	protected Source source = null;
	protected int jumpBuffer;

	public Ship(TexturedModel model, int textureIndex, Vector3f position, Vector3f rotation,
			float scale) {
		super(model, textureIndex, position, rotation, scale);
	}

	public Ship(TexturedModel model, Vector3f position, Vector3f rotation, float scale) {
		super(model, position, rotation, scale);
	}
	
	protected void jump() {
		if (!isInAir) {
			this.upwardsSpeed = JUMP_POWER;
			isInAir = true;
		}
	}

	protected void turn(float i) {
		
		if(i != 0){
			currentTurn += i;
			if(currentTurn > maxTurn){
				currentTurn = maxTurn;
			}else if(currentTurn < -maxTurn){
				currentTurn = -maxTurn;
			}
		}else{
			this.currentTurn /= 1.1f;
		}
		
	}

	protected void move(float acceleration) {
		
		velocity.x += Math.sin(Math.toRadians(this.getRotation().y)) * acceleration;
		velocity.z += Math.cos(Math.toRadians(this.getRotation().y)) * acceleration;
		
		float speed = (float) Math.sqrt(Math.pow(velocity.x,2) + Math.pow(velocity.z,2));
		if(speed > maxSpeed){
			velocity.x = (velocity.x / speed) * maxSpeed;
			velocity.z = (velocity.z / speed) * maxSpeed;
		}
		if(acceleration == 0){
			velocity.x /= 1.01f;
			velocity.z /= 1.01f;
		}
		
	}
}
