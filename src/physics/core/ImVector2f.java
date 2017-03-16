package physics.core;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class ImVector2f {

	private float x;
	private float y;

	public ImVector2f(float x, float y) {
		super();
		this.x = x;
		this.y = y;
	}
	public ImVector2f(double x, double y) {
		this.x = (float) x;
		this.y = (float) y;
	}
	public ImVector2f(Vector2f v) {
		super();
		this.x = v.x;
		this.y = v.y;
	}
	public ImVector2f(Vector3f v) {
		super();
		this.x = v.x;
		this.y = v.z;
	}

	public ImVector2f add(ImVector2f v) {
		return new ImVector2f(x + v.x, y + v.y);
	}
	public ImVector2f sub(ImVector2f v) {
		return new ImVector2f(x - v.x, y - v.y);
	}
	public float dot(ImVector2f v) {
		return x * v.x + y * v.y;
	}
	public ImVector2f div(float v) {
		return new ImVector2f(x / v, y / v);
	}
	public ImVector2f mul(float v) {
		return new ImVector2f(x * v, y * v);
	}
	public float length() {
		return (float) Math.sqrt(x * x + y * y);
	}
	public ImVector2f normalize() {
		return this.div(this.length());
	}
	public ImVector2f makeBase() {
		return new ImVector2f(this.length(), 0);
	}
	public ImVector2f rotateTo(ImVector2f v) {
		float angle = angle(v);
		return new ImVector2f(Math.cos(angle), Math.sin(angle));
	}
	public float angle(ImVector2f vec) {
		return (float) Math.acos(this.dot(vec) / (this.length() * vec.length()));
	}



	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}

	@Override
	public String toString() {
		return "[" + x + ", " + y + "]";
	}
	public float distanceSquared(ImVector2f v) {
		return square(x - v.x) + square(y - v.y);
	}
	public float distance(ImVector2f v) {
		return (float) Math.sqrt(distanceSquared(v));
	}

	private float square(float a) {
		return a * a;
	}


}
