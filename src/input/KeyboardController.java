package input;

import org.lwjgl.input.Keyboard;

public class KeyboardController extends InputController{
	
	public KeyboardController() {
		super();
	}

	/** Updates the keyStatus and prevStatus maps with new key information. */
	public void update() {
		// mapping.keySet().forEach(key -> keyStatus.put(mapping.get(key),
		// Keyboard.isKeyDown(key)));
		for (Integer key : mapping.keySet()) {
			prevStatus.put(mapping.get(key), keyStatus.get(mapping.get(key)));
			if (Keyboard.isKeyDown(key)) {
				keyStatus.put(mapping.get(key), 1f);
			} else {
				keyStatus.put(mapping.get(key), 0f);
			}
		}
	}

	public static void main(String[] args) {
		KeyboardController input = new KeyboardController();
		System.out.println("----------Changing 42 to FORWARD--------------");
		input.changeKey(42, Action.FORWARD);
		input.printSettings();
		System.out.println("----------Changing 42 to BREAK--------------");
		input.changeKey(42, Action.BREAK);
		input.printSettings();
	}

}
