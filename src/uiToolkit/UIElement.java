package uiToolkit;

import org.joml.Vector2f;

import gameEngine.renderEngine.Loader;

/**
 * 
 * SuperClass that all UI elements such as Labels and TexturedUIElements should extend.
 * 
 * @author Reece Bennett
 *
 */
public abstract class UIElement {

  protected Vector2f position;
  protected Loader loader;
  protected boolean visible;
  protected UIElement parent;

  public UIElement(Loader loader, Vector2f position) {
    this.loader = loader;
    this.position = position;
    this.visible = true;
  }

  public abstract void update();

  public abstract void render();

  public boolean isVisible() {
    return visible;
  }

  public void setVisibility(boolean visible) {
    this.visible = visible;
  }

  public UIElement getParent() {
    return parent;
  }

  public void setParent(UIElement parent) {
    if (parent instanceof Container) {
      this.parent = parent;
      ((Container) parent).add(this);
      position.add(parent.position);
    } else {
      System.err.println("Cannot set parent, parent must be of class 'Container'!");
    }
  }

}
