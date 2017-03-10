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
      int n = i * 5;
      int offset = i * 30;

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

//      else {
//        // If we are at the last point we should join to the start of the track
//        addToArray(n, 2, 0, indices, offset);
//        addToArray(n, n + 1, 2, indices, offset + 3);
//
//        addToArray(n + 1, 3, 2, indices, offset + 6);
//        addToArray(n + 1, n + 2, 3, indices, offset + 9);
//
//        addToArray(n + 2, 4, 3, indices, offset + 12);
//        addToArray(n + 2, n + 3, 4, indices, offset + 15);
//
//        addToArray(n + 3, 5, 4, indices, offset + 18);
//        addToArray(n + 3, n + 4, 5, indices, offset + 21);
//
//        addToArray(n + 4, 6, 5, indices, offset + 24);
//        addToArray(n + 4, n + 5, 5, indices, offset + 27);
//      }
    }

    // for (int i = 0; i <= trackPoints.size(); i++) {
    //
    // if (i < trackPoints.size()) {
    //
    // TrackPoint curPoint = trackPoints.get(i);
    //
    // // If we are at the first point the previous is the last point
    // int p = i == 0 ? trackPoints.size() - 1 : i - 1;
    // TrackPoint prevPoint = trackPoints.get(p);
    //
    // // If we are at the last point the next is the first point
    // p = (i + 1) >= trackPoints.size() ? 0 : i + 1;
    // TrackPoint nextPoint = trackPoints.get(p);
    //
    // // System.out.println("Cur: " + curPoint + " Next: " + nextPoint
    // // + " " + (nextPoint.getX() - curPoint.getX()));
    //
    // Vector2f dirFromPrev = new Vector2f(curPoint.getX() - prevPoint.getX(),
    // curPoint.getY() - prevPoint.getY());
    //
    // Vector2f dirToNext = new Vector2f(nextPoint.getX() - curPoint.getX(),
    // nextPoint.getY() - curPoint.getY());
    //
    // Vector2f dirVec = dirFromPrev.add(dirToNext).normalize();
    //
    // Vector2f left = new Vector2f(dirVec.y, -dirVec.x).mul(curPoint.getWidth()
    // / 2);
    // Vector2f right = new Vector2f(-dirVec.y,
    // dirVec.x).mul(curPoint.getWidth() / 2);
    // Vector2f leftNorm = new Vector2f(left).normalize();
    // Vector2f rightNorm = new Vector2f(right).normalize();
    //
    // // System.out.println("Dir: " + dirVec + " Left: " +
    // // left.length() + " Right: " + right.length());
    //
    // Vector3f normal;
    //
    // // Top Left
    // vertices[i * 15] = curPoint.getX() + left.x;
    // vertices[i * 15 + 1] = trackHeight;
    // vertices[i * 15 + 2] = curPoint.getY() + left.y;
    // normal = new Vector3f(leftNorm.x, 1, leftNorm.y).normalize();
    // normals[i * 15 + 0] = normal.x;
    // normals[i * 15 + 1] = normal.y;
    // normals[i * 15 + 2] = normal.z;
    //
    // // Bottom Left
    // vertices[i * 15 + 3] = curPoint.getX() + left.x;
    // vertices[i * 15 + 4] = 0;
    // vertices[i * 15 + 5] = curPoint.getY() + left.y;
    // normal = new Vector3f(leftNorm.x, -1, leftNorm.y).normalize();
    // normals[i * 15 + 3] = normal.x;
    // normals[i * 15 + 4] = normal.y;
    // normals[i * 15 + 5] = normal.z;
    //
    // // Top Center
    // vertices[i * 15 + 6] = curPoint.getX();
    // vertices[i * 15 + 7] = trackHeight;
    // vertices[i * 15 + 8] = curPoint.getY();
    // normal = new Vector3f(0, 1, 0).normalize();
    // normals[i * 15 + 6] = normal.x;
    // normals[i * 15 + 7] = normal.y;
    // normals[i * 15 + 8] = normal.z;
    //
    // // Top Right
    // vertices[i * 15 + 9] = curPoint.getX() + right.x;
    // vertices[i * 15 + 10] = trackHeight;
    // vertices[i * 15 + 11] = curPoint.getY() + right.y;
    // normal = new Vector3f(rightNorm.x, 1, rightNorm.y).normalize();
    // normals[i * 15 + 9] = normal.x;
    // normals[i * 15 + 10] = normal.y;
    // normals[i * 15 + 11] = normal.z;
    //
    // // Bottom Right
    // vertices[i * 15 + 12] = curPoint.getX() + right.x;
    // vertices[i * 15 + 13] = 0;
    // vertices[i * 15 + 14] = curPoint.getY() + right.y;
    // normal = new Vector3f(rightNorm.x, -1, rightNorm.y).normalize();
    // normals[i * 15 + 12] = normal.x;
    // normals[i * 15 + 13] = normal.y;
    // normals[i * 15 + 14] = normal.z;
    //
    // // System.out.println("Left: " + vertices[i*9] + " " +
    // // vertices[i*9+1] + " " + vertices[i*9+2]
    // // + " Center: " + vertices[i*9+3] + " " + vertices[i*9+4] + " "
    // // + vertices[i*9+5]
    // // + " Right: " + vertices[i*9+6] + " " + vertices[i*9+7] + " "
    // // + vertices[i*9+8]);
    // }
    //
    // int k = i * 5;
    // if (i > 0 && i < trackPoints.size()) {
    //
    // indices[(i - 1) * 24 + 0] = k - 4;
    // indices[(i - 1) * 24 + 1] = k;
    // indices[(i - 1) * 24 + 2] = k + 1;
    //
    // indices[(i - 1) * 24 + 3] = k - 5;
    // indices[(i - 1) * 24 + 4] = k;
    // indices[(i - 1) * 24 + 5] = k - 4;
    //
    // indices[(i - 1) * 24 + 6] = k + 2;
    // indices[(i - 1) * 24 + 7] = k;
    // indices[(i - 1) * 24 + 8] = k - 5;
    //
    // indices[(i - 1) * 24 + 9] = k - 3;
    // indices[(i - 1) * 24 + 10] = k + 2;
    // indices[(i - 1) * 24 + 11] = k - 5;
    //
    // indices[(i - 1) * 24 + 12] = k - 3;
    // indices[(i - 1) * 24 + 13] = k - 2;
    // indices[(i - 1) * 24 + 14] = k + 2;
    //
    // indices[(i - 1) * 24 + 15] = k - 2;
    // indices[(i - 1) * 24 + 16] = k + 3;
    // indices[(i - 1) * 24 + 17] = k + 2;
    //
    // indices[(i - 1) * 24 + 18] = k - 1;
    // indices[(i - 1) * 24 + 19] = k + 3;
    // indices[(i - 1) * 24 + 20] = k - 2;
    //
    // indices[(i - 1) * 24 + 21] = k - 1;
    // indices[(i - 1) * 24 + 22] = k + 4;
    // indices[(i - 1) * 24 + 23] = k + 3;
    // } else if (i > 0) {
    //
    // indices[(i - 1) * 24 + 0] = k - 4;
    // indices[(i - 1) * 24 + 1] = 0;
    // indices[(i - 1) * 24 + 2] = 0 + 1;
    //
    // indices[(i - 1) * 24 + 3] = k - 5;
    // indices[(i - 1) * 24 + 4] = 0;
    // indices[(i - 1) * 24 + 5] = k - 4;
    //
    // indices[(i - 1) * 24 + 6] = 0 + 2;
    // indices[(i - 1) * 24 + 7] = 0;
    // indices[(i - 1) * 24 + 8] = k - 5;
    //
    // indices[(i - 1) * 24 + 9] = k - 3;
    // indices[(i - 1) * 24 + 10] = 0 + 2;
    // indices[(i - 1) * 24 + 11] = k - 5;
    //
    // indices[(i - 1) * 24 + 12] = k - 3;
    // indices[(i - 1) * 24 + 13] = k - 2;
    // indices[(i - 1) * 24 + 14] = 0 + 2;
    //
    // indices[(i - 1) * 24 + 15] = k - 2;
    // indices[(i - 1) * 24 + 16] = 0 + 3;
    // indices[(i - 1) * 24 + 17] = 0 + 2;
    //
    // indices[(i - 1) * 24 + 18] = k - 1;
    // indices[(i - 1) * 24 + 19] = 0 + 3;
    // indices[(i - 1) * 24 + 20] = k - 2;
    //
    // indices[(i - 1) * 24 + 21] = k - 1;
    // indices[(i - 1) * 24 + 22] = 0 + 4;
    // indices[(i - 1) * 24 + 23] = 0 + 3;
    // }
    // }
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