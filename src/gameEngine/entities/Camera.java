package gameEngine.entities;

import java.awt.geom.Point2D;

import org.lwjgl.input.Mouse;
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
	
	public void move(){

//		Mouse.setCursorPosition(Display.getWidth()/2, Display.getHeight()/2);
		calculateZoom();
		calculatePitch();
		calculateAngleAroundEntity();
		
		float horizontalDistance = calculateHorizontalDistance();
		verticalDistance = calculateVerticalDistance();
		calculateCameraPosition(horizontalDistance, verticalDistance);
		
		float angle = (float)Math.toDegrees(getAngle(new Point2D.Double(position.x,position.z), new Point2D.Double(entity.getPosition().x,entity.getPosition().z))+Math.PI/2);
		
		
		this.yaw += ((180 - entity.getRoty()) - this.yaw)/2;
//		yaw = angle;
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
		position.x = entity.getPosition().x - offsetX;
		position.z = entity.getPosition().z - offsetZ;
		position.y = entity.getPosition().y + (entity.getScale()) + verticalDistance;
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
	
	private void calculateAngleAroundEntity(){
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

	/**
	 * get's the angle from east between two points
	 * 
	 * @param p1
	 *            the first point
	 * @param p2
	 *            the second point
	 * @return the angle between the two points
	 */
	public double getAngle(Point2D.Double p1, Point2D.Double p2) {
		if (p1.x != p2.x && p1.y != p2.y) {
			double xdif = (p2.getX() - p1.getX());
			double ydif = (p2.getY() - p1.getY());
			double angle = 0; // in radians
			angle = -Math.atan(ydif / xdif);
			if (xdif < 0) {
				if (ydif < 0) {
					angle += Math.PI;
				} else {
					angle -= Math.PI;
				}
			}
			return -angle;
		} else if (p1.x > p2.x) {
			return Math.PI;
		} else if (p1.x < p2.x) {
			return 0.0;
		} else if (p1.y > p2.y) {
			return -Math.PI / 2.0;
		} else if (p1.y < p2.y) {
			return Math.PI / 2.0;
		}
		return 0.0;
	}
	
}





















