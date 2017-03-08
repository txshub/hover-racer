package userInterface;

import java.io.IOException;

import clientComms.Client;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import serverComms.GameRoom;

/**
 * 
 * @author Andreea Gheorghe
 *
 */

public class HostGameRoom extends GridPane {
	
	private Client client;
	private int gameRoomSeed;
	private int maxPlayers;
	private int lapNo;
	private String gameRoomName;
	private GameRoom gameRoom;
	private GameRoomLobby gameRoomLobby;
	
	public HostGameRoom(){
		
		this.setAlignment(Pos.CENTER);
        this.setHgap(30);
        this.setVgap(3);
        this.setPadding(new Insets(20, 10, 0, 10));
        
        VBox box6 = new VBox(10);
		GridPane.setRowSpan(box6, 2);
		box6.setAlignment(Pos.CENTER);
		
		TextStyle name = new TextStyle ("CHOOSE A GAME NAME", 25);
		Text nameText = name.getTextStyled();
		
		TextStyle seed = new TextStyle("CHOOSE A SEED", 25);
		Text seedText = seed.getTextStyled();
		
		TextStyle noPlayers = new TextStyle("CHOOSE THE NUMBER OF PLAYERS", 25);
		Text noPlayersText = noPlayers.getTextStyled();
		
		TextStyle noLaps = new TextStyle("CHOOSE THE NUMBER OF LAPS", 25);
		Text noLapsText = noLaps.getTextStyled();
		
		TextField nameInput = new TextField();
		TextField seedInput = new TextField();
		TextField noPlayersInput = new TextField();
		TextField noLapsInput = new TextField();
		
			
		MenuButton generateTrack = new MenuButton("PREVIEW THIS TRACK");
		
		generateTrack.setOnMouseClicked(event -> {
		
			if (box6.getChildren().size() > 0) {
			
					box6.getChildren().remove(0);
			}
			
			Map track = new Map(Integer.valueOf(seedInput.getText()));
			box6.getChildren().add(track);
			
		});
		
		
		MenuButton hostGameRoom = new MenuButton("CREATE THE GAME ROOM");
		
		hostGameRoom.setOnMouseClicked(event -> {
			
			//create a game room
			gameRoomSeed = Integer.valueOf(seedInput.getText());
			maxPlayers =  Integer.valueOf(noPlayersInput.getText());
			lapNo = Integer.valueOf(noLapsInput.getText());
			gameRoomName = nameInput.getText();
			
			try {
				
				gameRoom = client.createGame(gameRoomSeed, maxPlayers, lapNo, gameRoomName);
//				gameRoomLobby = new GameRoomLobby(gameRoom);
				
			//	getChildren().add(gameRoomLobby);
				
				getChildren().removeAll(nameInput, nameText, seedText, seedInput, noPlayersText, noPlayersInput, noLapsText,
						noLapsInput, generateTrack, hostGameRoom, box6 );
				
			} catch (Exception e) {
				
				e.printStackTrace();
			}
				
			
		});
		
		
		add(nameText,0,1);
		add(nameInput,0,2);
		
		add(seedText,0,3);
		add(seedInput,0,4);
		
		add(noPlayersText, 1, 1);
		add(noPlayersInput,1, 2);
		
		add(noLapsText, 1, 3);
		add(noLapsInput,1, 4);
		
		add(box6, 0, 5);
		
		add(generateTrack, 0, 8);
		add(hostGameRoom, 1, 8);
		
		GridPane.setMargin(nameInput, new Insets(0,0,20,0));
		GridPane.setMargin(seedInput, new Insets(0,0,20,0));
		GridPane.setMargin(noPlayersInput, new Insets(0,0,20,0));
		GridPane.setMargin(generateTrack, new Insets(0,0,20,0));
		GridPane.setMargin(hostGameRoom, new Insets(0,0,20,0));
		
	}
	
	public void setClient (Client client){
		
		this.client = client;
		
	}
	
}
