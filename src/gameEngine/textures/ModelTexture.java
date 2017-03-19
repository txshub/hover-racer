package gameEngine.textures;

/**
 * @author rtm592 A models texture
 */
public class ModelTexture {

	private int textureID;
	private int normalMap;

	private float shineDamper = 1;
	private float reflectivity = 0;

	private boolean hasTransparency = false;
	private boolean useFakeLighting = false;

	private int numberOfRows = 1;

	/**
	 * @param texture
	 *            the texture id
	 */
	public ModelTexture(int texture) {
		this.textureID = texture;
	}

	/**
	 * @return the number of rows on the texture
	 */
	public int getNumberOfRows() {
		return numberOfRows;
	}

	/**
	 * @return the normal map id
	 */
	public int getNormalMap() {
		return normalMap;
	}

	/**
	 * @param normalMap
	 *            the id of a normal map
	 */
	public void setNormalMap(int normalMap) {
		this.normalMap = normalMap;
	}

	/**
	 * @param numberOfRows
	 *            the number of rows on the texture
	 */
	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}

	/**
	 * @return if the texture has transparency
	 */
	public boolean isHasTransparency() {
		return hasTransparency;
	}

	/**
	 * @return whether to use fake lighting
	 */
	public boolean isUseFakeLighting() {
		return useFakeLighting;
	}

	/**
	 * @param useFakeLighting
	 *            whether to use fake lighting
	 */
	public void setUseFakeLighting(boolean useFakeLighting) {
		this.useFakeLighting = useFakeLighting;
	}

	/**
	 * @param hasTransparency
	 *            if the texture has transparency
	 */
	public void setHasTransparency(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
	}

	/**
	 * @return the texture id
	 */
	public int getID() {
		return textureID;
	}

	/**
	 * @return the textures shine damper
	 */
	public float getShineDamper() {
		return shineDamper;
	}

	/**
	 * @param shineDamper
	 *            the textures shine damper
	 */
	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	/**
	 * @return the textures reflectivity
	 */
	public float getReflectivity() {
		return reflectivity;
	}

	/**
	 * @param reflectivitythe
	 *            textures reflectivity
	 */
	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}
}
