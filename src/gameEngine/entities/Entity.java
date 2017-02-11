package gameEngine.entities;



import org.joml.Vector3f;

import gameEngine.models.TexturedModel;

public class Entity {

	private TexturedModel model;
	protected Vector3f position;
	protected Vector3f rotation;
	protected float scale;

	private int textureIndex = 0;

	public Entity(TexturedModel model, Vector3f position, Vector3f rotation, float scale) {
	  this(model, 0, position, rotation, scale);
	}

	public Entity(TexturedModel model, int textureIndex, Vector3f position, Vector3f rotation,
			float scale) {
    this.model = model;
		this.textureIndex = textureIndex;
		this.position = position;
    this.rotation = rotation;
		this.scale = scale;
	}
	
	public float getTextureXOffset(){
		int column = textureIndex % model.getTexture().getNumOfRows();
		return (float) column / (float) model.getTexture().getNumOfRows();
	}
	
	public float getTextureYOffset(){
		int row = textureIndex / model.getTexture().getNumOfRows();
		return (float) row / (float) model.getTexture().getNumOfRows();
	}

	public TexturedModel getModel() {
		return model;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
	}

  public Vector3f getPosition() {
    return position;
  }

  public void setPosition(Vector3f position) {
    this.position = position;
  }

  public Vector3f getRotation() {
    return rotation;
  }

  public void setRotation(Vector3f rotation) {
    this.rotation = rotation;
  }

  public float getScale() {
    return scale;
  }

  public void setScale(float scale) {
    this.scale = scale;
  }

}
