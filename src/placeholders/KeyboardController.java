package placeholders;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.HashSet;


/** Keyboard controller for steering a player controlled ship with the keyboard.
 * The default mapping is W-FORWARD, S-BRAK< A-STRAFE_LEFT, D-STRAFE_RIGHT, right arrow-TURN_RIGHT, left arrow-TURN LEFT
 * Mappings can be changes with the changeKey function.
 * This class is a KeyListener - make sure to add it where needed.
 * 
 * @author Maciej Bogacki */
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

	/** Change the key mapping for a specific action.
	 * 
	 * @param action Action to update the corresponding key for.
	 * @param key Key code */
	public void changeKey(Action action, int key) {
		mapping.entrySet().stream().filter(e -> e.getValue().equals(action)).map(e -> e.getKey()).findAny()
			.map(oldKey -> mapping.remove(oldKey)); // Remove old mapping for this action, if existed
		mapping.put(key, action);
	}


	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		Action action = mapping.get(e.getKeyCode());
		if (action != null) pressed.add(action);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		Action action = mapping.get(e.getKeyCode());
		if (action != null) pressed.remove(action);
	}

}