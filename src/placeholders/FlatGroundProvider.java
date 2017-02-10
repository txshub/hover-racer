package placeholders;

import org.joml.Vector3f;


/** GroundProvider the provides a flat ground at y=0 and assumes all vehicles are horizontal.
 * 
 * @author Maciej Bogacki */
public class FlatGroundProvider implements GroundProvider {

	float height;

	public FlatGroundProvider(float height) {
		this.height = height;
	}

	@Override
	public float distanceToGround(Vector3f position, Vector3f direction) {
		return position.y;
	}

	@Override
	public Vector3f normalToGround(Vector3f point) {
		return new Vector3f(0, 1, 0);
	}

}
