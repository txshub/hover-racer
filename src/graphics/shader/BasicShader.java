package graphics.shader;

import math.Matrix4f;
import math.Vector3f;

public class BasicShader extends Shader {

  private static final String VERTEX_FILE = "basicVertex.vs";
  private static final String FRAGMENT_FILE = "basicFragment.fs";
  
  public BasicShader() {
    super();
    
    addVertexShader(loadShader(VERTEX_FILE));
    addFragmentShader(loadShader(FRAGMENT_FILE));
    compileShader();
    
    addUniform("projectionMatrix");
    addUniform("viewMatrix");
    addUniform("worldMatrix");
    
    addUniform("sun");
  }
  
  @Override
  public void bindAttributes() {
    bindAttribute(0, "position");
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
