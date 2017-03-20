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
import javafx.application.Platform;
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
import userInterface.GameMenu;
import userInterface.MainMenu;

/** Main game class
 * 
 * @author Reece Bennett
 * @author rtm592 */
public class MultiplayerGame {

	// Set this to print debug messages
	public static boolean debug = true;

	private Loader loader;
	private ArrayList<Entity> entities;
	private ArrayList<Terrain> terrains;
	private ArrayList<Light> lights;
	private ArrayList<TrackPoint> trackPoints;
	private ArrayList<Vector3f> barrierPoints;
	private Camera camera;
	private MasterRenderer renderer;
	private String trackSeed;
	public static InputController input;

	private MultiplayerShipManager ships;
	private Client client;

	private long startsAt;
	private boolean running;

	// Tudor
	private int ranking;
	private int currentLap;
	private boolean finished;
	private List<String> nicknames;
	private ArrayList<String> leaderboard;
	private boolean changed = false; // TODO temporary

	// UI Globals
	private Container menu;
	private Container optionsMenu;
	private Label posCurrent;
	private Label posTotal;
	private Label lapCurrent;
	private Label lapTotal;
	private UIRenderer uiRenderer;
	private ArrayList<Container> containers;
	private String currentMenu = "none";
	private Container finishContainer;
	private Label finishText;
	private Vector3f textColour;
	private FontType font;
	private ArrayList<Label> leaderboardTexts;


	public MultiplayerGame(RaceSetupData data, Client client) {
		init(data, client);
	}

	private void init(RaceSetupData data, Client client) {
		running = true;
		startsAt = System.nanoTime() + data.getTimeToStart();

		this.client = client;
		client.setMultiplayerGame(this);

		DisplayManager.createDisplay();
		loader = new Loader();
		MultiplayerGame.input = new KeyboardController();
		AudioMaster.init();
		entities = new ArrayList<Entity>();
		TextMaster.init(loader);

		if (debug) System.out.println("Screen size: " + Display.getWidth() + " x " + Display.getHeight());

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

		TexturedModel trackModel = createTrackModel(st);
		Entity track = new Entity(trackModel, new Vector3f(0, 0, 0), new Vector3f(), 1f);
		entities.add(track);

		// Finish Line
		TexturedModel finishLineModel =
			new TexturedModel(getModel("finishLineUpdated", loader), new ModelTexture(loader.loadTexture("new/finishLineTextureUpdated")));
		Vector3f firstPoint = new Vector3f(st.getStart());
		firstPoint.y = 1.05f;
		Entity finishLine = new Entity(finishLineModel, firstPoint, data.startingOrientation, st.getTrack().get(0).getWidth() * 0.7f);
		entities.add(finishLine);

		// Lighting
		lights = new ArrayList<Light>();
		Light sun = new Light(new Vector3f(0, 100000, 0), new Vector3f(1f, 1f, 1f));
		lights.add(sun);

		// Create ships
		ships = new MultiplayerShipManager(data, new FlatGroundProvider(0), input, loader, trackPoints);
		ships.addBarrier(barrierPoints);
		ships.addShipsTo(entities);
		client.setManager(ships);

		// Player following camera
		camera = new Camera(ships.getPlayerShip());

		// Renderers
		renderer = new MasterRenderer(loader);
		uiRenderer = new UIRenderer(loader);

		// Store player nicknames
		nicknames = data.getNicknames();

		leaderboard = new ArrayList<String>(nicknames.size());
		for (int i = 0; i < nicknames.size(); i++) {
			leaderboard.add("-----------");
		}

    setupUI(data);

		AudioMaster.playInGameMusic();
		try {
			Keyboard.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		client.setManager(ships); // This also indicates game setup is ready

	}

	public void update(float delta) {
		input.update();

		// Check for menu
		if (input.wasPressed(Action.MENU) > 0.5f) {
			if (currentMenu.equals("none")) {
				menu.setVisibility(true);
				currentMenu = "main";
			} else if (currentMenu.equals("main")) {
				menu.setVisibility(false);
				currentMenu = "none";
			} else if (currentMenu.equals("options")) {
				menu.setVisibility(true);
				optionsMenu.setVisibility(false);
				currentMenu = "main";
			}
		}

		// Check for audio controls
		/** @author Tudor */
		if (input.wasPressed(Action.MUSIC_UP) > 0.5f) AudioMaster.increaseMusicVolume();
		if (input.wasPressed(Action.MUSIC_DOWN) > 0.5f) AudioMaster.decreaseMusicVolume();
		if (input.wasPressed(Action.MUSIC_SKIP) > 0.5f) AudioMaster.skipInGameMusic();
		if (input.wasPressed(Action.SFX_UP) > 0.5f) AudioMaster.increaseSFXVolume();
		if (input.wasPressed(Action.SFX_DOWN) > 0.5f) AudioMaster.decreaseSFXVolume();

		// Allow inputs iff the race has started
		if (System.nanoTime() > startsAt) ships.getPlayerShip().start();

		ships.updateShips(delta);
		try {
			client.sendByteMessage(ships.getShipPacket(), ServerComm.SENDPLAYERDATA);
		} catch (IOException e) {}
		for (Container c : containers) {
			c.update();
		}

		// Update UI Stuff
		lapCurrent.setText(Integer.toString(currentLap));
		posCurrent.setText(Integer.toString(ranking));
		
		if (!finishContainer.isVisible() && finished) {
			finishContainer.setVisibility(true);
		} else if (finished) {
		  for (int i = 0; i < leaderboard.size(); i++) {
		    String text = "\n" + (i+1) + " : " + leaderboard.get(i);
		    leaderboardTexts.get(i).setText(text);
		  }
		}
		

		camera.move();

		// Move terrain with the player so it seems infinite
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
		renderer.renderScene(entities, terrains, lights, camera, new Vector4f());
		uiRenderer.render(containers);
		TextMaster.render();
		GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
		DisplayManager.updateDisplay();
		sortLights(lights, ships.getPlayerShip().getPosition());
	}

	public void cleanUp() {
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

	/** Setup the UIs
	 * 
	 * @param data
	 *        RaceSetupData including total laps and total players
	 * @author Reece Bennett */
	private void setupUI(RaceSetupData data) {
		containers = new ArrayList<>();

		textColour = new Vector3f(0.0275f, 0.6510f, 0.9412f);
		font = new FontType(loader.loadFontTexture("ui/calibri"), new File("src/resources/ui/calibri.fnt"));

		// Main pause menu
		menu = new Container(loader, "ui/MenuBackground", new Vector2f(448, 120));
		containers.add(menu);

		Button resumeButton = new Button(loader, "ui/ButtonBackground", new Vector2f(58, 64));
		resumeButton.setParent(menu);
		resumeButton.addListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals("pressed")) {
					menu.setVisibility(false);
					currentMenu = "none";
				}
			}
		});
		Label resumeText = new Label(loader, "RESUME", font, 2.5f, true, new Vector2f(0, 8), 266);
		resumeText.setParent(resumeButton);
		resumeText.setColour(textColour);

		Button optionsButton = new Button(loader, "ui/ButtonBackground", new Vector2f(58, 160));
		optionsButton.setParent(menu);
		optionsButton.addListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals("pressed")) {
					menu.setVisibility(false);
					optionsMenu.setVisibility(true);
					currentMenu = "options";
				}
			}
		});
		Label optionsText = new Label(loader, "OPTIONS", font, 2.5f, true, new Vector2f(0, 8), 266);
		optionsText.setParent(optionsButton);
		optionsText.setColour(textColour);

		Button menuButton = new Button(loader, "ui/ButtonBackground", new Vector2f(58, 256));
		menuButton.setParent(menu);
		menuButton.addListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				System.out.println("Exiting to the menu");
				Platform.runLater(new Runnable() {
					
					public void run(){
						MainMenu.reloadScene();	
					}
				});	
			}
		});
		Label menuText = new Label(loader, "MENU", font, 2.5f, true, new Vector2f(0, 8), 266);
		menuText.setParent(menuButton);
		menuText.setColour(textColour);

		Button exitButton = new Button(loader, "ui/ButtonBackground", new Vector2f(58, 352));
		exitButton.setParent(menu);
		exitButton.addListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals("pressed")) {
					System.out.println("Exit button pressed");
					running = false;
				}
			}
		});
		Label exitText = new Label(loader, "EXIT", font, 2.5f, true, new Vector2f(0, 8), 266);
		exitText.setParent(exitButton);
		exitText.setColour(textColour);

		menu.setVisibility(false);

		// Options sub menu
		optionsMenu = new Container(loader, "ui/MenuBackground", new Vector2f(448, 120));
		containers.add(optionsMenu);

		Label musicText = new Label(loader, "MUSIC VOLUME", font, 2f, true, new Vector2f(0, 67), 384);
		musicText.setParent(optionsMenu);
		musicText.setColour(textColour);

		Label musicVolume = new Label(loader, "-1%", font, 2.5f, true, new Vector2f(128, 114), 120);
		musicVolume.setParent(optionsMenu);
		musicVolume.setColour(textColour);
		musicVolume.setText(Float.toString(AudioMaster.getMusicVolume()));

		Button musicMinusButton = new Button(loader, "ui/minusButton", new Vector2f(58, 114));
		musicMinusButton.setParent(optionsMenu);
		musicMinusButton.addListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals("pressed")) {
					AudioMaster.decreaseMusicVolume();
					musicVolume.setText(Float.toString(AudioMaster.getMusicVolume()));
				}
			}
		});

		Button musicPlusButton = new Button(loader, "ui/plusButton", new Vector2f(260, 114));
		musicPlusButton.setParent(optionsMenu);
		musicPlusButton.addListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals("pressed")) {
					AudioMaster.increaseMusicVolume();
					musicVolume.setText(Float.toString(AudioMaster.getMusicVolume()));
				}
			}
		});

		Label sfxText = new Label(loader, "SFX VOLUME", font, 2f, true, new Vector2f(0, 208), 384);
		sfxText.setParent(optionsMenu);
		sfxText.setColour(textColour);

		Label sfxVolume = new Label(loader, "-1%", font, 2.5f, true, new Vector2f(128, 255), 120);
		sfxVolume.setParent(optionsMenu);
		sfxVolume.setColour(textColour);
		sfxVolume.setText(Float.toString(AudioMaster.getSFXVolume()));

		Button sfxMinusButton = new Button(loader, "ui/minusButton", new Vector2f(58, 255));
		sfxMinusButton.setParent(optionsMenu);
		sfxMinusButton.addListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals("pressed")) {
					AudioMaster.decreaseSFXVolume();
					sfxVolume.setText(Float.toString(AudioMaster.getSFXVolume()));
				}
			}
		});

		Button sfxPlusButton = new Button(loader, "ui/plusButton", new Vector2f(260, 255));
		sfxPlusButton.setParent(optionsMenu);
		sfxPlusButton.addListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals("pressed")) {
					AudioMaster.increaseSFXVolume();
					sfxVolume.setText(Float.toString(AudioMaster.getSFXVolume()));
				}
			}

      /** Create a track model
       * 
       * @return
       * @author Reece Bennett */
      private TexturedModel createTrackModel(SeedTrack st) {
      	float trackHeight = SeedTrack.getTrackHeight();
      	float barrierHeight = SeedTrack.getBarrierHeight();
      	float barrierWidth = SeedTrack.getBarrierWidth();
      	barrierPoints = new ArrayList<>();
      
      	// 6 vertices for each track point, 3 components for each vertex
      	float[] vertices = new float[(trackPoints.size() + 1) * 6 * 3];
      	// 10 triangles for each track point, 3 vertices per triangle
      	int[] indices = new int[(trackPoints.size() + 1) * 10 * 3];
      	float[] texCoords = new float[(trackPoints.size() + 1) * 6 * 2];
      	float[] normals = new float[vertices.length];
      
      	// We can pre-calculate some stuff for normals
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
      
      		// Find the line between previous and next point for direction of
      		// this
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
      		barrierPoints.add(leftPoint);
      		barrierPoints.add(rightPoint);
      
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
      
      		// First calculate surface normals (technically edge normals as we
      		// are
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
		});

		Button backButton = new Button(loader, "ui/ButtonBackground", new Vector2f(58, 349));
		backButton.setParent(optionsMenu);
		backButton.addListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				optionsMenu.setVisibility(false);
				menu.setVisibility(true);
				currentMenu = "main";
			}
		});
		Label backText = new Label(loader, "BACK", font, 2.5f, true, new Vector2f(0, 6), 266);
		backText.setParent(backButton);
		backText.setColour(textColour);

		optionsMenu.setVisibility(false);

		Container posDisplay = new Container(loader, "ui/posBackground", new Vector2f(Display.getWidth() - 170, 10));

		containers.add(posDisplay);
		posCurrent = new Label(loader, "", font, 5f, true, new Vector2f(30, 45), 130);
		posCurrent.setParent(posDisplay);
		posCurrent.setColour(1, 1, 1);
		posTotal = new Label(loader, Integer.toString(data.shipData.size()), font, 2.8f, true, new Vector2f(4, 2), 50);
		posTotal.setParent(posDisplay);
		posTotal.setColour(1, 1, 1);

		Container lapDisplay = new Container(loader, "ui/lapBackground", new Vector2f(21, 10));
		containers.add(lapDisplay);
		lapCurrent = new Label(loader, "", font, 5f, true, new Vector2f(0, 45), 120);
		lapCurrent.setParent(lapDisplay);
		lapCurrent.setColour(1, 1, 1);
		lapTotal = new Label(loader, Integer.toString(data.laps), font, 2.8f, true, new Vector2f(95, 2), 50);
		lapTotal.setParent(lapDisplay);
		lapTotal.setColour(1, 1, 1);

		finishContainer = new Container(loader, "ui/menuBackground", new Vector2f(448, 120));
		containers.add(finishContainer);

		finishText = new Label(loader, "You finished!", font, 3f, true, new Vector2f(0, 50), 384);
		finishText.setColour(textColour);
		finishText.setParent(finishContainer);
		
		leaderboardTexts = new ArrayList<>();
		
		for (int i = 0; i < nicknames.size(); i++) {
		  String text = "\n" + (i+1) + " : " + leaderboard.get(i);
      Label posText = new Label(loader, text, font, 2.5f, true, new Vector2f(0, 100 + i*40), 384);
      posText.setParent(finishContainer);
      posText.setColour(textColour);
      leaderboardTexts.add(posText);
		}

		finishContainer.setVisibility(false);
	}

	/** Create a track model
	 * 
	 * @return
	 * @author Reece Bennett */
	private TexturedModel createTrackModel(SeedTrack st) {
		float trackHeight = SeedTrack.getTrackHeight();
		float barrierHeight = SeedTrack.getBarrierHeight();
		float barrierWidth = SeedTrack.getBarrierWidth();
		barrierPoints = new ArrayList<>();

		// 6 vertices for each track point, 3 components for each vertex
		float[] vertices = new float[(trackPoints.size() + 1) * 6 * 3];
		// 10 triangles for each track point, 3 vertices per triangle
		int[] indices = new int[(trackPoints.size() + 1) * 10 * 3];
		float[] texCoords = new float[(trackPoints.size() + 1) * 6 * 2];
		float[] normals = new float[vertices.length];

		// We can pre-calculate some stuff for normals
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

			// Find the line between previous and next point for direction of
			// this
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
			barrierPoints.add(leftPoint);
			barrierPoints.add(rightPoint);

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

			// First calculate surface normals (technically edge normals as we
			// are
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

	public void updateLogic(int ranking, boolean finished, int currentLap) {
		this.currentLap = currentLap;
		this.ranking = ranking;
		this.finished = finished;
	}

	public void updateFinishData(byte[] msg) {
		for (int i = 0; i < msg.length; i++) {
			if (!nicknames.get(msg[i]).equals(leaderboard.get(i))) changed = true;
			leaderboard.set(i, nicknames.get(msg[i]));
			if (i == ships.getPlayerShip().getId()) ships.getPlayerShip().finish();
		}

	}
}