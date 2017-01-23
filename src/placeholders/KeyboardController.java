package placeholders;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.HashSet;


public class KeyboardController implements KeyListener {

	HashSet<Action> pressed;
	HashMap<Integer, Action> mapping;

	public KeyboardController() {
		pressed = new HashSet<Action>();
		mapping = getDefaultSettings();
	}

	private HashMap<Integer, Action> getDefaultSettings() {
		HashMap<Integer, Action> res = new HashMap<Integer, Action>();
		res.put(87, Action.FORWARD); // w
		res.put(83, Action.BREAK); // s
		res.put(65, Action.STRAFE_LEFT); // a
		res.put(68, Action.STRAFE_RIGHT); // d
		res.put(39, Action.TURN_RIGHT); // right arrow
		res.put(37, Action.TURN_LEFT); // left arrow
		return res;
	}

	public void changeKey(Action action, int key) {
		mapping.remove(mapping.entrySet().stream().filter(e -> e.getValue().equals(action)).map(e -> e.getKey()).findAny());
		mapping.put(key, action);
	}


	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		pressed.add(mapping.get(e.getKeyCode()));

	}

	@Override
	public void keyReleased(KeyEvent e) {
		pressed.remove(mapping.get(e.getKeyCode()));
	}

}
