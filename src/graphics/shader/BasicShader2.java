package graphics.shader;

import org.joml.Matrix4f;

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

}
