package graphics;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.HashMap;

import camera.Camera;
import entity.Entity;
import graphics.model.Model;
import graphics.shader.BasicShader;
import graphics.shader.Shader;
import math.Matrix4f;
import math.Transform;
import math.Vector3f;

public class MasterRenderer {
  
  private BasicShader basicShader;
  private EntityRenderer entityRenderer;
  private Matrix4f projectionMatrix;
  private HashMap<Model, ArrayList<Entity>> entities = new HashMap<>();

  public MasterRenderer() {
    init();
    
    basicShader = new BasicShader();
    entityRenderer = new EntityRenderer(basicShader);
    
    updateProjection(70, 640, 360, 0.1f, 1000f);
  }
  
  private void init() {
    glEnable(GL_DEPTH_TEST);
  }
  
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
  
  private void prepare() {
    glClearColor(0.53f, 0.81f, 0.98f, 1f);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
  }
  
  public void render(Camera camera) {
    Matrix4f viewMatrix = Transform.getViewMatrix(camera);
    
    prepare();
    
    basicShader.bind();
    basicShader.updateViewMatrix(viewMatrix);
    basicShader.updateSun(new Vector3f(0f, 0f, -1f));
    entityRenderer.render(entities);
    
    Shader.unbind();
    entities.clear();
  }
  
  public void cleanUp() {
    
  }
  
  private void updateProjection(float fov, int width, int height, float zNear, float zFar) {
    projectionMatrix = Transform.getPerspectiveProjection(fov, width, height, zNear, zFar);
    
    basicShader.bind();
    basicShader.updateProjectionMatrix(projectionMatrix);
    
    Shader.unbind();
  }
  
}
