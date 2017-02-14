package placeholders;

import java.util.HashMap;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class InputController extends Thread {

	public static boolean close = false;

	public enum Action {
		FORWARD, BACKWARDS, STRAFE_LEFT, STRAFE_RIGHT, TURN_LEFT, TURN_RIGHT, JUMP;

	}

	private HashMap<Integer, Action> mapping; // Maps key codes to actions (can
												// be changed via changeKey)
	private HashMap<Action, Boolean> keyStatus;

	public InputController() {
		mapping = getDefaultSettings();
		keyStatus = new HashMap<Action, Boolean>();
	}

	private HashMap<Integer, Action> getDefaultSettings() {
		HashMap<Integer, Action> res = new HashMap<Integer, Action>();
		res.put(Keyboard.KEY_W, Action.FORWARD); // w
		res.put(Keyboard.KEY_S, Action.BACKWARDS); // s
		res.put(Keyboard.KEY_A, Action.STRAFE_LEFT); // a
		res.put(Keyboard.KEY_D, Action.STRAFE_RIGHT); // d
		res.put(Keyboard.KEY_RIGHT, Action.TURN_RIGHT); // right arrow
		res.put(Keyboard.KEY_LEFT, Action.TURN_LEFT); // left arrow
		res.put(Keyboard.KEY_SPACE, Action.JUMP); // space
		return res;
	}

	public void changeKey(int key, Action action) {
		mapping.put(key, action);
	}

	public void run() {

		while (!close) {
			
			for (Integer key : mapping.keySet()) {
				if (Keyboard.isKeyDown(key)) {
					keyStatus.put(mapping.get(key), true);
				} else {
					keyStatus.put(mapping.get(key), false);
				}
			}

		}

	}

	public boolean checkAction(Action action) {
		if (action == null) {
			return false;
		}
		if (keyStatus.containsKey(action)) {
			boolean check = keyStatus.get(action);
			if (check) {
				keyStatus.put(action, false);
			}
			return check;
		}

		return false;
	}

}
