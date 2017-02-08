package userInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class MenuButton extends StackPane {
	
	private Text buttonText;
	
	/**
	 * Constructor for the MenuButton class that creates a menu button
	 * with a certain style, and with the given name as the label. 
	 * @param name The name of the button.
	 */
	public MenuButton (String name) {
		buttonText = new Text(name);
		
		
		try {
			Font f = Font.loadFont(new FileInputStream(new File("res/fonts/War is Over.ttf")), 20);
			buttonText.setFont(f);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//create button
		Rectangle bg = new Rectangle (200,40);
		bg.setOpacity(0.7);
		bg.setFill(Color.BLACK);
		//blur the button colour
		GaussianBlur blur = new GaussianBlur(3.6);
		bg.setEffect(blur);
		
		buttonText.setFill(Color.WHITE);
		
		//customise position
		this.setAlignment(Pos.CENTER);
		//setRotate(-0.8);
		
		//add button to stack with text over the background
		getChildren().addAll(bg, buttonText);
	
		
		//hover over button
		this.setOnMouseEntered(event -> {
			bg.setTranslateX(15);
			buttonText.setTranslateX(15);
			bg.setFill(Color.DIMGRAY);
			buttonText.setFill(Color.WHITE);
		});
		
		//stop hovering over button
		this.setOnMouseExited(event -> {
			bg.setTranslateX(0);
			buttonText.setTranslateX(0);
			bg.setFill(Color.BLACK);
			buttonText.setFill(Color.WHITE);
		});
		
		//create glow effect to let user know they have clicked a button
		DropShadow effect = new DropShadow (50, Color.WHITE);
		effect.setInput(new Glow());
		
		//clicked on button
		this.setOnMousePressed(event -> {
			setEffect(effect);
		});
		
		//released button
		this.setOnMouseReleased(event -> {
			setEffect(null);
		});
		
	}
	
}

