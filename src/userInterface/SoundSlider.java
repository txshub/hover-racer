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
 * @author Andreea Gheorghe Class that defines the style of the sound effects slider.
 *
 */
public class SoundSlider extends GridPane {

  private Slider soundSlider;
  private Text value;

  /**
   * Constructor for the SoundSlider class.
   */
  public SoundSlider() {

    this.setPadding(new Insets(0, 0, 20, 0));

    TextStyle soundS = new TextStyle("SOUND EFFECTS", 30);
    Text soundSliderText = soundS.getTextStyled();

    GridPane.setColumnSpan(soundSliderText, 2);

    soundSlider = new Slider(0, 10, 5);

    value = new Text(Integer.toString((int) soundSlider.getValue()));

    try {
      Font f = Font.loadFont(new FileInputStream(new File("src/resources/fonts/War is Over.ttf")),
          30);
      value.setFont(f);
      value.setFill(Color.WHITE);

    } catch (IOException e) {
      e.printStackTrace();
    }

    soundSlider.setBlockIncrement(1);
    soundSlider.setPrefWidth(350);

    soundSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
      int i = newValue.intValue();
      value.setText(Integer.toString(i));

      // TUDOR - add audio functionality
      AudioMaster.setSFXVolume((float) (i / 10.0));
    }

    );

    add(soundSliderText, 0, 1);
    add(value, 0, 3);
    add(soundSlider, 1, 3);

    GridPane.setMargin(soundSliderText, new Insets(0, 0, 30, 0));
    GridPane.setMargin(value, new Insets(0, 20, 0, 0));

  }

}
