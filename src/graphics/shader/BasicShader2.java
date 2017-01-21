package graphics.shader;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class BasicShader2 extends Shader {

  public BasicShader2() {
    super();
    
    addVertexShader(loadShader("vertex.vs"));
    addFragmentShader(loadShader("fragment.fs"));
    compileShader();
    
    // Create the uniforms
    addUniform("projectionMatrix");
    addUniform("modelViewMatrix");
    addUniform("texture_sampler");
    addUniform("color");
    addUniform("useColor");
  }

  @Override
  public void bindAttributes() {}
  
  public void updateProjection(Matrix4f matrix) {
    setUniform("projectionMatrix", matrix);
  }
  
  public void updateModelViewMatrix(Matrix4f matrix) {
    setUniform("modelViewMatrix", matrix);
  }
  
  public void updateTextureSampler(int value) {
    setUniform("texture_sampler", value);
  }
  
  public void setColor(Vector3f color) {
    setUniform("color", color);
  }
  
  public void setUseColor(boolean useColor) {
    setUniform("useColor", useColor ? 0 : 1);
  }

}
