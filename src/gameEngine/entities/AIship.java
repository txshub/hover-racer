package gameEngine.entities;


import org.joml.Vector3f;

import gameEngine.models.TexturedModel;

public class AIship extends Ship {

	public AIship(TexturedModel model, int textureIndex, Vector3f position, Vector3f rotation,
			float scale) {
		super(model, textureIndex, position, rotation, scale);
	}

	public AIship(TexturedModel model, Vector3f position, Vector3f rotation, float scale) {
		super(model, position, rotation, scale);
	}
	
	public void AImove(){
		// TODO Reece to implement AI
	}

}
