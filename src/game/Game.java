package game;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import audioEngine.AudioMaster;
import gameEngine.entities.Camera;
import gameEngine.entities.Entity;
import gameEngine.entities.Light;
import gameEngine.guis.GuiRenderer;
import gameEngine.models.RawModel;
import gameEngine.models.TexturedModel;
import gameEngine.objConverter.ModelData;
import gameEngine.objConverter.OBJFileLoader;
import gameEngine.renderEngine.DisplayManager;
import gameEngine.renderEngine.Loader;
import gameEngine.renderEngine.MasterRenderer;
import gameEngine.terrains.Terrain;
import gameEngine.textures.ModelTexture;
import gameEngine.textures.TerrainTexture;
import gameEngine.textures.TerrainTexturePack;
import gameEngine.toolbox.MousePicker;
import physics.Ship;
import placeholders.FlatGroundProvider;
import placeholders.LwjglController;
import trackDesign.SeedTrack;
import trackDesign.TrackMaker;
import trackDesign.TrackPoint;

public class Game {
  
  private Loader loader;
  private ArrayList<Entity> entities;
  private ArrayList<Entity> normalEntities;
  private Terrain[][] terrains;
  private ArrayList<Light> lights;
  private Ship player;
  private Camera camera;
  private MousePicker picker;
  private MasterRenderer renderer;
  private GuiRenderer guiRender;
  private long trackSeed;
  private ArrayList<TrackPoint> trackPoints;
  
  public Game() {
    init();
  }
  
  private void init() {
    DisplayManager.createDisplay();
    loader = new Loader();
    AudioMaster.init();
    AL10.alDistanceModel(AL11.AL_LINEAR_DISTANCE_CLAMPED);

    entities = new ArrayList<Entity>();
    normalEntities = new ArrayList<Entity>();

    // Terrain
    TerrainTexture background = new TerrainTexture(loader.loadTexture("grassy2"));
    TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
    TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
    TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
    TerrainTexturePack texturePack = new TerrainTexturePack(background, rTexture, gTexture,
        bTexture);

    TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

    int size = 1;
    terrains = new Terrain[size][size];
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        terrains[i][j] = new Terrain((int) (Terrain.SIZE) * i, (int) (Terrain.SIZE) * j, loader,
            texturePack, blendMap, "new/FlatMap");
      }
    }
    
    
    
    
    
    // Track
    SeedTrack st = TrackMaker.makeTrack(10, 20, 30, 1, 40, 40, 4);
    trackPoints = st.getTrack();
    trackSeed = st.getSeed();
    
    float trackWidth = 10;
    float trackHeight = 0; 
    
    float[] vertices = new float[trackPoints.size() * 3 * 3];
    int[] indices = new int[trackPoints.size() * 3 * 4];
    float[] texCoords = new float[indices.length];
    float[] normals = new float[vertices.length];
    
    // TODO Actually implement textures
    for (int i = 0; i < texCoords.length; i++) {
      texCoords[i] = 0;
    }
    
    // TODO Actually implement normals
    for (int i = 0; i < normals.length; i += 3) {
      normals[i] = 0;
      normals[i+1] = 1;
      normals[i+2] = 0;
    }
    
    for (int i = 0; i < trackPoints.size(); i++) {
      TrackPoint curPoint = trackPoints.get(i);
      int j = i;
      if (j + 1 >= trackPoints.size()) j = -1;
      TrackPoint nextPoint = trackPoints.get(j + 1);
      
      Vector2f dirVec = new Vector2f(nextPoint.getX() - curPoint.getX(), nextPoint.getY() - curPoint.getY()).normalize();
      Vector2f left = new Vector2f(-dirVec.y, dirVec.x).mul(trackWidth / 2);
      Vector2f right = new Vector2f(dirVec.y, -dirVec.x).mul(trackWidth / 2);
      
      vertices[i * 9] = curPoint.getX() + left.x;
      vertices[i * 9 + 1] = trackHeight;
      vertices[i * 9 + 2] = curPoint.getY() + left.y;
      
      vertices[i * 9 + 3] = curPoint.getX();
      vertices[i * 9 + 4] = trackHeight;
      vertices[i * 6 + 5] = curPoint.getY();
      
      vertices[i * 6 + 6] = curPoint.getX() + right.x;
      vertices[i * 6 + 7] = trackHeight;
      vertices[i * 6 + 8] = curPoint.getY() + right.y;
      
      if (i > 0) {
        int k = i * 3;
        
        indices[i * 12 + 0] = k - 3;
        indices[i * 12 + 1] = k - 2;
        indices[i * 12 + 2] = k;
        
        indices[i * 12 + 3] = k - 3;
        indices[i * 12 + 4] = k - 2;
        indices[i * 12 + 5] = k + 1;
        
        indices[i * 12 + 6] = k - 1;
        indices[i * 12 + 7] = k + 2;
        indices[i * 12 + 8] = k - 2;
        
        indices[i * 12 + 9] = k - 1;
        indices[i * 12 + 10] = k + 1;
        indices[i * 12 + 11] = k - 2;
      }
    }
    
    TexturedModel trackModel = new TexturedModel(
        loader.loadToVAO(vertices, texCoords, normals, indices),
        new ModelTexture(loader.loadTexture("mud")));
    
    Entity track = new Entity(trackModel, new Vector3f(20, 0, 20), new Vector3f(), 1f);
    entities.add(track);
    
    
    
    
    
    
    

    // Lighting
    lights = new ArrayList<Light>();
    Light sun = new Light(
        new Vector3f((float) Math.cos(0), 100, (float) Math.sin(0) + Terrain.SIZE / 2),
        new Vector3f(1f, 1f, 1f));
    lights.add(sun);

    // Player Ship
    TexturedModel playerTModel = new TexturedModel(getModel("newShip", loader),
        new ModelTexture(loader.loadTexture("newShipTexture")));
    LwjglController input = new LwjglController();
    ArrayList<Ship> otherShips = new ArrayList<>();
    player = new Ship(playerTModel, new Vector3f(50, 20, 50), otherShips, input, new FlatGroundProvider(-40f));
    entities.add(player);

    // Player following camera
    camera = new Camera(player);

    // Renderers
    renderer = new MasterRenderer(loader);
    guiRender = new GuiRenderer(loader);

    // Camera rotation with right click
    picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrains);
  }
  
  public void update(double delta) {
    player.update((float) delta);
    camera.move();
    picker.update();
  }
  
  public void render() {
    GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
    renderer.renderScene(entities, normalEntities, terrains, lights, camera, new Vector4f());
    GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
    DisplayManager.updateDisplay();
    sortLights(lights, player.getPosition());
  }
  
  public void cleanUp() {
    guiRender.cleanUp();
    renderer.cleanUp();
    loader.cleanUp();
    AudioMaster.cleanUP();
    DisplayManager.closeDisplay();
  }

  public boolean shouldClose() {
    return Display.isCloseRequested();
  }

  private static RawModel getModel(String fileName, Loader loader) {
    ModelData data = OBJFileLoader.loadOBJ(fileName);

    return loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(),
        data.getIndices());
  }

  private static void sortLights(List<Light> lights, Vector3f currentPosition) {
    float[] distance = new float[lights.size() - 1];
    for (int i = 1; i < lights.size(); i++) {
      distance[i - 1] = lights.get(i).getdistance(currentPosition);
    }
  }
  
}
