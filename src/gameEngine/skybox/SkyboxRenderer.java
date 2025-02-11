package gameEngine.skybox;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import gameEngine.entities.Camera;
import gameEngine.models.RawModel;
import gameEngine.renderEngine.DisplayManager;
import gameEngine.renderEngine.Loader;
import gameEngine.renderEngine.MasterRenderer;

/**
 * @author rtm592 A renderer for the skybox
 */
public class SkyboxRenderer {

  public static final float SIZE = 6000f;

  private static final float[] VERTICES = { -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE,
      -SIZE, SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE,

      -SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE,
      SIZE, -SIZE, -SIZE, SIZE,

      SIZE, -SIZE, -SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE,
      SIZE, -SIZE, -SIZE,

      -SIZE, -SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE,
      -SIZE, -SIZE, SIZE,

      -SIZE, SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE, SIZE,
      -SIZE, SIZE, -SIZE,

      -SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE,
      SIZE, SIZE, -SIZE, SIZE };

  private static String head = "clouds";
  private static String[] TEXTURE_FILES = { head + "_rt", head + "_lf", head + "_up", head + "_dn",
      head + "_bk", head + "_ft" };
  private static String head1 = "night";
  private static String[] NIGHT_TEXTURE_FILES = { head1 + "_rt", head1 + "_lf", head1 + "_up",
      head1 + "_dn", head1 + "_bk", head1 + "_ft" };

  private RawModel cube;
  private int texture;
  private int nightTexture;
  private SkyboxShader shader;
  private float time = 0f;

  /**
   * @param loader
   *          the loader
   * @param projectionMatrix
   *          the projection matrix
   */
  public SkyboxRenderer(Loader loader, Matrix4f projectionMatrix) {

    cube = loader.loadToVAO(VERTICES, 3);
    texture = loader.loadCubeMap(TEXTURE_FILES);
    nightTexture = loader.loadCubeMap(NIGHT_TEXTURE_FILES);
    shader = new SkyboxShader();
    shader.start();
    shader.connectTExtureUnits();
    shader.loadProjectionMatrix(projectionMatrix);
    shader.stop();

  }

  /**
   * @param camera
   *          the camera
   * @param r
   *          red
   * @param g
   *          green
   * @param b
   *          blue
   */
  public void render(Camera camera, float r, float g, float b) {
    shader.start();
    shader.loadViewMatrix(camera);
    shader.loadFogColour(r, g, b);
    GL30.glBindVertexArray(cube.getVaoID());
    GL20.glEnableVertexAttribArray(0);
    bindTextures();
    GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
    GL20.glDisableVertexAttribArray(0);
    GL30.glBindVertexArray(0);
    shader.stop();
  }

  /**
   * bind the skybox textures
   */
  private void bindTextures() {
    time = DisplayManager.getMilliSecondTimer() % 24000;
    int texture1;
    int texture2;
    float blendFactor;
    int time1 = 5000;
    int time2 = 8000;
    int time3 = 21000;
    int time4 = 24000;
    MasterRenderer.RED = 0f;
    float greenDiff = 204f / 255f, blueDiff = 1f - 153f / 255f;

    if (time >= 0 && time < time1) {
      texture1 = nightTexture;
      texture2 = nightTexture;
      blendFactor = (time - 0) / (time1 - 0);
      MasterRenderer.GREEN = 0f;
      MasterRenderer.BLUE = 153f / 255f;
    } else if (time >= time1 && time < time2) {
      texture1 = nightTexture;
      texture2 = texture;
      blendFactor = (time - time1) / (time2 - time1);
      MasterRenderer.GREEN = 0 + greenDiff * blendFactor;
      MasterRenderer.BLUE = 153f / 255f + blueDiff * blendFactor;
    } else if (time >= time2 && time < time3) {
      texture1 = texture;
      texture2 = texture;
      MasterRenderer.GREEN = 0 + greenDiff;
      MasterRenderer.BLUE = 153f / 255f + blueDiff;
      blendFactor = (time - time2) / (time3 - time2);
    } else {
      texture1 = texture;
      texture2 = nightTexture;
      blendFactor = (time - time3) / (time4 - time3);
      MasterRenderer.GREEN = greenDiff - greenDiff * blendFactor;
      MasterRenderer.BLUE = 1f - blueDiff * blendFactor;
    }

    GL13.glActiveTexture(GL13.GL_TEXTURE0);
    GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture1);
    GL13.glActiveTexture(GL13.GL_TEXTURE1);
    GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture2);
    shader.loadBlendFactor(blendFactor);
  }

}
