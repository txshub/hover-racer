package gameEngine.renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import gameEngine.entities.Camera;
import gameEngine.entities.Entity;
import gameEngine.entities.Light;
import gameEngine.models.TexturedModel;
import gameEngine.normalMappingRenderer.NormalMappingRenderer;
import gameEngine.shaders.StaticShader;
import gameEngine.shaders.TerrainShader;
import gameEngine.skybox.SkyboxRenderer;
import gameEngine.terrains.Terrain;
import gameEngine.toolbox.VecCon;

public class MasterRenderer {

	private static final float FOV = 70f;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 10000f;
	
	public static final float R = 100/255f;
	public static final float G = 100/255f;
	public static final float B = 100/255f;

	Matrix4f projectionMatrix;
	
	private EntityRenderer renderer;
	private StaticShader shader = new StaticShader();
	
	private TerrainRenderer terrainRenderer;
	private TerrainShader terrainShader = new TerrainShader();
	
	private NormalMappingRenderer normalMapRend;
	
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	private Map<TexturedModel, List<Entity>> normalEntities = new HashMap<TexturedModel, List<Entity>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();
	
	private SkyboxRenderer skyboxRenderer;
	
	public MasterRenderer(Loader loader) {

		enableCulling();
		createProjectionMatrix();
		renderer = new EntityRenderer(shader,projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
		skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix);
		normalMapRend = new NormalMappingRenderer(projectionMatrix);
		
	}
	
	public void prepare(){
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(R, G, B, 1f);
	}
	
	public static void enableCulling(){
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public static void disableCulling(){
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	
	public Matrix4f getProjectionMatrix(){
		return projectionMatrix;
	}
	
	public void renderScene(List<Entity> entities, List<Entity> normalEntities, Terrain[][] terrains, List<Light> lights, Camera camera, org.joml.Vector4f clipPlane1){
		Vector4f clipPlane = VecCon.toLWJGL(clipPlane1);
		
		for (Entity entity2 : entities) {
			processEntity(entity2);
		}
		for (Entity normalEntity2 : normalEntities) {
			processNormalEntity(normalEntity2);
		}
		for (Terrain[] terrain1 : terrains) {
			for (Terrain terrain2 : terrain1) {
				processTerrain(terrain2);
			}
		}
		render(lights, camera, clipPlane);
	}
	
	public void render(List<Light> lights, Camera camera, Vector4f clipPlane){
		
		prepare();
		shader.start();
		shader.loadClipPlane(clipPlane);
		shader.loadSkyColour(R, G, B);
		shader.loadLights(lights);
		shader.loadViewMatrix(camera);
		renderer.render(entities);
		shader.stop();
		
		normalMapRend.render(normalEntities, clipPlane, lights, camera);
		
		terrainShader.start();
		terrainShader.loadClipPlane(clipPlane);
		terrainShader.loadSkyColour(R, G, B);
		terrainShader.loadLights(lights);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains);
		terrainShader.stop();
		
		skyboxRenderer.render(camera,R,G,B);
		
		normalEntities.clear();
		entities.clear();
		terrains.clear();
		
	}
	
	public void processTerrain(Terrain terrain){
		terrains.add(terrain);
	}
	
	public void processEntity(Entity entity){
		TexturedModel texturedModel = entity.getModel();
		List<Entity> batch = entities.get(texturedModel);
		if(batch!=null){
			batch.add(entity);
		}else{
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(texturedModel, newBatch);			
		}
		
	}
	
	public void processNormalEntity(Entity entity){
		TexturedModel texturedModel = entity.getModel();
		List<Entity> batch = normalEntities.get(texturedModel);
		if(batch!=null){
			batch.add(entity);
		}else{
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			normalEntities.put(texturedModel, newBatch);			
		}
		
	}
	
	private void createProjectionMatrix(){
		float aspectRatio = (float)Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float)((1f / Math.tan(Math.toRadians(FOV/2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;
		
		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1f;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
	}
	
	public void cleanUp(){
		shader.cleanUp();
		normalMapRend.cleanUp();
		terrainShader.cleanUp();
	}
	
}
