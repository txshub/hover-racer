package main;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import camera.Camera;
import entity.Entity;
import graphics.MasterRenderer;
import graphics.Window;
import graphics.model.Model;
import input.Input;
import math.Vector3f;

public class Main {
  
  @SuppressWarnings("unused")
  private GLFWErrorCallback errorCallBack;

  private Camera camera;
  private Input input;
  private MasterRenderer renderer;
  private Window window;
  
  private boolean running = false;
  
  // TEMP
  private ArrayList<Entity> entities = new ArrayList<>();
  
  public Main() {
    init();
    renderer = new MasterRenderer();
    
    camera = new Camera();
    input = new Input(window);
    
    // TEMP
    Model model = Model.loadModel("cube.obj");
    Random rand = new Random();
    
    for (int i = 0; i < 300; i++) {
      entities.add(new Entity(new Vector3f(rand.nextInt(500) - 250, rand.nextInt(500) - 250, rand.nextInt(500)), model));
    }

//    entities.add(new Entity(new Vector3f(0, 0, 5), model));
  }
  
  private void init() {
    glfwSetErrorCallback(errorCallBack = GLFWErrorCallback.createPrint(System.err));
    glfwInit();
    
    window = new Window(1280, 720, "Hover Racer");
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
    
    float moveAmount = 0.6f;
    
    float dx = 0;
    float dy = 0;
    float dz = 0;
    
    if (input.keys[GLFW_KEY_W]) dz += moveAmount;
    if (input.keys[GLFW_KEY_S]) dz -= moveAmount;
    if (input.keys[GLFW_KEY_A]) dx -= moveAmount;
    if (input.keys[GLFW_KEY_D]) dx += moveAmount;
    if (input.keys[GLFW_KEY_SPACE]) dy += moveAmount;
    if (input.keys[GLFW_KEY_LEFT_SHIFT]) dy -= moveAmount;
    
    camera.move(dx, dy, dz);
    
    float rotAmount = 1f;
    
    if (input.keys[GLFW_KEY_UP]) camera.rotateX(-rotAmount); 
    if (input.keys[GLFW_KEY_DOWN]) camera.rotateX(rotAmount); 
    if (input.keys[GLFW_KEY_RIGHT]) camera.rotateY(rotAmount); 
    if (input.keys[GLFW_KEY_LEFT]) camera.rotateY(-rotAmount); 
  }
  
  private void update() {
    
  }
  
  private void render() {
    for (int i = 0; i < entities.size(); i++) renderer.processEntity(entities.get(i));
    
    // Do drawing
    renderer.render(camera);
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
    
    // FPS cap
    double deltaFPS = 1000000000 / 60.0;
    double d = 0.0;
    long diff = 0;
    
    int fps = 0;
    int ups = 0;
    long timer = System.currentTimeMillis();
    
    while (running) {
      curTime = System.nanoTime();
      diff = curTime - lastTime;
      delta += diff / ns;
      d += diff / deltaFPS;
      lastTime = curTime;
      
      while (delta >= 1.0) {
        input();
        update();
        ups++;
        delta--;
      }
      
      if (d >= 1.0) {
        render();
        fps++;
        d = 0.0;
      }
      
      // Log the ups and fps to the window title every 1000ms
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
