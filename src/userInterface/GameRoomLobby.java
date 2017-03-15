package userInterface;

import java.io.IOException;
import java.util.ArrayList;

import audioEngine.AudioMaster;
import clientComms.Client;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import serverComms.GameRoom;

public class GameRoomLobby extends GridPane {

  private GameRoom gameRoom;
  private Client client;
  private int maxPlayers;
  private ArrayList<String> playerNames;
  private VBox playerNamesBox;
  private MenuButton refresh;
  private MenuButton startGame;

  public GameRoomLobby(GameRoom gameRoom) {
	
	this.setHgap(30);
	this.setVgap(15);
	
    this.gameRoom = gameRoom;
    maxPlayers = gameRoom.getNoPlayers();
    
    playerNamesBox = new VBox(10);
    
    refresh = new MenuButton("REFRESH", 350, 70, 30);
    refresh.setOnMouseClicked(e-> {
    	
    	refresh();
    	
    });
    
    // START THE GAME IF THE CLIENT IS THE HOST //
    
    startGame = new MenuButton("START GAME", 350, 70, 30);
    
    if (!GameMenu.usr.equals(gameRoom.getHostName())) {

      startGame.setVisible(false);

    }

    startGame.setOnMouseClicked(event -> {

    	try {
    		
			client.startGame();
			AudioMaster.stopMusic();
		    AudioMaster.cleanUp();
			Platform.exit();
			
		} catch (IOException e) {
			
			System.err.println("GAME WASN'T STARTED");
			
		}
      
    });
    

    add(refresh,1, 2);
    add(startGame, 1, 3);
    
  }
  
  public void refresh(){
	  
	  getChildren().clear();
	  add(refresh, 1, 2);
	  add(startGame, 1, 3);
	  
	  try {
		  
		gameRoom = client.getUpdatedRoom();
		
	} catch (IOException e) {
		
		System.err.println("DID NOT RECEIVE UPDATED GAME ROOM");
	}
	  int k = 0;
	  playerNames = gameRoom.getPlayers();
	  System.out.println(playerNames.size());
	  
	  playerNamesBox.getChildren().clear();
	  
	  for (int i = 0; i < playerNames.size(); i++) {

		  TextStyle player = new TextStyle(playerNames.get(i), 28);
	      Text playerText = player.getTextStyled();

	      playerNamesBox.getChildren().add(playerText);
	      k++;
	      
	  }
	    
	  for (int i=k; i< maxPlayers; i++){
	    	
	    TextStyle placeholder = new TextStyle("........................................", 28);
	    Text placeholderText = placeholder.getTextStyled();
	    	
	    	playerNamesBox.getChildren().add(placeholderText);
	  }

	  if (!getChildren().contains(playerNamesBox)) {

			add(playerNamesBox, 0, 1);
		}
		GridPane.setRowSpan(playerNamesBox, 6);
	  
  }
  
  public void setClient(Client client){
	  
	  this.client = client;
  }
  
  public GameRoom getGameRoom(){
	  
	  return this.gameRoom;
  }

}
