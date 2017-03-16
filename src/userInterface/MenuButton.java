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
 * Class that implements the customised design of a menu button,
 * which will be used throughout the User Interface.
 *
 */
public class MenuButton extends StackPane {

  private Text buttonText;
  private Rectangle bg;

  // Tudor - audio source
  private Source audioSource;

  /**
   * Constructor for the MenuButton class that creates a menu button,
   * according to the given style settings.
   * @param name 		The text that will be displayed on the button.
   * @param width 		The width of the button.
   * @param height 		The height of the button.
   * @param fontSize	The chosen font size.
   */
  public MenuButton(String name, int width, int height, int fontSize) {

    // Tudor - initialize audio source
    audioSource = AudioMaster.createSFXSource();

    TextStyle button = new TextStyle(name, fontSize);
    Text buttonText = button.getTextStyled();
    
    // Create button shape
    bg = new Rectangle(width, height);
    bg.setOpacity(0.8);
    bg.setFill(Color.BLACK);

    // Blur the button colour
    GaussianBlur blur = new GaussianBlur(3.6);
    bg.setEffect(blur);

    buttonText.setFill(Color.WHITE);

    // Customise text position
    StackPane.setAlignment(buttonText, Pos.CENTER);

    // Add button to stack with text over the background
    getChildren().addAll(bg, buttonText);

    // Hover over button
    this.setOnMouseEntered(event -> {
    	
    	bg.setTranslateX(6);
    	buttonText.setTranslateX(6);
    	bg.setFill(Color.DIMGRAY);
    	buttonText.setFill(Color.WHITE);

    	// Tudor - play sound
    	audioSource.play(Sounds.BUTTON_HOVER);
      
    });

    // Stop hovering over button
    this.setOnMouseExited(event -> {
    	
    	bg.setTranslateX(0);
    	buttonText.setTranslateX(0);
    	bg.setFill(Color.BLACK);
    	buttonText.setFill(Color.WHITE);
    	
    });

    // Create glow effect to let user know they have clicked a button
    DropShadow effect = new DropShadow(50, Color.WHITE);
    effect.setInput(new Glow());

    // Clicked on button
    this.setOnMousePressed(event -> {
      
    	setEffect(effect);
    	// Tudor - play sound
    	audioSource.play(Sounds.BUTTON_CLICK);
    	
    });

    // Release button and remove effect
    this.setOnMouseReleased(event -> {
    	setEffect(null);
    });

  }

}
