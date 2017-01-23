package game;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;

import org.joml.Vector3f;

import engine.IGameLogic;
import engine.camera.Camera;
import engine.entity.Entity;
import engine.graphics.MasterRenderer;
import engine.graphics.Window;
import engine.graphics.model.Material;
import engine.graphics.model.Model;
import engine.graphics.model.OBJLoader;
import engine.graphics.model.Texture;
import engine.graphics.shader.PointLight;
import engine.graphics.shader.PointLight.Attenuation;
import engine.input.Input;

public class Game implements IGameLogic {
  
  private ArrayList<Entity> entities;
  private Camera camera;
  private PointLight pointLight;
  private MasterRenderer renderer;
  private Vector3f ambientLight;

  public Game() {
    entities = new ArrayList<>();
  }

  @Override
  public void init(Window window) {
    renderer = new MasterRenderer();
    renderer.init();
    
    // Set up camera
    camera = new Camera();
    
    // Set up lighting
    ambientLight = new Vector3f(0.3f, 0.3f, 0.3f);
    Vector3f lightColor = new Vector3f(1f, 1f, 1f);
    Vector3f lightPosition = new Vector3f(0f, 0f, 1f);
    float lightIntensity = 2.0f;
    pointLight = new PointLight(lightColor, lightPosition, lightIntensity);
    pointLight.setAttenuation(new Attenuation(0f, 0f, 0.5f));
    
    Model cube = OBJLoader.loadModel("cube.obj");
    Texture texture = new Texture("grass.png");
    Material material = new Material(texture, 1f);
    cube.setMaterial(material);
    entities.add(new Entity(cube, new Vector3f(3f, 0f, -2f)));
    entities.add(new Entity(cube, new Vector3f(-2f, -2f, -2f)));
    
    Model bunny = OBJLoader.loadModel("bunny.obj");
    bunny.setMaterial(new Material(new Vector3f(0.8f, 0.5f, 0.5f), 2f));
    entities.add(new Entity(bunny, new Vector3f(0f, 0f, -5f)));
  }

  @Override
  public void input(Window window, Input input) {
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
  }

  @Override
  public void update(Window window, double delta) {
    
  }

  @Override
  public void render(Window window) {
    // Process the entities for rendering
    for (int i = 0; i < entities.size(); i++) renderer.processEntity(entities.get(i));
    
    renderer.render(window, camera, ambientLight, pointLight);
  }

  @Override
  public void cleanUp() {
    renderer.cleanUp();
  }

}
