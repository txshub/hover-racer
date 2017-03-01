package userInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import audioEngine.AudioMaster;
import javafx.geometry.Insets;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * 
 * @author Andreea Gheorghe
 *
 */
public class MusicSlider extends HBox {

  private Slider musicSlider;
  private Text value;

  public MusicSlider() {

    musicSlider = new Slider(0, 10, 5);

    value = new Text(Integer.toString((int) musicSlider.getValue()));

    try {
      Font f = Font.loadFont(new FileInputStream(new File("res/fonts/War is Over.ttf")), 30);
      value.setFont(f);
      value.setFill(Color.WHITE);

    } catch (IOException e) {
      e.printStackTrace();
    }

    musicSlider.setBlockIncrement(1);
    musicSlider.setPrefWidth(250);

    musicSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
      int i = newValue.intValue();
      value.setText(Integer.toString(i));

      // TUDOR ADD AUDIO STUFF
      AudioMaster.setMusicVolume((float) (i / 10.0));
    }

    );

    getChildren().addAll(value, musicSlider);
    setWidth(300);
    setPadding(new Insets(0, 5, 0, 0));
  }

}
