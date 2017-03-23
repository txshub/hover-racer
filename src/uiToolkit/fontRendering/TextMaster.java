package uiToolkit.fontRendering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gameEngine.renderEngine.Loader;
import uiToolkit.fontMeshCreator.FontType;
import uiToolkit.fontMeshCreator.GUIText;
import uiToolkit.fontMeshCreator.TextMeshData;

public class TextMaster {

  private static Loader loader;
  private static HashMap<FontType, List<GUIText>> texts = new HashMap<>();
  private static FontRenderer renderer;

  public static void init(Loader _loader) {
    loader = _loader;
    renderer = new FontRenderer();
  }

  public static void render() {
    renderer.render(texts);
  }

  /**
   * Loads a text into the textBatch for that font.
   * 
   * @param text
   */
  public static void loadText(GUIText text) {
    FontType font = text.getFont();
    TextMeshData data = font.loadText(text);
    int vao = loader.loadToVAO(data.getVertexPositions(), data.getTextureCoords()).getVaoID();
    text.setMeshInfo(vao, data.getVertexCount());
    List<GUIText> textBatch = texts.get(font);
    if (textBatch == null) {
      textBatch = new ArrayList<>();
      texts.put(font, textBatch);
    }
    textBatch.add(text);
  }

  /**
   * Removes a text from the textBatch for that font. If the textBatch is now empty, remove it as
   * well.
   * 
   * @param text
   */
  public static void removeText(GUIText text) {
    List<GUIText> textBatch = texts.get(text.getFont());
    textBatch.remove(text);

    // Remove the text batch if the font is not currently being used anywhere
    // else
    if (textBatch.isEmpty()) {
      texts.remove(text.getFont());
    }
  }

  public static void cleanUp() {
    renderer.cleanUp();
  }

}
