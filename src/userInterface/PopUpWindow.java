package userInterface;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PopUpWindow {
	
	private static Text popUpText;
	
	public PopUpWindow(String popUp){
		
		TextStyle popUpStyle = new TextStyle(popUp, 25);
		this.popUpText = popUpStyle.getTextStyled();
	}
	
	public static void display() {
		
		Stage popUp=new Stage();
	      
		popUp.initModality(Modality.APPLICATION_MODAL);
		popUp.setTitle("Error!");
		    		     
		MenuButton btnClose = new MenuButton("CLOSE",200,50,25);
		btnClose.setOnMouseClicked(event -> {
		
			popUp.close();
		});
		
		Rectangle background = new Rectangle(500,400);
		background.setFill(Color.BLACK);
		
		GridPane box = new GridPane();
		box.setPadding(new Insets (50,100,50,100));
		box.setVgap(10);
		box.add(popUpText, 0, 1);
		box.setHalignment(popUpText, HPos.CENTER);
		box.add(btnClose, 0, 2);
	
		Pane layout = new Pane();
		layout.getChildren().addAll(background, box); 
		      
		Scene scene1= new Scene(layout, 400, 200);
		      
		popUp.setScene(scene1);
		popUp.setResizable(false);
		popUp.showAndWait();
		       
	}
}
