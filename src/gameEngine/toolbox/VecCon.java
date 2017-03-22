package gameEngine.toolbox;

/**
 * @author rtm592 A class to translate between LWJGL and Joml vectors
 */
public class VecCon {

	/**
	 * @param vec
	 *            a joml 2d vector
	 * @return a lwjgl 2d vector
	 */
	public static org.lwjgl.util.vector.Vector2f toLWJGL(org.joml.Vector2f vec) {
		return new org.lwjgl.util.vector.Vector2f(vec.x, vec.y);
	}

	/**
	 * @param vec
	 *            a lwjgl 2d vector
	 * @return a joml 2d vector
	 */
	public static org.joml.Vector2f toJOML(org.lwjgl.util.vector.Vector2f vec) {
		return new org.joml.Vector2f(vec.x, vec.y);
	}

	/**
	 * @param vec
	 *            a joml 3d vector
	 * @return a lwjgl 3d vector
	 */
	public static org.lwjgl.util.vector.Vector3f toLWJGL(org.joml.Vector3f vec) {
		return new org.lwjgl.util.vector.Vector3f(vec.x, vec.y, vec.z);
	}

	/**
	 * @param vec
	 *            a lwjgl 3d vector
	 * @return a joml 3d vector
	 */
	public static org.joml.Vector3f toJOML(org.lwjgl.util.vector.Vector3f vec) {
		return new org.joml.Vector3f(vec.x, vec.y, vec.z);
	}

	/**
	 * @param vec
	 *            a joml 4d vector
	 * @return a lwjgl 4d vector
	 */
	public static org.lwjgl.util.vector.Vector4f toLWJGL(org.joml.Vector4f vec) {
		return new org.lwjgl.util.vector.Vector4f(vec.x, vec.y, vec.z, vec.w);

	}

	/**
	 * @param vec
	 *            a lwjgl 4d vector
	 * @return a joml 4d vector
	 */
	public static org.joml.Vector4f toJOML(org.lwjgl.util.vector.Vector4f vec) {
		return new org.joml.Vector4f(vec.x, vec.y, vec.z, vec.w);

	}

}
