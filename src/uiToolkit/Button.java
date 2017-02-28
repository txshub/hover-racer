package uiToolkit;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import org.joml.Vector2f;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import gameEngine.renderEngine.Loader;

/**
 * 
 * A TexturedUIElement that can perform an action when clicked. This is set by adding
 * ActionListener to the button.
 * 
 * @author Reece Bennett
 *
 */
public class Button extends TexturedUIElement {

  private Rectangle bounds;

  private ArrayList<ActionListener> listeners;
  private boolean pressed;

  public Button(Loader loader, String fileName, Vector2f position) {
    super(loader, fileName, position);

    bounds = new Rectangle();
    updateBounds();

    listeners = new ArrayList<>();
    pressed = false;
  }

  public void addListener(ActionListener listener) {
    listeners.add(listener);
  }
  
  @Override
  public void setParent(UIElement parent) {
    super.setParent(parent);
    
    updateBounds();
  }

  @Override
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
  
  private void updateBounds() {
    bounds.width = texture.getImageWidth();
    bounds.height = texture.getImageHeight();
    bounds.x = (int) position.x;
    bounds.y = Display.getHeight() - bounds.height - (int) position.y;
  }

}
