package game;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import audioEngine.AudioMaster;
import gameEngine.entities.Camera;
import gameEngine.entities.Entity;
import gameEngine.entities.Light;
import gameEngine.entities.Player;
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
import placeholders.InputController;
import placeholders.InputController.Action;
import trackDesign.SeedTrack;
import trackDesign.TrackMaker;
import trackDesign.TrackPoint;

public class Game {

  private Loader loader;
  private ArrayList<Entity> entities;
  private ArrayList<Entity> normalEntities;
  private Terrain[][] terrains;
  private ArrayList<Light> lights;
  private Player player;
  private Camera camera;
  private MousePicker picker;
  private MasterRenderer renderer;
  private GuiRenderer guiRender;
  private long trackSeed;
  private ArrayList<TrackPoint> trackPoints;
  public static InputController input;

  private boolean running;

  public Game() {
    init();
  }

  private void init() {
    running = true;

    DisplayManager.createDisplay();
    loader = new Loader();
    Game.input = new InputController();
    AudioMaster.init();
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
            texturePack, blendMap, "new/FlatHeightMap");
      }
    }

    // Track
    SeedTrack st = TrackMaker.makeTrack(10, 20, 30, 1, 40, 40, 4);
    trackPoints = st.getTrack();
    trackSeed = st.getSeed();

    TexturedModel trackModel = createTrackModel(trackSeed);
    Entity track = new Entity(trackModel, new Vector3f(0, 0, 0), new Vector3f(), 2f);
    entities.add(track);

    // Lighting
    lights = new ArrayList<Light>();
    Light sun = new Light(
        new Vector3f(256, 500, 256),
        new Vector3f(1f, 1f, 1f));
    lights.add(sun);

    // Player Ship
    TexturedModel playerTModel = new TexturedModel(getModel("newShip", loader),
        new ModelTexture(loader.loadTexture("newShipTexture")));
    ArrayList<Ship> otherShips = new ArrayList<>();
    player = new Player(playerTModel, new Vector3f(50, 20, 50), new Vector3f(), 1f, input);
    entities.add(player);

    // Player following camera
    camera = new Camera(player);

    // Renderers
    renderer = new MasterRenderer(loader);
    guiRender = new GuiRenderer(loader);

    // Camera rotation with right click
    picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrains);

    // Tudor
    AudioMaster.playInGameMusic();
  }

  public void update(double delta) {
    input.run();
    
    // Check if the escape key was pressed to exit the game
    if (input.checkAction(Action.EXIT))
      running = false;
    
    // Check for audio controls
    /** @author Tudor */
    if (input.checkAction(Action.MUSIC_UP)) AudioMaster.increaseMusicVolume();
    if (input.checkAction(Action.MUSIC_DOWN)) AudioMaster.decreaseMusicVolume();
    if (input.checkAction(Action.MUSIC_SKIP)) AudioMaster.skipInGameMusic();
    if (input.checkAction(Action.SFX_UP)) AudioMaster.increaseSFXVolume();
    if (input.checkAction(Action.SFX_DOWN)) AudioMaster.decreaseSFXVolume();

    player.update((float) delta);
    camera.move();
//    picker.update();
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
    InputController.close = true;
    AudioMaster.cleanUP();
    DisplayManager.closeDisplay();
  }

  public boolean shouldClose() {
    return Display.isCloseRequested() || !running;
  }

  private static RawModel getModel(String fileName, Loader loader) {
    ModelData data = OBJFileLoader.loadOBJ(fileName);

    return loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(),
        data.getIndices());
  }

  private TexturedModel createTrackModel(long seed) {
      float trackWidth = 50;
      float trackHeight = 0;
  
      float[] vertices = new float[trackPoints.size() * 3 * 3];
      int[] indices = new int[trackPoints.size() * 3 * 4];
      float[] texCoords = new float[indices.length];
      float[] normals = new float[vertices.length];
  
      // TODO Actually implement textures
  //    for (int i = 0; i < texCoords.length; i += 2) {
  //      if ((i / 2) % 2 == 0) {
  //        texCoords[i] = 0;
  //        texCoords[i+1] = 0;
  //      } else {
  //        texCoords[i] = 1;
  //        texCoords[i+1] = 3;
  //      }
  //    }
      
      for (int i = 0; i < texCoords.length; i++) {
        texCoords[i] = 0;
      }
  
      // TODO Actually implement normals
      for (int i = 0; i < normals.length; i += 3) {
        normals[i] = 0;
        normals[i + 1] = 1;
        normals[i + 2] = 0;
      }
      
      // Scale up the track
      for (TrackPoint p : trackPoints) {
        p.mul(5);
      }
  
      for (int i = 0; i <= trackPoints.size(); i++) {
  
        if (i < trackPoints.size()) {
  
          TrackPoint curPoint = trackPoints.get(i);
          
          // If we are at the first point the previous is the last point
          int p = i == 0 ? trackPoints.size() - 1 : i - 1;
          TrackPoint prevPoint = trackPoints.get(p);
          
          // If we are at the last point the next is the first point
          p = (i + 1) >= trackPoints.size() ? 0 : i + 1; 
          TrackPoint nextPoint = trackPoints.get(p);
  
          // System.out.println("Cur: " + curPoint + " Next: " + nextPoint
          // + " " + (nextPoint.getX() - curPoint.getX()));
  
          Vector2f dirFromPrev = new Vector2f(curPoint.getX() - prevPoint.getX(),
              curPoint.getY() - prevPoint.getY());
          
          Vector2f dirToNext = new Vector2f(nextPoint.getX() - curPoint.getX(),
              nextPoint.getY() - curPoint.getY());
          
          Vector2f dirVec = dirFromPrev.add(dirToNext).normalize();
  
          Vector2f left = new Vector2f(dirVec.y, -dirVec.x).mul(trackWidth / 2);
          Vector2f right = new Vector2f(-dirVec.y, dirVec.x).mul(trackWidth / 2);
  
          // System.out.println("Dir: " + dirVec + " Left: " +
          // left.length() + " Right: " + right.length());
  
          vertices[i * 9] = curPoint.getX() + left.x;
          vertices[i * 9 + 1] = trackHeight;
          vertices[i * 9 + 2] = curPoint.getY() + left.y;
  
          vertices[i * 9 + 3] = curPoint.getX();
          vertices[i * 9 + 4] = trackHeight;
          vertices[i * 9 + 5] = curPoint.getY();
  
          vertices[i * 9 + 6] = curPoint.getX() + right.x;
          vertices[i * 9 + 7] = trackHeight;
          vertices[i * 9 + 8] = curPoint.getY() + right.y;
  
          // System.out.println("Left: " + vertices[i*9] + " " +
          // vertices[i*9+1] + " " + vertices[i*9+2]
          // + " Center: " + vertices[i*9+3] + " " + vertices[i*9+4] + " "
          // + vertices[i*9+5]
          // + " Right: " + vertices[i*9+6] + " " + vertices[i*9+7] + " "
          // + vertices[i*9+8]);
        }
  
        int k = i * 3;
        if (i > 0 && i < trackPoints.size()) {
  
          indices[(i - 1) * 12 + 0] = k - 3;
          indices[(i - 1) * 12 + 1] = k - 2;
          indices[(i - 1) * 12 + 2] = k + 1;
  
          indices[(i - 1) * 12 + 3] = k - 3;
          indices[(i - 1) * 12 + 4] = k + 1;
          indices[(i - 1) * 12 + 5] = k;
  
          indices[(i - 1) * 12 + 6] = k - 2;
          indices[(i - 1) * 12 + 7] = k - 1;
          indices[(i - 1) * 12 + 8] = k + 1;
  
          indices[(i - 1) * 12 + 9] = k - 1;
          indices[(i - 1) * 12 + 10] = k + 2;
          indices[(i - 1) * 12 + 11] = k + 1;
        } else if (i > 0) {
  
          indices[(i - 1) * 12 + 0] = k - 3;
          indices[(i - 1) * 12 + 1] = k - 2;
          indices[(i - 1) * 12 + 2] = 0 + 1;
  
          indices[(i - 1) * 12 + 3] = k - 3;
          indices[(i - 1) * 12 + 4] = 0 + 1;
          indices[(i - 1) * 12 + 5] = 0;
  
          indices[(i - 1) * 12 + 6] = k - 2;
          indices[(i - 1) * 12 + 7] = k - 1;
          indices[(i - 1) * 12 + 8] = 0 + 1;
  
          indices[(i - 1) * 12 + 9] = k - 1;
          indices[(i - 1) * 12 + 10] = 0 + 2;
          indices[(i - 1) * 12 + 11] = 0 + 1;
        }
      }
      return new TexturedModel(loader.loadToVAO(vertices, texCoords, normals, indices),
          new ModelTexture(loader.loadTexture("mud")));
  
    }

  private static void sortLights(List<Light> lights, Vector3f currentPosition) {
    float[] distance = new float[lights.size() - 1];
    for (int i = 1; i < lights.size(); i++) {
      distance[i - 1] = lights.get(i).getdistance(currentPosition);
    }
  }
}