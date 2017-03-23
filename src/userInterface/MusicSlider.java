package userInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import audioEngine.AudioMaster;
import javafx.geometry.Insets;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * 
 * @author Andreea Gheorghe Class that defines the style of the music slider.
 *
 */
public class MusicSlider extends GridPane {

  private Slider musicSlider;
  private Text value;

  /**
   * Constructor for the MusicSlider class.
   */
  public MusicSlider() {

    this.setPadding(new Insets(0, 0, 20, 0));

    TextStyle musicS = new TextStyle("BACKGROUND MUSIC", 30);
    Text musicSliderText = musicS.getTextStyled();

    GridPane.setColumnSpan(musicSliderText, 2);

    musicSlider = new Slider(0, 10, 5);

    value = new Text(Integer.toString((int) musicSlider.getValue()));

    try {
      Font f = Font.loadFont(new FileInputStream(new File("src/resources/fonts/War is Over.ttf")),
          30);
      value.setFont(f);
      value.setFill(Color.WHITE);

    } catch (IOException e) {
      e.printStackTrace();
    }

    musicSlider.setBlockIncrement(1);
    musicSlider.setPrefWidth(350);

    musicSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
      int i = newValue.intValue();
      value.setText(Integer.toString(i));

      // TUDOR - audio functionality
      AudioMaster.setMusicVolume((float) (i / 10.0));
    }

    );

    add(musicSliderText, 0, 1);
    add(value, 0, 3);
    add(musicSlider, 1, 3);

    GridPane.setMargin(musicSliderText, new Insets(0, 0, 30, 0));
    GridPane.setMargin(value, new Insets(0, 20, 0, 0));

  }

}
