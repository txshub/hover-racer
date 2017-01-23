package engine.graphics;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import engine.camera.Camera;
import engine.entity.Entity;
import engine.graphics.model.Model;
import engine.graphics.shader.BasicShader2;
import engine.graphics.shader.PointLight;
import engine.graphics.shader.Shader;

public class MasterRenderer {
  
  private BasicShader2 shader;
  private EntityRenderer entityRenderer;

  private Transformation transform;
  
  private HashMap<Model, ArrayList<Entity>> entities = new HashMap<>();

  public MasterRenderer() {
    shader = new BasicShader2();
    entityRenderer = new EntityRenderer(shader);
    
    transform = new Transformation();
    
    // Draw in line-frame mode
    //glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
  }
  
  public void init() {
    glEnable(GL_DEPTH_TEST);
  }
  
  /**
   * Processes entities so that entities with the same model can all use the
   * same instance of that model.
   * 
   * @param entity the entity to process
   */
  public void processEntity(Entity entity) {
    // See what model the entity has and get the ArrayList of entities with that model
    Model model = entity.getModel();
    ArrayList<Entity> batch = entities.get(model);
    
    // If this is the first entity with that model then create and add an ArrayList
    if (batch == null) {
      batch = new ArrayList<>();
      entities.put(model, batch);
    }
    
    // Put this new entity into the ArrayList of entities with that model
    batch.add(entity);
  }
  
  private void clear() {
    glClearColor(0.53f, 0.81f, 0.98f, 1f);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
  }
  
  public void render(Window window, Camera camera, Vector3f ambientLight, PointLight pointLight) {
    //Matrix4f viewMatrix = Transform.getViewMatrix(camera);
    
    clear();
    
    shader.bind();

    // Update projection matrix
    float fov = (float) Math.toRadians(70f);
    float aspect = (float) window.getWidth() / window.getHeight();
    Matrix4f projectionMatrix = transform.getProjectionMatrix(fov, aspect, 0.01f, 1000);
    shader.updateProjectionMatrix(projectionMatrix);
    
    entityRenderer.render(entities, camera, ambientLight, pointLight);
    
    Shader.unbind();
    entities.clear();
  }

  public void cleanUp() {
    shader.cleanUp();
  }
  
}
