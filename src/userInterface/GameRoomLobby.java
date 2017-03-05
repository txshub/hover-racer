package userInterface;

import java.util.ArrayList;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import serverComms.GameRoom;

public class GameRoomLobby extends GridPane{
	
	private GameRoom gameRoom;
	
	public GameRoomLobby(GameRoom gameRoom){
		
		this.gameRoom = gameRoom;
		
		ArrayList<String> playerNames = gameRoom.getPlayers();
		
		VBox playerNamesBox = new VBox (10);
		
		for(int i=0; i< playerNames.size(); i++){
			
			TextStyle player = new TextStyle (playerNames.get(i), 10);
			Text playerText = player.getTextStyled();
			
			playerNamesBox.getChildren().add(playerText);
		}
		
		MenuButton startGame = new MenuButton("START GAME");
		
		if(GameMenu.usr != gameRoom.getHostName()){
			
			startGame.setVisible(false);
			
		}
		
		startGame.setOnMouseClicked(event ->{
			
			gameRoom.startGame(GameMenu.usr);
		});
		
		MenuButton leaveRoom = new MenuButton("EXIT THIS GAME ROOM");
		if(GameMenu.usr != gameRoom.getHostName()){
			
			leaveRoom.setVisible(false);
		}
		
		leaveRoom.setOnMouseClicked(event ->{
			
			//transition to joinGameRoom
			
		});
		
		//implement timer and timer task to refresh automatically
		
		add(playerNamesBox, 0, 1);
		GridPane.setRowSpan(playerNamesBox, REMAINING);
		
		add(startGame,1,3);
		add(leaveRoom,1,4);
	}
	
	

}
