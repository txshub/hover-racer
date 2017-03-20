package userInterface;

import audioEngine.Sounds;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * 
 * @author Andreea Gheorghe Class that defines the style of a button used for
 *         the ship customisation window.
 *
 */

public class CustomisationButton extends StackPane {

	public CustomisationButton(String description, int width, int height, int fontSize, ImageView image) {

		TextStyle descriptionText = new TextStyle(description, fontSize);
		Text descriptionStyled = descriptionText.getTextStyled();

		Rectangle background = new Rectangle(width, height);
		background.setOpacity(0.8);
		background.setFill(Color.BLACK);

		GaussianBlur blur = new GaussianBlur(3.6);
		background.setEffect(blur);

		VBox box = new VBox(10);
		box.getChildren().addAll(image, descriptionStyled);
		box.setAlignment(Pos.CENTER);

		this.setOnMouseEntered(event -> {

			background.setTranslateX(6);
			box.setTranslateX(6);
			background.setFill(Color.DIMGRAY);
		});
		
		this.setOnMouseExited(event -> {

			background.setTranslateX(0);
			box.setTranslateX(0);
			background.setFill(Color.BLACK);

		});

		// Create glow effect to let user know they have clicked a button
		DropShadow effect = new DropShadow(50, Color.WHITE);
		effect.setInput(new Glow());

		// Clicked on button
		this.setOnMousePressed(event -> {
			setEffect(effect);
		});

		// Release button and remove effect
		this.setOnMouseReleased(event -> {
			setEffect(null);
		});

		getChildren().addAll(background, box);

	}
}
