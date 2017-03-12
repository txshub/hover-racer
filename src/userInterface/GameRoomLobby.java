package userInterface;

import java.io.IOException;
import java.util.ArrayList;

import clientComms.Client;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import serverComms.GameRoom;

public class GameRoomLobby extends GridPane {

  private GameRoom gameRoom;
  private Client client;

  public GameRoomLobby(GameRoom gameRoom) {
	
	this.setHgap(30);
	this.setVgap(15);
	
	this.setTranslateX(700);

    this.gameRoom = gameRoom;
    
    int maxPlayers = gameRoom.getNoPlayers();
    int k = 0;

    ArrayList<String> playerNames = gameRoom.getPlayers();

    VBox playerNamesBox = new VBox(10);

    for (int i = 0; i < playerNames.size(); i++) {

      TextStyle player = new TextStyle(playerNames.get(i), 25);
      Text playerText = player.getTextStyled();

      playerNamesBox.getChildren().add(playerText);
      k++;
    }
    
    for (int i=k; i<= maxPlayers; i++){
    	
    	TextStyle placeholder = new TextStyle("....................................", 25);
    	Text placeholderText = placeholder.getTextStyled();
    	
    	playerNamesBox.getChildren().add(placeholderText);
    }

    MenuButton startGame = new MenuButton("START GAME", 350, 70, 30);

    System.out.println(GameMenu.usr);
    System.out.println(gameRoom.getHostName());
    
    if (!GameMenu.usr.equals(gameRoom.getHostName())) {

      startGame.setVisible(false);

    }

    startGame.setOnMouseClicked(event -> {

    	try {
    		
			client.startGame();
			
		} catch (IOException e) {
			
			System.err.println("GAME WASN'T STARTED");
			
		}
      
    });

    MenuButton leaveRoom = new MenuButton("EXIT GAME ROOM", 350, 70, 30);
    leaveRoom.setVisible(false);
    
    if (!GameMenu.usr.equals(gameRoom.getHostName())) {

      leaveRoom.setVisible(true);
    }

    leaveRoom.setOnMouseClicked(event -> {

      // transition to joinGameRoom

    });

    // implement timer and timer task to refresh automatically

    add(playerNamesBox, 0, 1);
    GridPane.setRowSpan(playerNamesBox, REMAINING);

    add(startGame, 2, 3);
    add(leaveRoom, 2, 4);
  }
  
  public void setClient(Client client){
	  
	  this.client = client;
  }

}
