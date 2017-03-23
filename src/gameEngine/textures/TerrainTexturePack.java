package gameEngine.textures;

/**
 * @author rtm592 Holds a terrains texture pack
 */
public class TerrainTexturePack {

  private TerrainTexture backgroundTexture;
  private TerrainTexture rTexture;
  private TerrainTexture gTexture;
  private TerrainTexture bTexture;

  /**
   * @param backgroundTexture
   *          the background texture
   * @param rTexture
   *          the red value texture
   * @param gTexture
   *          the green value texture
   * @param bTexture
   *          the blue value texture
   */
  public TerrainTexturePack(TerrainTexture backgroundTexture, TerrainTexture rTexture,
      TerrainTexture gTexture, TerrainTexture bTexture) {
    this.backgroundTexture = backgroundTexture;
    this.rTexture = rTexture;
    this.gTexture = gTexture;
    this.bTexture = bTexture;
  }

  /**
   * @return the background texture
   */
  public TerrainTexture getBackgroundTexture() {
    return backgroundTexture;
  }

  /**
   * @return the red value texture
   */
  public TerrainTexture getrTexture() {
    return rTexture;
  }

  /**
   * @return the green value texture
   */
  public TerrainTexture getgTexture() {
    return gTexture;
  }

  /**
   * @return the blue value texture
   */
  public TerrainTexture getbTexture() {
    return bTexture;
  }

}
