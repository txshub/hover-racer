package math;

public class Vector3f {
	
	public float x;
	public float y;
	public float z;
	
	public Vector3f() {
		this(0.0f, 0.0f, 0.0f);
	}
	
	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3f abs() {
		return new Vector3f(Math.abs(x), Math.abs(y), Math.abs(z));
	}
	
	public Vector3f cross(float rx, float ry, float rz) {
		float x_ = y * rz - z * ry;
		float y_ = z * rx - x * rz;
		float z_ = x * ry - y * rx;
		
		return new Vector3f(x_, y_, z_);
	}
	
	public Vector3f cross(Vector3f r) {
		float x_ = y * r.z - z * r.y;
		float y_ = z * r.x - x * r.z;
		float z_ = x * r.y - y * r.x;
		
		return new Vector3f(x_, y_, z_);
	}
	
	public float dot(Vector3f r) {
		return x * r.x + y * r.y + z * r.z;
	}
	
	public float length() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}
	
	public Vector3f normalized() {
		float length = length();
		float x_ = x / length;
		float y_ = y / length;
		float z_ = z / length;
		
		return new Vector3f(x_, y_, z_);
	}
	
	public Vector3f rotate(Vector3f axis, float a) {
		float angle = (float) Math.toRadians(a);
		float sin = (float) Math.sin(-angle);
		float cos = (float) Math.cos(-angle);
		
		float cosMin = 1 - cos;
		float dot = x * axis.x * cosMin + y * axis.y * cosMin + z * axis.z * cosMin;
		
		float x_ = (y * axis.z * sin - z * axis.y * sin) + x * cos + axis.x * dot;
		float y_ = (z * axis.x * sin - x * axis.z * sin) + y * cos + axis.y * dot;
		float z_ = (x * axis.y * sin - y * axis.x * sin) + z * cos + axis.z * dot;
		
		return new Vector3f(x_, y_, z_);
	}
	
	public Vector3f add(float r) {
		return new Vector3f(x + r, y + r, z + r);
	}
	
	public Vector3f add(Vector3f r) {
		return new Vector3f(x + r.x, y + r.y, z + r.z);
	}
	
	public Vector3f sub(float r) {
		return new Vector3f(x - r, y - r, z - r);
	}
	
	public Vector3f sub(Vector3f r) {
		return new Vector3f(x - r.x, y - r.y, z - r.z);
	}
	
	public Vector3f mul(float r) {
		return new Vector3f(x * r, y * r, z * r);
	}
	
	public Vector3f mul(Vector3f r) {
		return new Vector3f(x * r.x, y * r.y, z * r.z);
	}
	
	public Vector3f div(float r) {
		return new Vector3f(x / r, y / r, z /r);
	}
	
	public Vector3f div(Vector3f r) {
		return new Vector3f(x / r.x, y / r.y, z / r.z);
	}
	
	public String toString() {
		return "(" + x + "; " + y + "; " + z + ")";
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
	
	public float getZ() {
		return z;
	}
	
	public void setZ(float z) {
		this.z = z;
	}
	
}
