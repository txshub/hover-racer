package userInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * 
 * @author Andreea Gheorghe
 *
 */
public class TextStyle {

  private Text text;

  public TextStyle(String textInput, int fontSize) {

    text = new Text(textInput);

    try {
      Font f = Font.loadFont(new FileInputStream(new File("src/resources/fonts/War is Over.ttf")),
          fontSize);
      text.setFont(f);
      text.setFill(Color.WHITE);

    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  public Text getTextStyled() {
    return this.text;
  }
}
