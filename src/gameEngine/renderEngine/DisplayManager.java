package gameEngine.renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {

  private static final int WIDTH = 1024;
  private static final int HEIGHT = 720;
  private static final int FPS_CAP = 60;

  private static long lastFrameTime;
  private static long delta;

  private static long secondTimer = 99l;

  public static void createDisplay() {
    ContextAttribs attribs = new ContextAttribs(3, 2).withForwardCompatible(true)
        .withProfileCore(true);

    try {
      Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
      Display.create(new PixelFormat(), attribs);
      Display.setFullscreen(true);
      Display.setVSyncEnabled(true);
      Display.setTitle("Our First Display!");
      GL11.glEnable(GL13.GL_MULTISAMPLE);
    } catch (LWJGLException e) {
      e.printStackTrace();
    }

    GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
    lastFrameTime = getCurrentTime();
  }

  static int frames = 0;
  static long time = 0;

  public static void updateDisplay() {
    Display.sync(FPS_CAP);
    Display.update();
    long currentFrameTime = getCurrentTime();
    delta = (currentFrameTime - lastFrameTime);
    secondTimer += delta;
    lastFrameTime = currentFrameTime;
    if (secondTimer >= time + 1000) {
      // System.out.println(frames);
      frames = 0;
      time += 1000;
    } else {
      frames++;
    }
  }

  public static long getMilliSecondTimer() {
    return secondTimer;
  }

  public static float getFrameTimeSeconds() {
    return (float) delta / Sys.getTimerResolution();
  }

  public static void closeDisplay() {
    Display.destroy();
  }

  private static long getCurrentTime() {
    return Sys.getTime() * 1000 / Sys.getTimerResolution();
  }

  public static void changeTitle(String title) {
    Display.setTitle(title);
  }

}
