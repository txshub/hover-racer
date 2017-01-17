package main;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import graphics.MasterRenderer;
import graphics.Window;

public class Main {
  
  private GLFWErrorCallback errorCallBack;
  
  private Window window;
  private MasterRenderer renderer;
  
  private boolean running = false;
  
  public Main() {
    init();
    renderer = new MasterRenderer();
  }
  
  private void init() {
    glfwSetErrorCallback(errorCallBack = GLFWErrorCallback.createPrint(System.err));
    glfwInit();
    
    window = new Window(640, 360, "Hover Racer");
    GL.createCapabilities();
  }
  
  public void start() {
    if (running) return;
    running = true;
    run();
  }
  
  public void stop() {
    if (!running) return;
    running = false;
  }
  
  private void input() {
    glfwPollEvents();
    if (window.shouldClose()) stop();
  }
  
  private void update() {
    
  }
  
  private void render() {
    // Do drawing
    renderer.render();
    // Swap the buffers
    window.render();
  }
  
  private void run() {
    long lastTime = System.nanoTime();
    long curTime = lastTime;
    
    // ns is the minimum length between updates
    // Delta = 1.0 when enough time has elapsed for another update
    double ns = 1000000000 / 60.0;
    double delta = 0.0;
    
    int fps = 0;
    int ups = 0;
    long timer = System.currentTimeMillis();
    
    while (running) {
      curTime = System.nanoTime();
      delta += (curTime - lastTime) / ns;
      lastTime = curTime;
      
      while (delta >= 1.0) {
        input();
        update();
        ups++;
        delta--;
      }
      
      render();
      fps++;
      
      if (System.currentTimeMillis() > timer + 1000) {
        window.setTitle("Hover Racer - ups: " + ups + " | fps: " + fps);
        timer += 1000;
        fps = 0;
        ups = 0;
      }
    }
    
    cleanUp();
  }
  
  private void cleanUp() {
    window.hide();
    
    renderer.cleanUp();
    window.dispose();
  }

  public static void main(String[] args) {
    Main main = new Main();
    main.start();
  }

}
