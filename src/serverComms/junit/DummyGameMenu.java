package serverComms.junit;

import java.util.ArrayList;

import serverComms.GameNameNumber;
import userInterface.GameMenu;

public class DummyGameMenu extends GameMenu {

  public DummyGameMenu() {
    System.out.println("Dummy");
  }

  public boolean testsPassed = false;

  @Override
  public void passRooms(ArrayList<GameNameNumber> gameList) {
    testsPassed = true;
  }

}
