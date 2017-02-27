package uiToolkit;

import java.util.ArrayList;

import org.joml.Vector2f;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;

import gameEngine.models.RawModel;
import gameEngine.renderEngine.Loader;

/**
 * 
 * A UI object that contains other UI objects such as buttons and other
 * containers.
 * 
 * @author Reece Bennett
 *
 */
public class Container {

  private Texture texture;
  private ArrayList<Button> buttons;

  private Vector2f position;

  private Loader loader;

  public Container(Loader loader, String fileName, Vector2f position) {
    this.loader = loader;
    this.texture = loader.loadTextureTex(fileName);
    this.position = position;

    buttons = new ArrayList<>();
  }

  public void add(Button button) {
    buttons.add(button);
  }

  public void update() {
    for (Button b : buttons) {
      b.update();
    }
  }

  public void render() {
    int w = Display.getWidth();
    int h = Display.getHeight();

    // Calculate the position of the quad to draw onto and its texture coordinates
    Vector2f topLeft = new Vector2f((2 * position.x) / (float) w - 1,
        -((2 * position.y) / (float) h - 1));
    Vector2f botRight = new Vector2f((2 * (position.x + texture.getImageWidth())) / (float) w - 1,
        -((2 * (position.y + texture.getImageHeight())) / (float) h - 1));
    Vector2f texSize = new Vector2f((float) texture.getImageWidth() / texture.getTextureWidth(),
        (float) texture.getImageHeight() / texture.getTextureHeight());

    float[] positions = { topLeft.x, topLeft.y, topLeft.x, botRight.y, botRight.x, topLeft.y,
        botRight.x, botRight.y };
    float[] textureCoords = { 0, 0, 0, texSize.y, texSize.x, 0, texSize.x, texSize.y };

    // Load the quad into a VAO
    RawModel quad = loader.loadToVAO(positions, textureCoords);

    // Do render things
    GL30.glBindVertexArray(quad.getVaoID());
    GL20.glEnableVertexAttribArray(0);
    GL20.glEnableVertexAttribArray(1);
    GL13.glActiveTexture(GL13.GL_TEXTURE0);
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());

    GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());

    GL20.glDisableVertexAttribArray(0);
    GL20.glDisableVertexAttribArray(1);
    GL30.glBindVertexArray(0);

    // Render all children of this container
    for (Button b : buttons) {
      b.render(position, new Vector2f(texture.getImageWidth(), texture.getImageHeight()));
    }
  }

}
