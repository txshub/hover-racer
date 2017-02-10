package gameEngine.entities;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import gameEngine.terrains.Terrain;

public class Camera {

	private float distanceFromPlayer = 100;
	private float angleAroundPlayer = 0;
	private long aagTimer = System.currentTimeMillis(), pTimer = System.currentTimeMillis();
	private int defaultPitch = 20, defaultangle = 0;
	private float offsetX, verticalDistance, offsetZ;
	
	private Vector3f position = new Vector3f(0,0,0);
	private float pitch = 20, yaw = 180, roll = 0;
	
	private Entity entity;
	
	public Camera(Entity entity){
		this.entity = entity;
//		Mouse.setGrabbed(true);
	}
	
	public Entity getEntity(){
		return entity;
	}
	
	public Vector3f getOffset(){
		return new Vector3f(offsetX, verticalDistance, offsetZ);
	}
	
	public void move(Terrain[][] terrains){

//		Mouse.setCursorPosition(Display.getWidth()/2, Display.getHeight()/2);
//		player.move(terrains);
		calculateZoom();
		calculatePitch();
		calculateAngleAroundPlayer();
		float horizontalDistance = calculateHorizontalDistance();
		verticalDistance = calculateVerticalDistance();
		calculateCameraPosition(horizontalDistance, verticalDistance);
		this.yaw = 180 - (entity.getRoty() + angleAroundPlayer);
		if(angleAroundPlayer >= 180){
			angleAroundPlayer -= 360;
		}else if(angleAroundPlayer <= -180){
			angleAroundPlayer += 360;
		}

	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}
	
	public void invertPitch() {
		pitch = -pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
	
	public void invertRoll() {
		roll = -roll;
	}
	
	private void calculateCameraPosition(float horizontalDistance, float verticalDistance){
		float theta = entity.getRoty() + angleAroundPlayer;
		offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
		offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));
		position.x = player.getPosition().x - offsetX;
		position.z = player.getPosition().z - offsetZ;
		position.y = player.getPosition().y + (player.getScale()) + verticalDistance;
	}
	
	private float calculateHorizontalDistance(){
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}
	
	private float calculateVerticalDistance(){
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}
	
	private void calculateZoom(){
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		distanceFromPlayer -= zoomLevel;
		if(distanceFromPlayer < 25){
			distanceFromPlayer = 25;
		}else if(distanceFromPlayer > 1000){
			distanceFromPlayer = 1000;
		}
//		distanceFromPlayer = 0; // for FPS games
	}
	
	private void calculatePitch(){
		if(Mouse.isButtonDown(1)){
			float pitchChange = Mouse.getDY() * 0.1f;
			pitch -= pitchChange;
			if(pitch >= 90){
				pitch = 90;
			}else if(pitch <= -90){
				pitch = -90;
			}
			pTimer = System.currentTimeMillis();
		}else if(System.currentTimeMillis() >= pTimer + 1000){
			pitch -= defaultPitch;
			pitch -= pitch/10;
			pitch += defaultPitch;
		}
	}
	
	private void calculateAngleAroundPlayer(){
		if(Mouse.isButtonDown(1)){
			float angleChange = Mouse.getDX() * 0.3f;
			angleAroundPlayer -= angleChange;
//			if(angleAroundPlayer >= 90){
//				angleAroundPlayer = 90;
//			}else if(angleAroundPlayer <= -90){
//				angleAroundPlayer = -90;
//			}
			aagTimer = System.currentTimeMillis();
		}else if(System.currentTimeMillis() >= aagTimer + 1000){
			angleAroundPlayer -= defaultangle;
			angleAroundPlayer -= angleAroundPlayer/10;
			angleAroundPlayer += defaultangle;
		}
	}
	
}





















