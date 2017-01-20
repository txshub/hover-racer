package main;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import camera.Camera;
import entity.Entity;
import graphics.MasterRenderer;
import graphics.Window;
import graphics.model.Model;
import graphics.model.Vertex;
import input.Input;
import graphics.texture.Texture;

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
  private float frameCap = 60;
  
  // TEMP
  private ArrayList<Entity> entities = new ArrayList<>();
  
  // 1280x720 or 640x360
  private int width = 1280;
  private int height = 720;
  
  public Main() {
    init();
    renderer = new MasterRenderer(width, height);
    
    camera = new Camera();
    input = new Input(window);
    
    // TEMP
//    Model cube = Model.loadModel("cube.obj");
//    entities.add(new Entity(new Vector3f(0, 0, 5), new Vector3f(), 1f, cube));
    
//    Model quad = new Model();
//    Vertex[] verts = new Vertex[] {
//        new Vertex(-0.5f,  0.5f,  -1.0f),
//        new Vertex(-0.5f, -0.5f,  -1.0f),
//        new Vertex( 0.5f, -0.5f,  -1.0f),
//        new Vertex( 0.5f,  0.5f,  -1.0f)
//    };
//    int[] indices = new int[] { 0, 1, 3, 1, 2, 3 };
//    float[] colors = new float[] {
//        0.5f, 0.0f, 0.0f,
//        0.0f, 0.5f, 0.0f,
//        0.0f, 0.0f, 0.5f,
//        0.0f, 0.5f, 0.5f
//    };
//    
//    quad.bufferVertices(verts, indices, colors, false);
//    entities.add(new Entity(quad, new Vector3f(2f, 0f, -1f)));
    
    Model cube = new Model(new Texture("grass.png"));
    Vertex[] vertsCube = new Vertex[] {
        new Vertex(-0.5f,  0.5f,  0.5f), // V0
        new Vertex(-0.5f, -0.5f,  0.5f), // V1
        new Vertex( 0.5f, -0.5f,  0.5f), // V2
        new Vertex( 0.5f,  0.5f,  0.5f), // V3
        new Vertex(-0.5f,  0.5f, -0.5f), // V4
        new Vertex( 0.5f,  0.5f, -0.5f), // V5
        new Vertex(-0.5f, -0.5f, -0.5f), // V6
        new Vertex( 0.5f, -0.5f, -0.5f), // V7
        
        // For texCoords top face
        new Vertex(-0.5f,  0.5f, -0.5f), // V8 = V4
        new Vertex( 0.5f,  0.5f, -0.5f), // V9 = V5
        new Vertex(-0.5f,  0.5f,  0.5f), // V10 = V0
        new Vertex( 0.5f,  0.5f,  0.5f), // V11 = V3
        
        // For texCoords right face
        new Vertex( 0.5f,  0.5f,  0.5f), // V12 = V3
        new Vertex( 0.5f, -0.5f,  0.5f), // V13 = V2
        
        // For texCoords in left face
        new Vertex(-0.5f,  0.5f,  0.5f), // V14 = V0
        new Vertex(-0.5f, -0.5f,  0.5f), // V15 = V1
        
        // For texCoords in bottom face
        new Vertex(-0.5f, -0.5f, -0.5f), // V16 = V6
        new Vertex( 0.5f, -0.5f, -0.5f), // V17 = V7
        new Vertex(-0.5f, -0.5f,  0.5f), // V18 = V1
        new Vertex( 0.5f, -0.5f,  0.5f), // V19 = V2
    };
    int[] indicesCube = new int[] {
     // Front face
        0, 1, 3, 3, 1, 2,
        // Top Face
        8, 10, 11, 9, 8, 11,
        // Right face
        12, 13, 7, 5, 12, 7,
        // Left face
        14, 15, 6, 4, 14, 6,
        // Bottom face
        16, 18, 19, 17, 16, 19,
        // Back face
        4, 6, 7, 5, 4, 7
    };
    float[] texCoordsCube = new float[] {
        0.0f, 0.0f,
        0.0f, 0.5f,
        0.5f, 0.5f,
        0.5f, 0.0f,
        
        0.0f, 0.0f,
        0.5f, 0.0f,
        0.0f, 0.5f,
        0.5f, 0.5f,
        
        // For text coords in top face
        0.0f, 0.5f,
        0.5f, 0.5f,
        0.0f, 1.0f,
        0.5f, 1.0f,

        // For text coords in right face
        0.0f, 0.0f,
        0.0f, 0.5f,

        // For text coords in left face
        0.5f, 0.0f,
        0.5f, 0.5f,

        // For text coords in bottom face
        0.5f, 0.0f,
        1.0f, 0.0f,
        0.5f, 0.5f,
        1.0f, 0.5f
    };
    cube.bufferVertices(vertsCube, indicesCube, texCoordsCube);
    entities.add(new Entity(cube, new Vector3f(0f, 0f, -2f)));
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
    
    if (input.keys[GLFW_KEY_ESCAPE]) running = false;
  }
  
  private void update() {
    Entity e = entities.get(0);
    float rotation = e.getRotation().x + 0.5f;
    if (rotation > 360) {
      rotation = 0;
    }
    e.setRotation(rotation, rotation, rotation);
  }
  
  private void render() {
    // Process the entities for rendering
    for (int i = 0; i < entities.size(); i++) renderer.processEntity(entities.get(i));
    
    // Do drawing and swap the buffers to display the rendered image
    renderer.render(camera);
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
