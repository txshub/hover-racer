package uiToolkit;

import java.awt.Font;

import org.joml.Vector2f;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

import gameEngine.renderEngine.Loader;

public class Label extends UIElement {

  private TrueTypeFont font;

  public Label(Loader loader, Vector2f position) {
    super(loader, position);

    // Load a default Java font
    Font awtFont = new Font("Times New Roman", Font.BOLD, 24);
    font = new TrueTypeFont(awtFont, false);
  }

  @Override
  public void update() {
  }

  @Override
  public void render() {
    font.drawString(100, 50, "THE LIGHTWEIGHT JAVA GAMES LIBRARY", Color.yellow);
  }

}
