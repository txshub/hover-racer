package gameEngine.entities;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

/**
 * @author rtm592 The class that controls the camera This is used as the games viewport
 */
public class Camera {

  private float distanceFromPlayer = 50;
  private float angleAroundPlayer = 0;
  private long aagTimer = System.currentTimeMillis(), pTimer = System.currentTimeMillis();
  private int defaultPitch = 10, defaultangle = 0;
  private float offsetX, verticalDistance, offsetZ;

  private Vector3f position = new Vector3f(0, 0, 0);
  private float pitch = 20, yaw = 180, roll = 0;

  private Entity entity;

  public Camera(Entity entity) {
    this.entity = entity;
  }

  /**
   * @return the entity that the camera is following
   */
  public Entity getEntity() {
    return entity;
  }

  /**
   * @return the offset between the entity and the camera
   */
  public Vector3f getOffset() {
    return new Vector3f(offsetX, verticalDistance, offsetZ);
  }

  /**
   * moves the camera based on the entities movement
   */
  public void move() {

    calculateZoom();
    calculatePitch();
    calculateAngleAroundEntity();

    float horizontalDistance = calculateHorizontalDistance();
    verticalDistance = calculateVerticalDistance();
    calculateCameraPosition(horizontalDistance, verticalDistance);

    this.yaw = (180 - entity.getRotation().y) - angleAroundPlayer;
    if (angleAroundPlayer >= 180) {
      angleAroundPlayer -= 360;
    } else if (angleAroundPlayer <= -180) {
      angleAroundPlayer += 360;
    }

  }

  /**
   * @return the position of the camera
   */
  public Vector3f getPosition() {
    return position;
  }

  /**
   * @return the pitch of the camera
   */
  public float getPitch() {
    return pitch;
  }

  /**
   * inverts the cameras pitch
   */
  public void invertPitch() {
    pitch = -pitch;
  }

  /**
   * @return the cameras yaw
   */
  public float getYaw() {
    return yaw;
  }

  /**
   * @return the cameras roll
   */
  public float getRoll() {
    return roll;
  }

  /**
   * inverts the cameras roll
   */
  public void invertRoll() {
    roll = -roll;
  }

  /**
   * calculate the cameras coordinates
   * 
   * @param horizontalDistance
   *          horizontal distance to the entity
   * @param verticalDistance
   *          vertical distance to the entity
   */
  private void calculateCameraPosition(float horizontalDistance, float verticalDistance) {
    float theta = entity.getRotation().y + angleAroundPlayer;
    offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
    offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));
    position.x = entity.getPosition().x - offsetX;
    position.z = entity.getPosition().z - offsetZ;
    position.y = entity.getPosition().y + (entity.getScale()) + verticalDistance;
  }

  /**
   * @return the cameras horizontal distance
   */
  private float calculateHorizontalDistance() {
    return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
  }

  /**
   * @return the cameras vertical distance
   */
  private float calculateVerticalDistance() {
    return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
  }

  /**
   * calculates the cameras zoom
   */
  private void calculateZoom() {
    float zoomLevel = Mouse.getDWheel() * 0.1f;
    distanceFromPlayer -= zoomLevel;
    if (distanceFromPlayer < 25) {
      distanceFromPlayer = 25;
    } else if (distanceFromPlayer > 1000) {
      distanceFromPlayer = 1000;
    }
    // distanceFromPlayer = 0; // for FPS games
  }

  /**
   * calculates the cameras pitch
   */
  private void calculatePitch() {
    if (Mouse.isButtonDown(1)) {
      float pitchChange = Mouse.getDY() * 0.1f;
      pitch -= pitchChange;
      if (pitch >= 90) {
        pitch = 90;
      } else if (pitch <= -90) {
        pitch = -90;
      }
      pTimer = System.currentTimeMillis();
    } else if (System.currentTimeMillis() >= pTimer + 1000) {
      pitch -= defaultPitch;
      pitch -= pitch / 10;
      pitch += defaultPitch;
    }
  }

  /**
   * calculate the cameras angle around the entity
   */
  private void calculateAngleAroundEntity() {
    if (Mouse.isButtonDown(1)) {
      float angleChange = Mouse.getDX() * 0.3f;
      angleAroundPlayer -= angleChange;
      // if(angleAroundPlayer >= 90){
      // angleAroundPlayer = 90;
      // }else if(angleAroundPlayer <= -90){
      // angleAroundPlayer = -90;
      // }
      aagTimer = System.currentTimeMillis();
    } else if (System.currentTimeMillis() >= aagTimer + 1000) {
      angleAroundPlayer -= defaultangle;
      angleAroundPlayer -= angleAroundPlayer / 10;
      angleAroundPlayer += defaultangle;
    }
  }

}
