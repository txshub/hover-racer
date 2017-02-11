package gameEngine.guis;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import gameEngine.models.RawModel;
import gameEngine.renderEngine.Loader;
import gameEngine.toolbox.Maths;
import gameEngine.toolbox.VecCon;

public class GuiRenderer {

	private RawModel quad;
	private GuiShader guiShader;

	public GuiRenderer(Loader loader) {

		float[] positions = { -1, 1, -1, -1, 1, 1, 1, -1 };
		quad = loader.loadToVAO(positions,2);
		guiShader = new GuiShader();

	}

	public void render(List<GuiTexture> guis){
		
		guiShader.start();
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		for(GuiTexture gui: guis){
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTexture());
			Matrix4f matrix = Maths.createTransformationMatrix(VecCon.toLWJGL(gui.getPosition()), VecCon.toLWJGL(gui.getScale()));
			guiShader.loadTransformation(matrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		}
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		guiShader.stop();
		
	}
	public void cleanUp(){
		guiShader.cleanUp();
	}
	
}
