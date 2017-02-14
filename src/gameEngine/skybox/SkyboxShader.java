package gameEngine.skybox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import gameEngine.entities.Camera;
import gameEngine.renderEngine.DisplayManager;
import gameEngine.shaders.ShaderProgram;
import gameEngine.toolbox.Maths;
import gameEngine.toolbox.VecCon;

/**
 * @author rtm592
 *
 */
public class SkyboxShader extends ShaderProgram{

	private static final String VERTEX_FILE = "src/gameEngine/skybox/skyboxVertexShader.txt";
	private static final String FRAGMENT_FILE = "src/gameEngine/skybox/skyboxFragmentShader.txt";
	
	private static final float ROTATE_SPEED = 1f;
	
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_fogColour;
	private int location_blendFactor;
	private int location_cubeMap;
	private int location_cubeMap2;
	private float rotation = 0f;
	
	public SkyboxShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	public void loadFogColour(float r, float g, float b){
		super.loadVector3f(location_fogColour, new Vector3f(r,g,b));
	}
	
	public void loadProjectionMatrix(Matrix4f matrix){
		super.loadMatrix(location_projectionMatrix, matrix);
	}
	
	public void loadBlendFactor(float blend){
		super.loadFloat(location_blendFactor, blend);
	}
	
	public void loadViewMatrix(Camera camera){
		Matrix4f matrix = Maths.createViewMatrix(camera);
		Vector3f cameraPos = VecCon.toLWJGL(camera.getEntity().getPosition());
		matrix.translate(new Vector3f(cameraPos.x, cameraPos.y, cameraPos.z));
		rotation += (ROTATE_SPEED * DisplayManager.getFrameTimeSeconds());
		matrix.rotate((float)Math.toRadians(rotation), new Vector3f(0,1,0));
		super.loadMatrix(location_viewMatrix, matrix);
	}
	
	public void connectTExtureUnits(){
		super.loadInt(location_cubeMap, 0);
		super.loadInt(location_cubeMap2, 1);
	}

	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_fogColour = super.getUniformLocation("fogColour");
		location_blendFactor = super.getUniformLocation("blendFactor");
		location_cubeMap = super.getUniformLocation("cubeMap");
		location_cubeMap2 = super.getUniformLocation("cubeMap2");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttributes(0, "position");
		
	}
	
}
