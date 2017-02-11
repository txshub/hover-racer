package gameEngine.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import audioEngine.Source;
import gameEngine.engineTester.MainGameLoop;
import gameEngine.models.TexturedModel;
import gameEngine.renderEngine.DisplayManager;
import gameEngine.terrains.Terrain;

public class Player extends Entity {

	private static float RUN_SPEED = 20;
	private static final float TURN_SPEED = 120;
	private static final float GRAVITY = -50;
	private static final float JUMP_POWER = 30;

	private static float TERRAIN_HEIGHT = 0;

	private float currentRunSpeed = 0;
	private float currentStrafeSpeed = 0;
	private float currentTurn = 0;
	private float upwardsSpeed = 0;

	private boolean isInAir = false;

	public Player(TexturedModel model, Vector3f position, float dx, float dy, float dz, float scale) {
		super(model, position, dx, dy, dz, scale);
	}
	
	public void turn(){
		currentTurn = -Mouse.getDX() * 0.1f;
	}

	public void move(Terrain[][] terrains) {
		checkInputs();
		turn();
		super.increaseRotation(0, currentTurn, 0);
		float distance = currentRunSpeed * DisplayManager.getFrameTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRoty())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRoty())));
		distance = currentStrafeSpeed * DisplayManager.getFrameTimeSeconds();
		dx += (float) (distance * Math.cos(Math.toRadians(super.getRoty())));
		dz += (float) (distance * -Math.sin(Math.toRadians(super.getRoty())));
		super.increasePosition(dx, upwardsSpeed * DisplayManager.getFrameTimeSeconds(), dz);
		float terrainHeight = terrains[(int) Math.max(0,
				Math.min(MainGameLoop.size - 1, (super.getPosition().x / Terrain.SIZE)))][(int) Math.max(0,
						Math.min(MainGameLoop.size - 1, (super.getPosition().z / Terrain.SIZE)))].getHeightOfTerrain(
								super.getPosition().x, super.getPosition().z);
		if (super.getPosition().y <= terrainHeight) {
			upwardsSpeed = 0;
			isInAir = false;
			super.getPosition().y = terrainHeight;
		} else {
			upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
			isInAir = true;
		}
		
	}

	private void jump() {
		if (!isInAir) {
			this.upwardsSpeed = JUMP_POWER;
			isInAir = true;
		}
	}

	private void checkInputs() {
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			RUN_SPEED = 100;
		} else {
			RUN_SPEED = 20;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			this.currentRunSpeed = RUN_SPEED;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			this.currentRunSpeed = -RUN_SPEED;
		} else {
			this.currentRunSpeed = 0;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			this.currentStrafeSpeed = RUN_SPEED;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			this.currentStrafeSpeed = -RUN_SPEED;
		} else {
			this.currentStrafeSpeed = 0;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			jump();
		}
	}

}
