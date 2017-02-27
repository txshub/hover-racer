package uiToolkit;

import gameEngine.shaders.ShaderProgram;

public class UIShader extends ShaderProgram {
  
  private static final String VERTEX_FILE = "src/uiToolKit/uiVertexShader.txt";
  private static final String FRAGMENT_FILE = "src/uiToolKit/uiFragmentShader.txt";

  public UIShader() {
    super(VERTEX_FILE, FRAGMENT_FILE);
  }

  @Override
  protected void getAllUniformLocations() {
  }

  @Override
  protected void bindAttributes() {
    bindAttribute(0, "position");
  }
  
}
