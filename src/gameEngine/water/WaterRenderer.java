package gameEngine.water;
 
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import gameEngine.entities.Camera;
import gameEngine.entities.Light;
import gameEngine.models.RawModel;
import gameEngine.renderEngine.DisplayManager;
import gameEngine.renderEngine.Loader;
import gameEngine.renderEngine.MasterRenderer;
import gameEngine.toolbox.Maths;
 
/**
 * @author rtm592
 *
 */
public class WaterRenderer {
 
    private RawModel quad;
    private WaterShader shader;
    private WaterFrameBuffers fbos;
    
    private static String DUDV_MAP = "waterDUDV";
    private static int dudvTexture;
    private static String NORMAL_MAP = "waternormal";
    private static int normalTexture;
    private static final float WAVE_SPEED = 0.03f;
    private static float moveFactor = 0f;
 
    public WaterRenderer(Loader loader, WaterShader shader, Matrix4f projectionMatrix, WaterFrameBuffers fbos) {
        this.shader = shader;
        this.fbos = fbos;
        dudvTexture = loader.loadTexture(DUDV_MAP);
        normalTexture = loader.loadTexture(NORMAL_MAP);
        shader.start();
        shader.connectTextureUnits();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
        setUpVAO(loader);
    }
 
    public void render(WaterTile[][] waters, Camera camera, Light sun) {
        prepareRender(camera, sun);  
        for (WaterTile[] tiles : waters) {
            for (WaterTile tile : tiles) {
            Matrix4f modelMatrix = Maths.createTransformationMatrix(
                    new Vector3f(tile.getX(), tile.getHeight(), tile.getZ()), 0, 0, 0,
                    WaterTile.TILE_SIZE);
            shader.loadModelMatrix(modelMatrix);
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertexCount());
            }
        }
        unbind();
    }
     
    private void prepareRender(Camera camera, Light sun){
        shader.start();
        shader.loadSkyColour(MasterRenderer.R, MasterRenderer.G, MasterRenderer.B);
        shader.loadViewMatrix(camera);
        shader.loadLight(sun);
        moveFactor += WAVE_SPEED * DisplayManager.getFrameTimeSeconds();
        shader.loadMoveFactor(moveFactor);
        GL30.glBindVertexArray(quad.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getReflectionTexture());
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionTexture());
        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, dudvTexture);
        GL13.glActiveTexture(GL13.GL_TEXTURE3);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, normalTexture);
        GL13.glActiveTexture(GL13.GL_TEXTURE4);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionDepthTexture());
        
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }
     
    private void unbind(){
    	GL11.glDisable(GL11.GL_BLEND);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.stop();
    }
 
    private void setUpVAO(Loader loader) {
        // Just x and z vectex positions here, y is set to 0 in v.shader
        float[] vertices = { -1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1 };
        quad = loader.loadToVAO(vertices, 2);
    }
 
}