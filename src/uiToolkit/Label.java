package uiToolkit;

import org.joml.Vector2f;
import org.lwjgl.opengl.Display;

import gameEngine.renderEngine.Loader;
import uiToolkit.fontMeshCreator.FontType;
import uiToolkit.fontMeshCreator.GUIText;

/**
 * 
 * A text label.
 * 
 * @author Reece Bennett
 *
 */
public class Label extends UIElement {

  private GUIText text;

  public Label(Loader loader, String text, FontType font, float size, boolean centered,
      Vector2f position, float lineLength) {
    super(loader, position);
    this.text = new GUIText(text, size, font, new Vector2f(toScreenSpace(position)),
        lineLength / Display.getWidth(), centered);
  }

  @Override
  public void update() {
  }

  @Override
  public void render() {
  }

  @Override
  public void setVisibility(boolean visible) {
    super.setVisibility(visible);
    text.setVisibility(visible);
  }

  public void setColour(float r, float g, float b) {
    text.setColour(r, g, b);
  }

  @Override
  public void setParent(UIElement parent) {
    super.setParent(parent);
    text.setPosition(toScreenSpace(position));
  }

  private Vector2f toScreenSpace(Vector2f position) {
    int w = Display.getWidth();
    int h = Display.getHeight();
    return new Vector2f(position.x / (float) w, (position.y / (float) h));
  }

}
