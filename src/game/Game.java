package game;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import audioEngine.AudioMaster;
import gameEngine.entities.Camera;
import gameEngine.entities.Entity;
import gameEngine.entities.Light;
import gameEngine.guis.GuiRenderer;
import gameEngine.guis.GuiTexture;
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
import physics.core.Ship;
import physics.placeholders.FlatGroundProvider;
import physics.ships.PlayerShip;
import physics.support.Action;
import physics.support.InputController;
import trackDesign.SeedTrack;
import trackDesign.TrackMaker;
import trackDesign.TrackPoint;
import uiToolkit.Button;

/**
 * @author Reece Bennett and rtm592
 *
 */
public class Game {

	// Set this to print debug messages
	public static boolean debug = true;

	private Loader loader;
	private ArrayList<Entity> entities;
	private ArrayList<Entity> normalEntities;
	private ArrayList<Terrain> terrains;
	private ArrayList<Light> lights;
	private ArrayList<GuiTexture> guis;
	private Ship player;
	private Camera camera;
	// private MousePicker picker;
	private MasterRenderer renderer;
	private GuiRenderer guiRender;
	private long trackSeed;
	private ArrayList<TrackPoint> trackPoints;
	public static InputController input;

	private boolean running;
	
	// TODO temporary
	private Button button;

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
    
    System.out.println("Screen size: " + Display.getWidth() + " x " + Display.getHeight());

		// Terrain
		TerrainTexture background = new TerrainTexture(loader.loadTexture("new/GridTexture"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("new/GridTexture"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("new/GridTexture"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("new/GridTexture"));
		TerrainTexturePack texturePack = new TerrainTexturePack(background, rTexture, gTexture, bTexture);

		// TerrainTexture blendMap = new
		// TerrainTexture(loader.loadTexture("blendMap"));

		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("new/GridTexture"));

		terrains = new ArrayList<Terrain>();
		terrains.add(new Terrain((int) (-SkyboxRenderer.SIZE * 2f), (int) (-SkyboxRenderer.SIZE * 2f), loader,
				texturePack, blendMap, "new/FlatHeightMap"));

		// Track
		SeedTrack st = TrackMaker.makeTrack(10, 20, 30, 1, 40, 40, 4);
		trackPoints = st.getTrack();
		trackSeed = st.getSeed();

		TexturedModel trackModel = createTrackModel(trackSeed);
		Entity track = new Entity(trackModel, new Vector3f(0, 0, 0), new Vector3f(), 4f);
		entities.add(track);

		// Lighting
		lights = new ArrayList<Light>();
		Light sun = new Light(new Vector3f(256, 2000, 256), new Vector3f(1f, 1f, 1f));
		lights.add(sun);

		// Player Ship
		TexturedModel playerTModel = new TexturedModel(getModel("newShip", loader),
				new ModelTexture(loader.loadTexture("newShipTexture")));
		player = new PlayerShip((byte) 0, playerTModel,
				new Vector3f(st.getTrack().get(0).x * track.getScale(), 10, st.getTrack().get(0).y * track.getScale()),
				null, new FlatGroundProvider(0), input);
		entities.add(player);

		// Player following camera
		camera = new Camera(player);
		
		// GUIs
		guis = new ArrayList<>();
		GuiTexture logo = new GuiTexture(loader.loadTexture("newShipTexture"), new Vector2f(0f, 0f), new Vector2f(0.5f, 0.5f));
		button = new Button(logo);
		guis.add(logo);

		// Renderers
		renderer = new MasterRenderer(loader);
		guiRender = new GuiRenderer(loader);

		// Camera rotation with right click
		// picker = new MousePicker(camera, renderer.getProjectionMatrix(),
		// terrains);

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
		if (input.checkAction(Action.MUSIC_UP))
			AudioMaster.increaseMusicVolume();
		// if (input.checkAction(Action.MUSIC_DOWN))
		AudioMaster.decreaseMusicVolume();
		if (input.checkAction(Action.MUSIC_SKIP))
			AudioMaster.skipInGameMusic();
		if (input.checkAction(Action.SFX_UP))
			AudioMaster.increaseSFXVolume();
		if (input.checkAction(Action.SFX_DOWN))
			AudioMaster.decreaseSFXVolume();

		player.update((float) delta);
		camera.move();
		
		button.update();

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
		guiRender.render(guis);
		GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
		DisplayManager.updateDisplay();
		sortLights(lights, player.getPosition());
	}

	public void cleanUp() {
		guiRender.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		InputController.close = true;
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

	private TexturedModel createTrackModel(long seed) {
		float trackWidth = 100;
		float trackHeight = 5;

		float[] vertices = new float[trackPoints.size() * 5 * 3];
		int[] indices = new int[trackPoints.size() * 5 * 8];
		float[] texCoords = new float[indices.length];
		float[] normals = new float[vertices.length];

		// TODO Actually implement textures
		// for (int i = 0; i < texCoords.length; i += 2) {
		// if ((i / 2) % 2 == 0) {
		// texCoords[i] = 0;
		// texCoords[i+1] = 0;
		// } else {
		// texCoords[i] = 1;
		// texCoords[i+1] = 3;
		// }
		// }

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
			p.mul(10);
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

				vertices[i * 15] = curPoint.getX() + left.x;
				vertices[i * 15 + 1] = trackHeight;
				vertices[i * 15 + 2] = curPoint.getY() + left.y;

				vertices[i * 15 + 3] = curPoint.getX() + left.x;
				vertices[i * 15 + 4] = trackHeight - 5;
				vertices[i * 15 + 5] = curPoint.getY() + left.y;

				vertices[i * 15 + 6] = curPoint.getX();
				vertices[i * 15 + 7] = trackHeight;
				vertices[i * 15 + 8] = curPoint.getY();

				vertices[i * 15 + 9] = curPoint.getX() + right.x;
				vertices[i * 15 + 10] = trackHeight;
				vertices[i * 15 + 11] = curPoint.getY() + right.y;

				vertices[i * 15 + 12] = curPoint.getX() + right.x;
				vertices[i * 15 + 13] = trackHeight - 5;
				vertices[i * 15 + 14] = curPoint.getY() + right.y;

				// System.out.println("Left: " + vertices[i*9] + " " +
				// vertices[i*9+1] + " " + vertices[i*9+2]
				// + " Center: " + vertices[i*9+3] + " " + vertices[i*9+4] + " "
				// + vertices[i*9+5]
				// + " Right: " + vertices[i*9+6] + " " + vertices[i*9+7] + " "
				// + vertices[i*9+8]);
			}

			int k = i * 5;
			if (i > 0 && i < trackPoints.size()) {

				indices[(i - 1) * 24 + 0] = k - 4;
				indices[(i - 1) * 24 + 1] = k;
				indices[(i - 1) * 24 + 2] = k + 1;

				indices[(i - 1) * 24 + 3] = k - 5;
				indices[(i - 1) * 24 + 4] = k;
				indices[(i - 1) * 24 + 5] = k - 4;

				indices[(i - 1) * 24 + 6] = k + 2;
				indices[(i - 1) * 24 + 7] = k;
				indices[(i - 1) * 24 + 8] = k - 5;

				indices[(i - 1) * 24 + 9] = k - 3;
				indices[(i - 1) * 24 + 10] = k + 2;
				indices[(i - 1) * 24 + 11] = k - 5;

				indices[(i - 1) * 24 + 12] = k - 3;
				indices[(i - 1) * 24 + 13] = k - 2;
				indices[(i - 1) * 24 + 14] = k + 2;

				indices[(i - 1) * 24 + 15] = k - 2;
				indices[(i - 1) * 24 + 16] = k + 3;
				indices[(i - 1) * 24 + 17] = k + 2;

				indices[(i - 1) * 24 + 18] = k - 1;
				indices[(i - 1) * 24 + 19] = k + 3;
				indices[(i - 1) * 24 + 20] = k - 2;

				indices[(i - 1) * 24 + 21] = k - 1;
				indices[(i - 1) * 24 + 22] = k + 4;
				indices[(i - 1) * 24 + 23] = k + 3;
			} else if (i > 0) {

				indices[(i - 1) * 24 + 0] = k - 4;
				indices[(i - 1) * 24 + 1] = 0;
				indices[(i - 1) * 24 + 2] = 0 + 1;

				indices[(i - 1) * 24 + 3] = k - 5;
				indices[(i - 1) * 24 + 4] = 0;
				indices[(i - 1) * 24 + 5] = k - 4;

				indices[(i - 1) * 24 + 6] = 0 + 2;
				indices[(i - 1) * 24 + 7] = 0;
				indices[(i - 1) * 24 + 8] = k - 5;

				indices[(i - 1) * 24 + 9] = k - 3;
				indices[(i - 1) * 24 + 10] = 0 + 2;
				indices[(i - 1) * 24 + 11] = k - 5;

				indices[(i - 1) * 24 + 12] = k - 3;
				indices[(i - 1) * 24 + 13] = k - 2;
				indices[(i - 1) * 24 + 14] = 0 + 2;

				indices[(i - 1) * 24 + 15] = k - 2;
				indices[(i - 1) * 24 + 16] = 0 + 3;
				indices[(i - 1) * 24 + 17] = 0 + 2;

				indices[(i - 1) * 24 + 18] = k - 1;
				indices[(i - 1) * 24 + 19] = 0 + 3;
				indices[(i - 1) * 24 + 20] = k - 2;

				indices[(i - 1) * 24 + 21] = k - 1;
				indices[(i - 1) * 24 + 22] = 0 + 4;
				indices[(i - 1) * 24 + 23] = 0 + 3;
			}
		}
		return new TexturedModel(loader.loadToVAO(vertices, texCoords, normals, indices),
				new ModelTexture(loader.loadTexture("new/TrackTexture")));

	}

	private static void sortLights(List<Light> lights, Vector3f currentPosition) {
		float[] distance = new float[lights.size() - 1];
		for (int i = 1; i < lights.size(); i++) {
			distance[i - 1] = lights.get(i).getdistance(currentPosition);
		}
	}
}