package gameEngine.entities;

import org.joml.Vector3f;

import gameEngine.models.TexturedModel;

/**
 * @author rtm592
 * @author Reece Bennett A generic class that holds a model that gets draw in the world
 */
public class Entity {

  private TexturedModel model;
  protected Vector3f position;
  protected Vector3f rotation;
  protected float scale;

  private int textureIndex = 0;

  /**
   * @param model
   *          the model
   * @param position
   *          the models position in the world
   * @param rotation
   *          the models rotation in the world
   * @param scale
   *          the models scale
   */
  public Entity(TexturedModel model, Vector3f position, Vector3f rotation, float scale) {
    this(model, 0, position, rotation, scale);
  }

  /**
   * @param model
   *          the model
   * @param textureIndex
   *          the models texture index
   * @param position
   *          the models position in the world
   * @param rotation
   *          the models rotation in the world
   * @param scale
   *          the models scale
   */
  public Entity(TexturedModel model, int textureIndex, Vector3f position, Vector3f rotation,
      float scale) {
    this.model = model;
    this.textureIndex = textureIndex;
    this.position = position;
    this.rotation = rotation;
    this.scale = scale;
  }

  /**
   * @return the texture X offset if there is one
   */
  public float getTextureXOffset() {
    int column = textureIndex % model.getTexture().getNumberOfRows();
    return (float) column / (float) model.getTexture().getNumberOfRows();
  }

  /**
   * @return the texture Y offset if there is one
   */
  public float getTextureYOffset() {
    int row = textureIndex / model.getTexture().getNumberOfRows();
    return (float) row / (float) model.getTexture().getNumberOfRows();
  }

  /**
   * @return the textured model
   */
  public TexturedModel getModel() {
    return model;
  }

  /**
   * if you want to change the model
   * 
   * @param model
   *          the model to change to
   */
  public void setModel(TexturedModel model) {
    this.model = model;
  }

  /**
   * @return the models position in the world
   */
  public Vector3f getPosition() {
    return new Vector3f(position);
  }

  /**
   * @param position
   *          the models new position
   */
  public void setPosition(Vector3f position) {
    this.position = position;
  }

  /**
   * move the model
   * 
   * @param dPosition
   *          the amount to change the position by
   */
  public void changePosition(Vector3f dPosition) {
    position.add(dPosition);
  }

  /**
   * @return the models rotation in the world
   */
  public Vector3f getRotation() {
    return new Vector3f(rotation);
  }

  /**
   * @param rotation
   *          the models new rotation
   */
  public void setRotation(Vector3f rotation) {
    this.rotation = rotation;
  }

  /**
   * @param dRotation
   *          the amount by which to rotate the model
   */
  public void changeRotation(Vector3f dRotation) {
    rotation.add(dRotation);
  }

  /**
   * @return the models scale
   */
  public float getScale() {
    return scale;
  }

  /**
   * @param scale
   *          the models new scale
   */
  public void setScale(float scale) {
    this.scale = scale;
  }

}
