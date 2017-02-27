package uiToolkit;

import java.util.List;

import org.lwjgl.opengl.GL11;

import gameEngine.renderEngine.Loader;

/**
 * 
 * @author Reece Bennett
 *
 */
public class UIRenderer {

  private UIShader shader;
  
  public UIRenderer(Loader loader) {
    shader = new UIShader();
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
