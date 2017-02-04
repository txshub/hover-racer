package physics;

import java.util.function.DoubleUnaryOperator;

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
	public float distanceTo(Vector3 v) {
		return (float) (Math.sqrt(Math.pow(x - v.getX(), 2) + Math.pow(y - v.getY(), 2) + Math.pow(z - v.getZ(), 2)));
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

	public void changeX(DoubleUnaryOperator f) {
		this.x = (float) f.applyAsDouble(x);
	}
	public void changeY(DoubleUnaryOperator f) {
		this.y = (float) f.applyAsDouble(y);
	}
	public void changeZ(DoubleUnaryOperator f) {
		this.z = (float) f.applyAsDouble(z);
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