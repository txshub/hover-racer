package gameEngine.entities;

import org.lwjgl.util.vector.Vector3f;

import gameEngine.models.TexturedModel;

public class Ship extends Entity{

	public Ship(TexturedModel model, int textureIndex, Vector3f position, float rotx, float roty, float rotz,
			float scale) {
		super(model, textureIndex, position, rotx, roty, rotz, scale);
	}

	public Ship(TexturedModel model, Vector3f position, float rotx, float roty, float rotz, float scale) {
		super(model, position, rotx, roty, rotz, scale);
	}
	
	

}
