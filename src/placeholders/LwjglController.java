package placeholders;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import org.lwjgl.input.Keyboard;

public class LwjglController implements ControllerInt {

	HashSet<Action> pressed; // Set of currently pressed keys (updated with each call of getPressedKeys)
	HashMap<Integer, Action> mapping; // Maps key codes to actions (can be changed via changeKey)



	public LwjglController() {
		pressed = new HashSet<Action>();
		mapping = getDefaultSettings();
	}

	private HashMap<Integer, Action> getDefaultSettings() {
		HashMap<Integer, Action> res = new HashMap<Integer, Action>();
		res.put(Keyboard.KEY_W, Action.FORWARD); // w
		res.put(Keyboard.KEY_S, Action.BREAK); // s
		res.put(Keyboard.KEY_A, Action.STRAFE_LEFT); // a
		res.put(Keyboard.KEY_D, Action.STRAFE_RIGHT); // d
		res.put(Keyboard.KEY_RIGHT, Action.TURN_RIGHT); // right arrow
		res.put(Keyboard.KEY_LEFT, Action.TURN_LEFT); // left arrow
		res.put(Keyboard.KEY_SPACE, Action.JUMP); // space
		return res;
	}

	/** Change the key mapping for a specific action.
	 * 
	 * @param action Action to update the corresponding key for, e.g. Action.TURN_LEFT for turning left
	 * @param key Key code, e.g. Keyboard.KEY_A for a */
	public void changeKey(Action action, int key) {
		mapping.entrySet().stream().filter(e -> e.getValue().equals(action)).map(e -> e.getKey()).findAny()
			.map(oldKey -> mapping.remove(oldKey)); // Remove old mapping for this action, if existed
		mapping.put(key, action);
	}


	/** Updates a specific key, adding to 'pressed' if it's pressed and removing if it's not
	 * 
	 * @param keyCode Key code for the key to update */
	private void updateKey(Integer keyCode) {
		if (Keyboard.isKeyDown(keyCode)) pressed.add(mapping.get(keyCode));
		else pressed.remove(mapping.get(keyCode));
	}

	@Override
	public Collection<Action> getPressedKeys() {
		mapping.keySet().stream().forEach(keyCode -> updateKey(keyCode));
		pressed.forEach(a -> System.out.println(a));
		return pressed;
	}

}
