package game;

import java.awt.GraphicsEnvironment;
import java.io.IOException;

import clientComms.Client;
import gameEngine.renderEngine.DisplayManager;
import javafx.application.Platform;
import physics.network.RaceSetupData;
import userInterface.MainMenu;

public class MainGameLoop extends Thread {

  public void main(MultiplayerGame game) {

    boolean debug = false;

    // Set the FPS and UPS caps
    int frameCap = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
        .getDisplayMode().getRefreshRate();
    int updateCap = 60;

    long lastTime = System.nanoTime();
    long curTime = lastTime;
    long diff = 0;

    // updateDur is the minimum time between updates
    // deltaUPS is the time since the last update
    double updateDur = 1000000000 / updateCap;
    if (updateCap == -1)
      updateDur = 0;
    double deltaUPS = 0.0;

    // renderDur is the minimum time between renders
    // deltaFPS is the time since the last render
    double renderDur = 1000000000 / frameCap;
    if (frameCap == -1)
      renderDur = 0;
    double deltaFPS = 0.0;

    // FPS and UPS reporting variables
    int fps = 0;

    int ups = 0;
    long timer = System.currentTimeMillis();

    while (!game.shouldClose()) {
      if (ups % 10 == 0) {
        System.gc();
      }
      curTime = System.nanoTime();
      diff = curTime - lastTime;
      deltaUPS += diff / updateDur;
      deltaFPS += diff / renderDur;
      lastTime = curTime;

      // If updateDur has passed since the last update, do an update
      while (deltaUPS >= 1.0) {
        game.update(1f / updateCap);
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
        // if (debug) System.out.println("Hover Racer - ups: " + ups + " | fps:
        // " + fps);
        if (debug)
          DisplayManager.changeTitle("Hover Racer - ups: " + ups + " | fps: " + fps);
        timer += 1000;
        fps = 0;
        ups = 0;
      }
    }
    System.out.println("Game Closed");
    game.cleanUp();
    System.out.println("Closing Threads");
    for (Thread t : MainMenu.allThreads) {
      if (!t.equals(this) && !t.isInterrupted())
        t.interrupt();
    }
    MainMenu.allThreads.clear();
    Platform.runLater(new Runnable() {
      public void run() {
        try {
          MainMenu.reloadScene();
        } catch (IOException e) {
          System.out.println("Main Menu didn't reload");
        }
      }
    });
  }

  public static void startMultiplayerGame(RaceSetupData data, Client client) {
    System.out.println("------STARTING GAME------");
    // Start the game in a new thread - ensure all OpenGL stays in that thread
    Thread newThread = new Thread(() -> {
      MainGameLoop main = new MainGameLoop();
      MultiplayerGame game = new MultiplayerGame(data, client);
      main.main(game);
    });
    newThread.start();
    MainMenu.allThreads.add(0, newThread);
    // Ensure setup is finished before proceeding
    try {
      while (client.getManager() == null)
        Thread.sleep(10);
    } catch (InterruptedException e) {
    }
  }

}
