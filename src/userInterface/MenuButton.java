package userInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import audioEngine.AudioMaster;
import audioEngine.Sounds;
import audioEngine.Source;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * 
 * @author Andreea Gheorghe
 *
 */
public class MenuButton extends StackPane {

  private Text buttonText;
  private Rectangle bg;

  // Tudor - audio source
  private Source audioSource;

  /**
   * Constructor for the MenuButton class that creates a menu button with a
   * certain style, and with the given name as the label.
   * 
   * @param name
   *          The name of the button.
   */
  public MenuButton(String name, int width, int height, int fontSize) {

    // Tudor - initialize source
    audioSource = AudioMaster.createSFXSource();

    TextStyle button = new TextStyle(name, fontSize);
    Text buttonText = button.getTextStyled();
    
    // create button
    bg = new Rectangle(width, height);
    bg.setOpacity(0.7);
    bg.setFill(Color.BLACK);

    // blur the button colour
    GaussianBlur blur = new GaussianBlur(3.6);
    bg.setEffect(blur);

    buttonText.setFill(Color.WHITE);

    // customise position
    StackPane.setAlignment(buttonText, Pos.CENTER);
    // setRotate(-0.8);

    // add button to stack with text over the background
    getChildren().addAll(bg, buttonText);

    // hover over button
    this.setOnMouseEntered(event -> {
      bg.setTranslateX(6);
      buttonText.setTranslateX(6);
      bg.setFill(Color.DIMGRAY);
      buttonText.setFill(Color.WHITE);

      // Tudor - play sound
      audioSource.play(Sounds.BUTTON_HOVER);

    });

    // stop hovering over button
    this.setOnMouseExited(event -> {
      bg.setTranslateX(0);
      buttonText.setTranslateX(0);
      bg.setFill(Color.BLACK);
      buttonText.setFill(Color.WHITE);
    });

    // create glow effect to let user know they have clicked a button
    DropShadow effect = new DropShadow(50, Color.WHITE);
    effect.setInput(new Glow());

    // clicked on button
    this.setOnMousePressed(event -> {
      setEffect(effect);
      // Tudor - play sound
      audioSource.play(Sounds.BUTTON_CLICK);
    });

    // released button
    this.setOnMouseReleased(event -> {
      setEffect(null);
    });

  }

 
}
