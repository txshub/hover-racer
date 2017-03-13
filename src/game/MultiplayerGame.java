package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import audioEngine.AudioMaster;
import clientComms.Client;
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
import gameEngine.toolbox.MousePicker;
import input.Action;
import input.InputController;
import input.KeyboardController;
import physics.core.Ship;
import physics.network.RaceSetupData;
import physics.placeholders.FlatGroundProvider;
import physics.ships.MultiplayerShipManager;
import trackDesign.SeedTrack;
import trackDesign.TrackMaker;
import trackDesign.TrackPoint;
import uiToolkit.Button;
import uiToolkit.Container;
import uiToolkit.Label;
import uiToolkit.UIRenderer;
import uiToolkit.fontMeshCreator.FontType;
import uiToolkit.fontRendering.TextMaster;

public class MultiplayerGame implements GameInt {

	// Set this to print debug messages
	public static boolean debug = true;

	private Loader loader;
	private ArrayList<Entity> entities;
	private ArrayList<Entity> normalEntities;
	private ArrayList<Terrain> terrains;
	private ArrayList<Light> lights;
	private ArrayList<GuiTexture> guis;
	private Camera camera;
	private MousePicker picker;
	private MasterRenderer renderer;
	private GuiRenderer guiRender;
	private long trackSeed;
	private ArrayList<TrackPoint> trackPoints;
	public static InputController input;

	private MultiplayerShipManager ships;
	private Client client;

	private long startsAt;
	private boolean running;

	// Tudor
	private GameLogic logic;
	// TODO temporary
	private Container menu;
	private Label posCurrent;
	private Label posTotal;
	private UIRenderer uiRenderer;
	private ArrayList<Container> containers;

	public MultiplayerGame(RaceSetupData data, Client client) {
		init(data, client);
	}

	private void init(RaceSetupData data, Client client) {
		running = true;
		startsAt = System.nanoTime() + data.getTimeToStart();

		this.client = client;

		DisplayManager.createDisplay();
		loader = new Loader();
		MultiplayerGame.input = new KeyboardController();
		AudioMaster.init();
		entities = new ArrayList<Entity>();
		normalEntities = new ArrayList<Entity>();
		TextMaster.init(loader);

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
		terrains.add(new Terrain((int) (-SkyboxRenderer.SIZE * 2f), (int) (-SkyboxRenderer.SIZE * 2f), loader, texturePack, blendMap,
			"new/FlatHeightMap"));

		// Track
		trackSeed = data.getTrackSeed();
		SeedTrack st = TrackMaker.makeTrack(trackSeed);
		trackPoints = st.getTrack();

		// Scale up the track so it isn't so tiny
		for (TrackPoint tp : st.getTrack()) {
			tp.mul(5);
		}
		trackPoints = st.getTrack();

		TexturedModel trackModel = createTrackModel(trackSeed);
		Entity track = new Entity(trackModel, new Vector3f(0, 0, 0), new Vector3f(), 3f);
		entities.add(track);

		// Lighting
		// Lighting
		lights = new ArrayList<Light>();
		Light sun = new Light(new Vector3f(0, 100000, 0), new Vector3f(1f, 1f, 1f));
		lights.add(sun);
		// Light sun = new Light(new Vector3f(256, 1000, 256), new Vector3f(1f, 1f, 1f));

		// Create ships
		ships = new MultiplayerShipManager(data, new FlatGroundProvider(0), input, loader);
		ships.addShipsTo(entities);
		client.setManager(ships); // Add the manager for the client for
									// communication

		// Player following camera
		camera = new Camera(ships.getPlayerShip());

		// GUIs
		setupGUIs();

		// Renderers
		renderer = new MasterRenderer(loader);
		guiRender = new GuiRenderer(loader);
		uiRenderer = new UIRenderer(loader);

		// Camera rotation with right click
		// picker = new MousePicker(camera, renderer.getProjectionMatrix(),
		// terrains);

		// Tudor
		ArrayList<Ship> opponents = new ArrayList<Ship>();
		logic = new GameLogic(ships.getPlayerShip(), opponents, st, data.laps);

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

		if (System.nanoTime() > startsAt) ships.startRace();

		// Tudor
		logic.update();
		// Check if the escape key was pressed to exit the game
		if (input.isDown(Action.EXIT) > 0.5f) running = false;

		// Check for menu
		if (input.wasPressed(Action.MENU) > 0.5f) menu.setVisibility(!menu.isVisible());

		// Check for audio controls
		/** @author Tudor */
		if (input.wasPressed(Action.MUSIC_UP) > 0.5f) AudioMaster.increaseMusicVolume();
		if (input.wasPressed(Action.MUSIC_DOWN) > 0.5f) AudioMaster.decreaseMusicVolume();
		if (input.wasPressed(Action.MUSIC_SKIP) > 0.5f) AudioMaster.skipInGameMusic();
		if (input.wasPressed(Action.SFX_UP) > 0.5f) AudioMaster.increaseSFXVolume();
		if (input.wasPressed(Action.SFX_DOWN) > 0.5f) AudioMaster.decreaseSFXVolume();

		ships.updateShips((float) delta);
		for (Container c : containers) {
			c.update();
		}

		camera.move();

		// move terrain based on player location so terrain seems infinite
		if (ships.getPlayerShip().getPosition().x > terrains.get(0).getX() + Terrain.SIZE * 3 / 4) {
			terrains.get(0).moveX(Terrain.SIZE / 4);
		} else if (ships.getPlayerShip().getPosition().x < terrains.get(0).getX() + Terrain.SIZE * 1 / 4) {
			terrains.get(0).moveX(-Terrain.SIZE / 4);
		}

		if (ships.getPlayerShip().getPosition().z > terrains.get(0).getZ() + Terrain.SIZE * 3 / 4) {
			terrains.get(0).moveZ(Terrain.SIZE / 4);
		} else if (ships.getPlayerShip().getPosition().z < terrains.get(0).getZ() + Terrain.SIZE * 1 / 4) {
			terrains.get(0).moveZ(-Terrain.SIZE / 4);
		}
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

	private void setupGUIs() {
		// GUIs
		guis = new ArrayList<>();
		containers = new ArrayList<>();

		Vector3f colour = new Vector3f(0.0275f, 0.6510f, 0.9412f);
		FontType font = new FontType(loader.loadFontTexture("ui/calibri"), new File("src/resources/ui/calibri.fnt"));

		menu = new Container(loader, "ui/MenuBackground", new Vector2f(448, 120));
		containers.add(menu);

		Button resumeButton = new Button(loader, "ui/ButtonBackground", new Vector2f(58, 64));
		resumeButton.setParent(menu);
		resumeButton.addListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				System.out.println(e.getActionCommand());
				if (e.getActionCommand().equals("pressed")) {
					menu.setVisibility(false);
				}
			}
		});
		Label resumeText = new Label(loader, "RESUME", font, 2.5f, true, new Vector2f(0, 8), 266);
		resumeText.setParent(resumeButton);
		resumeText.setColor(colour);

		Button optionsButton = new Button(loader, "ui/ButtonBackground", new Vector2f(58, 160));
		optionsButton.setParent(menu);
		Label optionsText = new Label(loader, "OPTIONS", font, 2.5f, true, new Vector2f(0, 8), 266);
		optionsText.setParent(optionsButton);
		optionsText.setColor(colour);

		Button lobbyButton = new Button(loader, "ui/ButtonBackground", new Vector2f(58, 256));
		lobbyButton.setParent(menu);
		Label lobbyText = new Label(loader, "LOBBY", font, 2.5f, true, new Vector2f(0, 8), 266);
		lobbyText.setParent(lobbyButton);
		lobbyText.setColor(colour);

		Button menuButton = new Button(loader, "ui/ButtonBackground", new Vector2f(58, 352));
		menuButton.setParent(menu);
		Label menuText = new Label(loader, "MENU", font, 2.5f, true, new Vector2f(0, 8), 266);
		menuText.setParent(menuButton);
		menuText.setColor(colour);

		menu.setVisibility(true);

		Container posDisplay = new Container(loader, "ui/posBackground", new Vector2f(Display.getWidth() - 170, 10));
		containers.add(posDisplay);
		posCurrent = new Label(loader, "2", font, 5f, true, new Vector2f(30, 45), 130);
		posCurrent.setParent(posDisplay);
		posCurrent.setColour(1, 1, 1);
		posCurrent = new Label(loader, "8", font, 2.8f, true, new Vector2f(15, 2), 30);
		posCurrent.setParent(posDisplay);
		posCurrent.setColour(1, 1, 1);

		Container lapDisplay = new Container(loader, "ui/lapBackground", new Vector2f(21, 10));
		containers.add(lapDisplay);
		posCurrent = new Label(loader, "2", font, 5f, true, new Vector2f(0, 45), 120);
		posCurrent.setParent(lapDisplay);
		posCurrent.setColour(1, 1, 1);
		posCurrent = new Label(loader, "5", font, 2.8f, true, new Vector2f(105, 2), 30);
		posCurrent.setParent(lapDisplay);
		posCurrent.setColour(1, 1, 1);
	}



	private TexturedModel createTrackModel(long seed) {
		float trackHeight = 1;

		float[] vertices = new float[trackPoints.size() * 5 * 3];
		int[] indices = new int[trackPoints.size() * 5 * 8];
		float[] texCoords = new float[indices.length];
		float[] normals = new float[vertices.length];

		// TODO Actually implement textures
		for (int i = 0; i < texCoords.length; i++) {
			texCoords[i] = 0;
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

				Vector2f left = new Vector2f(dirVec.y, -dirVec.x).mul(curPoint.getWidth() / 2);
				Vector2f right = new Vector2f(-dirVec.y, dirVec.x).mul(curPoint.getWidth() / 2);
				Vector2f leftNorm = new Vector2f(left).normalize();
				Vector2f rightNorm = new Vector2f(right).normalize();

				// System.out.println("Dir: " + dirVec + " Left: " +
				// left.length() + " Right: " + right.length());

				Vector3f normal;

				// Top Left
				vertices[i * 15] = curPoint.getX() + left.x;
				vertices[i * 15 + 1] = trackHeight;
				vertices[i * 15 + 2] = curPoint.getY() + left.y;
				normal = new Vector3f(leftNorm.x, 1, leftNorm.y).normalize();
				normals[i * 15 + 0] = normal.x;
				normals[i * 15 + 1] = normal.y;
				normals[i * 15 + 2] = normal.z;

				// Bottom Left
				vertices[i * 15 + 3] = curPoint.getX() + left.x;
				vertices[i * 15 + 4] = 0;
				vertices[i * 15 + 5] = curPoint.getY() + left.y;
				normal = new Vector3f(leftNorm.x, -1, leftNorm.y).normalize();
				normals[i * 15 + 3] = normal.x;
				normals[i * 15 + 4] = normal.y;
				normals[i * 15 + 5] = normal.z;

				// Top Center
				vertices[i * 15 + 6] = curPoint.getX();
				vertices[i * 15 + 7] = trackHeight;
				vertices[i * 15 + 8] = curPoint.getY();
				normal = new Vector3f(0, 1, 0).normalize();
				normals[i * 15 + 6] = normal.x;
				normals[i * 15 + 7] = normal.y;
				normals[i * 15 + 8] = normal.z;

				// Top Right
				vertices[i * 15 + 9] = curPoint.getX() + right.x;
				vertices[i * 15 + 10] = trackHeight;
				vertices[i * 15 + 11] = curPoint.getY() + right.y;
				normal = new Vector3f(rightNorm.x, 1, rightNorm.y).normalize();
				normals[i * 15 + 9] = normal.x;
				normals[i * 15 + 10] = normal.y;
				normals[i * 15 + 11] = normal.z;

				// Bottom Right
				vertices[i * 15 + 12] = curPoint.getX() + right.x;
				vertices[i * 15 + 13] = 0;
				vertices[i * 15 + 14] = curPoint.getY() + right.y;
				normal = new Vector3f(rightNorm.x, -1, rightNorm.y).normalize();
				normals[i * 15 + 12] = normal.x;
				normals[i * 15 + 13] = normal.y;
				normals[i * 15 + 14] = normal.z;

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


	public MultiplayerShipManager getManager() {
		return ships;
	}

	private static void sortLights(List<Light> lights, Vector3f currentPosition) {
		float[] distance = new float[lights.size() - 1];
		for (int i = 1; i < lights.size(); i++) {
			distance[i - 1] = lights.get(i).getdistance(currentPosition);
		}
	}
}