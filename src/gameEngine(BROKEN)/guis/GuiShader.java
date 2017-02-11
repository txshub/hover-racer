package gameEngine.guis;

import org.joml.Matrix4f;

import gameEngine.shaders.ShaderProgram;

public class GuiShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/gameEngine/guis/guiVertexShader.txt";
	private static final String FRAGMENT_FILE = "src/gameEngine/guis/guiFragmentShader.txt";

	private int location_transformationMatrix;

	public GuiShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	public void loadTransformation(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttributes(0, "position");
	}

}