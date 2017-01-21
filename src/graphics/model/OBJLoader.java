package graphics.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class OBJLoader {

  public static Model loadModel(String fileName) {
    BufferedReader reader = null;
    
    ArrayList<Vector3f> vertices = new ArrayList<>();
    ArrayList<Vector2f> textures = new ArrayList<>();
    ArrayList<Vector3f> normals = new ArrayList<>();
    ArrayList<Face> faces = new ArrayList<>();
    
    // Read the OBJ file
    try {
      reader = new BufferedReader(new FileReader("./res/models/" + fileName));
      
      String line;
      while ((line = reader.readLine()) != null) {
        String[] tokens = line.split(" ");
        switch (tokens[0]) {
          case "v":
            // Vertex
            Vector3f vecV = new Vector3f(
                Float.parseFloat(tokens[1]),
                Float.parseFloat(tokens[2]),
                Float.parseFloat(tokens[3]));
            vertices.add(vecV);
            break;
          case "vt":
            // Texture coordinate
            Vector2f vecTC = new Vector2f(
                Float.parseFloat(tokens[1]),
                Float.parseFloat(tokens[2]));
            textures.add(vecTC);
            break;
          case "vn":
            // Vertex normal
            Vector3f vecN = new Vector3f(
                Float.parseFloat(tokens[1]),
                Float.parseFloat(tokens[2]),
                Float.parseFloat(tokens[3]));
            normals.add(vecN);
            break;
          case "f":
            Face face = new Face(tokens[1], tokens[2], tokens[3]);
            faces.add(face);
            break;
          default:
            // Ignore anything not above
            break;
        }
      }
    } catch (IOException e) {
      System.err.println("Error loading model: " + e.getMessage());
    }
    
    return reorderLists(vertices, textures, normals, faces);
  }
  
  /**
   * Runs through the face list and aligns lists of vertex indices, texture 
   * coordinate indices and normal indices. An indices list is created which
   * points to the correct "column" in these lists.
   * 
   * @param vertList
   * @param texList
   * @param normList
   * @param faceList
   * @return a model with the ordered lists
   */
  private static Model reorderLists(ArrayList<Vector3f> vertList, ArrayList<Vector2f> texList, 
      ArrayList<Vector3f> normList, ArrayList<Face> faceList) {
    
    // The list of indices to fill
    ArrayList<Integer> indices = new ArrayList<>();
    
    // Copy the vertex list in the order that it has been declared
    float[] vertArr = new float[vertList.size() * 3];
    int i = 0; // Offset 
    for (Vector3f vertex : vertList) {
      vertArr[i * 3] = vertex.x;
      vertArr[i * 3 + 1] = vertex.y;
      vertArr[i * 3 + 2] = vertex.z;
      i++;
    }
    
    float[] textCoordArr = new float[vertList.size() * 2];
    float[] normArr = new float[vertList.size() * 3];
    
    for (Face face : faceList) {
      int[] faceIndices = face.getFaceIndices();
      for (int j = 0; j < faceIndices.length; j += 3) {
        
        // Set index for vertex coordinates
        int vertIndex = faceIndices[j];
        indices.add(vertIndex);
        
        // Reorder texture coordinates
        if (faceIndices[j + 1] >= 0) {
          Vector2f textCoord = texList.get(faceIndices[j + 1]);
          textCoordArr[vertIndex * 2] = textCoord.x;
          textCoordArr[vertIndex * 2 + 1] = 1 - textCoord.y; // Is a UV so 1 - 
        }
        
        // Reorder normals
        if (faceIndices[j + 2] >= 0) {
          Vector3f vecNorm = normList.get(faceIndices[j + 2]);
          normArr[vertIndex * 3] = vecNorm.x;
          normArr[vertIndex * 3 + 1] = vecNorm.y;
          normArr[vertIndex * 3 + 2] = vecNorm.z;
        }
        
      }
    }
    
    // Convert the indices list to an array
    int[] indicesArr = new int[indices.size()];
    indicesArr = indices.stream().mapToInt((Integer v) -> v).toArray();
    
    return new Model(vertArr, indicesArr, normArr, textCoordArr);
  }
  
  private static class Face {
    
    private int[] indices;
    public static final int NO_VALUE = -1;
    
    public Face(String v1, String v2, String v3) {
      indices = new int[9];
      
      // Parse the lines
      parseLine(v1, 0);
      parseLine(v2, 3);
      parseLine(v3, 6);
    }
    
    private void parseLine(String line, int offset) {
      String[] tokens = line.split("/");
      
      // NOTE: OBJ indices start at 1, hence -1 so they start at 0
      // First index is the vertex to use (always present)
      indices[0 + offset] = Integer.parseInt(tokens[0]) - 1;
      
      if (tokens.length > 1) {
        // Second index is the texture coordinate (if empty make "NO_VALUE")
        indices[1 + offset] = tokens[1].length() > 0 ? Integer.parseInt(tokens[1]) - 1 : NO_VALUE;
        
        if (tokens.length > 2) {
          // Final index is the normal to use
          indices[2 + offset] = Integer.parseInt(tokens[2]) - 1;
        }
      }
    }
    
    public int[] getFaceIndices() {
      return indices;
    }
  }
}
