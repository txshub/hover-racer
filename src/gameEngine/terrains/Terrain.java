package gameEngine.terrains;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import gameEngine.models.RawModel;
import gameEngine.renderEngine.Loader;
import gameEngine.skybox.SkyboxRenderer;
import gameEngine.textures.TerrainTexture;
import gameEngine.textures.TerrainTexturePack;
import gameEngine.toolbox.Maths;

/**
 * @author rtm592 The terrain
 */
public class Terrain {

	public static final float SIZE = 3 * SkyboxRenderer.SIZE;
	private static final float MAX_HEIGHT = 40;
	private static final float MAX_PIXEL_COLOUR = 256 * 256 * 256;

	private float x;
	private float z;
	private RawModel model;
	private TerrainTexturePack texturePack;
	private TerrainTexture blendMap;

	private float[][] heights;

	/**
	 * @param gridX
	 *            start x coord
	 * @param gridZ
	 *            start z coord
	 * @param loader
	 *            the loader
	 * @param texturePack
	 *            the terrain texture pack
	 * @param blendMap
	 *            the blend map
	 * @param heightMap
	 *            the height map
	 */
	public Terrain(int gridX, int gridZ, Loader loader, TerrainTexturePack texturePack, TerrainTexture blendMap,
			String heightMap) {
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.x = gridX;
		this.z = gridZ;
		this.model = generateTerrain(loader, heightMap);
	}

	/**
	 * @return the x coord
	 */
	public float getX() {
		return x;
	}

	/**
	 * @return the z coord
	 */
	public float getZ() {
		return z;
	}

	/**
	 * @param x
	 *            the new x coord
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * @param z
	 *            the new z coord
	 */
	public void setZ(float z) {
		this.z = z;
	}

	/**
	 * @param x
	 *            move by x
	 */
	public void moveX(float x) {
		this.x += x;
	}

	/**
	 * @param z
	 *            move by z
	 */
	public void moveZ(float z) {
		this.z += z;
	}

	/**
	 * @return the terrains raw model
	 */
	public RawModel getModel() {
		return model;
	}

	/**
	 * @return the texture pack
	 */
	public TerrainTexturePack getTexturePack() {
		return texturePack;
	}

	/**
	 * @return the blend map
	 */
	public TerrainTexture getBlendMap() {
		return blendMap;
	}

	/**
	 * get a position of the world on the terrain
	 * 
	 * @param worldX
	 *            the world x coord
	 * @param worldZ
	 *            the world z coord
	 * @return the position on the terrain
	 */
	public float getHeightOfTerrain(float worldX, float worldZ) {
		float terrainX = worldX - this.x;
		float terrainZ = worldZ - this.z;
		float gridSquareSize = SIZE / ((float) heights.length - 1);
		int gridX = (int) Math.floor(terrainX / gridSquareSize);
		int gridZ = (int) Math.floor(terrainZ / gridSquareSize);

		if (gridX >= heights.length - 1 || gridZ >= heights.length - 1 || gridX < 0 || gridZ < 0) {
			return 0;
		}

		float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
		float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;
		float answer;

		if (xCoord <= (1 - zCoord)) {
			answer = Maths.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0),
					new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(0, heights[gridX][gridZ + 1], 1),
					new Vector2f(xCoord, zCoord));
		} else {
			answer = Maths.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0),
					new Vector3f(1, heights[gridX + 1][gridZ + 1], 1), new Vector3f(0, heights[gridX][gridZ + 1], 1),
					new Vector2f(xCoord, zCoord));
		}

		return answer;
	}

	/**
	 * @param loader
	 *            the loader
	 * @param heightMap
	 *            the height map
	 * @return the terrains raw model
	 */
	private RawModel generateTerrain(Loader loader, String heightMap) {

		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("src/resources/" + heightMap + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		int VERTEX_COUNT = image.getHeight();

		int count = VERTEX_COUNT * VERTEX_COUNT;
		heights = new float[VERTEX_COUNT][VERTEX_COUNT];
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count * 2];
		int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT * 1)];
		int vertexPointer = 0;
		for (int i = 0; i < VERTEX_COUNT; i++) {
			for (int j = 0; j < VERTEX_COUNT; j++) {
				vertices[vertexPointer * 3] = (float) j / ((float) VERTEX_COUNT - 1) * SIZE;
				float height = getHeight(j, i, image);
				vertices[vertexPointer * 3 + 1] = height;
				heights[j][i] = height;
				vertices[vertexPointer * 3 + 2] = (float) i / ((float) VERTEX_COUNT - 1) * SIZE;
				Vector3f normal = calculateNormal(j, i, image);
				normals[vertexPointer * 3] = normal.x;
				normals[vertexPointer * 3 + 1] = normal.y;
				normals[vertexPointer * 3 + 2] = normal.z;
				textureCoords[vertexPointer * 2] = (float) j / ((float) VERTEX_COUNT - 1);
				textureCoords[vertexPointer * 2 + 1] = (float) i / ((float) VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for (int gz = 0; gz < VERTEX_COUNT - 1; gz++) {
			for (int gx = 0; gx < VERTEX_COUNT - 1; gx++) {
				int topLeft = (gz * VERTEX_COUNT) + gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz + 1) * VERTEX_COUNT) + gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return loader.loadToVAO(vertices, textureCoords, normals, indices);
	}

	/**
	 * add the height map to the terrain
	 * 
	 * @param x
	 *            the x coord
	 * @param z
	 *            the z coord
	 * @param image
	 *            the height map
	 * @return the xyz coords of the point on the terrain
	 */
	private Vector3f calculateNormal(int x, int z, BufferedImage image) {
		float heightL = getHeight(x - 1, z, image);
		float heightR = getHeight(x + 1, z, image);
		float heightD = getHeight(x, z - 1, image);
		float heightU = getHeight(x, z + 1, image);
		Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
		normal.normalise();
		return normal;
	}

	/**
	 * @param x
	 *            the x coord
	 * @param z
	 *            the z coord
	 * @param image
	 *            the height map
	 * @return the height of the terrain
	 */
	private float getHeight(int x, int z, BufferedImage image) {
		if (x < 0 || x >= image.getHeight() || z < 0 || z >= image.getHeight()) {
			return 0;
		}
		float height = image.getRGB(x, z);
		height += MAX_PIXEL_COLOUR / 2f;
		height /= MAX_PIXEL_COLOUR / 2f;
		height *= MAX_HEIGHT;
		return height;
	}

}
