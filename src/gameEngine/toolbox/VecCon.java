package gameEngine.toolbox;

import org.lwjgl.util.vector.Vector3f;

public class VecCon {

	public static org.lwjgl.util.vector.Vector3f toLWJGL3(org.joml.Vector3f vec){
		return new Vector3f(vec.x,vec.y,vec.z);
	}
	
	public static org.joml.Vector3f toJOML3(Vector3f vec){
		return new org.joml.Vector3f(vec.x,vec.y,vec.z);
	}
	
	public static org.lwjgl.util.vector.Vector4f toLWJGL4(org.joml.Vector4f vec) {
	  return new org.lwjgl.util.vector.Vector4f(vec.x, vec.y, vec.z, vec.w);
	}
	
	public static org.joml.Vector4f toJOML4(org.lwjgl.util.vector.Vector4f vec) {
	  return new org.joml.Vector4f(vec.x, vec.y, vec.z, vec.w);
	}
	
}
