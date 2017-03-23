package gameEngine.objConverter;

/**
 * @author rtm592 A class to hold data about a model
 */
public class ModelData {

  private float[] vertices;
  private float[] textureCoords;
  private float[] normals;
  private float[] tangents;
  private int[] indices;
  private float furthestPoint;

  /**
   * @param vertices
   *          the verticies of the model
   * @param textureCoords
   *          the texture coords of the verticies
   * @param normals
   *          the vertex normals
   * @param tangents
   *          the tangents of each vertex
   * @param indices
   *          the models indicies
   * @param furthestPoint
   *          the models furthest point
   */
  public ModelData(float[] vertices, float[] textureCoords, float[] normals, float[] tangents,
      int[] indices, float furthestPoint) {
    this.vertices = vertices;
    this.textureCoords = textureCoords;
    this.normals = normals;
    this.indices = indices;
    this.furthestPoint = furthestPoint;
    this.tangents = tangents;
  }

  /**
   * @return the verticies of the model
   */
  public float[] getVertices() {
    return vertices;
  }

  /**
   * @return the texture coords of the verticies
   */
  public float[] getTextureCoords() {
    return textureCoords;
  }

  /**
   * @return the tangents of each vertex
   */
  public float[] getTangents() {
    return tangents;
  }

  /**
   * @return the vertex normals
   */
  public float[] getNormals() {
    return normals;
  }

  /**
   * @return the models indicies
   */
  public int[] getIndices() {
    return indices;
  }

  /**
   * @return the models furthest point
   */
  public float getFurthestPoint() {
    return furthestPoint;
  }

}
