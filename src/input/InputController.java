package input;

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

  private HashMap<Integer, Action> mapping;
  private HashMap<Action, Boolean> keyStatus;
  private HashMap<Action, Boolean> prevStatus;

  public InputController() {
    mapping = getDefaultSettings();
    keyStatus = new HashMap<Action, Boolean>();
    prevStatus = new HashMap<Action, Boolean>();
  }

  private HashMap<Integer, Action> getDefaultSettings() {
    HashMap<Integer, Action> res = new HashMap<>();
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

  private void load() {
    try {
      Scanner scanner = new Scanner(new File(SETTINGS_PATH));
      String imported = scanner.useDelimiter("\\Z").next();
      Gson gson = new GsonBuilder().create();
      mapping = gson.fromJson(imported, new HashMap<Integer, Action>().getClass());
      printSettings();
      scanner.close();

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

  /** Updates the keyStatus and prevStatus maps with new key information. */
  public void update() {
    // mapping.keySet().forEach(key -> keyStatus.put(mapping.get(key),
    // Keyboard.isKeyDown(key)));
    for (Integer key : mapping.keySet()) {
      prevStatus.put(mapping.get(key), keyStatus.get(mapping.get(key)));
      if (Keyboard.isKeyDown(key)) {
        keyStatus.put(mapping.get(key), true);
      } else {
        keyStatus.put(mapping.get(key), false);
      }
    }
  }

  /**
   * Checks whether the key for an action is currently being held.
   * 
   * @param action
   *          the action to check for
   * @return True or False
   * @author Reece Bennett
   */
  public boolean isDown(Action action) {
    if (action != null && keyStatus.containsKey(action)) {
      return keyStatus.get(action);
    }
    return false;
  }

  /**
   * Checks whether the key for an action was pressed this frame.
   * 
   * @param action
   *          what action to check for
   * @return True or False
   * @author Reece Bennett
   */
  public boolean wasPressed(Action action) {
    if (action != null && keyStatus.containsKey(action)) {
      return keyStatus.get(action) && !prevStatus.get(action);
    }
    return false;
  }

  public void printSettings() {
    /*
     * for (Entry<Integer, Action> entry : mapping.entrySet()) { String key =
     * entry.getKey().toString(); String value = entry.getValue().toString();
     * System.out.println(key + " -> " + value); }
     */
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
