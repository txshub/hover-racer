package game;

import engine.GameEngine;
import engine.IGameLogic;

public class Main {

  public static void main(String[] args) {
    IGameLogic logic = new Game();
    GameEngine engine = new GameEngine("Hover Racer", 1280, 720, logic);
    engine.start();
  }

}
