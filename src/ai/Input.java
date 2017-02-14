package ai;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Set;
import java.util.TreeSet;

/**
 * 
 * @author Reece Bennett
 *
 */
public class Input implements KeyListener {
  
  private Set<Integer> heldKeys;
  
  public Input() {
    heldKeys = new TreeSet<>();
  }

  @Override
  public void keyPressed(KeyEvent e) {
    heldKeys.add(e.getKeyCode());
  }

  @Override
  public void keyReleased(KeyEvent e) {
    heldKeys.remove(Integer.valueOf(e.getKeyCode()));
  }

  @Override
  public void keyTyped(KeyEvent e) {}
  
  public boolean isKeyDown(int keyCode) {
    return heldKeys.contains(keyCode);
  }

}
