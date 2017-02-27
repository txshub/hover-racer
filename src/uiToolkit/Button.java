package uiToolkit;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import org.joml.Vector2f;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import gameEngine.guis.GuiTexture;

public class Button {
  
  private GuiTexture guiTexture;
  private Rectangle bounds;
  
  private ArrayList<ActionListener> listeners;
  private boolean pressed;
  
  public Button(GuiTexture guiTexture) {
    this.guiTexture = guiTexture;
    bounds = new Rectangle();
    Vector2f pos = guiTexture.getPosition();
    Vector2f scale = guiTexture.getScale();
    bounds.x = (int) ((Display.getWidth() / 2) * (1 + pos.x) - (Display.getWidth() * scale.x * 0.5));
    bounds.y = (int) ((Display.getHeight() / 2) * (1 - pos.y) - (Display.getHeight() * scale.y * 0.5));
    bounds.width = (int) (Display.getWidth() * scale.x);
    bounds.height = (int) (Display.getHeight() * scale.y);
    
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
    // TODO Put button rendering code here, account for container position
  }

}
