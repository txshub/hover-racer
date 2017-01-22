package engine.graphics.shader;

import org.joml.Vector3f;
import org.joml.Matrix4f;;

public class BasicShader extends Shader {

  private static final String VERTEX_FILE = "basicVertex.vs";
  private static final String FRAGMENT_FILE = "basicFragment.fs";
  
  public BasicShader() {
    super();
    
    // Load the shader files and compile
    addVertexShader(loadShader(VERTEX_FILE));
    addFragmentShader(loadShader(FRAGMENT_FILE));
    compileShader();
    
    // Add uniforms to be used in the program
    addUniform("projectionMatrix");
    addUniform("viewMatrix");
    addUniform("worldMatrix");
    addUniform("sun");
  }
  
  @Override
  public void bindAttributes() {
    bindAttribute(0, "position");
    bindAttribute(1, "normal");
  }
  
  public void updateProjectionMatrix(Matrix4f projectionMatrix) {
    setUniform("projectionMatrix", projectionMatrix);
  }
  
  public void updateWorldMatrix(Matrix4f worldMatrix) {
    setUniform("worldMatrix", worldMatrix);
  }
  
  public void updateSun(Vector3f sun) {
    setUniform("sun", sun);
  }
  
  public void updateViewMatrix(Matrix4f viewMatrix) {
    setUniform("viewMatrix", viewMatrix);
  }

}
