package gameEngine.toolbox;

public class VecCon {

	public static org.lwjgl.util.vector.Vector2f toLWJGL(org.joml.Vector2f vec){
		return new org.lwjgl.util.vector.Vector2f(vec.x,vec.y);
	}
	
	public static org.joml.Vector2f toJOML(org.lwjgl.util.vector.Vector2f vec){
		return new org.joml.Vector2f(vec.x,vec.y);
	}

	public static org.lwjgl.util.vector.Vector3f toLWJGL(org.joml.Vector3f vec){
		return new org.lwjgl.util.vector.Vector3f(vec.x,vec.y,vec.z);
	}
	
	public static org.joml.Vector3f toJOML(org.lwjgl.util.vector.Vector3f vec){
		return new org.joml.Vector3f(vec.x,vec.y,vec.z);
	}

	public static org.lwjgl.util.vector.Vector4f toLWJGL(org.joml.Vector4f vec){
		return new org.lwjgl.util.vector.Vector4f(vec.x,vec.y,vec.z,vec.w);
		
	}
	
	public static org.joml.Vector4f toJOML(org.lwjgl.util.vector.Vector4f vec){
		return new org.joml.Vector4f(vec.x,vec.y,vec.z,vec.w);
		
	}
	
}
