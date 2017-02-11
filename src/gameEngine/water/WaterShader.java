package gameEngine.water;
 
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import gameEngine.entities.Camera;
import gameEngine.entities.Light;
import gameEngine.shaders.ShaderProgram;
import gameEngine.toolbox.Maths;
import gameEngine.toolbox.VecCon;
 
public class WaterShader extends ShaderProgram {
 
    private final static String VERTEX_FILE = "src/gameEngine/water/waterVertex.txt";
    private final static String FRAGMENT_FILE = "src/gameEngine/water/waterFragment.txt";
 
    private int location_modelMatrix;
    private int location_viewMatrix;
    private int location_projectionMatrix;
    private int location_reflectionTexture;
    private int location_refractionTexture;
    private int location_dudvMap;
    private int location_normalMap;
	private int location_depthMap;
    private int location_moveFactor;
    private int location_cameraPosition;
	private int location_lightPosition;
	private int location_lightColour;
	private int location_skyColour;
	private int location_isAboveTerrain;
 
    public WaterShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }
 
    @Override
    protected void bindAttributes() {
    	super.bindAttributes(0, "position");
    }
 
    @Override
    protected void getAllUniformLocations() {
        location_projectionMatrix = getUniformLocation("projectionMatrix");
        location_viewMatrix = getUniformLocation("viewMatrix");
        location_modelMatrix = getUniformLocation("modelMatrix");
        location_reflectionTexture = getUniformLocation("reflectionTexture");
        location_refractionTexture = getUniformLocation("refractionTexture");
        location_dudvMap = getUniformLocation("dudvMap");
        location_normalMap = getUniformLocation("normalMap");
		location_depthMap = getUniformLocation("depthMap");
        location_moveFactor = getUniformLocation("moveFactor");
        location_cameraPosition = getUniformLocation("cameraPosition");
		location_lightPosition = getUniformLocation("lightPosition");
		location_lightColour = getUniformLocation("lightColour");
		location_skyColour = getUniformLocation("skyColour");
		location_isAboveTerrain = getUniformLocation("isAboveTerrain");
    }
    
    public void loadIsAboveTerrain(boolean isAboveTerrain){
		super.loadBoolean(location_isAboveTerrain, isAboveTerrain);
    }
    
    public void loadSkyColour(float r, float g, float b){
		super.loadVector3f(location_skyColour, new Vector3f(r,g,b));
    }
    
    public void loadLight(Light sun){
    	super.loadVector3f(location_lightColour, VecCon.toLWJGL(sun.getColour()));
    	super.loadVector3f(location_lightPosition, VecCon.toLWJGL(sun.getPosition()));
    }
    
    public void connectTextureUnits(){
    	super.loadInt(location_reflectionTexture, 0);
    	super.loadInt(location_refractionTexture, 1);
    	super.loadInt(location_dudvMap, 2);
    	super.loadInt(location_normalMap, 3);
    	super.loadInt(location_depthMap, 4);
    }
    
    public void loadMoveFactor(float moveFactor) {
    	super.loadFloat(location_moveFactor, moveFactor);
    }
    
    public void loadProjectionMatrix(Matrix4f projection) {
    	super.loadMatrix(location_projectionMatrix, projection);
    }
     
    public void loadViewMatrix(Camera camera){
    	Matrix4f viewMatrix = Maths.createViewMatrix(camera);
    	super.loadMatrix(location_viewMatrix, viewMatrix);
    	super.loadVector3f(location_cameraPosition, camera.getPosition());
    }
 
    public void loadModelMatrix(Matrix4f modelMatrix){
    	super.loadMatrix(location_modelMatrix, modelMatrix);
    }
 
}
