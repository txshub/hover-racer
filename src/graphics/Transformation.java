package graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transformation {
  
  private final Matrix4f projectionMatrix;
  private final Matrix4f worldMatrix;

  public Transformation() {
    worldMatrix = new Matrix4f();
    projectionMatrix = new Matrix4f();
  }
  
  /**
   * Gets the projection matrix based on FOV, aspect ratio and the Z-near and far.
   * 
   * @param fov
   * @param width width of window
   * @param height height of window
   * @param zNear
   * @param zFar
   * @return the projection matrix
   */
  public Matrix4f getProjectionMatrix(float fov, float aspect, float zNear, float zFar) {
    projectionMatrix.identity();
    projectionMatrix.perspective(fov, aspect, zNear, zFar);
    return projectionMatrix;
  }
  
  /**
   * Gets the world matrix based on the offset, rotation and scale of the entity.
   * The world matrix transforms the entity from local coordinates to the world
   * coordinates so that it can be placed in the scene.
   * 
   * @param offset
   * @param rotation
   * @param scale
   * @return the world matrix
   */
  public Matrix4f getWorldMatrix(Vector3f offset, Vector3f rotation, float scale) {
    worldMatrix.identity()
      .translate(offset)
      .rotateX((float) Math.toRadians(rotation.x))
      .rotateY((float) Math.toRadians(rotation.y))
      .rotateZ((float) Math.toRadians(rotation.z))
      .scale(scale);
    return worldMatrix;
  }

}
