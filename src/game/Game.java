package game;

import java.util.ArrayList;
import java.util.List;

import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
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
import gameEngine.objConverter.OBJFileLoader;
import gameEngine.renderEngine.DisplayManager;
import gameEngine.renderEngine.Loader;
import gameEngine.renderEngine.MasterRenderer;
import gameEngine.skybox.SkyboxRenderer;
import gameEngine.terrains.Terrain;
import gameEngine.textures.ModelTexture;
import gameEngine.textures.TerrainTexture;
import gameEngine.textures.TerrainTexturePack;
import input.Action;
import input.InputController;
import input.KeyboardController;
import physics.core.Ship;
import physics.placeholders.FlatGroundProvider;
import physics.ships.MultiplayerShipManager;
import trackDesign.SeedTrack;
import trackDesign.TrackMaker;
import trackDesign.TrackPoint;

/** @author Reece Bennett and rtm592 */
public class Game implements GameInt {

  // Set this to print debug messages
  public static boolean debug = true;

  private Loader loader;
  private ArrayList<Entity> entities;
  private ArrayList<Entity> normalEntities;
  private ArrayList<Terrain> terrains;
  private ArrayList<Light> lights;
  private Ship player;
  private Camera camera;
  // private MousePicker picker;
  private MasterRenderer renderer;
  private GuiRenderer guiRender;
  private long trackSeed;
  private ArrayList<TrackPoint> trackPoints;
  public static InputController input;

  // Mac's additions
  private MultiplayerShipManager ships;

  private boolean running;

  // Tudor
  private GameLogic logic;

  public Game() {
    init();
  }

  private void init() {
    running = true;

    DisplayManager.createDisplay();
    loader = new Loader();
    Game.input = new KeyboardController();
    AudioMaster.init();
    entities = new ArrayList<Entity>();
    normalEntities = new ArrayList<Entity>();

    // Terrain
    TerrainTexture background = new TerrainTexture(loader.loadTexture("new/GridTexture"));
    TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("new/GridTexture"));
    TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("new/GridTexture"));
    TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("new/GridTexture"));
    TerrainTexturePack texturePack = new TerrainTexturePack(background, rTexture, gTexture,
        bTexture);

    // TerrainTexture blendMap = new
    // TerrainTexture(loader.loadTexture("blendMap"));

    TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("new/GridTexture"));

    terrains = new ArrayList<Terrain>();
    terrains.add(new Terrain((int) (-SkyboxRenderer.SIZE * 2f), (int) (-SkyboxRenderer.SIZE * 2f),
        loader, texturePack, blendMap, "new/FlatHeightMap"));

    // Track
    SeedTrack st = TrackMaker.makeTrack(10, 20, 30, 1, 40, 40, 4);
    // Scale up the track so it isn't so tiny
    for (TrackPoint tp : st.getTrack()) {
      tp.mul(20);
    }
    trackPoints = st.getTrack();
    trackSeed = st.getSeed();

    TexturedModel trackModel = createTrackModel(trackSeed);
    Entity track = new Entity(trackModel, new Vector3f(0, 0, 0), new Vector3f(), 1f);
    entities.add(track);

    // Lighting
    lights = new ArrayList<Light>();
    Light sun = new Light(new Vector3f(0, 100000, 0), new Vector3f(1f, 1f, 1f));
    lights.add(sun);

    // Creating ships
    TexturedModel playerTModel = new TexturedModel(getModel("newShip", loader),
        new ModelTexture(loader.loadTexture("newShipTexture")));
    ArrayList<TexturedModel> shipTextures = new ArrayList<TexturedModel>();
    shipTextures.add(playerTModel);
    ArrayList<Vector3f> startingPositions = new ArrayList<Vector3f>();
    startingPositions.add(new Vector3f(st.getTrack().get(0).x * track.getScale(), 10,
        st.getTrack().get(0).y * track.getScale()));
    startingPositions.add(new Vector3f(st.getTrack().get(0).x * track.getScale() + 20, 10,
        st.getTrack().get(0).y * track.getScale()));

    // Create ships
    ships = new MultiplayerShipManager((byte) 0, input, playerTModel, shipTextures,
        startingPositions, new FlatGroundProvider(0));
    ships.addShipsTo(entities);
    player = ships.getPlayerShip();

    // Player following camera
    camera = new Camera(player);

    // Renderers
    renderer = new MasterRenderer(loader);
    guiRender = new GuiRenderer(loader);

    // Camera rotation with right click
    // picker = new MousePicker(camera, renderer.getProjectionMatrix(),
    // terrains);

    // Tudor
    ArrayList<Ship> opponents = new ArrayList<Ship>();
    logic = new GameLogic(player, opponents, st, 4);

    AudioMaster.playInGameMusic();
    try {
      Keyboard.create();
    } catch (LWJGLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void update(float delta) {
    input.update();

    // Tudor
    logic.update();
    // Check if the escape key was pressed to exit the game
    if (input.isDown(Action.EXIT) > 0.5f)
      running = false;

    // Check for audio controls
    /** @author Tudor */
    if (input.wasPressed(Action.MUSIC_UP) > 0.5f)
      AudioMaster.increaseMusicVolume();
    if (input.wasPressed(Action.MUSIC_DOWN) > 0.5f)
      AudioMaster.decreaseMusicVolume();
    if (input.wasPressed(Action.MUSIC_SKIP) > 0.5f)
      AudioMaster.skipInGameMusic();
    if (input.wasPressed(Action.SFX_UP) > 0.5f)
      AudioMaster.increaseSFXVolume();
    if (input.wasPressed(Action.SFX_DOWN) > 0.5f)
      AudioMaster.decreaseSFXVolume();

    ships.updateShips((float) delta);
    camera.move();

    // move terrain based on player location so terrain seems infinite
    if (player.getPosition().x > terrains.get(0).getX() + Terrain.SIZE * 3 / 4) {
      terrains.get(0).moveX(Terrain.SIZE / 4);
    } else if (player.getPosition().x < terrains.get(0).getX() + Terrain.SIZE * 1 / 4) {
      terrains.get(0).moveX(-Terrain.SIZE / 4);
    }

    if (player.getPosition().z > terrains.get(0).getZ() + Terrain.SIZE * 3 / 4) {
      terrains.get(0).moveZ(Terrain.SIZE / 4);
    } else if (player.getPosition().z < terrains.get(0).getZ() + Terrain.SIZE * 1 / 4) {
      terrains.get(0).moveZ(-Terrain.SIZE / 4);
    }
    // picker.update();
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
    AudioMaster.cleanUp();
    DisplayManager.closeDisplay();
  }

  public boolean shouldClose() {
    return Display.isCloseRequested() || !running;
  }

  private static RawModel getModel(String fileName, Loader loader) {
    RawModel data = OBJFileLoader.loadOBJ(fileName, loader);

    return data;
  }

  /**
   * Create a track model from a seed
   * 
   * @param seed
   * @return
   * @author Reece Bennett
   */
  private TexturedModel createTrackModel(long seed) {
    float trackHeight = 1;
    float barrierHeight = 20;
    float barrierWidth = 10;

    // 6 vertices for each track point, 3 components for each vertex
    float[] vertices = new float[trackPoints.size() * 6 * 3];
    // 10 triangles for each track point, 3 vertices per triangle
    int[] indices = new int[trackPoints.size() * 10 * 3];
    float[] texCoords = new float[indices.length];
    float[] normals = new float[vertices.length];

    // TODO Actually implement textures
    for (int i = 0; i < texCoords.length; i++) {
      texCoords[i] = 0;
    }

    for (int i = 0; i < normals.length; i += 3) {
      addToArray(new Vector3f(0, 1, 0), normals, i);
    }

    // We can pre-calculate some stuff for normals
    Vector3f normBottom = new Vector3f(0, -1, 0);
    Vector3f normTop = new Vector3f(0, 1, 0);
    Vector3f normLeftOuter2D = new Vector3f(-barrierHeight, barrierWidth, 0).normalize();
    Vector3f normRightOuter2D = new Vector3f(barrierHeight, barrierWidth, 0).normalize();

    // Populate vertex and normal arrays
    for (int i = 0; i < trackPoints.size(); i++) {
      TrackPoint curPoint = trackPoints.get(i);

      // If we are at the first point the previous is the last point
      int prev = (i == 0) ? trackPoints.size() - 1 : i - 1;
      TrackPoint prevPoint = trackPoints.get(prev);

      // If we are at the last point the next is the first point
      int next = (i == trackPoints.size() - 1) ? 0 : i + 1;
      TrackPoint nextPoint = trackPoints.get(next);

      // Find the line between previous and next point for direction of this
      // slice
      Vector2f dirVec = new Vector2f(nextPoint).sub(prevPoint).normalize();

      // Calculate the perpendicular normal vectors
      Vector2f left = new Vector2f(dirVec.y, -dirVec.x).normalize();
      Vector2f right = new Vector2f(-dirVec.y, dirVec.x).normalize();

      // Apply the offsets to the center point
      float w = curPoint.getWidth() / 2;
      Vector3f centerPoint = new Vector3f(curPoint.x, trackHeight, curPoint.y);
      Vector3f leftPoint = new Vector3f(centerPoint).add(left.x * w, 0, left.y * w);
      Vector3f rightPoint = new Vector3f(centerPoint).add(right.x * w, 0, right.y * w);

      // Create barrier points
      float b = barrierWidth;
      Vector3f lBarrierT = new Vector3f(leftPoint).add(0, barrierHeight, 0);
      Vector3f rBarrierT = new Vector3f(rightPoint).add(0, barrierHeight, 0);
      Vector3f lBarrierB = new Vector3f(leftPoint).add(left.x * b, -trackHeight, left.y * b);
      Vector3f rBarrierB = new Vector3f(rightPoint).add(right.x * b, -trackHeight, right.y * b);

      addToArray(lBarrierB, vertices, i * 18 + 0);
      addToArray(lBarrierT, vertices, i * 18 + 3);
      addToArray(leftPoint, vertices, i * 18 + 6);
      addToArray(rightPoint, vertices, i * 18 + 9);
      addToArray(rBarrierT, vertices, i * 18 + 12);
      addToArray(rBarrierB, vertices, i * 18 + 15);

      // First calculate surface normals (technically edge normals as we are
      // working in a slice but whatever)

      // Get Quaternions for rotation to align with left and right vectors
      Vector3f normLeftInner = new Vector3f(left.x, 0, left.y);
      Quaternionf rotationLeft = new Vector3f(-1, 0, 0).rotationTo(normLeftInner,
          new Quaternionf());
      Vector3f normRightInner = new Vector3f(right.x, 0, right.y);
      Quaternionf rotationRight = new Vector3f(1, 0, 0).rotationTo(normRightInner,
          new Quaternionf());

      Vector3f normLeftOuter = new Vector3f(normLeftOuter2D).rotate(rotationLeft);
      Vector3f normRightOuter = new Vector3f(normRightOuter2D).rotate(rotationRight);

      // Calculate the vertex normals
      Vector3f normLBarrierB = new Vector3f(normBottom).add(normLeftOuter).normalize();
      Vector3f normLBarrierT = new Vector3f(normLeftOuter).add(normLeftInner).normalize();
      Vector3f normLPoint = new Vector3f(normLeftInner).add(normTop).normalize();
      Vector3f normRPoint = new Vector3f(normTop).add(normRightInner).normalize();
      Vector3f normRBarrierT = new Vector3f(normRightInner).add(normRightOuter).normalize();
      Vector3f normRBarrerB = new Vector3f(normRightOuter).add(normBottom).normalize();

      addToArray(normLBarrierB, normals, i * 18 + 0);
      addToArray(normLBarrierT, normals, i * 18 + 3);
      addToArray(normLPoint, normals, i * 18 + 6);
      addToArray(normRPoint, normals, i * 18 + 9);
      addToArray(normRBarrierT, normals, i * 18 + 12);
      addToArray(normRBarrerB, normals, i * 18 + 15);
    }
    
    System.out.println(trackPoints.size());

    // Populate indices
    for (int i = 0; i < trackPoints.size(); i++) {
      int n = i * 6;
      int offset = i * 30;

      if (i < trackPoints.size() - 1) {
        addToArray(n, n + 7, n + 6, indices, offset);
        addToArray(n, n + 1, n + 7, indices, offset + 3);
  
        addToArray(n + 1, n + 8, n + 7, indices, offset + 6);
        addToArray(n + 1, n + 2, n + 8, indices, offset + 9);
  
        addToArray(n + 2, n + 9, n + 8, indices, offset + 12);
        addToArray(n + 2, n + 3, n + 9, indices, offset + 15);
  
        addToArray(n + 3, n + 10, n + 9, indices, offset + 18);
        addToArray(n + 3, n + 4, n + 10, indices, offset + 21);
  
        addToArray(n + 4, n + 11, n + 10, indices, offset + 24);
        addToArray(n + 4, n + 5, n + 11, indices, offset + 27);
        
      } else {
        addToArray(n, 1, 0, indices, offset);
        addToArray(n, n + 1, 1, indices, offset + 3);
  
        addToArray(n + 1, 2, 1, indices, offset + 6);
        addToArray(n + 1, n + 2, 2, indices, offset + 9);
  
        addToArray(n + 2, 3, 2, indices, offset + 12);
        addToArray(n + 2, n + 3, 3, indices, offset + 15);
  
        addToArray(n + 3, 4, 3, indices, offset + 18);
        addToArray(n + 3, n + 4, 4, indices, offset + 21);
  
        addToArray(n + 4, 5, 4, indices, offset + 24);
        addToArray(n + 4, n + 5, 5, indices, offset + 27);
        
      }
    }
    
    return new TexturedModel(loader.loadToVAO(vertices, texCoords, normals, indices),
        new ModelTexture(loader.loadTexture("new/TrackTexture")));

  }

  private void addToArray(Vector3f vector, float[] array, int offset) {
    array[offset + 0] = vector.x;
    array[offset + 1] = vector.y;
    array[offset + 2] = vector.z;
  }

  private void addToArray(int first, int second, int third, int[] array, int offset) {
    array[offset++] = first;
    array[offset++] = second;
    array[offset++] = third;
  }

  private static void sortLights(List<Light> lights, Vector3f currentPosition) {
    float[] distance = new float[lights.size() - 1];
    for (int i = 1; i < lights.size(); i++) {
      distance[i - 1] = lights.get(i).getdistance(currentPosition);
    }
  }

}