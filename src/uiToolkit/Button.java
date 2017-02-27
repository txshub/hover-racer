package uiToolkit;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import org.joml.Vector2f;
import org.lwjgl.input.Mouse;
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
 * @author Reece Bennett
 *
 */
public class Button {

  private Texture texture;

  private Vector2f position;
  private Rectangle bounds;

  private Loader loader;

  private ArrayList<ActionListener> listeners;
  private boolean pressed;

  public Button(Loader loader, String fileName, Vector2f position) {
    this.loader = loader;
    this.texture = loader.loadTextureTex(fileName);
    this.position = position;

    bounds = new Rectangle();
    bounds.x = (int) position.x;
    bounds.y = (int) position.y;
    bounds.width = texture.getImageWidth();
    bounds.height = texture.getImageHeight();

    listeners = new ArrayList<>();
    pressed = false;
  }

  public void addListener(ActionListener listener) {
    listeners.add(listener);
  }

  public void update() {
    if (bounds.contains(Mouse.getX(), Mouse.getY()) && Mouse.isButtonDown(0) && !pressed) {
      pressed = true;
      for (ActionListener a : listeners) {
        a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "pressed"));
      }
    } else if (!Mouse.isButtonDown(0) && pressed) {
      pressed = false;
      for (ActionListener a : listeners) {
        a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "released"));
      }
    }
  }

  public void render(Vector2f containerPos, Vector2f containerSize) {
    int w = Display.getWidth();
    int h = Display.getHeight();

    // Calculate the position of the quad to draw onto and its texture
    // coordinates
    Vector2f topLeft = new Vector2f((2 * (position.x + containerPos.x)) / (float) w - 1,
        -((2 * (position.y + containerPos.y)) / (float) h - 1));

    Vector2f botRight = new Vector2f(
        (2 * (position.x + containerPos.x + texture.getImageWidth())) / (float) w - 1,
        -((2 * (position.y + containerPos.y + texture.getImageHeight())) / (float) h - 1));

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
  }

}
