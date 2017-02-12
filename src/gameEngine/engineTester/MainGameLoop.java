package gameEngine.engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import gameEngine.entities.Camera;
import gameEngine.entities.Entity;
import gameEngine.entities.Light;
import gameEngine.entities.Player;
import gameEngine.guis.GuiRenderer;
import gameEngine.guis.GuiTexture;
import gameEngine.models.RawModel;
import gameEngine.models.TexturedModel;
import gameEngine.normalMappingObjConverter.NormalMappedObjLoader;
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
import gameEngine.water.WaterFrameBuffers;
import gameEngine.water.WaterRenderer;
import gameEngine.water.WaterShader;
import gameEngine.water.WaterTile;
import trackDesign.SeedTrack;
import trackDesign.TrackMaker;
import trackDesign.TrackPoint;

public class MainGameLoop {

	public static int size = 4;

	public static void main(String[] args) {

		DisplayManager.createDisplay();
		Loader loader = new Loader();
		AL10.alDistanceModel(AL11.AL_LINEAR_DISTANCE_CLAMPED);

		/************************* Terrain ********************************/

		TerrainTexture background = new TerrainTexture(loader.loadTexture("grassy2"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		TerrainTexturePack texturePack = new TerrainTexturePack(background, rTexture, gTexture, bTexture);

		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

		Terrain[][] terrains = new Terrain[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				terrains[i][j] = new Terrain((int) (Terrain.SIZE) * i, (int) (Terrain.SIZE) * j, loader, texturePack,
						blendMap, "white");
			}
		}
		
		// race track
		
		SeedTrack st = TrackMaker.makeTrack(10, 20, 30, 1, 40, 40, 4); // Generate a random track
    ArrayList<TrackPoint> track = st.getTrack();
    long seed = st.getSeed();

		// List<Terrain> list2 = new ArrayList<Terrain>();
		// Terrain terrain = new Terrain((int) (-Terrain.SIZE / 2), (int)
		// (-Terrain.SIZE / 2), loader, texturePack,
		// blendMap, "heightMap2");
		// list2.add(terrain);

		/************************* Player ********************************/

		TexturedModel playerTModel = new TexturedModel(getModel("shipGhost", loader),
				new ModelTexture(loader.loadTexture("ghostcloak")));

		Player p1 = new Player(playerTModel, new Vector3f(50, 0, 50), new Vector3f(), 5);

		/************************* Trees ********************************/

		List<Entity> list = new ArrayList<Entity>();
		list.add(p1);
		Random random = new Random();
		RawModel treeModel = getModel("tree", loader);
		ModelTexture treeTexture = new ModelTexture(loader.loadTexture("tree"));
		TexturedModel treeTextureModel = new TexturedModel(treeModel, treeTexture);
		for (int i = 0; i < 100; i++) {

			float x = (float) random.nextDouble() * Terrain.SIZE * size;
			float z = (float) random.nextDouble() * Terrain.SIZE * size;

			Terrain temp = terrains[(int) Math.max(0, Math.min(size - 1, (x / Terrain.SIZE)))][(int) Math.max(0,
					Math.min(size - 1, (z / Terrain.SIZE)))];
			Entity entity = new Entity(treeTextureModel,
					new Vector3f(x, Math.min(
							Math.min(Math.min(temp.getHeightOfTerrain(x, z) - 2, temp.getHeightOfTerrain(x - 3, z) - 2),
									Math.min(temp.getHeightOfTerrain(x, z - 3) - 2,
											temp.getHeightOfTerrain(x + 3, z) - 2)),
					temp.getHeightOfTerrain(x, z + 3) - 2), z), 
					new Vector3f(), 
					Math.max(1f, (float) random.nextDouble() * 50f));
			entity.getModel().getTexture().setReflectivity(0.5f);
			entity.getModel().getTexture().setShineDamper(50);
			list.add(entity);
		}

		/************************* Grass ********************************/

		RawModel grassModel = getModel("grassModel", loader);
		ModelTexture grassTexture = new ModelTexture(loader.loadTexture("grassTexture"));
		grassTexture.setReflectivity(0);
		grassTexture.setShineDamper(0);
		grassTexture.setHasTransparency(true);
		grassTexture.setUseFakeLighting(true);
		TexturedModel grassTextureModel = new TexturedModel(grassModel, grassTexture);
		for (int i = 0; i < 3000; i++) {

			float x = (float) random.nextDouble() * Terrain.SIZE * size;
			float z = (float) random.nextDouble() * Terrain.SIZE * size;

			Entity entity = new Entity(grassTextureModel,
					new Vector3f(x,
							terrains[(int) Math.max(0, Math.min(size - 1, (x / Terrain.SIZE)))][(int) Math.max(0,
									Math.min(size - 1, (z / Terrain.SIZE)))].getHeightOfTerrain(x, z),
							z),
					new Vector3f(), 
					(float) Math.max(.75f, random.nextDouble() * 4));
			list.add(entity);
		}

		/************************* Ferns ********************************/

		RawModel fernModel = getModel("fern", loader);
		ModelTexture fernTexture = new ModelTexture(loader.loadTexture("fern"));
		fernTexture.setReflectivity(.01f);
		fernTexture.setShineDamper(100);
		fernTexture.setHasTransparency(true);
		fernTexture.setUseFakeLighting(true);
		fernTexture.setNumOfRows(2);
		TexturedModel fernTextureModel = new TexturedModel(fernModel, fernTexture);
		for (int i = 0; i < 500; i++) {

			float x = (float) random.nextDouble() * Terrain.SIZE * size;
			float z = (float) random.nextDouble() * Terrain.SIZE * size;

			Entity entity = new Entity(fernTextureModel, random.nextInt(4),
					new Vector3f(x,
							terrains[(int) Math.max(0, Math.min(size - 1, (x / Terrain.SIZE)))][(int) Math.max(0,
									Math.min(size - 1, (z / Terrain.SIZE)))].getHeightOfTerrain(x, z),
							z),
					new Vector3f(), (float) Math.max(.5f, random.nextDouble()));
			list.add(entity);
		}

		/*****************************
		 * Normal Mapped Entities
		 *******************************/

		List<Entity> normalEntities = new ArrayList<Entity>();

		String normalObjs = "normalMappedModels/";

		TexturedModel barrel = new TexturedModel(NormalMappedObjLoader.loadOBJ("barrel", loader),
				new ModelTexture(loader.loadTexture(normalObjs + "barrel")));
		barrel.getTexture().setReflectivity(0.5f);
		barrel.getTexture().setShineDamper(10f);
		barrel.getTexture().setNormalMapID(loader.loadTexture(normalObjs + "barrelNormal"));

		normalEntities.add(new Entity(barrel, new Vector3f(100, 20, 100), new Vector3f(), 1));

		TexturedModel boulder = new TexturedModel(NormalMappedObjLoader.loadOBJ("boulder", loader),
				new ModelTexture(loader.loadTexture(normalObjs + "boulder")));
		boulder.getTexture().setReflectivity(0.1f);
		boulder.getTexture().setShineDamper(50f);
		boulder.getTexture().setNormalMapID(loader.loadTexture(normalObjs + "boulderNormal"));

		normalEntities.add(new Entity(boulder, new Vector3f(110, 20, 100), new Vector3f(), 1));

		TexturedModel crate = new TexturedModel(NormalMappedObjLoader.loadOBJ("crate", loader),
				new ModelTexture(loader.loadTexture(normalObjs + "crate")));
		crate.getTexture().setReflectivity(0.5f);
		crate.getTexture().setShineDamper(10f);
		crate.getTexture().setNormalMapID(loader.loadTexture(normalObjs + "crateNormal"));

		normalEntities.add(new Entity(crate, new Vector3f(90, 20, 100), new Vector3f(), 0.01f));

		/*************************
		 * Lights and Others
		 ********************************/

		Entity lightmodel1 = new Entity(
				new TexturedModel(getModel("dragon", loader), new ModelTexture(loader.loadTexture("white"))),
				new Vector3f((float) Math.cos(0), 100, (float) Math.sin(0)), new Vector3f(), 5f);
		Entity lightmodel2 = new Entity(
				new TexturedModel(getModel("dragon", loader), new ModelTexture(loader.loadTexture("white"))),
				new Vector3f(-20, 30, -30), new Vector3f(), 2f);
		list.add(lightmodel1);
		list.add(lightmodel2);

		Light sun = new Light(
		    new Vector3f((float) Math.cos(0), 100, (float) Math.sin(0) + Terrain.SIZE / 2),
				new Vector3f(1f, 1f, 1f));
		
		Light light2 = new Light(
		    new Vector3f(0, 10, 0), 
		    new Vector3f(1, 0.5f, 0.5f), 
        new Vector3f(0.1f, 0, 0.002f));
		
		// Light light3 = new Light(new Vector3f(-50, 30, -30), new
		// Vector3f(0.3f, 0.7f, 1), new Vector3f(1,0.001f,0.001f));
		List<Light> lights = new ArrayList<Light>();
		lights.add(sun);
		lights.add(light2);
		//lights.add(light3);

		Camera camera = new Camera(p1);

		MasterRenderer renderer = new MasterRenderer(loader);
		GuiRenderer guiRender = new GuiRenderer(loader);

		MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrains);

		
		WaterFrameBuffers wfb = new WaterFrameBuffers();

		WaterShader waterShader = new WaterShader();
		WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(), wfb);
		// List<WaterTile> waters = new ArrayList<>();
		// WaterTile water = new WaterTile(Terrain.SIZE/2, Terrain.SIZE/2, -10);
		// waters.add(water);
		WaterTile[][] waters = new WaterTile[1][1];
		for (int i = 0; i < 1; i++) {
			for (int j = 0; j < 1; j++) {
				waters[i][j] = new WaterTile(i * Terrain.SIZE + (Terrain.SIZE / 2),
						j * Terrain.SIZE + (Terrain.SIZE / 2), -25f);
			}
		}

		/************************* Guis ********************************/

		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		GuiTexture crosshair = new GuiTexture(
		    loader.loadTexture("crosshair"), 
		    new Vector2f(0, 0),
				new Vector2f(64f / Display.getWidth(), 60f / Display.getHeight()));
//		guis.add(crosshair);
		// GuiTexture gui2 = new GuiTexture(loader.loadTexture("fern"), new
		// Vector2f(.5f, .5f), new Vector2f(.25f, .25f));
		// guis.add(gui);

		/*************************
		 * Main Game Loop
		 ********************************/

		float time = 0;
		Vector3f point = null;
		while (!Display.isCloseRequested()) {
			GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
			picker.update();
			time += DisplayManager.getFrameTimeSeconds();
			sun.setPosition(new Vector3f(sun.getPosition().x + (float) Math.sin(time) * 10, sun.getPosition().y,
					sun.getPosition().z + (float) Math.cos(time) * 10));
			lightmodel1.setPosition(sun.getPosition());
			// point = picker.getCurrentTerrainPoint();
			// if (point != null) {
			// lightmodel2.setPosition(point);
			// if (Mouse.isButtonDown(0)) {
			// lights.add(new Light(light2.getPosition(), light2.getColour(),
			// light2.getAttenuation()));
			// }
			// light2.setPosition(new Vector3f(point.x, point.y + 5, point.z));
			// }
			
			// move entities 
			p1.move(terrains);
			// move camera after entities
			camera.move();
			
			// render
//			for (WaterTile[] waters1 : waters) {
//				for (WaterTile water : waters1) {
//
//					// reflection
//					wfb.bindReflectionFrameBuffer();
//					float distance = 2 * (camera.getPosition().y - water.getHeight());
//					camera.getPosition().y -= distance;
//					camera.invertPitch();
//					renderer.renderScene(list, normalEntities, terrains, lights, camera,
//							new Vector4f(0, 1, 0, -water.getHeight() + 0.1f));
//					camera.invertPitch();
//					camera.getPosition().y += distance;
//					// refraction
//					wfb.bindRefractionFrameBuffer();
//					renderer.renderScene(list, normalEntities, terrains, lights, camera,
//							new Vector4f(0, -1, 0, water.getHeight() + 0.1f));
//					wfb.unbindCurrentFrameBuffer();
//				}
//			}

			renderer.renderScene(list, normalEntities, terrains, lights, camera, new Vector4f(0, 0, 0, 0));
			GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
			waterRenderer.render(waters, camera, sun);
			guiRender.render(guis);
			DisplayManager.updateDisplay();
			sortLights(lights, p1.getPosition());

			if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				break;
			}

		}

		/************************* Clean up ********************************/

		guiRender.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		wfb.cleanUp();
		DisplayManager.closeDisplay();

	}

	private static RawModel getModel(String fileName, Loader loader) {
		ModelData data = OBJFileLoader.loadOBJ(fileName);

		return loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
	}

	private static void sortLights(List<Light> lights, Vector3f currentPosition) {
		float[] distance = new float[lights.size() - 1];
		for (int i = 1; i < lights.size(); i++) {
			distance[i - 1] = lights.get(i).getdistance(currentPosition);
		}

	}

}
