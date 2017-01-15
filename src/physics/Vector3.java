package physics;

import java.util.function.DoubleUnaryOperator;

public class Vector3 {

	double x, y, z;


	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public Vector3(double[] values) {
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
	public Vector3 multiply(double scalar) {
		return forEach(a -> a * scalar);
	}
	public Vector3 forEach(DoubleUnaryOperator f) {
		this.x = f.applyAsDouble(x);
		this.y = f.applyAsDouble(y);
		this.z = f.applyAsDouble(z);
		return this;
	}
	public double distanceTo(Vector3 v) {
		return Math.sqrt(Math.pow(x - v.getX(), 2) + Math.pow(y - v.getY(), 2) + Math.pow(z - v.getZ(), 2));
	}

	public double[] asArray() {
		return new double[]{x, y, z};
	}

	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public void setZ(double z) {
		this.z = z;
	}
	public double getZ() {
		return z;
	}

	public void changeX(DoubleUnaryOperator f) {
		this.x = f.applyAsDouble(x);
	}
	public void changeY(DoubleUnaryOperator f) {
		this.y = f.applyAsDouble(y);
	}
	public void changeZ(DoubleUnaryOperator f) {
		this.z = f.applyAsDouble(z);
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
