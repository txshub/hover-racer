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

	public JoinGameRoom() {

		setHgap(30);
		setVgap(10);

		refresh = new MenuButton("REFRESH LIST", 350, 70, 30);
		add(refresh, 1, 7);

		gameRoomData = new VBox(10);
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

				String joinedText = "CURRENTLY " + gameRoom.getPlayers().size() + " OUT OF " + gameRoom.getNoPlayers();
				TextStyle joinedPlayers = new TextStyle(joinedText, 25);
				Text joinedPlayersStyled = joinedPlayers.getTextStyled();

				String seed = gameRoom.getSeed();

				Map track = new Map(seed);

				MenuButton joinGR = new MenuButton("JOIN THIS GAME ROOM", 350, 70, 30);
				joinGR.setOnMouseClicked(eventjoin -> {

					try {
						gameRoomChosen = client.joinGame(gameRoom.id, DataGenerator.basicShipSetup(GameMenu.usr));
						gameRoomChosen.addPlayer(GameMenu.usr);

						gameRoomLobby = new GameRoomLobby(gameRoomChosen);
						gameRoomLobby.setClient(client);
						
						getChildren().clear();
						
						getChildren().add(gameRoomLobby);

					       TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25), this);
					       trans.setToX(this.getTranslateX() - 600);

					       TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25), gameRoomLobby);
					       trans1.setToX(gameRoomLobby.getTranslateX() - 600);

					       trans.play();
					       trans1.play();
					       trans.setOnFinished(evt -> {
					         getChildren().remove(this);
					       });

					} catch (IOException e) {

						System.err.println("JOIN DIDN'T WORK");
					}

				});

				gameRoomData.getChildren().addAll(joinedPlayersStyled, track, joinGR);

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

}
