package gameEngine.shaders;

import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import gameEngine.entities.Camera;
import gameEngine.entities.Light;
import gameEngine.toolbox.Maths;

public class StaticShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/gameEngine/shaders/vertexShader.txt";
	private static final String FRAGMENT_FILE = "src/gameEngine/shaders/fragmentShader.txt";

	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition[];
	private int location_lightColour[];
	private int location_attenuation[];
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_useFakeLighting;
	private int location_skyColour;
	private int location_numOfRows;
	private int location_offset;
	private int location_clipPlane;

	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttributes(0, "position");
		super.bindAttributes(1, "textureCoords");
		super.bindAttributes(2, "normal");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_useFakeLighting = super.getUniformLocation("useFakeLighting");
		location_skyColour = super.getUniformLocation("skyColour");
		location_numOfRows = super.getUniformLocation("numOfRows");
		location_offset = super.getUniformLocation("offset");
		location_clipPlane = super.getUniformLocation("clipPlane");

		location_lightPosition = new int[MAX_LIGHTS];
		location_lightColour = new int[MAX_LIGHTS];
		location_attenuation = new int[MAX_LIGHTS];
		
		for (int i = 0; i < MAX_LIGHTS; i++) {
			location_lightPosition[i] = super.getUniformLocation("lightPosition["+i+"]");
			location_lightColour[i] = super.getUniformLocation("lightColour["+i+"]");
			location_attenuation[i] = super.getUniformLocation("attenuation["+i+"]");
		}
		
	}

	public void loadClipPlane(Vector4f clipPlane){
		super.loadVector4f(location_clipPlane, clipPlane);
	}
	
	public void loadOffset(float xOffset, float yOffset){
		super.loadVector2f(location_offset, new Vector2f(xOffset,yOffset));
	}
	
	public void loadNumberOfRow(float numOfRows){
		super.loadFloat(location_numOfRows, numOfRows);
	}
	
	public void loadSkyColour(float r, float g, float b){
		super.loadVector3f(location_skyColour, new Vector3f(r,g,b));
		
	}
	
	public void loadFakeLighting(boolean useFakeLighting) {
		super.loadBoolean(location_useFakeLighting, useFakeLighting);
	}
	
	public void loadshineVariables(float shine, float reflectivity) {
		super.loadFloat(location_shineDamper, shine);
		super.loadFloat(location_reflectivity, reflectivity);
	}

	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);

	}

	public void loadLights(List<Light> lights) {	
		
		for (int i = 0; i < MAX_LIGHTS; i++) {
			if (i < lights.size()) {
				super.loadVector3f(location_lightPosition[i], lights.get(i).getPosition());
				super.loadVector3f(location_lightColour[i], lights.get(i).getColour());
				super.loadVector3f(location_attenuation[i], lights.get(i).getAttenuation());
			} else {
				super.loadVector3f(location_lightPosition[i], new Vector3f(0, 0, 0));
				super.loadVector3f(location_lightColour[i], new Vector3f(0, 0, 0));
				super.loadVector3f(location_attenuation[i], new Vector3f(1, 0, 0));
			}
		}
	}

	public void loadProjectionMatrix(Matrix4f matrix) {
		super.loadMatrix(location_projectionMatrix, matrix);

	}

	public void loadViewMatrix(Camera camera) {
		Matrix4f vewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, vewMatrix);

	}

}
