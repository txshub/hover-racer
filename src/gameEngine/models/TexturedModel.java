package gameEngine.models;

import gameEngine.textures.ModelTexture;

/**
 * @author rtm592 A model with a texture
 */
public class TexturedModel {

	private RawModel rawModel;
	private ModelTexture texture;

	/**
	 * @param model
	 *            the raw model
	 * @param texture
	 *            the models texture
	 */
	public TexturedModel(RawModel model, ModelTexture texture) {
		this.rawModel = model;
		this.texture = texture;
	}

	/**
	 * @return the model
	 */
	public RawModel getRawModel() {
		return rawModel;
	}

	/**
	 * @return the texture
	 */
	public ModelTexture getTexture() {
		return texture;
	}

}
