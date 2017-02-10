package gameEngine.guis;

import org.joml.Vector2f;

public class GuiTexture {

	private int texture;
	private Vector2f position;
	private Vector2f scale;

	public GuiTexture(int texture, Vector2f position, Vector2f scale) {
		this.texture = texture;
		this.scale = scale;
		this.position = position;
	}

	public int getTexture() {
		return texture;
	}

	public Vector2f getScale() {
		return scale;
	}

	public Vector2f getPosition() {
		return position;
	}

}
