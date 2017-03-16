package input;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;

import org.lwjgl.input.Keyboard;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class InputController {

  public static boolean close = false;
  public static final String SETTINGS_PATH = "src/data/settings/" + "keySettings" + ".json";

  protected HashMap<Integer, Action> mapping;
  protected HashMap<Action, Float> keyStatus;
  protected HashMap<Action, Float> prevStatus;

  public InputController() {
    mapping = getDefaultSettings();
    keyStatus = new HashMap<Action, Float>();
    prevStatus = new HashMap<Action, Float>();
  }

  public abstract void update();

  protected HashMap<Integer, Action> getDefaultSettings() {
    HashMap<Integer, Action> res = new HashMap<>();
    res.put(Keyboard.KEY_W, Action.FORWARD); // w
    res.put(Keyboard.KEY_S, Action.BREAK); // s
    res.put(Keyboard.KEY_A, Action.STRAFE_LEFT); // a
    res.put(Keyboard.KEY_D, Action.STRAFE_RIGHT); // d
    res.put(Keyboard.KEY_RIGHT, Action.TURN_RIGHT); // right arrow
    res.put(Keyboard.KEY_LEFT, Action.TURN_LEFT); // left arrow
    res.put(Keyboard.KEY_SPACE, Action.JUMP); // space

    res.put(Keyboard.KEY_ESCAPE, Action.MENU);

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

  protected void load() {
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

  protected void save() {
    try {
      PrintWriter writer = new PrintWriter(SETTINGS_PATH, "UTF-8");
      writer.println(new Gson().toJson(mapping));
      writer.close();
    } catch (IOException e) {
      System.err.println("Failed to write to file");
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
  public float isDown(Action action) {
    if (action != null && keyStatus.containsKey(action)) {
      return keyStatus.get(action);
    }
    return 0;
  }

  /**
   * Checks whether the key for an action was pressed this frame.
   * 
   * @param action
   *          what action to check for
   * @return True or False
   * @author Reece Bennett
   */
  public float wasPressed(Action action) {
    if (action != null && keyStatus.containsKey(action)) {
      if (keyStatus.get(action) > 0.5f && prevStatus.get(action) < 0.5f) {
        return 1f;
      }

    }
    return 0f;

  }

  public void printSettings() {
    /*
     * for (Entry<Integer, Action> entry : mapping.entrySet()) { String key =
     * entry.getKey().toString(); String value = entry.getValue().toString();
     * System.out.println(key + " -> " + value); }
     */
  }

}
