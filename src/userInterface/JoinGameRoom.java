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
	
	public JoinGameRoom(Client client) {
	
		this.client = client;
		
		this.gameRoomList = client.getAllGames(); //this gets the actual list of game rooms
		
		
	}
	
	
}
