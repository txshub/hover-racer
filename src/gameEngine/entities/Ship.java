package gameEngine.entities;


import org.joml.Vector3f;

import audioEngine.Source;
import gameEngine.models.TexturedModel;

public class Ship extends Entity{

	protected final float TURN_SPEED = 120;
	protected final float GRAVITY = -50;
	protected final float JUMP_POWER = 30;
	protected float TERRAIN_HEIGHT = 0;

	protected Vector3f velocity;

	protected float acceleration = 1f;
	protected float maxSpeed = 30;
	protected float currentRunSpeed = 0;
	protected float currentStrafeSpeed = 0;
	protected float currentTurn = 0;
	protected float maxTurn = 5;
	protected float upwardsSpeed = 0;

	protected boolean isInAir = false;

	protected Source source = null;
	protected int jumpBuffer;

	public Ship(TexturedModel model, int textureIndex, Vector3f position, float rotx, float roty, float rotz,
			float scale) {
		super(model, textureIndex, position, rotx, roty, rotz, scale);
	}

	public Ship(TexturedModel model, Vector3f position, float rotx, float roty, float rotz, float scale) {
		super(model, position, rotx, roty, rotz, scale);
	}
	
	protected void jump() {
		if (!isInAir) {
			this.upwardsSpeed = JUMP_POWER;
			isInAir = true;
			
			// Play sound
			source.setPosition(super.getPosition().x, super.getPosition().y, super.getPosition().z);
			source.play(jumpBuffer);
		}
	}

	protected void turn(float i) {
		
		if(i != 0){
			currentTurn += i;
			if(currentTurn > maxTurn){
				currentTurn = maxTurn;
			}
			if(currentTurn < -maxTurn){
				currentTurn = -maxTurn;
			}
		}else{
			this.currentTurn /= 1.1f;
		}
		
	}

	protected void move(float acceleration) {
		
		velocity.x += Math.sin(Math.toRadians(this.getRoty())) * acceleration * 3;
		velocity.z += Math.cos(Math.toRadians(this.getRoty())) * acceleration * 3;
		
		float speed = (float) Math.sqrt(Math.pow(velocity.x,2) + Math.pow(velocity.z,2));
		if(speed > maxSpeed){
			velocity.x = (velocity.x / speed) * maxSpeed;
			velocity.z = (velocity.z / speed) * maxSpeed;
		}
		if(acceleration == 0){
			velocity.x /= 1.1f;
			velocity.z /= 1.1f;
		}
		
	}
	
	

}
