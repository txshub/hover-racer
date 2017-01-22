package main;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import camera.Camera;
import entity.Entity;
import graphics.MasterRenderer;
import graphics.Material;
import graphics.PointLight;
import graphics.PointLight.Attenuation;
import graphics.Window;
import graphics.model.Model;
import graphics.model.OBJLoader;
import graphics.texture.Texture;
import input.Input;

public class Main {
  
  @SuppressWarnings("unused")
  private GLFWErrorCallback errorCallBack;

  private Camera camera;
  private Input input;
  private MasterRenderer renderer;
  private Window window;
  
  private boolean running = false;
  
  // Set to -1 to disable the cap
  private float updateCap = 60;
  private float frameCap = 120;
  
  private ArrayList<Entity> entities = new ArrayList<>();
  
  private Vector3f ambientLight;
  private PointLight pointLight;
  
  private boolean spinning = false;
  
  // 1280x720 or 640x360
  private int width = 1280;
  private int height = 720;
  
  public Main() {
    init();
    renderer = new MasterRenderer(width, height);
    
    camera = new Camera();
    input = new Input(window);

    Model cube = OBJLoader.loadModel("cube.obj");
    Texture texture = new Texture("grass.png");
    Material material = new Material(texture, 1f);
    cube.setMaterial(material);
    
//    Random r = new Random();
//    for (int i = 0; i < 100; i++) {
//      float x = r.nextFloat() * 100 - 50;
//      float y = r.nextFloat() * 100 - 50;
//      float z = r.nextFloat() * -100;
//      entities.add(new Entity(cube, new Vector3f(x, y, z)));
//    }
    
    entities.add(new Entity(cube, new Vector3f(3f, 0f, -2f)));
    entities.add(new Entity(cube, new Vector3f(-2f, -2f, -2f)));
    
    Model bunny = OBJLoader.loadModel("bunny.obj");
    bunny.setMaterial(new Material(new Vector3f(0.8f, 0.5f, 0.5f), 2f));
    entities.add(new Entity(bunny, new Vector3f(0f, 0f, -5f)));
    
    ambientLight = new Vector3f(0.3f, 0.3f, 0.3f);
    Vector3f lightColor = new Vector3f(1f, 1f, 1f);
    Vector3f lightPosition = new Vector3f(0f, 0f, 1f);
    float lightIntensity = 2.0f;
    pointLight = new PointLight(lightColor, lightPosition, lightIntensity);
    pointLight.setAttenuation(new Attenuation(0f, 0f, 0.5f));
  }
  
  private void init() {
    glfwSetErrorCallback(errorCallBack = GLFWErrorCallback.createPrint(System.err));
    glfwInit();
    
    window = new Window(width, height, "Hover Racer");
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
  
  /**
   * Handles input not related to a specific entity.
   */
  private void input() {
    glfwPollEvents();
    if (window.shouldClose()) stop();
    
    float moveAmount = 0.1f;
    float dx = 0;
    float dy = 0;
    float dz = 0;
    
    if (input.keys[GLFW_KEY_LEFT_CONTROL]) moveAmount *= 6;
    
    if (input.keys[GLFW_KEY_W]) dz -= moveAmount;
    if (input.keys[GLFW_KEY_S]) dz += moveAmount;
    if (input.keys[GLFW_KEY_A]) dx -= moveAmount;
    if (input.keys[GLFW_KEY_D]) dx += moveAmount;
    if (input.keys[GLFW_KEY_SPACE]) dy += moveAmount;
    if (input.keys[GLFW_KEY_LEFT_SHIFT]) dy -= moveAmount;
    
    camera.movePosition(dx, dy, dz);
    
    float rotAmount = 1f;
    float rx = 0;
    float ry = 0;
    
    if (input.keys[GLFW_KEY_UP]) rx -= rotAmount;
    if (input.keys[GLFW_KEY_DOWN]) rx += rotAmount;
    if (input.keys[GLFW_KEY_RIGHT]) ry += rotAmount;
    if (input.keys[GLFW_KEY_LEFT]) ry -= rotAmount;
    
    camera.moveRotation(rx, ry, 0);
    
    moveAmount = 0.1f;
    dx = 0;
    dy = 0;
    dz = 0;
    
    if (input.keys[GLFW_KEY_I]) dz -= moveAmount;
    if (input.keys[GLFW_KEY_K]) dz += moveAmount;
    if (input.keys[GLFW_KEY_J]) dx -= moveAmount;
    if (input.keys[GLFW_KEY_L]) dx += moveAmount;
    
    pointLight.movePosition(dx, dy, dz);
    
    if (input.keys[GLFW_KEY_P]) spinning = !spinning;
    
    if (input.keys[GLFW_KEY_ESCAPE]) running = false;
  }
  
  private void update() {
    if (spinning) {
      for (Entity e : entities) {
        float off = entities.indexOf(e) % 100 / 20;
        float rotation = e.getRotation().x + 0.5f + off;
        if (rotation > 360) {
          rotation = 0;
        }
        e.setRotation(rotation, rotation, rotation);
      }
    }
    
    pointLight.setIntensity(5f);
    pointLight.setAttenuation(new Attenuation(0f, 0f, 0.2f));
  }
  
  private void render() {
    // Process the entities for rendering
    for (int i = 0; i < entities.size(); i++) renderer.processEntity(entities.get(i));
    
    // Do drawing and swap the buffers to display the rendered image
    renderer.render(camera, ambientLight, pointLight);
    window.render();
  }
  
  private void run() {
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
        input();
        update();
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
    
    // When the game ends clean up
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
