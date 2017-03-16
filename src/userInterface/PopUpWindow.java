package userInterface;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * 
 * @author Andreea Gheorghe
 * Class that implements the pop up windows that will be used 
 * for error handling throughout the User Interface.
 *
 */
public class PopUpWindow {
	
	/**
	 * Displays the pop up window with a given text,
	 * to reflect the type of error and the action that must be taken by the user.
	 * @param popText The error text.
	 * @throws IOException
	 */
	public static void display(String popText) throws IOException {
		
		TextStyle popUpStyle = new TextStyle(popText, 25);
		Text popUpText = popUpStyle.getTextStyled();
		
		Stage popUp=new Stage();
	      
		popUp.initModality(Modality.APPLICATION_MODAL);
		popUp.setTitle("Error!");
		    		     
		MenuButton btnClose = new MenuButton("CLOSE",200,50,25);
		btnClose.setOnMouseClicked(event -> {
		
			popUp.close();
		});
		
		Rectangle background = new Rectangle(500,400);
		background.setOpacity(0.6);
		background.setFill(Color.BLACK);
		
		GridPane box = new GridPane();
		box.setPadding(new Insets (50,100,50,100));
		box.setVgap(20);
		box.add(popUpText, 0, 1);
		GridPane.setHalignment(popUpText, HPos.CENTER);
		box.add(btnClose, 0, 2);
	
		// Style - the same background as the main menu
		InputStream is = Files.newInputStream(Paths.get("src/resources/img/hover-racer.jpg"));
	    Image image = new Image(is);
	    is.close();

	    ImageView imgView = new ImageView(image);
		
		Pane layout = new Pane();
		layout.getChildren().addAll(background, imgView, box); 
		      
		// Create the scene that will be displayed on the stage
		Scene scene1= new Scene(layout, 400, 200);
		      
		popUp.setScene(scene1);
		popUp.setResizable(false);
		popUp.showAndWait();
		       
	}
}
