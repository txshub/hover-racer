package gameEngine.renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.PixelFormat;

/**
 * @author rtm592 A class to manage the display
 */
public class DisplayManager {

  private static final int WIDTH = 1280;
  private static final int HEIGHT = 720;
  private static final int FPS_CAP = 60;

  private static long lastFrameTime;
  private static long delta;

  private static long secondTimer = 99l;

  private static boolean goFullscreen = false;

  /**
   * create a new display
   */
  public static void createDisplay() {
    ContextAttribs attribs = new ContextAttribs(3, 2).withForwardCompatible(true)
        .withProfileCore(true);

    try {
      if (!goFullscreen) {
        Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
      } else {
        Display.setFullscreen(true);
      }
      Display.create(new PixelFormat(), attribs);
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

  /**
   * updates the display
   */
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

  /**
   * @return the up time of the display
   */
  public static long getMilliSecondTimer() {
    return secondTimer;
  }

  /**
   * @return the time taken to render the last frame
   */
  public static float getFrameTimeSeconds() {
    return (float) delta / Sys.getTimerResolution();
  }

  /**
   * close the display
   */
  public static void closeDisplay() {
    Display.destroy();
  }

  /**
   * @return the current time
   */
  private static long getCurrentTime() {
    return Sys.getTime() * 1000 / Sys.getTimerResolution();
  }

  /**
   * @param title
   *          the new title
   */
  public static void changeTitle(String title) {
    Display.setTitle(title);
  }

}
