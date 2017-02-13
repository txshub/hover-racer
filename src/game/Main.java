package game;

import java.awt.GraphicsEnvironment;

import org.lwjgl.input.Keyboard;

import placeholders.InputController;

public class Main {

	
  public static void main(String[] args) {
    Game game = new Game();
    
    // Set the FPS and UPS caps
    int frameCap = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getRefreshRate();
    int updateCap = 60;
    
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
    
    // FPS and UPS reporting variables
    int fps = 0;
    int ups = 0;
    long timer = System.currentTimeMillis();
    
    while (!game.shouldClose()) {
      curTime = System.nanoTime();
      diff = curTime - lastTime;
      deltaUPS += diff / updateDur;
      deltaFPS += diff / renderDur;
      lastTime = curTime;
      
      // If updateDur has passed since the last update, do an update
      while (deltaUPS >= 1.0) {
        game.update(deltaUPS);
        ups++;
        deltaUPS--;
      }
      
      // If renderDur has passed since that last render, do a render
      if (deltaFPS >= 1.0) {
        game.render();
        fps++;
        deltaFPS = 0.0;
      }
      
      // Log the ups and fps to the window title every 1000ms
      if (System.currentTimeMillis() > timer + 1000) {
//        System.out.println("Hover Racer - ups: " + ups + " | fps: " + fps);
        timer += 1000;
        fps = 0;
        ups = 0;
      }
      if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
    	  break;
      }
    }

    game.cleanUp();
  }
  
}
