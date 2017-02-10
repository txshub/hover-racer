package physics;

import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

import org.joml.Matrix3f;
import org.lwjgl.util.vector.Vector3f;

/** Utility class representing a 3-dimensional vector. Custom built for the Ship class - use {@link as3f} to convert to Vector3f
 * 
 * @author Maciej Bogacki */
public class Vector3 {

	float x, y, z;


	public Vector3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public Vector3(float[] values) {
		this.x = values[0];
		this.y = values[1];
		this.z = values[2];
	}
	public Vector3(Vector3 v) {
		this.x = v.getX();
		this.y = v.getY();
		this.z = v.getZ();
	}


	public Vector3 add(Vector3 v) {
		this.x += v.getX();
		this.y += v.getY();
		this.z += v.getZ();
		return this;
	}
	public Vector3 substract(Vector3 v) {
		return add(v.multiply(-1));
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
	public Vector3 forEach(Vector3 v, DoubleBinaryOperator f) {
		changeX(x -> f.applyAsDouble(x, v.getX()));
		changeY(y -> f.applyAsDouble(y, v.getY()));
		changeZ(z -> f.applyAsDouble(z, v.getZ()));
		return this;
	}
	public float distanceTo(Vector3 v) {
		return (float) (Math.sqrt(Math.pow(x - v.getX(), 2) + Math.pow(y - v.getY(), 2) + Math.pow(z - v.getZ(), 2)));
	}
	public Vector3f getDownDirection() {
		// return new Vector3f(0,-1,0).mul(getRotationMatrix(1, x)).mul(getRotationMatrix(2, y)).mul(getRotationMatrix(3, z));
		return new Vector3f(0, -1, 0);// TODO - support own rotation
		// TODO: http://stackoverflow.com/questions/2936459/euler-rotation-of-direction-vector
	}
	private Matrix3f getRotationMatrix(int dimension, float angle) {
		float cos = (float) Math.cos(angle);
		float sin = (float) Math.sin(angle);
		if (dimension == 1) return new Matrix3f(1, 0, 0, 0, cos, sin, 0, -sin, cos);
		else if (dimension == 2) return new Matrix3f(cos, 0, -sin, 0, 1, 0, sin, 0, cos);
		else return new Matrix3f(cos, sin, 0, -sin, cos, 0, 0, 0, 1);
	}

	public float[] asArray() {
		return new float[]{x, y, z};
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

	public Vector3 changeX(DoubleUnaryOperator f) {
		this.x = (float) f.applyAsDouble(x);
		return this;
	}
	public Vector3 changeY(DoubleUnaryOperator f) {
		this.y = (float) f.applyAsDouble(y);
		return this;
	}
	public Vector3 changeZ(DoubleUnaryOperator f) {
		this.z = (float) f.applyAsDouble(z);
		return this;
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
		return "Vector [" + x + ", " + y + ", " + z + "]";
	}

}
