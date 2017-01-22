package engine.graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import engine.camera.Camera;
import engine.entity.Entity;

public class Transformation {

  private final Matrix4f modelViewMatrix;
  private final Matrix4f projectionMatrix;
  private final Matrix4f viewMatrix;

  public Transformation() {
    projectionMatrix = new Matrix4f();
    viewMatrix = new Matrix4f();
    modelViewMatrix = new Matrix4f();
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
//  public Matrix4f getWorldMatrix(Vector3f offset, Vector3f rotation, float scale) {
//    worldMatrix.identity()
//      .translate(offset)
//      .rotateX((float) Math.toRadians(rotation.x))
//      .rotateY((float) Math.toRadians(rotation.y))
//      .rotateZ((float) Math.toRadians(rotation.z))
//      .scale(scale);
//    return worldMatrix;
//  }
  
  /**
   * Returns a model view matrix from an entity and view matrix
   * 
   * @param entity
   * @param viewMatrix
   * @return the model view matrix
   */
  public Matrix4f getModelViewMatrix(Entity entity, Matrix4f viewMatrix) {
    Vector3f rotation = entity.getRotation();
    modelViewMatrix.identity()
      .translate(entity.getPosition())
      .rotateX((float) Math.toRadians(-rotation.x))
      .rotateY((float) Math.toRadians(-rotation.y))
      .rotateZ((float) Math.toRadians(-rotation.z))
      .scale(entity.getScale());
    
    // Multiply the model view by the camera view to get the world coordinates
    // of the entity
    Matrix4f currentView = new Matrix4f(viewMatrix);
    return currentView.mul(modelViewMatrix);
  }
  
  /**
   * Returns a view matrix from a Camera object
   * 
   * @param camera
   * @return the view matrix
   */
  public Matrix4f getViewMatrix(Camera camera) {
    Vector3f cameraPos = camera.getPosition();
    Vector3f rotation = camera.getRotation();
    
    viewMatrix.identity()
    // First do rotations
      .rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0))
      .rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0));
    // Then do translation
    viewMatrix.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
    return viewMatrix;
  }

}
