package gameEngine.shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 * @author rtm592 A abstract shader
 */
public abstract class ShaderProgram {

  private int programID;
  private int vertexShaderID;
  private int fragmentShaderID;

  private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

  /**
   * @param vertexFile
   *          the vertex shader file
   * @param fragmentFile
   *          the fragment shader file
   */
  public ShaderProgram(String vertexFile, String fragmentFile) {
    vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
    fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
    programID = GL20.glCreateProgram();
    GL20.glAttachShader(programID, vertexShaderID);
    GL20.glAttachShader(programID, fragmentShaderID);
    bindAttributes();
    GL20.glLinkProgram(programID);
    GL20.glValidateProgram(programID);
    getAllUniformLocations();
  }

  protected abstract void getAllUniformLocations();

  /**
   * @param uniformName
   *          the name of the uniform variable
   * @return the location
   */
  protected int getUniformLocation(String uniformName) {
    return GL20.glGetUniformLocation(programID, uniformName);
  }

  /**
   * starts the shader
   */
  public void start() {
    GL20.glUseProgram(programID);
  }

  /**
   * stops the shader
   */
  public void stop() {
    GL20.glUseProgram(0);
  }

  /**
   * cleans the shader program
   */
  public void cleanUp() {
    stop();
    GL20.glDetachShader(programID, vertexShaderID);
    GL20.glDetachShader(programID, fragmentShaderID);
    GL20.glDeleteShader(vertexShaderID);
    GL20.glDeleteShader(fragmentShaderID);
    GL20.glDeleteProgram(programID);
  }

  protected abstract void bindAttributes();

  /**
   * @param attribute
   *          the attribute id
   * @param variableName
   *          the name of the variable
   */
  protected void bindAttribute(int attribute, String variableName) {
    GL20.glBindAttribLocation(programID, attribute, variableName);
  }

  /**
   * @param location
   *          the location to load to
   * @param value
   *          the value to load
   */
  protected void loadFloat(int location, float value) {
    GL20.glUniform1f(location, value);
  }

  /**
   * @param location
   *          the location to load to
   * @param value
   *          the value to load
   */
  protected void loadInt(int location, int value) {
    GL20.glUniform1i(location, value);
  }

  /**
   * @param location
   *          the location to load to
   * @param vector
   *          the vector to load
   */
  protected void loadVector(int location, Vector3f vector) {
    GL20.glUniform3f(location, vector.x, vector.y, vector.z);
  }

  /**
   * @param location
   *          the location to load to
   * @param vector
   *          the vector to load
   */
  protected void loadVector(int location, Vector4f vector) {
    GL20.glUniform4f(location, vector.x, vector.y, vector.z, vector.w);
  }

  /**
   * @param location
   *          the location to load to
   * @param vector
   *          the vector to load
   */
  protected void load2DVector(int location, Vector2f vector) {
    GL20.glUniform2f(location, vector.x, vector.y);
  }

  /**
   * @param location
   *          the location to load to
   * @param value
   *          the value to load
   */
  protected void loadBoolean(int location, boolean value) {
    float toLoad = 0;
    if (value) {
      toLoad = 1;
    }
    GL20.glUniform1f(location, toLoad);
  }

  /**
   * @param location
   *          the location to load to
   * @param matrix
   *          the matrix to load
   */
  protected void loadMatrix(int location, Matrix4f matrix) {
    matrix.store(matrixBuffer);
    matrixBuffer.flip();
    GL20.glUniformMatrix4(location, false, matrixBuffer);
  }

  /**
   * @param file
   *          the name of the shader file
   * @param type
   *          the type of the shader
   * @return the shader id
   */
  private static int loadShader(String file, int type) {
    StringBuilder shaderSource = new StringBuilder();
    try {
      BufferedReader reader = new BufferedReader(new FileReader(file));
      String line;
      while ((line = reader.readLine()) != null) {
        shaderSource.append(line).append("//\n");
      }
      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(-1);
    }
    int shaderID = GL20.glCreateShader(type);
    GL20.glShaderSource(shaderID, shaderSource);
    GL20.glCompileShader(shaderID);
    if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
      System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
      System.err.println("Could not compile shader!");
      System.exit(-1);
    }
    return shaderID;
  }

}
