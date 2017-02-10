package gameEngine.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import audioEngine.AudioMaster;
import audioEngine.Source;
import gameEngine.engineTester.MainGameLoop;
import gameEngine.models.TexturedModel;
import gameEngine.renderEngine.DisplayManager;
import gameEngine.terrains.Terrain;

public class Player extends Entity {

	private final float TURN_SPEED = 120;
	private final float GRAVITY = -50;
	private final float JUMP_POWER = 30;
	private float TERRAIN_HEIGHT = 0;

	private Vector3f velocity;

	private float acceleration = 1f;
	private float maxSpeed = 30;
	private float currentRunSpeed = 0;
	private float currentStrafeSpeed = 0;
	private float currentTurn = 0;
	private float maxTurn = 5;
	private float upwardsSpeed = 0;

	private boolean isInAir = false;

	private Source source = null;
	private int jumpBuffer;
	
	public Player(TexturedModel model, org.joml.Vector3f position, float dx, float dy, float dz, float scale) {
	  this(model, new Vector3f(position.x, position.y, position.z), dx, dy, dz, scale);
	}
	
	public Player(TexturedModel model, Vector3f position, float dx, float dy, float dz, float scale) {
		super(model, position, dx, dy, dz, scale);
		velocity = new Vector3f(0,0,0);
		initAudio();
	}

	public void move(Terrain[][] terrains) {
		checkInputs();
		super.increaseRotation(0, currentTurn, 0);
		
		float distance = currentRunSpeed * DisplayManager.getFrameTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRoty())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRoty())));
		distance = currentStrafeSpeed * DisplayManager.getFrameTimeSeconds();
		dx += (float) (distance * Math.cos(Math.toRadians(super.getRoty())));
		dz += (float) (distance * -Math.sin(Math.toRadians(super.getRoty())));
		
		velocity.y = upwardsSpeed * DisplayManager.getFrameTimeSeconds();
		super.increasePosition(velocity.x, velocity.y, velocity.z);
		
		float terrainHeight = terrains[(int) Math.max(0,
				Math.min(MainGameLoop.size - 1, (super.getPosition().x / Terrain.SIZE)))][(int) Math.max(0,
						Math.min(MainGameLoop.size - 1, (super.getPosition().z / Terrain.SIZE)))].getHeightOfTerrain(
								super.getPosition().x, super.getPosition().z);
		
		if (super.getPosition().y <= terrainHeight + 5) {
			upwardsSpeed = 0;
			isInAir = false;
			super.getPosition().y = terrainHeight + 5;
		} else {
			upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
			isInAir = true;
		}
		
		// Update listener position
		AudioMaster.setListenerData(super.getPosition().x, super.getPosition().y, super.getPosition().z);
	}

	private void initAudio() {
		source = new Source();
		jumpBuffer = AudioMaster.loadSound("audioEngine/bounce.wav");
	}
	
	private void jump() {
		if (!isInAir) {
			this.upwardsSpeed = JUMP_POWER;
			isInAir = true;
			
			// Play sound
			source.setPosition(super.getPosition().x, super.getPosition().y, super.getPosition().z);
			source.play(jumpBuffer);
		}
	}

	private void checkInputs() {
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			maxSpeed = 60;
		} else {
			maxSpeed = 30;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			turn(1);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			turn(-1);
		} else {
			turn(0);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			move(acceleration);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			move(-acceleration);
		} else {
			move(0);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			jump();
		}
	}

	private void turn(float i) {
		
		if(i != 0){
			currentTurn += i;
			if(currentTurn > maxTurn){
				currentTurn = maxTurn;
			}
		}else{
			this.currentTurn /= 1.5f;
		}
		
	}

	private void move(float acceleration) {
		
		velocity.x += Math.sin(Math.toRadians(this.getRoty())) * acceleration;
		velocity.z += Math.cos(Math.toRadians(this.getRoty())) * acceleration;
		
		float speed = (float) Math.sqrt(Math.pow(velocity.x,2) + Math.pow(velocity.z,2));
		if(speed > maxSpeed){
			velocity.x = (velocity.x / speed) * maxSpeed;
			velocity.z = (velocity.z / speed) * maxSpeed;
		}
		if(acceleration == 0){
			velocity.x /= 1.5f;
			velocity.z /= 1.5f;
		}
		
	}

}
