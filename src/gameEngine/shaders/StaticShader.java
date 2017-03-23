package gameEngine.shaders;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import gameEngine.entities.Camera;
import gameEngine.entities.Light;
import gameEngine.toolbox.Maths;
import gameEngine.toolbox.VecCon;

public class StaticShader extends ShaderProgram {

  private static final int MAX_LIGHTS = 4;

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
  private int location_numberOfRows;
  private int location_offset;
  private int location_plane;

  /**
   * loads vertex and fragment shader files
   */
  public StaticShader() {
    super(VERTEX_FILE, FRAGMENT_FILE);
  }

  @Override
  protected void bindAttributes() {
    super.bindAttribute(0, "position");
    super.bindAttribute(1, "textureCoordinates");
    super.bindAttribute(2, "normal");
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
    location_numberOfRows = super.getUniformLocation("numberOfRows");
    location_offset = super.getUniformLocation("offset");
    location_plane = super.getUniformLocation("plane");

    location_lightPosition = new int[MAX_LIGHTS];
    location_lightColour = new int[MAX_LIGHTS];
    location_attenuation = new int[MAX_LIGHTS];
    for (int i = 0; i < MAX_LIGHTS; i++) {
      location_lightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
      location_lightColour[i] = super.getUniformLocation("lightColour[" + i + "]");
      location_attenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
    }
  }

  /**
   * @param plane
   *          the clipplane to load
   */
  public void loadClipPlane(Vector4f plane) {
    super.loadVector(location_plane, plane);
  }

  /**
   * @param numberOfRows
   *          the number of rows
   */
  public void loadNumberOfRows(int numberOfRows) {
    super.loadFloat(location_numberOfRows, numberOfRows);
  }

  /**
   * @param x
   *          the x value
   * @param y
   *          the y value
   */
  public void loadOffset(float x, float y) {
    super.load2DVector(location_offset, new Vector2f(x, y));
  }

  /**
   * @param r
   *          the red value
   * @param g
   *          the green value
   * @param b
   *          the blue value
   */
  public void loadSkyColour(float r, float g, float b) {
    super.loadVector(location_skyColour, new Vector3f(r, g, b));
  }

  /**
   * @param useFake
   *          whether to use fake lighting
   */
  public void loadFakeLightingVariable(boolean useFake) {
    super.loadBoolean(location_useFakeLighting, useFake);
  }

  /**
   * @param damper
   *          the light damper
   * @param reflectivity
   *          the light reflectivity
   */
  public void loadShineVariables(float damper, float reflectivity) {
    super.loadFloat(location_shineDamper, damper);
    super.loadFloat(location_reflectivity, reflectivity);
  }

  /**
   * @param matrix
   *          the transformation matrix
   */
  public void loadTransformationMatrix(Matrix4f matrix) {
    super.loadMatrix(location_transformationMatrix, matrix);
  }

  /**
   * @param lights
   *          a list of lights to load
   */
  public void loadLights(List<Light> lights) {
    for (int i = 0; i < MAX_LIGHTS; i++) {
      if (i < lights.size()) {
        super.loadVector(location_lightPosition[i], VecCon.toLWJGL(lights.get(i).getPosition()));
        super.loadVector(location_lightColour[i], VecCon.toLWJGL(lights.get(i).getColour()));
        super.loadVector(location_attenuation[i], VecCon.toLWJGL(lights.get(i).getAttenuation()));
      } else {
        super.loadVector(location_lightPosition[i], new Vector3f(0, 0, 0));
        super.loadVector(location_lightColour[i], new Vector3f(0, 0, 0));
        super.loadVector(location_attenuation[i], new Vector3f(1, 0, 0));
      }
    }
  }

  /**
   * @param camera
   *          the camera
   */
  public void loadViewMatrix(Camera camera) {
    Matrix4f viewMatrix = Maths.createViewMatrix(camera);
    super.loadMatrix(location_viewMatrix, viewMatrix);
  }

  /**
   * @param projection
   *          the projection matrix
   */
  public void loadProjectionMatrix(Matrix4f projection) {
    super.loadMatrix(location_projectionMatrix, projection);
  }

}
