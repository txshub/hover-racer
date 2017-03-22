package uiToolkit;

import org.joml.Vector2f;
import org.joml.Vector3f;
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

  private String content;
  private FontType font;
  private float size;
  private boolean centered;
  private Vector2f position;
  private float lineLength;
  private Vector3f colour;

  public Label(Loader loader, String text, FontType font, float size, boolean centered,
      Vector2f position, float lineLength) {
    super(loader, position);

    this.content = text;
    this.font = font;
    this.size = size;
    this.centered = centered;
    this.position = position;
    this.lineLength = lineLength;

    createText();
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
    setColour(new Vector3f(r, g, b));
  }

  public void setColour(Vector3f colour) {
    this.colour = colour;
    text.setColour(colour.x, colour.y, colour.z);
  }

  @Override
  public void setParent(UIElement parent) {
    super.setParent(parent);
    text.setPosition(toScreenSpace(position));
  }

  public void setText(String text) {
    if (!text.equals(content)) {
      content = text;
      createText();
    }
  }

  private Vector2f toScreenSpace(Vector2f position) {
    int w = Display.getWidth();
    int h = Display.getHeight();
    return new Vector2f(position.x / (float) w, (position.y / (float) h));
  }

  private void createText() {
    if (text != null)
      text.remove();
    text = new GUIText(content, size, font, new Vector2f(toScreenSpace(position)),
        lineLength / Display.getWidth(), centered);
    if (colour != null)
      setColour(colour);
  }

}
