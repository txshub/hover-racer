package game;

import java.util.ArrayList;
import java.util.List;

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
import gameEngine.toolbox.VecCon;
import physics.Ship;
import physics.Vector3;
import placeholders.FlatGroundProvider;
import placeholders.KeyboardController;

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
            texturePack, blendMap, "new/flatMap");
      }
    }

    // Lighting
    lights = new ArrayList<Light>();
    Light sun = new Light(
        VecCon.toLWJGL3(new Vector3f((float) Math.cos(0), 100, (float) Math.sin(0) + Terrain.SIZE / 2)),
        VecCon.toLWJGL3(new Vector3f(1f, 1f, 1f)));
    lights.add(sun);

    // Player Ship
    TexturedModel playerTModel = new TexturedModel(getModel("newShip", loader),
        new ModelTexture(loader.loadTexture("newShipTexture")));
    KeyboardController input = new KeyboardController();
    ArrayList<Ship> otherShips = new ArrayList<>();
    player = new Ship(playerTModel, new Vector3(50, 0, 50), otherShips, input, new FlatGroundProvider(2f));
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
    camera.move(terrains);
    picker.update();
  }
  
  public void render() {
    GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
    renderer.renderScene(entities, normalEntities, terrains, lights, camera,
        VecCon.toLWJGL4(new Vector4f((float) Math.sin(Math.toRadians(player.getRoty())), 0,
            (float) Math.cos(Math.toRadians(player.getRoty())), 10f)));
    GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
    DisplayManager.updateDisplay();
    sortLights(lights, VecCon.toJOML3(player.getPosition()));
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
      distance[i - 1] = lights.get(i).getdistance(VecCon.toLWJGL3(currentPosition));
    }
  }

}
