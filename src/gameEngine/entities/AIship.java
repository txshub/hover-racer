package gameEngine.entities;

import org.lwjgl.util.vector.Vector3f;

import gameEngine.models.TexturedModel;

public class AIship extends Ship{

	public AIship(TexturedModel model, int textureIndex, Vector3f position, float rotx, float roty, float rotz,
			float scale) {
		super(model, textureIndex, position, rotx, roty, rotz, scale);
	}

	public AIship(TexturedModel model, Vector3f position, float rotx, float roty, float rotz, float scale) {
		super(model, position, rotx, roty, rotz, scale);
	}
	
	public void AImove(){
		// TODO Reece to implement AI
	}

}
