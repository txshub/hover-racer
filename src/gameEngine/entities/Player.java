package gameEngine.entities;

import org.joml.Vector3f;
import org.lwjgl.input.Keyboard;

import audioEngine.AudioMaster;
import audioEngine.Source;
import gameEngine.models.TexturedModel;
import gameEngine.renderEngine.DisplayManager;
import gameEngine.terrains.Terrain;

public class Player extends Ship {
	
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
				Math.min(terrains.length, (super.getPosition().x / Terrain.SIZE)))][(int) Math.max(0,
						Math.min(terrains[1].length - 1, (super.getPosition().z / Terrain.SIZE)))].getHeightOfTerrain(
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

	protected void initAudio() {
		source = new Source();
		jumpBuffer = AudioMaster.loadSound("audioEngine/bounce.wav");
	}

	protected void checkInputs() {
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

}
