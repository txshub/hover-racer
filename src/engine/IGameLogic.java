package engine;

import engine.graphics.Window;
import engine.input.Input;

public interface IGameLogic {

  void init(Window window);
  void input(Window window, Input input);
  void update(Window window, double delta);
  void render(Window window);
  void cleanUp();
  
}
