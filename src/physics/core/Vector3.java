package physics.core;

import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

import org.joml.Matrix3f;
import org.joml.Vector3f;

/**
 * Utility class representing a 3-dimensional vector. Custom built for the Ship
 * class - use {@link as3f} to convert to Vector3f
 * 
 * @author Maciej Bogacki
 */
public class Vector3 extends Vector3f {

  // float x, y, z;

  public Vector3(float x, float y, float z) {
    super(x, y, z);
  }

  public Vector3(float[] values) {
    super(values[0], values[1], values[2]);
  }

  public Vector3(Vector3f v) {
    super(v);
  }

  public Vector3 add(Vector3f v) {
    super.add(v);
    return this;
  }

  public Vector3 substract(Vector3f v) {
    add(v.mul(-1));
    return this;
  }

  public Vector3 multiply(float scalar) {
    return forEach(a -> a * scalar);
  }

  public Vector3 multiply(double scalar) {
    return forEach(a -> (float) (a * scalar));
  }

  public Vector3 forEach(DoubleUnaryOperator f) {
    changeX(f);
    changeY(f);
    changeZ(f);
    return this;
  }

  public Vector3 forEach(Vector3f v, DoubleBinaryOperator f) {
    changeX(x -> f.applyAsDouble(x, v.x));
    changeY(y -> f.applyAsDouble(y, v.y));
    changeZ(z -> f.applyAsDouble(z, v.z));
    return this;
  }

  public float distanceTo(Vector3f v) {
    return (float) (Math.sqrt(Math.pow(x - v.x, 2) + Math.pow(y - v.y, 2) + Math.pow(z - v.z, 2)));
  }

  public void bounceOff(ImVector2f wall, float elasticity) {
    // System.out.println("Bounced of " + wall + " at " + System.nanoTime());

    ImVector2f normal = new ImVector2f(wall.getY(), -wall.getX());
    ImVector2f current = new ImVector2f(this);

    float cosTheta = current.dot(wall) / (current.length() * wall.length());
    if (cosTheta < 0)
      cosTheta *= -1;
    if (cosTheta < 0 || cosTheta > 1)
      return;

    float theta = (float) Math.acos(cosTheta);
    float nLength = (float) (current.length() * Math.sin(theta));

    ImVector2f scaledNormal = normal.div(normal.length()).mul(nLength);
    ImVector2f res = current.add(scaledNormal.mul(2));

    if (current.rotateTo(wall).getY() > 0) {
      x = res.getX();
      z = res.getY();
      forEach(v -> v * elasticity);
    }
  }

  public Vector3f getDownDirection() {
    // return new Vector3f(0,-1,0).mul(getRotationMatrix(1,
    // x)).mul(getRotationMatrix(2, y)).mul(getRotationMatrix(3, z));
    return new Vector3f(0, -1, 0);// TODO - support own rotation
    // TODO:
    // http://stackoverflow.com/questions/2936459/euler-rotation-of-direction-vector
  }

  private Matrix3f getRotationMatrix(int dimension, float angle) {
    float cos = (float) Math.cos(angle);
    float sin = (float) Math.sin(angle);
    if (dimension == 1)
      return new Matrix3f(1, 0, 0, 0, cos, sin, 0, -sin, cos);
    else if (dimension == 2)
      return new Matrix3f(cos, 0, -sin, 0, 1, 0, sin, 0, cos);
    else
      return new Matrix3f(cos, sin, 0, -sin, cos, 0, 0, 0, 1);
  }

  public float[] asArray() {
    return new float[] { x, y, z };
  }

  public float getX() {
    return x;
  }

  public void setX(float x) {
    this.x = x;
  }

  public float getY() {
    return y;
  }

  public void setY(float y) {
    this.y = y;
  }

  public void setZ(float z) {
    this.z = z;
  }

  public float getZ() {
    return z;
  }

  public void changeX(DoubleUnaryOperator f) {
    this.x = (float) f.applyAsDouble(x);
  }

  public void changeY(DoubleUnaryOperator f) {
    this.y = (float) f.applyAsDouble(y);
  }

  public void changeZ(DoubleUnaryOperator f) {
    this.z = (float) f.applyAsDouble(z);
  }

  public Vector3f as3f() {
    return new Vector3f(x, y, z);
  }

  public Vector3 copy() {
    return new Vector3(this);
  }

  @Override
  protected Object clone() throws CloneNotSupportedException {
    return copy();
  }

  @Override
  public boolean equals(Object o) {
    try {
      Vector3 v = (Vector3) o;
      return v.getX() == x && v.getY() == y && v.getZ() == z;
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public String toString() {
    return "[" + x + ", " + y + ", " + z + "]";
  }

}
