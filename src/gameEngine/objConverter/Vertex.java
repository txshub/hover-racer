package gameEngine.objConverter;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

/**
 * @author rtm592 The vertex object
 */
public class Vertex {

	private static final int NO_INDEX = -1;

	private Vector3f position;
	private int textureIndex = NO_INDEX;
	private int normalIndex = NO_INDEX;
	private Vertex duplicateVertex = null;
	private int index;
	private float length;
	private List<Vector3f> tangents = new ArrayList<Vector3f>();
	private Vector3f averagedTangent = new Vector3f(0, 0, 0);

	/**
	 * @param index
	 *            the vertexes index
	 * @param position
	 *            the position of the vertex
	 */
	public Vertex(int index, Vector3f position) {
		this.index = index;
		this.position = position;
		this.length = position.length();
	}

	/**
	 * @param tangent
	 *            the tangent to be added
	 */
	public void addTangent(Vector3f tangent) {
		tangents.add(tangent);
	}

	/**
	 * calculates the average of the tangents
	 */
	public void averageTangents() {
		if (tangents.isEmpty()) {
			return;
		}
		for (Vector3f tangent : tangents) {
			Vector3f.add(averagedTangent, tangent, averagedTangent);
		}
		averagedTangent.normalise();
	}

	/**
	 * @return the average tangent
	 */
	public Vector3f getAverageTangent() {
		return averagedTangent;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @return the vertexes length
	 */
	public float getLength() {
		return length;
	}

	/**
	 * @return whether the texture and normals have been set
	 */
	public boolean isSet() {
		return textureIndex != NO_INDEX && normalIndex != NO_INDEX;
	}

	/**
	 * @param textureIndexOther
	 *            the new index to check
	 * @param normalIndexOther
	 *            the new index to check
	 * @return whether the texture index and normal index match
	 */
	public boolean hasSameTextureAndNormal(int textureIndexOther, int normalIndexOther) {
		return textureIndexOther == textureIndex && normalIndexOther == normalIndex;
	}

	/**
	 * @param textureIndex
	 *            the new texture index
	 */
	public void setTextureIndex(int textureIndex) {
		this.textureIndex = textureIndex;
	}

	/**
	 * @param normalIndex
	 *            the new normal index
	 */
	public void setNormalIndex(int normalIndex) {
		this.normalIndex = normalIndex;
	}

	/**
	 * @return the position
	 */
	public Vector3f getPosition() {
		return position;
	}

	/**
	 * @return the texture index
	 */
	public int getTextureIndex() {
		return textureIndex;
	}

	/**
	 * @return the normal index
	 */
	public int getNormalIndex() {
		return normalIndex;
	}

	/**
	 * @return gets the duplicate vertex
	 */
	public Vertex getDuplicateVertex() {
		return duplicateVertex;
	}

	/**
	 * @param duplicateVertex
	 *            the new duplicate vertex
	 */
	public void setDuplicateVertex(Vertex duplicateVertex) {
		this.duplicateVertex = duplicateVertex;
	}

}
