package gameEngine.entities;

import org.joml.Vector3f;

/**
 * @author rtm592
 *
 */
public class Light {
	
	private Vector3f position;
	private Vector3f colour;
	
	private Vector3f attenuation = new Vector3f(1,0,0);
	
	public Light(Vector3f position, Vector3f colour) {
		
		this.colour = colour;
		this.position = position;
		
	}
	
	public Light(Vector3f position, Vector3f colour, Vector3f attenuation) {
		
		this.colour = colour;
		this.position = position;
		this.attenuation = attenuation;
		
	}
	
	public Vector3f getAttenuation() {
		return attenuation;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getColour() {
		return colour;
	}

	public void setColour(Vector3f colour) {
		this.colour = colour;
	}

	public float getdistance(Vector3f currentPosition) {
		float deltaX = currentPosition.x - position.x;
		float deltaY = currentPosition.y - position.y;
		float deltaZ = currentPosition.z - position.z;
		return (float) Math.sqrt((deltaX * deltaX) + (deltaY * deltaY) + (deltaZ * deltaZ));
	}
	
}
