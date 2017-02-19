package userInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javafx.animation.TranslateTransition;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class ServerLobby extends HBox{
	
	
	public ServerLobby(){
		
		
		Text input = new Text("Please input your username");
		try {
			Font f = Font.loadFont(new FileInputStream(new File("res/fonts/War is Over.ttf")), 20);
			input.setFont(f);
			input.setFill(Color.WHITE);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		input.setTextAlignment(TextAlignment.CENTER);
		
		TextField userInput = new TextField();
//		userInput.setLayoutX(100);
//		userInput.setLayoutY(160);
//		
		
		//Rectangle bg2 = new Rectangle(550,412);
		//bg2.setFill(Color.GREY);
		//bg2.setOpacity(0.4);
		
		getChildren().addAll(input, userInput);
	}
	
}
