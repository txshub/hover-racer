package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
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
import physics.network.RaceSetupData;
import physics.placeholders.FlatGroundProvider;
import physics.ships.MultiplayerShipManager;
import serverComms.ServerComm;
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
	private String trackSeed;
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
	private Label lapCurrent;
	private Label lapTotal;
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
		terrains.add(new Terrain((int) (-SkyboxRenderer.SIZE * 2f), (int) (-SkyboxRenderer.SIZE * 2f), loader, texturePack, blendMap,
			"new/FlatHeightMap"));


		// Track
		trackSeed = data.getTrackSeed();
		SeedTrack st = TrackMaker.makeTrack(trackSeed);
		// Scale up the track so it isn't so tiny
		for (TrackPoint tp : st.getTrack()) {
			tp.mul(20);
		}
		trackPoints = st.getTrack();

		TexturedModel trackModel = createTrackModel();
		Entity track = new Entity(trackModel, new Vector3f(0, 0, 0), new Vector3f(), 1f);
		entities.add(track);

		// Finish Line
		TexturedModel finishLineModel =
			new TexturedModel(getModel("finishLine", loader), new ModelTexture(loader.loadTexture("new/finishLineTexture")));
		Vector3f firstPoint = new Vector3f(st.getStart());
		firstPoint.y = 1.05f;
		Entity finishLine = new Entity(finishLineModel, firstPoint, new Vector3f(), 16f);
		entities.add(finishLine);


		// Lighting
		// Lighting
		lights = new ArrayList<Light>();
		Light sun = new Light(new Vector3f(0, 100000, 0), new Vector3f(1f, 1f, 1f));
		lights.add(sun);
		// Light sun = new Light(new Vector3f(256, 1000, 256), new Vector3f(1f, 1f,
		// 1f));

		// Create ships
		ships = new MultiplayerShipManager(data, new FlatGroundProvider(0), input, loader, trackPoints);
		ships.addShipsTo(entities);
		client.setManager(ships); // Add the manager for the client for
		// communication

		// Player following camera
		camera = new Camera(ships.getPlayerShip());

		// Camera rotation with right click
		// picker = new MousePicker(camera, renderer.getProjectionMatrix(),
		// terrains);

		// Tudor
		logic = new GameLogic(ships.getPlayerShip(), st, 4);

		// GUIs
		setupGUIs();

		// Renderers
		renderer = new MasterRenderer(loader);
		guiRender = new GuiRenderer(loader);
		uiRenderer = new UIRenderer(loader);

		AudioMaster.playInGameMusic();
		try {
			Keyboard.create();
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		menu.setVisibility(false);
	}

	public void update(float delta) {
		input.update();

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

		if (System.nanoTime() > startsAt) ships.getPlayerShip().start();

		ships.updateShips((float) delta);
		try {
			client.sendByteMessage(ships.getShipPacket(), ServerComm.SENDPLAYERDATA);
		} catch (IOException e) {}
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

		// Update GUI
		lapCurrent.setText(Integer.toString(logic.getCurrentLap()));
	}

	public void render() {
		GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
		renderer.renderScene(entities, normalEntities, terrains, lights, camera, new Vector4f());
		uiRenderer.render(containers);
		TextMaster.render();
		GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
		DisplayManager.updateDisplay();
		sortLights(lights, ships.getPlayerShip().getPosition());
	}

	public void cleanUp() {
		guiRender.cleanUp();
		TextMaster.cleanUp();
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
		resumeText.setColour(colour);

		Button optionsButton = new Button(loader, "ui/ButtonBackground", new Vector2f(58, 160));
		optionsButton.setParent(menu);
		Label optionsText = new Label(loader, "OPTIONS", font, 2.5f, true, new Vector2f(0, 8), 266);
		optionsText.setParent(optionsButton);
		optionsText.setColour(colour);

		Button lobbyButton = new Button(loader, "ui/ButtonBackground", new Vector2f(58, 256));
		lobbyButton.setParent(menu);
		Label lobbyText = new Label(loader, "LOBBY", font, 2.5f, true, new Vector2f(0, 8), 266);
		lobbyText.setParent(lobbyButton);
		lobbyText.setColour(colour);

		Button menuButton = new Button(loader, "ui/ButtonBackground", new Vector2f(58, 352));
		menuButton.setParent(menu);
		Label menuText = new Label(loader, "MENU", font, 2.5f, true, new Vector2f(0, 8), 266);
		menuText.setParent(menuButton);
		menuText.setColour(colour);

		menu.setVisibility(true);

		Container posDisplay = new Container(loader, "ui/posBackground", new Vector2f(Display.getWidth() - 170, 10));
		containers.add(posDisplay);
		posCurrent = new Label(loader, "2", font, 5f, true, new Vector2f(30, 45), 130);
		posCurrent.setParent(posDisplay);
		posCurrent.setColour(1, 1, 1);
		posTotal = new Label(loader, "8", font, 2.8f, true, new Vector2f(15, 2), 30);
		posTotal.setParent(posDisplay);
		posTotal.setColour(1, 1, 1);

		Container lapDisplay = new Container(loader, "ui/lapBackground", new Vector2f(21, 10));
		containers.add(lapDisplay);
		lapCurrent = new Label(loader, Integer.toString(logic.getCurrentLap()), font, 5f, true, new Vector2f(0, 45), 120);
		lapCurrent.setParent(lapDisplay);
		lapCurrent.setColour(1, 1, 1);
		lapTotal = new Label(loader, Integer.toString(logic.getTotalLaps()), font, 2.8f, true, new Vector2f(105, 2), 30);
		lapTotal.setParent(lapDisplay);
		lapTotal.setColour(1, 1, 1);
	}

	/** Create a track model
	 * 
	 * @return
	 * @author Reece Bennett */
	private TexturedModel createTrackModel() {
		float trackHeight = 1;
		float barrierHeight = 20;
		float barrierWidth = 10;

		// 6 vertices for each track point, 3 components for each vertex
		float[] vertices = new float[(trackPoints.size() + 1) * 6 * 3];
		// 10 triangles for each track point, 3 vertices per triangle
		int[] indices = new int[(trackPoints.size() + 1) * 10 * 3];
		float[] texCoords = new float[(trackPoints.size() + 1) * 6 * 2];
		float[] normals = new float[vertices.length];

		// We can pre-calculate some stuff for normals
		Vector3f normBot = new Vector3f(0, -1, 0);
		Vector3f normTop = new Vector3f(0, 1, 0);
		Vector3f normLeftOuter2D = new Vector3f(-barrierHeight, barrierWidth, 0).normalize();
		Vector3f normRightOuter2D = new Vector3f(barrierHeight, barrierWidth, 0).normalize();

		// Populate vertex and normal arrays
		for (int i = 0; i <= trackPoints.size(); i++) {
			TrackPoint curPoint = null;
			TrackPoint prevPoint = null;
			TrackPoint nextPoint = null;
			if (i < trackPoints.size()) {
				curPoint = trackPoints.get(i);

				// If we are at the first point the previous is the last point
				int prev = (i == 0) ? trackPoints.size() - 1 : i - 1;
				prevPoint = trackPoints.get(prev);

				// If we are at the last point the next is the first point
				int next = (i == trackPoints.size() - 1) ? 0 : i + 1;
				nextPoint = trackPoints.get(next);
			} else {
				curPoint = trackPoints.get(0);
				prevPoint = trackPoints.get(trackPoints.size() - 1);
				nextPoint = trackPoints.get(1);
			}

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

			// Define the texture coordinates
			int n = i * 6 * 2;

			texCoords[n + 0] = 0f;
			texCoords[n + 1] = i;

			texCoords[n + 2] = 0.15f;
			texCoords[n + 3] = i;

			texCoords[n + 4] = 0.3f;
			texCoords[n + 5] = i;

			texCoords[n + 6] = 0.7f;
			texCoords[n + 7] = i;

			texCoords[n + 8] = 0.85f;
			texCoords[n + 9] = i;

			texCoords[n + 10] = 1f;
			texCoords[n + 11] = i;

			// First calculate surface normals (technically edge normals as we are
			// working in a slice but whatever)

			// Get Quaternions for rotation to align with left and right vectors
			Vector3f normLeft = new Vector3f(left.x, 0, left.y);
			Vector3f normRight = new Vector3f(right.x, 0, right.y);

			Quaternionf rotationLeft = new Vector3f(-1, 0, 0).rotationTo(normLeft, new Quaternionf());
			Quaternionf rotationRight = new Vector3f(1, 0, 0).rotationTo(normRight, new Quaternionf());

			Vector3f normLeftOuter = new Vector3f(normLeftOuter2D).rotate(rotationLeft);
			Vector3f normRightOuter = new Vector3f(normRightOuter2D).rotate(rotationRight);

			// Calculate the vertex normals from the surface normals
			Vector3f normLBarrierB = new Vector3f(normLeftOuter).add(normTop).normalize();
			Vector3f normLBarrierT = new Vector3f(normLeftOuter).add(normRight).normalize();
			Vector3f normLPoint = new Vector3f(normRight).add(normTop).normalize();
			Vector3f normRPoint = new Vector3f(normTop).add(normLeft).normalize();
			Vector3f normRBarrierT = new Vector3f(normLeft).add(normRightOuter).normalize();
			Vector3f normRBarrierB = new Vector3f(normRightOuter).add(normTop).normalize();

			addToArray(normLBarrierB, normals, i * 18 + 0);
			addToArray(normLBarrierT, normals, i * 18 + 3);
			addToArray(normLPoint, normals, i * 18 + 6);
			addToArray(normRPoint, normals, i * 18 + 9);
			addToArray(normRBarrierT, normals, i * 18 + 12);
			addToArray(normRBarrierB, normals, i * 18 + 15);
		}

		// Populate indices
		for (int i = 0; i < trackPoints.size(); i++) {
			int n = i * 6;
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
		}

		return new TexturedModel(loader.loadToVAO(vertices, texCoords, normals, indices),
			new ModelTexture(loader.loadTexture("new/trackTexture")));
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