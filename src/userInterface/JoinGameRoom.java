package userInterface;

import java.io.IOException;
import java.util.ArrayList;

import clientComms.Client;
import javafx.scene.layout.GridPane;
import serverComms.GameNameNumber;

/**
 * 
 * @author Andreea Gheorghe
 *
 */

public class JoinGameRoom extends GridPane {

	private ArrayList<GameNameNumber> gameRoomList;
	private Client client;
	
	public JoinGameRoom(Client client) throws IOException {
	
		this.client = client;
		this.gameRoomList = client.requestAllGames(); //this gets the actual list of game rooms
		
		//Display the array of game rooms
		for(int i=0; i< gameRoomList.size(); i++){
			
		//	gameRoomList.get(i).
			
		}
		
		MenuButton refresh = new MenuButton("REFRESH");
		refresh.setOnMouseClicked(event -> {
			
			this.gameRoomList = client.requestAllGames();
			
		});
		
		
		
	}
	
	
}
