package gameEngine.models;

/**
 * @author rtm592 An objects raw model
 */
public class RawModel {

  private int vaoID;
  private int vertexCount;

  /**
   * @param vaoID
   *          the models vao id
   * @param vertexCount
   *          the number of vertecies in the model
   */
  public RawModel(int vaoID, int vertexCount) {
    this.vaoID = vaoID;
    this.vertexCount = vertexCount;
  }

  /**
   * @return the vao id
   */
  public int getVaoID() {
    return vaoID;
  }

  /**
   * @return the number of vertecies in the model
   */
  public int getVertexCount() {
    return vertexCount;
  }

}
