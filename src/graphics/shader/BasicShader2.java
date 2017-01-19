package graphics.shader;

public class BasicShader2 extends Shader {

  public BasicShader2() {
    super();
    
    addVertexShader(loadShader("vertex2.vs"));
    addFragmentShader(loadShader("fragment2.fs"));
    compileShader();
  }

  @Override
  public void bindAttributes() {
    // TODO Auto-generated method stub
    
  }

}
