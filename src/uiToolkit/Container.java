package uiToolkit;

import java.util.ArrayList;

import org.joml.Vector2f;

import gameEngine.renderEngine.Loader;

/**
 * 
 * A UIElement that can contain other UIElement such as buttons and other
 * containers.
 * 
 * @author Reece Bennett
 *
 */
public class Container extends UIElement {

  private ArrayList<UIElement> children;

  public Container(Loader loader, String fileName, Vector2f position) {
    super(loader, fileName, position);

    children = new ArrayList<>();
  }

  protected void add(UIElement child) {
    children.add(child);
  }
  
  @Override
  public void setVisibility(boolean visible) {
    super.setVisibility(visible);
    for (UIElement e : children) {
      e.setVisibility(visible);
    }
  }

  @Override
  public void update() {
    for (UIElement e : children) {
      e.update();
    }
  }

  @Override
  public void render() {
    super.render();

    // Render all children of this container
    for (UIElement e : children) {
      e.render();
    }
  }

}
