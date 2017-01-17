package math;

public class Vector2f {
	
	public float x;
	public float y;
	
	public Vector2f() {
		this(0.0f, 0.0f);
	}
	
	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2f abs() {
		return new Vector2f(Math.abs(x), Math.abs(y));
	}
	
	public float dot(Vector2f r) {
		return x * r.x + y * r.y;
	}
	
	public float length() {
		return (float) Math.sqrt(x * x + y * y);
	}
	
	public Vector2f normalized() {
		float length = length();
		float x_ = x / length;
		float y_ = y / length;
		
		return new Vector2f(x_, y_);
	}
	
	public Vector2f rotate(float angle) {
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		
		return new Vector2f((float) (x * cos - y * sin), (float) (x * sin + y * cos));
	}
	
	public Vector2f add(float r) {
		return new Vector2f(x + r, y + r);
	}
	
	public Vector2f add(Vector2f r) {
		return new Vector2f(x + r.x, y + r.y);
	}
	
	public Vector2f sub(float r) {
		return new Vector2f(x - r, y - r);
	}
	
	public Vector2f sub(Vector2f r) {
		return new Vector2f(x - r.x, y - r.y);
	}
	
	public Vector2f mul(float r) {
		return new Vector2f(x * r, y * r);
	}
	
	public Vector2f mul(Vector2f r) {
		return new Vector2f(x * r.x, y * r.y);
	}
	
	public Vector2f div(float r) {
		return new Vector2f(x / r, y / r);
	}
	
	public Vector2f div(Vector2f r) {
		return new Vector2f(x / r.x, y / r.y);
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
	
}
