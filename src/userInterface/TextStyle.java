package userInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * 
 * @author Andreea Gheorghe Class that implements the text style that will be
 *         used throughout the User Interface.
 *
 */
public class TextStyle {

  private Text text;

  /**
   * Constructor for the TextStyle class.
   * 
   * @param textInput
   *          The text that must be displayed.
   * @param fontSize
   *          The chosen font size.
   */
  public TextStyle(String textInput, int fontSize) {

    // Convert the input so that it can be rendered
    String textInputCapitals = textInput.toUpperCase();
    text = new Text(textInputCapitals);

    try {
      Font f = Font.loadFont(new FileInputStream(new File("src/resources/fonts/War is Over.ttf")),
          fontSize);
      text.setFont(f);
      text.setFill(Color.WHITE);

    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  /**
   * Get method for the styled text that can be easily displayed.
   * 
   * @return The styled text.
   */
  public Text getTextStyled() {
    return this.text;
  }

}
