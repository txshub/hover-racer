package gameEngine.toolbox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import gameEngine.entities.Camera;

/**
 * @author rtm592 A maths helper class
 */
public class Maths {

  /**
   * create a tranformation matrix
   * 
   * @param translation
   *          the translation vector
   * @param scale
   *          the scale vector
   * @return the transformation matrix
   */
  public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
    Matrix4f matrix = new Matrix4f();
    matrix.setIdentity();
    Matrix4f.translate(translation, matrix, matrix);
    Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
    return matrix;
  }

  /**
   * create a tranformation matrix
   * 
   * @param translation
   *          the translation vector
   * @param rx
   *          rotation x
   * @param ry
   *          rotation y
   * @param rz
   *          ratation z
   * @param scale
   *          the scale vector
   * @return the transformation matrix
   */
  public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry,
      float rz, float scale) {
    Matrix4f matrix = new Matrix4f();
    matrix.setIdentity();
    Matrix4f.translate(translation, matrix, matrix);
    Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1, 0, 0), matrix, matrix);
    Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0, 1, 0), matrix, matrix);
    Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0, 0, 1), matrix, matrix);
    Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);
    return matrix;
  }

  /**
   * get the barry centric coords of three points
   * 
   * @param p1
   *          point1
   * @param p2
   *          point2
   * @param p3
   *          point3
   * @param pos
   *          the position
   * @return the barry centric value
   */
  public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
    float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
    float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
    float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
    float l3 = 1.0f - l1 - l2;
    return l1 * p1.y + l2 * p2.y + l3 * p3.y;
  }

  /**
   * @param camera
   *          the camera
   * @return the view matrix
   */
  public static Matrix4f createViewMatrix(Camera camera) {
    Matrix4f viewMatrix = new Matrix4f();
    viewMatrix.setIdentity();
    Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0), viewMatrix,
        viewMatrix);
    Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0), viewMatrix,
        viewMatrix);
    Vector3f cameraPos = camera.getPosition();
    Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
    Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
    return viewMatrix;
  }

  /**
   * @param vec
   *          the vector to translate by
   * @return a transformation matrix
   */
  public static org.joml.Matrix4f createTransformMatrix(org.joml.Vector3f vec) {
    org.joml.Matrix4f viewMatrix = new org.joml.Matrix4f();
    viewMatrix.identity();
    org.joml.Vector3f negativeCameraPos = new org.joml.Vector3f(-vec.x, -vec.y, -vec.z);
    viewMatrix.translate(negativeCameraPos);
    return viewMatrix;
  }
}
