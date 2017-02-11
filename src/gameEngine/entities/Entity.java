package gameEngine.entities;



import org.lwjgl.util.vector.Vector3f;

import gameEngine.models.TexturedModel;

public class Entity {

	private TexturedModel model;
	private Vector3f position;
	private float rotx, roty, rotz;
	private float scale;

	private int textureIndex = 0;

	public Entity(TexturedModel model, Vector3f position, float rotx, float roty, float rotz, float scale) {

		this.position = position;
		this.model = model;
		this.rotx += rotx;
		this.roty += roty;
		this.rotz += rotz;
		this.scale = scale;

	}

	public Entity(TexturedModel model, int textureIndex, Vector3f position, float rotx, float roty, float rotz,
			float scale) {

		this.textureIndex = textureIndex;
		this.position = position;
		this.model = model;
		this.rotx += rotx;
		this.roty += roty;
		this.rotz += rotz;
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

	public void increasePosition(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}

	public void increaseRotation(float rotx, float roty, float rotz) {
		this.rotx += rotx;
		this.roty += roty;
		this.rotz += rotz;
	}

	public TexturedModel getModel() {
		return model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getRotx() {
		return rotx;
	}

	public float getRoty() {
		return roty;
	}

	public float getRotz() {
		return rotz;
	}

	public float getScale() {
		return scale;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public void setRotx(float rotx) {
		this.rotx = rotx;
	}

	public void setRoty(float roty) {
		this.roty = roty;
	}

	public void setRotz(float rotz) {
		this.rotz = rotz;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

}
