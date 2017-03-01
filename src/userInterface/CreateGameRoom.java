package userInterface;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import serverComms.ClientTable;
import serverComms.GameRoom;

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
     //   this.setPadding(new Insets(30, 10, 10, 10));
		
		VBox box6 = new VBox(10);
		GridPane.setRowSpan(box6, 3);
		
		TextStyle username = new TextStyle ("CHOOSE A USERNAME", 20);
		Text usernameText = username.getTextStyled();
		
		TextStyle name = new TextStyle ("CHOOSE A GAME NAME", 20);
		Text nameText = name.getTextStyled();
		
		TextStyle seed = new TextStyle("CHOOSE A SEED", 20);
		Text seedText = seed.getTextStyled();
		
		TextStyle noAIs = new TextStyle("CHOOSE THE NUMBER OF AI'S", 20);
		Text noAIsText = noAIs.getTextStyled();
		
		TextStyle noLaps = new TextStyle("CHOOSE THE NUMBER OF LAPS", 20);
		Text noLapsText = noLaps.getTextStyled();
		
		TextField usernameInput = new TextField();
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
		
		MenuButton createGameRoom = new MenuButton("START GAME");
		
		createGameRoom.setOnMouseClicked(event -> {
			
			//create a game room
			GameRoom gameRoom = new GameRoom(0, usernameInput.getText(), Integer.valueOf(seedInput.getText()), (Integer.valueOf(noAIsInput.getText()) + 1) , "", new ClientTable());
			gameRoom.startGame(usernameInput.getText());
			//((Node) event.getSource()).getScene().getWindow().hide();
			
		});
		
		add(usernameText,0,1);
		add(usernameInput,1,1);
		
		add(nameText,0,2);
		add(nameInput,1,2);
		
		add(seedText,0,3);
		add(seedInput,1,3);
		
		add(noAIsText, 0, 4);
		add(noAIsInput,1, 4);
		
		add(noLapsText, 0, 5);
		add(noLapsInput,1, 5);
		
		add(box6, 0, 6);
		
		add(generateTrack, 1, 6);
		add(createGameRoom, 1, 7);
		
		GridPane.setMargin(usernameText, new Insets(0,0,10,0));
		GridPane.setMargin(nameText, new Insets(0,0,10,0));
		GridPane.setMargin(seedText, new Insets(0,0,10,0));
		GridPane.setMargin(noAIsText, new Insets(0,0,10,0));
		GridPane.setMargin(noLapsText, new Insets(0,0,10,0));
		GridPane.setMargin(generateTrack, new Insets(0,0,10,0));
		GridPane.setMargin(createGameRoom, new Insets(0,0,10,0));
		
	}

}
