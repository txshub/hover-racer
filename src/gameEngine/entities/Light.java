package gameEngine.entities;

import org.joml.Vector3f;

/**
 * @author rtm592 A class to control all lights in the game
 */
public class Light {

  private Vector3f position;
  private Vector3f colour;
  private Vector3f attenuation = new Vector3f(1, 0, 0);

  /**
   * @param position
   *          the lights coordinates
   * @param colour
   *          the rgb colour of the light
   */
  public Light(Vector3f position, Vector3f colour) {
    this.position = position;
    this.colour = colour;
  }

  /**
   * @param position
   *          the lights coordinates
   * @param colour
   *          the rgb colour of the light
   * @param attenuation
   *          the lights rgb dropoff coeficients
   */
  public Light(Vector3f position, Vector3f colour, Vector3f attenuation) {
    this.position = position;
    this.colour = colour;
    this.attenuation = attenuation;
  }

  /**
   * @return the lights attenuation
   */
  public Vector3f getAttenuation() {
    return attenuation;
  }

  /**
   * @return the lights position
   */
  public Vector3f getPosition() {
    return position;
  }

  /**
   * @param position
   *          the lights new position
   */
  public void setPosition(Vector3f position) {
    this.position = position;
  }

  /**
   * @return the lights colour
   */
  public Vector3f getColour() {
    return colour;
  }

  /**
   * @param colour
   *          the lights new colour
   */
  public void setColour(Vector3f colour) {
    this.colour = colour;
  }

  /**
   * @param currentPosition
   *          a point in the world
   * @return the distance to that point
   */
  public float getdistance(Vector3f currentPosition) {
    float deltaX = currentPosition.x - position.x;
    float deltaY = currentPosition.y - position.y;
    float deltaZ = currentPosition.z - position.z;
    return (float) Math.sqrt((deltaX * deltaX) + (deltaY * deltaY) + (deltaZ * deltaZ));
  }

}
