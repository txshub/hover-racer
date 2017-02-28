package userInterface;

import game.MainGameLoop;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * 
 * @author Andreea Gheorghe
 *
 */

public class CreateGameRoom extends GridPane {
	
	public CreateGameRoom(){
		
		this.setAlignment(Pos.CENTER);
        this.setHgap(10);
        this.setVgap(10);
        this.setPadding(new Insets(30, 10, 10, 10));
		
		VBox box6 = new VBox(10);
		GridPane.setRowSpan(box6, 3);
		
		
		TextStyle name = new TextStyle ("CHOOSE A NAME", 20);
		Text nameText = name.getTextStyled();
		
		TextStyle seed = new TextStyle("CHOOSE A SEED", 20);
		Text seedText = seed.getTextStyled();
		
		TextStyle noAIs = new TextStyle("CHOOSE THE NUMBER OF AI'S", 20);
		Text noAIsText = noAIs.getTextStyled();
		
		TextStyle noLaps = new TextStyle("CHOOSE THE NUMBER OF LAPS", 20);
		Text noLapsText = noLaps.getTextStyled();
		
		TextField nameInput = new TextField();
		TextField seedInput = new TextField();
		TextField noAIsInput = new TextField();
		TextField noLapsInput = new TextField();
			
		
		MenuButton generateTrack = new MenuButton("GENERATE THIS TRACK");
		
		generateTrack.setOnMouseClicked(event -> {
		
			if (box6.getChildren().size() > 0) {
			
					box6.getChildren().remove(0);
			}
			
			Map track = new Map(Integer.valueOf(seedInput.getText()));
			box6.getChildren().add(track);
			
		});
		
		MenuButton hostGameRoom = new MenuButton("START GAME");
		
		hostGameRoom.setOnMouseClicked(event -> {
			
			//create a game room
			MainGameLoop.main(null);
			((Node) event.getSource()).getScene().getWindow().hide();
			
		});
		
		add(nameText,0,1);
		add(nameInput,1,1);
		
		add(seedText,0,2);
		add(seedInput,1,2);
		
		add(noAIsText, 0, 3);
		add(noAIsInput,1, 3);
		
		add(noLapsText, 0, 4);
		add(noLapsInput,1, 4);
		
		add(box6, 0, 5);
		
		add(generateTrack, 1, 5);
		add(hostGameRoom, 1, 6);
		
		GridPane.setMargin(nameText, new Insets(0,0,10,0));
		GridPane.setMargin(seedText, new Insets(0,0,10,0));
		GridPane.setMargin(noAIsText, new Insets(0,0,10,0));
		GridPane.setMargin(noLapsText, new Insets(0,0,10,0));
		GridPane.setMargin(generateTrack, new Insets(0,0,10,0));
		GridPane.setMargin(hostGameRoom, new Insets(0,0,10,0));
		
	}

}
