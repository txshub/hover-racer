package physics.support;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;

import org.lwjgl.input.Keyboard;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class InputController {

	public static boolean close = false;
	public static final String SETTINGS_PATH = "src/data/settings/" + "keySettings" + ".json";



	private HashMap<Integer, Action> mapping; // Maps key codes to actions (can
												// be
												// changed via changeKey)
	private transient HashMap<Action, Boolean> keyStatus;

	public InputController() {
		mapping = getDefaultSettings();
		// load();
		keyStatus = new HashMap<Action, Boolean>();
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

		res.put(Keyboard.KEY_ESCAPE, Action.EXIT);

		res.put(Keyboard.KEY_M, Action.MUSIC_UP);
		res.put(Keyboard.KEY_N, Action.MUSIC_DOWN);
		res.put(Keyboard.KEY_B, Action.MUSIC_SKIP);
		res.put(Keyboard.KEY_K, Action.SFX_UP);
		res.put(Keyboard.KEY_J, Action.SFX_DOWN);
		return res;
	}

	public void changeKey(int key, Action action) {
		mapping.remove(key);
		mapping.put(key, action);
		save();
	}

	public void run() {
		mapping.keySet().forEach(key -> keyStatus.put(mapping.get(key), Keyboard.isKeyDown(key)));
	}

	private void load() {
		try {
			String imported = new Scanner(new File(SETTINGS_PATH)).useDelimiter("\\Z").next();
			Gson gson = new GsonBuilder().create();
			mapping = gson.fromJson(imported, new HashMap<Integer, Action>().getClass());
			printSettings();

		} catch (IOException e) {
			System.err.println("IO exception");
			mapping = getDefaultSettings();
		} catch (NullPointerException e) {
			e.printStackTrace();
			mapping = getDefaultSettings();
		}
	}

	private void save() {
		try {
			PrintWriter writer = new PrintWriter(SETTINGS_PATH, "UTF-8");
			writer.println(new Gson().toJson(mapping));
			writer.close();
		} catch (IOException e) {
			System.err.println("Failed to write to file");
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

	public void printSettings() {
		/* for (Entry<Integer, Action> entry : mapping.entrySet()) {
		 * String key = entry.getKey().toString();
		 * String value = entry.getValue().toString();
		 * System.out.println(key + " -> " + value);
		 * } */
	}

	public static void main(String[] args) {
		InputController input = new InputController();
		System.out.println("----------Changing 42 to FORWARD--------------");
		input.changeKey(42, Action.FORWARD);
		input.printSettings();
		System.out.println("----------Changing 42 to BREAK--------------");
		input.changeKey(42, Action.BREAK);
		input.printSettings();
	}

}
