package userInterface;

import java.io.IOException;
import java.util.ArrayList;

import clientComms.Client;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import physics.placeholders.DataGenerator;
import serverComms.GameRoom;

/**
 * 
 * @author Andreea Gheorghe
 *
 */

public class JoinGameRoom extends GridPane {

	private ArrayList<GameRoom> gameRoomList;
	private Client client;
	private GameRoomLobby gameRoomLobby; 
	private GameRoom gameRoomChosen;
	
	
	public JoinGameRoom(Client client) throws IOException {
		
		this.client = client;
		this.gameRoomList = client.requestAllGames(); //this gets the actual list of game rooms
		
		int column = 0;
		int row = 1;
		
		VBox playerNames = new VBox(10);
		
		for(int i=0; i<gameRoomList.size(); i++){
			
			GameRoom gameRoom = gameRoomList.get(i); 
			MenuButton btnRoom = new MenuButton(gameRoom.getName(), 350, 70, 30);
			this.add(btnRoom, column, row);
			row++;
			
			btnRoom.setOnMouseClicked(event->{
				
				ArrayList<String> players = gameRoom.getPlayers();
				for(int k=0; k< players.size(); k++){
					
					TextStyle player = new TextStyle (players.get(k), 10);
					Text playerText = player.getTextStyled();
					
					playerNames.getChildren().add(playerText);
				}
				
				
				MenuButton joinGR = new MenuButton("JOIN THIS GAME ROOM", 200, 50, 20);
				joinGR.setOnMouseClicked(eventjoin-> {
						
				try {
					gameRoomChosen = client.joinGame(gameRoom.id, DataGenerator.basicShipSetup(GameMenu.usr));
					gameRoomChosen.addPlayer(GameMenu.usr);
					
					 gameRoomLobby = new GameRoomLobby(gameRoomChosen);
				     gameRoomLobby.setClient(client);
				     
				     getChildren().removeAll(playerNames, joinGR);
				     
				} catch (IOException e) {
				
					System.err.println("JOIN DIDN'T WORK");
				}
					
				});
				
				add(playerNames,1,1);
				GridPane.setRowSpan(playerNames, REMAINING);
				add(joinGR, 1, 6);
				
			});
		}
		
	}
	
	public void setClient(Client client){
		
		this.client =client ;
	}
	
}
