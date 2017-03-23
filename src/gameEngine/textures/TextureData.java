package gameEngine.textures;

import java.nio.ByteBuffer;

/**
 * @author rtm592 Holds data about a texture
 */
public class TextureData {

  private int width;
  private int height;
  private ByteBuffer buffer;

  /**
   * @param buffer
   *          the data buffer
   * @param width
   *          the width of the buffer
   * @param height
   *          the height of the buffer
   */
  public TextureData(ByteBuffer buffer, int width, int height) {
    this.buffer = buffer;
    this.width = width;
    this.height = height;
  }

  /**
   * @return the width
   */
  public int getWidth() {
    return width;
  }

  /**
   * @return the height
   */
  public int getHeight() {
    return height;
  }

  /**
   * @return the buffer
   */
  public ByteBuffer getBuffer() {
    return buffer;
  }
}
