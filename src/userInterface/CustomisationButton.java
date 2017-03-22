package userInterface;

import audioEngine.AudioMaster;
import audioEngine.Sounds;
import audioEngine.Source;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * 
 * @author Andreea Gheorghe Class that defines the style of a button used for
 *         the ship customisation window.
 *
 */

public class CustomisationButton extends StackPane {

	Rectangle background;
	boolean clicked;
	Source audioSource;

	/**
	 * Constructor for the CustomisationButton class.
	 * 
	 * @param shipType
	 *            The type of the ship from the three available models.
	 * @param width
	 *            The width of the button.
	 * @param height
	 *            The height of the button.
	 * @param fontSize
	 *            The font size of the description text.
	 * @param image
	 *            The ship image.
	 */
	public CustomisationButton(int shipType, int width, int height, int fontSize, ImageView image) {

		TextStyle descriptionText;
		Text descriptionStyled = null;
		
		audioSource = AudioMaster.createSFXSource();

		if (shipType == 1) {

			String description = "GOOD ACCELERATION \n POWERFUL BRAKES";
			descriptionText = new TextStyle(description, fontSize);
			descriptionStyled = descriptionText.getTextStyled();

		} else if (shipType == 2) {

			String description = "MAXIMUM SPEED \n HARD TO CONTROL";
			descriptionText = new TextStyle(description, fontSize);
			descriptionStyled = descriptionText.getTextStyled();

		} else if (shipType == 3) {

			String description = "LARGEST ACCELERATION \n NO BRAKES";
			descriptionText = new TextStyle(description, fontSize);
			descriptionStyled = descriptionText.getTextStyled();
		}

		background = new Rectangle(width, height);
		background.setOpacity(0.8);
		setSelectedEffect(Color.BLACK);

		GaussianBlur blur = new GaussianBlur(3.6);
		background.setEffect(blur);

		VBox box = new VBox(10);
		descriptionStyled.setTextAlignment(TextAlignment.CENTER);
		box.getChildren().addAll(image, descriptionStyled);
		box.setAlignment(Pos.CENTER);

		this.setOnMouseEntered(event -> {

			background.setTranslateX(6);
			box.setTranslateX(6);
			if (!this.clicked) {
				setSelectedEffect(Color.DIMGRAY);
			}
		});

		this.setOnMouseExited(event -> {

			if (!this.clicked) {
				background.setTranslateX(0);
				box.setTranslateX(0);
				setSelectedEffect(Color.BLACK);
			} else {
				background.setTranslateX(0);
				box.setTranslateX(0);
			}
			
			audioSource.play(Sounds.BUTTON_HOVER);
		});

		// Create glow effect to let user know they have clicked a button
		DropShadow effect = new DropShadow(50, Color.WHITE);
		effect.setInput(new Glow());

		// Clicked on button
		this.setOnMousePressed(event -> {
			setEffect(effect);
			audioSource.play(Sounds.BUTTON_CLICK);
		});

		// Release button and remove effect
		this.setOnMouseReleased(event -> {
			setEffect(null);
		});

		getChildren().addAll(background, box);

		this.setCache(true);
		this.setCacheHint(CacheHint.SPEED);
	}

	/**
	 * Sets the colour of the button.
	 * @param colour The chosen colour.
	 */
	public void setSelectedEffect(Color colour) {
		background.setFill(colour);
	}

	/**
	 * Sets the clicked status of the button.
	 * @param clicked The clicked status.
	 */
	public void setClicked(boolean clicked) {
		this.clicked = clicked;
	}

}
