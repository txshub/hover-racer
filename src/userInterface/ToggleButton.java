package userInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import audioEngine.AudioMaster;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

/**
 * 
 * @author Andreea Gheorghe
 *
 */
public class ToggleButton extends HBox {
	
	
	private Label label;
	private Button button;
	private boolean switchedOn;
	
	/**
	 * Constructor for the ToggleButton class.
	 */
	public ToggleButton(){
		label = new Label();
		button = new Button();
		switchedOn = false;
	}
	
	/**
	 * Sets the initial state of the toggle button.
	 */
	private void initialState() {
		
		label.setText("OFF");
		
		try {
			Font f = Font.loadFont(new FileInputStream(new File("res/fonts/War is Over.ttf")), 20);
			label.setFont(f);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		getChildren().addAll(label, button);
		
		button.setOnAction(event -> {
			this.setSwitchedOn(!isSwitchedOn());
		});
		
		label.setOnMouseClicked(event -> {
			this.setSwitchedOn(!isSwitchedOn());
		});
		
		setButtonStyle();
	}
	
	/**
	 * Sets the style of the toggle button.
	 */
	public void setButtonStyle(){
		
		setWidth(200);
		label.setAlignment(Pos.CENTER);
		setStyle("-fx-background-color: white; -fx-text-fill:black; -fx-background-radius: 4;");
		setAlignment(Pos.CENTER_LEFT);
		
		label.prefWidthProperty().bind(widthProperty().divide(2));
		label.prefHeightProperty().bind(heightProperty());
		button.prefWidthProperty().bind(widthProperty().divide(2));
		button.prefHeightProperty().bind(heightProperty());
	}
	
	/**
	 * Method that implements the ON/OFF toggle of the button.
	 */
	public void toggle(){
		
		initialState();
		this.setOnMouseClicked(event -> {
			if (this.switchedOn) {
                		label.setText("ON");
                		setStyle("-fx-background-color: #3aea3a;");
                		label.toFront();
                		
                		// Tudor
                		AudioMaster.setMusicVolume(1f);
            		}
            		else {
            			label.setText("OFF");
            			setStyle("-fx-background-color: white;");
                		button.toFront();
                		
                		// Tudor
                		AudioMaster.setMusicVolume(0f);
            		}
		});
	}
	
	/**
	 * Get method for the switchedOn property.
	 * @return True if the button is switched on, false otherwise.
	 */
	public boolean isSwitchedOn() {
		return this.switchedOn;
	}
	
	/**
	 * Set method for the switchedOn property.
	 * @param state The current state of the toggle button.
	 */
	public void setSwitchedOn (boolean state){
		this.switchedOn = state;
	}

}
