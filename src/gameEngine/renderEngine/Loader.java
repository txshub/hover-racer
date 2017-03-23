package gameEngine.renderEngine;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import gameEngine.models.RawModel;
import gameEngine.textures.TextureData;

/**
 * @author rtm592 A class to handle openGL loading
 */
public class Loader {

  private List<Integer> vaos = new ArrayList<Integer>();
  private List<Integer> vbos = new ArrayList<Integer>();
  private List<Integer> textures = new ArrayList<Integer>();

  /**
   * loads model data to a VAO
   * 
   * @param positions
   *          the vertex position
   * @param textureCoords
   *          the vertex texture coords
   * @param normals
   *          the vertex normals
   * @param indices
   *          the indices
   * @return the raw model
   */
  public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals,
      int[] indices) {
    int vaoID = createVAO();
    bindIndicesBuffer(indices);
    storeDataInAttributeList(0, 3, positions);
    storeDataInAttributeList(1, 2, textureCoords);
    storeDataInAttributeList(2, 3, normals);
    unbindVAO();
    return new RawModel(vaoID, indices.length);
  }

  /**
   * loads model data to a VAO
   * 
   * @param positions
   *          the vertex position
   * @param textureCoords
   *          the vertex texture coords
   * @param normals
   *          the vertex normals
   * @param tangents
   *          the vertex tangents
   * @param indices
   *          the indices
   * @return the raw model
   */
  public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals,
      float[] tangents, int[] indices) {
    int vaoID = createVAO();
    bindIndicesBuffer(indices);
    storeDataInAttributeList(0, 3, positions);
    storeDataInAttributeList(1, 2, textureCoords);
    storeDataInAttributeList(2, 3, normals);
    storeDataInAttributeList(3, 3, tangents);
    unbindVAO();
    return new RawModel(vaoID, indices.length);
  }

  /**
   * loads model data to a VAO
   * 
   * @param positions
   *          the vertex position
   * @param textureCoords
   *          the vertex texture coords
   * @return the raw model
   */
  public RawModel loadToVAO(float[] positions, float[] textureCoords) {
    int vaoID = createVAO();
    storeDataInAttributeList(0, 2, positions);
    storeDataInAttributeList(1, 2, textureCoords);
    unbindVAO();
    return new RawModel(vaoID, positions.length / 2);
  }

  /**
   * loads model data to a VAO
   * 
   * @param positions
   *          the vertex position
   * @param dimensions
   *          the positions dimensions
   * @return the raw model
   */
  public RawModel loadToVAO(float[] positions, int dimensions) {
    int vaoID = createVAO();
    storeDataInAttributeList(0, dimensions, positions);
    unbindVAO();
    return new RawModel(vaoID, positions.length / dimensions);
  }

  /**
   * @param fileName
   *          the texture file
   * @return the texture
   */
  public Texture loadRawTexture(String fileName) {
    Texture texture = null;
    try {
      texture = TextureLoader.getTexture("PNG",
          new FileInputStream("src/resources/" + fileName + ".png"));
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("Tried to load texture " + fileName + ".png , didn't work");
      System.exit(-1);
    }
    textures.add(texture.getTextureID());
    return texture;
  }

  /**
   * @param fileName
   *          the texture file
   * @return the texture
   */
  public int loadTexture(String fileName) {
    Texture texture = null;
    try {
      texture = TextureLoader.getTexture("PNG",
          new FileInputStream("src/resources/" + fileName + ".png"));
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("Tried to load texture " + fileName + ".png , didn't work");
      System.exit(-1);
    }
    textures.add(texture.getTextureID());
    return texture.getTextureID();
  }

  /**
   * @param fileName
   *          the texture file
   * @param the
   *          mipmapping value to use on the texture
   * @return the texture
   */
  public int loadMipmappedTexture(String fileName, float mipmappingValue) {
    Texture texture = null;
    try {
      texture = TextureLoader.getTexture("PNG",
          new FileInputStream("src/resources/" + fileName + ".png"));
      // mipmapping
      GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
      GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
          GL11.GL_LINEAR_MIPMAP_LINEAR);
      GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, mipmappingValue);
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("Tried to load texture " + fileName + ".png , didn't work");
      System.exit(-1);
    }
    textures.add(texture.getTextureID());
    return texture.getTextureID();
  }

  /**
   * @param fileName
   *          the texture file
   * @return the texture id
   */
  public int loadFontTexture(String fileName) {
    Texture texture = null;
    try {
      texture = TextureLoader.getTexture("PNG",
          new FileInputStream("src/resources/" + fileName + ".png"));
      // GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
      // GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
      // GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
      // GL11.glTexParameterf(GL11.GL_TEXTURE_2D,
      // GL14.GL_TEXTURE_LOD_BIAS, 0f);
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("Tried to load texture " + fileName + ".png , didn't work");
      System.exit(-1);
    }
    textures.add(texture.getTextureID());
    return texture.getTextureID();
  }

  /**
   * clean the vaos, vbos and textures
   */
  public void cleanUp() {
    for (int vao : vaos) {
      GL30.glDeleteVertexArrays(vao);
    }
    System.out.println(vbos.size());
    for (int vbo : vbos) {
      GL15.glDeleteBuffers(vbo);
    }
    for (int texture : textures) {
      GL11.glDeleteTextures(texture);
    }
  }

  /**
   * @param textureFiles
   *          the texture files
   * @return a cube map id
   */
  public int loadCubeMap(String[] textureFiles) {
    int texID = GL11.glGenTextures();
    GL13.glActiveTexture(GL13.GL_TEXTURE0);
    GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);

    for (int i = 0; i < textureFiles.length; i++) {
      TextureData data = decodeTextureFile("src/resources/skybox/" + textureFiles[i] + ".png");
      GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, data.getWidth(),
          data.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
    }

    GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
    GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
    GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
    GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
    textures.add(texID);
    return texID;
  }

  /**
   * @param fileName
   *          the texture file
   * @return the textureData object
   */
  private TextureData decodeTextureFile(String fileName) {
    int width = 0;
    int height = 0;
    ByteBuffer buffer = null;
    try {
      FileInputStream in = new FileInputStream(fileName);
      PNGDecoder decoder = new PNGDecoder(in);
      width = decoder.getWidth();
      height = decoder.getHeight();
      buffer = ByteBuffer.allocateDirect(4 * width * height);
      decoder.decode(buffer, width * 4, Format.RGBA);
      buffer.flip();
      in.close();
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("Tried to load texture " + fileName + ", didn't work");
      System.exit(-1);
    }
    return new TextureData(buffer, width, height);
  }

  /**
   * @return the vao id
   */
  private int createVAO() {
    int vaoID = GL30.glGenVertexArrays();
    vaos.add(vaoID);
    GL30.glBindVertexArray(vaoID);
    return vaoID;
  }

  /**
   * stores data in a vbo
   * 
   * @param attributeNumber
   *          the number of attributes
   * @param coordinateSize
   *          the size of the coordinates
   * @param data
   *          the data to be stored
   */
  private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
    int vboID = GL15.glGenBuffers();
    vbos.add(vboID);
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
    FloatBuffer buffer = storeDataInFloatBuffer(data);
    GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
  }

  /**
   * unbinds a vao
   */
  private void unbindVAO() {
    GL30.glBindVertexArray(0);
  }

  /**
   * @param indices
   *          the array of indices
   */
  private void bindIndicesBuffer(int[] indices) {
    int vboID = GL15.glGenBuffers();
    vbos.add(vboID);
    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
    IntBuffer buffer = storeDataInIntBuffer(indices);
    GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
  }

  /**
   * @param data
   *          the data to be stored
   * @return the int buffer
   */
  private IntBuffer storeDataInIntBuffer(int[] data) {
    IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
    buffer.put(data);
    buffer.flip();
    return buffer;
  }

  /**
   * @param data
   *          the data to be stored
   * @return the float buffer
   */
  private FloatBuffer storeDataInFloatBuffer(float[] data) {
    FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
    buffer.put(data);
    buffer.flip();
    return buffer;
  }

  public List<Integer> getVaos() {
    return vaos;
  }

  public List<Integer> getVbos() {
    return vbos;
  }

  public List<Integer> getTextures() {
    return textures;
  }

}
