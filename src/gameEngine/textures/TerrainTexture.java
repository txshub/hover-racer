package gameEngine.textures;

/**
 * @author rtm592 Hold a terrains texture
 */
public class TerrainTexture {

  private int textureID;

  /**
   * @param textureID
   *          the texture id
   */
  public TerrainTexture(int textureID) {
    this.textureID = textureID;
  }

  /**
   * @return the texture id
   */
  public int getTextureID() {
    return textureID;
  }

}
