package graphics;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.HashMap;

import camera.Camera;
import entity.Entity;
import graphics.model.Model;
import graphics.shader.BasicShader2;
import graphics.shader.Shader;
import org.joml.Matrix4f;

public class MasterRenderer {
  
  private BasicShader2 basicShader;
  private EntityRenderer entityRenderer;

  private Transformation transform;
  
  private HashMap<Model, ArrayList<Entity>> entities = new HashMap<>();
  
  private int width;
  private int height;

  public MasterRenderer(int width, int height) {
    init();
    
    basicShader = new BasicShader2();
    entityRenderer = new EntityRenderer(basicShader);
    
    transform = new Transformation();
    
    this.width = width;
    this.height = height;
    
    // Draw in line-frame mode
    //glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
  }
  
  private void init() {
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
  
  public void render(Camera camera) {
    //Matrix4f viewMatrix = Transform.getViewMatrix(camera);
    
    clear();
    
    basicShader.bind();

    float fov = (float) Math.toRadians(70f);
    float aspect = (float) width / height;
    updateProjection(fov, aspect, 0.01f, 1000f);
    
    entityRenderer.render(entities, camera);
    
    Shader.unbind();
    entities.clear();
  }
  
  private void updateProjection(float fov, float aspect, float zNear, float zFar) {
    Matrix4f projectionMatrix = transform.getProjectionMatrix(fov, aspect, zNear, zFar);
    basicShader.updateProjection(projectionMatrix);
  }

  public void cleanUp() {
    basicShader.cleanUp();
  }
  
}
