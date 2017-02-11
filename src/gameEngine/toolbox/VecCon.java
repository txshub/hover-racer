package gameEngine.toolbox;

import org.lwjgl.util.vector.Vector3f;

public class VecCon {

	public static Vector3f toLWJGL(org.joml.Vector3f vec){
		return new Vector3f(vec.x,vec.y,vec.z);
		
	}
	
	public static org.joml.Vector3f toJOML(Vector3f vec){
		return new org.joml.Vector3f(vec.x,vec.y,vec.z);
		
	}
	
}
