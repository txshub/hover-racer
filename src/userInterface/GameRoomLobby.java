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

  public GameRoomLobby(GameRoom gameRoom) {
	
	this.setHgap(30);
	this.setVgap(15);
	
	this.setTranslateX(700);
	this.setTranslateY(80);

    this.gameRoom = gameRoom;
    
    maxPlayers = gameRoom.getNoPlayers();
    
    int k = 0;

    playerNames = gameRoom.getPlayers();
    
    VBox playerNamesBox = new VBox(10);

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
    
    MenuButton refresh = new MenuButton("REFRESH", 350, 70, 30);
    //TO DO
    

    MenuButton startGame = new MenuButton("START GAME", 350, 70, 30);
    
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

    MenuButton leaveRoom = new MenuButton("EXIT GAME ROOM", 350, 70, 30);
    leaveRoom.setVisible(false);
    
    if (!GameMenu.usr.equals(gameRoom.getHostName())) {

      leaveRoom.setVisible(true);
    }

    leaveRoom.setOnMouseClicked(event -> {

      JoinGameRoom joinGameRoom = new JoinGameRoom();
      joinGameRoom.setClient(client);
      
      getChildren().add(joinGameRoom);

      TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25), this);
      trans.setToX(this.getTranslateX() - 600);

      TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25), joinGameRoom);
      trans1.setToX(joinGameRoom.getTranslateX() - 600);

      trans.play();
      trans1.play();
      trans.setOnFinished(evt -> {
        getChildren().remove(this);
      });

    });


    add(playerNamesBox, 0, 1);
    GridPane.setRowSpan(playerNamesBox, REMAINING);

    add(refresh,1, 2);
    add(startGame, 1, 3);
    add(leaveRoom, 1, 4);
  }
  
  public void setClient(Client client){
	  
	  this.client = client;
  }

}
