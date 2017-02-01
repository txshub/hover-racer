package userInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

public class ToggleButton extends HBox {
	
	
	private Label label = new Label();
	private Button button = new Button();
	
	private boolean switchedOn = false;
	
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
	
	public void toggle(){
		
		initialState();
		this.setOnMouseClicked(event -> {
			if (this.switchedOn) {
                		label.setText("ON");
                		setStyle("-fx-background-color: #3aea3a;");
                		label.toFront();
            		}
            		else {
            			label.setText("OFF");
            			setStyle("-fx-background-color: white;");
                		button.toFront();
            		}
		});
	}
	
	
	public boolean isSwitchedOn() {
		return this.switchedOn;
	}
	
	public void setSwitchedOn (boolean state){
		this.switchedOn = state;
	}

}
