package userInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import clientComms.Client;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

/**
 * 
 * @author Andreea Gheorghe
 *
 */
public class ServerLobby extends VBox{
	
	
	public ServerLobby(){
		
		this.setAlignment(Pos.CENTER);
		
		VBox box1 = new VBox(10);
		box1.setPadding(new Insets(0, 0, 8, 0));
		
		VBox box2 = new VBox(10);
		box2.setPadding(new Insets(0, 0, 8, 0));
		
		VBox box3 = new VBox(10);
		box3.setPadding(new Insets(0, 0, 8, 0));
		
		VBox box4 = new VBox(10);
		box4.setPadding(new Insets(0, 0, 8, 0));
		
		TextStyle username = new TextStyle("USERNAME", 20);
		Text usernameText = username.getTextStyled();
		
		TextField usernameInput = new TextField();
		
		TextStyle port = new TextStyle("PORT", 20);
		Text portText = port.getTextStyled();
		
		TextField portInput = new TextField();
		
		TextStyle machine = new TextStyle("MACHINE NAME", 20);
		Text machineText = machine.getTextStyled();
		
		TextField machineInput = new TextField();
		
		MenuButton connect = new MenuButton("CONNECT TO THE LOBBY");
		connect.setOnMouseClicked(event->{
			
			String usr = usernameInput.getText();
			int portNo = Integer.valueOf(portInput.getText());
			String machineName = machineInput.getText();
			
			Client client = new Client(usr, portNo, machineName);
			client.start();
			
			//getChildren().removeAll(box1, box2, box3, box4);
			
		});
		
		box1.setAlignment(Pos.CENTER);
		box1.getChildren().addAll(usernameText, usernameInput);
		
		box2.setAlignment(Pos.CENTER);
		box2.getChildren().addAll(portText, portInput);
		
		box3.setAlignment(Pos.CENTER);
		box3.getChildren().addAll(machineText, machineInput);
		
		box4.setAlignment(Pos.CENTER);
		box4.getChildren().add(connect);
		
		getChildren().addAll(box1, box2, box3, box4);
	}
	
}
