package placeholders;

import org.lwjgl.util.vector.Vector3f;

/** Provides a Ship object with information about the ground below. See methods for details.
 * 
 * @author Maciej Bogacki */
public interface GroundProvider {

	/** Returns distance to the ground from a given point in a given direction. If no ground is found, checks in the other direction and
	 * return a negative value. If ground is still not found, return {@link Float.POSITIVE_INFINITY}
	 * 
	 * @param position Position of the point to measure distance to
	 * @param direction Direction in which to look for the ground, e.g. (0,-1,0) when the vehicle is horizontal and wants to know what is
	 *        exactly below it, (0,-0.8,0.2) when it's slightly tilted, or (0,1,0) when it's upside down for some reason
	 *        For the prototype you can assume direction is always (0,-1,0)
	 * @return Length of the line from the vehicle to the ground which goes in the given direction */
	public float distanceToGround(Vector3f position, Vector3f direction);


	/** Returns the normal vector to the ground at a given point. If no ground is found there throws {@link IllegalArgumentException}
	 * 
	 * @param point Point at which to take the normal from, accurate down to float rounding errors
	 * @return Normal vector the the ground, pointing in the 'up' direction (where the vehicles should be) */
	public Vector3f normalToGround(Vector3f point);



}
