package gameEngine.toolbox;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class VecCon {

	public static org.lwjgl.util.vector.Vector2f toLWJGL(org.joml.Vector2f vec){
		return new Vector2f(vec.x,vec.y);
	}
	
	public static org.joml.Vector2f toJOML(Vector2f vec){
		return new org.joml.Vector2f(vec.x,vec.y);
	}

	public static org.lwjgl.util.vector.Vector3f toLWJGL(org.joml.Vector3f vec){
		return new Vector3f(vec.x,vec.y,vec.z);
	}
	
	public static org.joml.Vector3f toJOML(Vector3f vec){
		return new org.joml.Vector3f(vec.x,vec.y,vec.z);
	}

	public static Vector4f toLWJGL(org.joml.Vector4f vec){
		return new Vector4f(vec.x,vec.y,vec.z,vec.w);
		
	}
	
	public static org.joml.Vector4f toJOML(Vector4f vec){
		return new org.joml.Vector4f(vec.x,vec.y,vec.z,vec.w);
		
	}
	
}
