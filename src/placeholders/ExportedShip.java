package placeholders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import physics.Vector3;

/** An object representing a Ship entity, exported for transmission over the network. It serves as an intermediate step between a Ship
 * object and a float array. This will likely be upgraded to a byte array later
 * 
 * @author Maciej Bogacki */
public class ExportedShip implements ControllerInt {

	Vector3 position;
	Vector3 velocity;
	Collection<Action> keys;
	public ExportedShip(Vector3 position, Vector3 velocity, Collection<Action> keys) {
		super();
		this.position = position;
		this.velocity = velocity;
		this.keys = keys;
	}

	public ExportedShip(float[] n) {
		this.position = new Vector3(n[0], n[1], n[2]);
		this.velocity = new Vector3(n[3], n[4], n[5]);
		boolean[] boolkeys = decompose((int) n[6]);
		keys = new ArrayList<Action>();
		for (int i = 0; i < boolkeys.length; i++) {
			if (boolkeys[i]) keys.add(Action.ordered[i]);
		}
		// this.keys = Arrays.stream(Action.ordered).filter(
		// a -> n[6] % powOf2(Arrays.asList(Action.ordered).indexOf(a)) - n[6] % powOf2(Arrays.asList(Action.ordered).indexOf(a) - 1) == 0)
		// .collect(Collectors.toList());

	}

	public Vector3 getPosition() {
		return position;
	}
	public Vector3 getVelocity() {
		return velocity;
	}
	public Collection<Action> getKeys() {
		return keys;
	}

	public float[] toNumbers() {
		int comKeys = keys.stream().mapToInt(k -> powOf2(Arrays.asList(Action.ordered).indexOf(k))).sum();
		System.out.println();
		return new float[]{position.getX(), position.getY(), position.getZ(), velocity.getX(), velocity.getY(), velocity.getZ(), comKeys};
	}
	private int powOf2(int x) {
		return (int) Math.pow(2, x);
	}
	private boolean[] decompose(int x) {
		boolean[] res = new boolean[Action.ordered.length];
		int i = 0;
		while (x > 0) {
			if (x % 2 == 0) res[i++] = false;
			else res[i++] = true;
			x /= 2;
		}
		return res;
	}

	public static void main(String[] args) {
		ArrayList<Action> keys = new ArrayList<Action>();
		// keys.add(Action.FORWARD);
		// keys.add(Action.STRAFE_RIGHT);
		// keys.add(Action.BREAK);
		keys.addAll(Arrays.asList(Action.ordered));
		ExportedShip first = new ExportedShip(new Vector3(1, 2, 3), new Vector3(6, 7, 8), keys);
		float[] numbers = first.toNumbers();
		System.out.println(numbers[6]);
		System.out.println(first);
		System.out.println(new ExportedShip(numbers));
	}

	@Override
	public String toString() {
		String res = position + ", " + velocity;
		for (Action action : keys) {
			res += action + ", ";
		}
		return res;
	}

	@Override
	public Collection<Action> getPressedKeys() {
		return getKeys();
	}


}
