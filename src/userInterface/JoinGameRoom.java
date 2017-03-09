package userInterface;

import java.io.IOException;
import java.util.ArrayList;

import clientComms.Client;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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
	private GameRoom gameRoom;
	
	
	public JoinGameRoom() {
		
		MenuButton refresh = new MenuButton("REFRESH", 350, 70, 30);
		refresh.setOnMouseClicked(event -> {
			
			try {
				for(int i = 0; i < this.getChildren().size(); i++) {
					
					if(this.getChildren().get(i) != refresh) {
						
						this.getChildren().remove(i);
					}
				}
				this.refresh();
			} catch (IOException e) {
	
				e.printStackTrace();
			}
			
		});
		
		add(refresh, 1, 7);
		
	}
	
	public void refresh() throws IOException {
		
		this.gameRoomList = client.requestAllGames(); //this gets the actual list of game rooms
		
		int column = 0;
		int row = 1;
		
		VBox playerNames = new VBox(10);
		
		for(int i=0; i<6; i++){
			
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
				
				
				MenuButton joinGR = new MenuButton("JOIN THIS GAME ROOM", 350, 70, 30);
				joinGR.setOnMouseClicked(eventjoin-> {
				
				//what's the id?
				//ship data?	
				//gameRoom = client.joinGame(id, basicShipData);
				//gameRoom.add(player);
					
				});
				
				add(playerNames,1,1);
				GridPane.setRowSpan(playerNames, REMAINING);
				add(joinGR, 1, 6);
				
			});
		}
		
	}
	
	public void setClient(Client client){
		
		this.client = client;
	}

}
