package uiToolkit;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import gameEngine.models.RawModel;
import gameEngine.renderEngine.Loader;

public class UIRenderer {

  private RawModel quad;
  private UIShader shader;
  private Loader loader;
  
  public UIRenderer(Loader loader) {
    shader = new UIShader();
    this.loader = loader;
  }
  
  public void render(List<Container> containers) {
    shader.start();
    GL11.glEnable(GL11.GL_BLEND);
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    GL11.glDisable(GL11.GL_DEPTH_TEST);
    
    for (Container c : containers) {
      c.render();
    }
    
    GL11.glEnable(GL11.GL_DEPTH_TEST);
    GL11.glDisable(GL11.GL_BLEND);
    shader.stop();
  }

}
