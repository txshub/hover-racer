package physics.core;

import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

import org.joml.Vector3f;

/** Utility class representing a 3-dimensional vector. Custom built for the Ship
 * class. Supports various functional operations, such as applying a lambda to all components at once.
 * 
 * @author Maciej Bogacki */
public class Vector3 extends Vector3f {

	public Vector3(float x, float y, float z) {
		super(x, y, z);
	}

	public Vector3(float[] values) {
		super(values[0], values[1], values[2]);
	}

	public Vector3(Vector3f v) {
		super(v);
	}

	/** Adds another vector to this one. */
	public Vector3 add(Vector3f v) {
		super.add(v);
		return this;
	}

	/** Subtracts another vector from this one */
	public Vector3 substract(Vector3f v) {
		super.sub(v);
		return this;
	}

	/** Multiplies each component of this vector by a scalar */
	public Vector3 multiply(float scalar) {
		return forEach(a -> a * scalar);
	}

	/** Multiplies each component of this vector by a scalar */
	public Vector3 multiply(double scalar) {
		return forEach(a -> (float) (a * scalar));
	}

	/** Applies a function to each component of this vector */
	public Vector3 forEach(DoubleUnaryOperator f) {
		changeX(f);
		changeY(f);
		changeZ(f);
		return this;
	}

	/** For each component, applies the supplied function to both it and the corresponding component in supplied vector, and stores the
	 * result in the components of this vector. For example forEach(new Vector3f(4,6,7),(a,b)->a+b) would add 4 to x, 6 to y and 7 to z. */
	public Vector3 forEach(Vector3f v, DoubleBinaryOperator f) {
		changeX(x -> f.applyAsDouble(x, v.x));
		changeY(y -> f.applyAsDouble(y, v.y));
		changeZ(z -> f.applyAsDouble(z, v.z));
		return this;
	}

	/** Return the euclidean distance to target vector */
	public float distanceTo(Vector3f v) {
		return (float) (Math.sqrt(Math.pow(x - v.x, 2) + Math.pow(y - v.y, 2) + Math.pow(z - v.z, 2)));
	}

	/** Bounces, or reflects, this vector over a supplied 2d vector, which represents a vertical wall. The reflection preserves then
	 * magnitude of the vector, and then multiplies each component by elasticity. Used for collisions with track edges. The wall is assumed
	 * to be infinite - call this after collision was already detected.
	 * 
	 * @param wall A 2d direction vector to construct the 3d vertical wall from.
	 * @param elasticity Value to multiply each component by after the reflection */
	public void bounceOff(ImVector2f wall, float elasticity) {
		// System.out.println("Bounced of " + wall + " at " + System.nanoTime());

		ImVector2f normal = new ImVector2f(wall.getY(), -wall.getX());
		ImVector2f current = new ImVector2f(this);

		float cosTheta = current.dot(wall) / (current.length() * wall.length());
		if (cosTheta < 0) cosTheta *= -1;
		if (cosTheta < 0 || cosTheta > 1) return;

		float theta = (float) Math.acos(cosTheta);
		float nLength = (float) (current.length() * Math.sin(theta));

		ImVector2f scaledNormal = normal.div(normal.length()).mul(nLength);
		ImVector2f res = current.add(scaledNormal.mul(2));

		if (current.rotateTo(wall).getY() > 0) {
			//x = res.getX();
			//z = res.getY();
			x += res.getX() * 2;
			z += res.getY() * 2;
			forEach(v -> v * elasticity);
		}
	}

	/** @return This vectors components in an array */
	public float[] asArray() {
		return new float[]{x, y, z};
	}

	/** @return This vector's x component */
	public float getX() {
		return x;
	}
	/** Sets this vector's x component to the specified value */
	public void setX(float x) {
		this.x = x;
	}
	/** @return This vector's y component */
	public float getY() {
		return y;
	}
	/** Sets this vector's y component to the specified value */
	public void setY(float y) {
		this.y = y;
	}
	/** @return This vector's z component */
	public float getZ() {
		return z;
	}
	/** Sets this vector's z component to the specified value */
	public void setZ(float z) {
		this.z = z;
	}


	/** Applies a function to this vector's x component */
	public void changeX(DoubleUnaryOperator f) {
		this.x = (float) f.applyAsDouble(x);
	}
	/** Applies a function to this vector's y component */
	public void changeY(DoubleUnaryOperator f) {
		this.y = (float) f.applyAsDouble(y);
	}
	/** Applies a function to this vector's z component */
	public void changeZ(DoubleUnaryOperator f) {
		this.z = (float) f.applyAsDouble(z);
	}


	/** @return A copy of this vector */
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
