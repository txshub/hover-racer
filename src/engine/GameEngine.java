package engine;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import engine.graphics.Window;
import engine.input.Input;

public class GameEngine {

  @SuppressWarnings("unused")
  private GLFWErrorCallback errorCallBack;
  
  private Input input;
  private Window window;
  
  private IGameLogic gameLogic;
  
  private boolean running = false;
  
  // Set to -1 to disable the caps
  private float updateCap = 60;
  private float frameCap = 120;

  public GameEngine(String title, int width, int height, IGameLogic gameLogic) {
    glfwSetErrorCallback(errorCallBack = GLFWErrorCallback.createPrint(System.err));
    glfwInit();
    window = new Window(width, height, title);
    input = new Input(window);
    this.gameLogic = gameLogic;
  }
  
  public void start() {
    if (running) return;
    running = true;
    init();
    gameLoop();
  }
  
  private void init() {
    GL.createCapabilities();
    gameLogic.init(window);
  }
  
  private void gameLoop() {
    try {
      long lastTime = System.nanoTime();
      long curTime = lastTime;
      long diff = 0;
      
      // updateDur is the minimum time between updates
      // deltaUPS is the time since the last update
      double updateDur = 1000000000 / updateCap;
      if (updateCap == -1) updateDur = 0;
      double deltaUPS = 0.0;
  
      // renderDur is the minimum time between renders
      // deltaFPS is the time since the last render
      double renderDur = 1000000000 / frameCap;
      if (frameCap == -1) renderDur = 0;
      double deltaFPS = 0.0;
      
      int fps = 0;
      int ups = 0;
      
      long timer = System.currentTimeMillis();
      
      while (running) {
        curTime = System.nanoTime();
        diff = curTime - lastTime;
        deltaUPS += diff / updateDur;
        deltaFPS += diff / renderDur;
        lastTime = curTime;
        
        // If updateDur has passed since the last update, do an update
        while (deltaUPS >= 1.0) {
          update(deltaUPS);
          ups++;
          deltaUPS--;
        }
        
        // If renderDur has passed since that last render, do a render
        if (deltaFPS >= 1.0) {
          render();
          fps++;
          deltaFPS = 0.0;
        }
        
        // Log the ups and fps to the window title every 1000ms
        if (System.currentTimeMillis() > timer + 1000) {
          window.setTitle("Hover Racer - ups: " + ups + " | fps: " + fps);
          timer += 1000;
          fps = 0;
          ups = 0;
        }
      }
    } catch (Exception e) {
      e.printStackTrace(System.err);
    } finally {
      window.hide();
      gameLogic.cleanUp();
      window.dispose();
    }
  }
  
  private void update(double delta) {
    glfwPollEvents();
    if (input.keys[GLFW_KEY_ESCAPE]) running = false;
    if (window.shouldClose()) running = false;
    
    gameLogic.input(window, input);
    gameLogic.update(window, delta);
  }
  
  private void render() {
    gameLogic.render(window);
    window.render();
  }

}
