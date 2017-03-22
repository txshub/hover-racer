package physics.support;

import java.util.HashMap;

import org.lwjgl.input.Keyboard;

public class InputController {

  private HashMap<Integer, Action> mapping;
  private HashMap<Action, Boolean> keyStatus;
  private HashMap<Action, Boolean> prevStatus;

  public InputController() {
    mapping = getDefaultSettings();
    keyStatus = new HashMap<>();
    prevStatus = new HashMap<>();
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
    res.put(Keyboard.KEY_P, Action.MENU);

    res.put(Keyboard.KEY_M, Action.MUSIC_UP);
    res.put(Keyboard.KEY_N, Action.MUSIC_DOWN);
    res.put(Keyboard.KEY_B, Action.MUSIC_SKIP);
    res.put(Keyboard.KEY_K, Action.SFX_UP);
    res.put(Keyboard.KEY_J, Action.SFX_DOWN);
    return res;
  }

  public void changeKey(int key, Action action) {
    mapping.put(key, action);
  }

  /**
   * Updates the keyStatus and prevStatus maps with new key information.
   */
  public void update() {
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

}
