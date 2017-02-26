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
import gameEngine.guis.GuiRenderer;
import gameEngine.models.RawModel;
import gameEngine.models.TexturedModel;
import gameEngine.objConverter.OBJFileLoader;
import gameEngine.renderEngine.DisplayManager;
import gameEngine.renderEngine.Loader;
import gameEngine.renderEngine.MasterRenderer;
import gameEngine.terrains.Terrain;
import gameEngine.textures.ModelTexture;
import gameEngine.textures.TerrainTexture;
import gameEngine.textures.TerrainTexturePack;
import gameEngine.toolbox.MousePicker;
import physics.network.RaceSetupData;
import physics.placeholders.FlatGroundProvider;
import physics.ships.MultiplayerShipManager;
import physics.support.Action;
import physics.support.InputController;
import trackDesign.SeedTrack;
import trackDesign.TrackMaker;
import trackDesign.TrackPoint;

public class MultiplayerGame {

	// Set this to print debug messages
	public static boolean debug = true;


	private Loader loader;
	private ArrayList<Entity> entities;
	private ArrayList<Entity> normalEntities;
	private ArrayList<Terrain> terrains;
	private ArrayList<Light> lights;
	private Camera camera;
	private MousePicker picker;
	private MasterRenderer renderer;
	private GuiRenderer guiRender;
	private long trackSeed;
	private ArrayList<TrackPoint> trackPoints;
	public static InputController input;

	private MultiplayerShipManager ships;

	private long startsAt;
	private boolean running;

	public MultiplayerGame(RaceSetupData data) {
		init(data);
	}

	private void init(RaceSetupData data) {
		running = true;
		startsAt = System.nanoTime() + data.getTimeToStart();

		DisplayManager.createDisplay();
		loader = new Loader();
		MultiplayerGame.input = new InputController();
		AudioMaster.init();
		entities = new ArrayList<Entity>();
		normalEntities = new ArrayList<Entity>();

		// Terrain
		TerrainTexture background = new TerrainTexture(loader.loadTexture("new/GridTexture"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("new/GridTexture"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("new/GridTexture"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("new/GridTexture"));
		TerrainTexturePack texturePack = new TerrainTexturePack(background, rTexture, gTexture, bTexture);

		// TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("new/GridTexture"));

		int size = 1;
		terrains = new ArrayList<Terrain>();
		terrains.add(new Terrain((int) (Terrain.SIZE), (int) (Terrain.SIZE), loader, texturePack, blendMap, "new/FlatHeightMap"));

		// Track
		trackSeed = data.getTrackSeed();
		SeedTrack st = TrackMaker.makeTrack(trackSeed, 10, 20, 30, 1, 40, 40, 4);
		trackPoints = st.getTrack();

		TexturedModel trackModel = createTrackModel(trackSeed);
		Entity track = new Entity(trackModel, new Vector3f(0, 0, 0), new Vector3f(), 3f);
		entities.add(track);

		// Lighting
		lights = new ArrayList<Light>();
		Light sun = new Light(new Vector3f(256, 1000, 256), new Vector3f(1f, 1f, 1f));
		lights.add(sun);

		// Create ships
		ships = new MultiplayerShipManager(data, new FlatGroundProvider(0), input, loader);
		ships.addShipsTo(entities);

		// Player following camera
		camera = new Camera(ships.getPlayerShip());

		// Renderers
		renderer = new MasterRenderer(loader);
		guiRender = new GuiRenderer(loader);

		// Camera rotation with right click
		// picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrains);

		// Tudor
		AudioMaster.playInGameMusic();
	}

	public void update(double delta) {
		input.run();

		// Check if the escape key was pressed to exit the game
		if (input.checkAction(Action.EXIT)) running = false;

		// Check for audio controls
		/** @author Tudor */
		if (input.checkAction(Action.MUSIC_UP)) AudioMaster.increaseMusicVolume();
		if (input.checkAction(Action.MUSIC_DOWN)) AudioMaster.decreaseMusicVolume();
		if (input.checkAction(Action.MUSIC_SKIP)) AudioMaster.skipInGameMusic();
		if (input.checkAction(Action.SFX_UP)) AudioMaster.increaseSFXVolume();
		if (input.checkAction(Action.SFX_DOWN)) AudioMaster.decreaseSFXVolume();

		if (System.nanoTime() > startsAt) ships.getPlayerShip().start();
		ships.updateShips((float) delta);
		camera.move();
		// picker.update();
	}

	public void render() {
		GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
		renderer.renderScene(entities, normalEntities, terrains, lights, camera, new Vector4f());
		GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
		DisplayManager.updateDisplay();
		sortLights(lights, ships.getPlayerShip().getPosition());
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
		float trackWidth = 50;
		float trackHeight = 0;

		float[] vertices = new float[trackPoints.size() * 3 * 3];
		int[] indices = new int[trackPoints.size() * 3 * 4];
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

				Vector2f dirFromPrev = new Vector2f(curPoint.getX() - prevPoint.getX(), curPoint.getY() - prevPoint.getY());

				Vector2f dirToNext = new Vector2f(nextPoint.getX() - curPoint.getX(), nextPoint.getY() - curPoint.getY());

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
		return new TexturedModel(loader.loadToVAO(vertices, texCoords, normals, indices), new ModelTexture(loader.loadTexture("mud")));

	}


	private static void sortLights(List<Light> lights, Vector3f currentPosition) {
		float[] distance = new float[lights.size() - 1];
		for (int i = 1; i < lights.size(); i++) {
			distance[i - 1] = lights.get(i).getdistance(currentPosition);
		}
	}
}