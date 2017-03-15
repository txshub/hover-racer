package userInterface;

import java.io.IOException;
import java.util.ArrayList;

import clientComms.Client;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
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
	private MenuButton refresh;
	private VBox gameRoomData;
	private int chosenGRid;

	public JoinGameRoom() {

		setHgap(30);
		setVgap(5);

		refresh = new MenuButton("REFRESH LIST", 320, 60, 30);
		add(refresh, 1, 7);

		gameRoomData = new VBox(5);
		gameRoomData.setAlignment(Pos.CENTER);
		
		refresh.setOnMouseClicked(eventRefresh -> {
			
			refresh();
			
		});

	}

	public void refresh() {

		getChildren().clear();
		add(refresh, 1, 7);

		int column = 0;
		int row = 1;

		try {
			setGameList(client.requestAllGames());
		} catch (IOException e1) {
			System.err.println("Didn't receive game room list");
		}

		for (int i = 0; i < gameRoomList.size(); i++) {

			GameRoom gameRoom = gameRoomList.get(i);
			String roomName = gameRoom.getName();
			MenuButton btnRoom = new MenuButton(roomName, 350, 70, 30);
			this.add(btnRoom, column, row);
			row++;

			btnRoom.setOnMouseClicked(event -> {

				gameRoomData.getChildren().clear();

				String joinedText = "CURRENTLY " + gameRoom.getPlayers().size() + " OUT OF " + gameRoom.getNoPlayers() + " PLAYERS JOINED";
				TextStyle joinedPlayers = new TextStyle(joinedText, 25);
				Text joinedPlayersStyled = joinedPlayers.getTextStyled();

				String seed = gameRoom.getSeed();

				Map track = new Map(seed);

				MenuButton selectGR = new MenuButton ("SELECT THIS GAME ROOM", 320, 60, 30);
				selectGR.setOnMouseClicked(ev-> {
				
					setChosenGRId(gameRoom.id);
				
				});

				gameRoomData.getChildren().addAll(joinedPlayersStyled, track, selectGR);

				if (!getChildren().contains(gameRoomData)) {

					add(gameRoomData, 1, 0);
				}
				GridPane.setRowSpan(gameRoomData, 6);

			});
		}

	}

	public void setClient(Client client) {

		this.client = client;
	}

	public void setGameList(ArrayList<GameRoom> gameRoomList) {

		this.gameRoomList = gameRoomList;
	}
	
	public void setChosenGRId(int chosenGRid){
		
		this.chosenGRid = chosenGRid;
		
	}
	
	public int getChosenGRid(){
		
		return this.chosenGRid;
		
	}

}
