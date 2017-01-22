package engine.graphics.shader;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.FloatBuffer;
import java.util.HashMap;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

public abstract class Shader {
  
  private HashMap<String, Integer> uniforms;

  private int programID;
  
  /**
   * A shader program with Vertex and Fragment shaders.
   */
  public Shader() {
    programID = glCreateProgram();
    uniforms = new HashMap<>();
    
    if (programID == 0) {
      System.err.println("Shader creation failed! No valid memory.");
      System.exit(1);
    }
  }
  
  /**
   * Loads a shader file from the /res/shaders/ folder.
   * 
   * @param fileName the shader filename
   * @return the shader code
   */
  public static String loadShader(String fileName) {
    StringBuilder source = new StringBuilder();
    BufferedReader reader = null;
    
    // Read the shader file
    try {
      
      reader = new BufferedReader(new FileReader("./res/shaders/" + fileName));
      
      String line;
      while ((line = reader.readLine()) != null) {
        source.append(line);
        source.append("\n");
      }
      
      reader.close();
      
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
    
    return source.toString();
  }
  
  /**
   * Adds a shader to the program.
   *  
   * @param text the source of the shader
   * @param type the type of shader to be created
   */
  private void addShader(String text, int type) {
    int shader = glCreateShader(type);
    
    if (shader == 0) {
      System.err.println("Shader creation failed! No valid memory.");
      System.exit(1);
    }
    
    glShaderSource(shader, text);
    glCompileShader(shader);
    
    if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
      System.err.println("Error compiling shader code: " + glGetShaderInfoLog(shader, 1024));
    }
    
    glAttachShader(programID, shader);
  }
  
  /**
   * Adds a shader of type GL_VERTEX_SHADER to the program.
   * 
   * @param text the source of the shader
   */
  protected void addVertexShader(String text) {
    addShader(text, GL_VERTEX_SHADER);
  }
  
  /**
   * Adds a shader of type GL_FRAGMENT_SHADER to the program.
   * 
   * @param text the source of the shader
   */
  protected void addFragmentShader(String text) {
    addShader(text, GL_FRAGMENT_SHADER);
  }
  
  /**
   * Compiles the shader program and binds the attributes used in the shader file.
   */
  protected void compileShader() {
    glLinkProgram(programID);
    
    if (glGetProgrami(programID, GL_LINK_STATUS) == GL_FALSE) {
      System.err.println("Error linking shader code: " + glGetShaderInfoLog(programID, 1024));
    }
    
    glValidateProgram(programID);
    
    if (glGetProgrami(programID, GL_VALIDATE_STATUS) == GL_FALSE) {
      System.err.println("Warning validating shader code: " + glGetShaderInfoLog(programID, 1024));
    }
    
    bindAttributes();
  }
  
  /**
   * Activates this program for use in rendering.
   */
  public void bind() {
    glUseProgram(programID);
  }
  
  /**
   * Stops using any program in rendering.
   */
  public static void unbind() {
    glUseProgram(0);
  }
  
  public abstract void bindAttributes();
  
  protected void bindAttribute(int attribute, String variableName) {
    glBindAttribLocation(programID, attribute, variableName);
  }
  
  protected void addUniform(String uniformName) {
    int uniformLocation = glGetUniformLocation(programID, uniformName);
    
    if (uniformLocation == 0xFFFFFFFF) {
      System.err.println("Error: Could not find uniform " + uniformName);
      System.exit(1);
    }
    
    uniforms.put(uniformName, uniformLocation);
  }
  
  protected void setUniform(String uniformName, int value) {
    glUniform1i(uniforms.get(uniformName), value);
  }
  
  protected void setUniform(String uniformName, float value) {
    glUniform1f(uniforms.get(uniformName), value);
  }
  
  protected void setUniform(String uniformName, Vector2f value) {
    glUniform2f(uniforms.get(uniformName), value.x, value.y);
  }
  
  protected void setUniform(String uniformName, Vector3f value) {
    glUniform3f(uniforms.get(uniformName), value.x, value.y, value.z);
  }
  
  protected void setUniform(String uniformName, Vector4f value) {
    glUniform4f(uniforms.get(uniformName), value.x, value.y, value.z, value.w);
  }
  
  protected void setUniform(String uniformName, Matrix4f value) {
    // Dump the matrix into a float buffer
    FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
    value.get(buffer);
    glUniformMatrix4fv(uniforms.get(uniformName), false, buffer);
  }
  
  public void cleanUp() {
    unbind();
    if (programID != 0) {
      glDeleteProgram(programID);
    }
  }

}
