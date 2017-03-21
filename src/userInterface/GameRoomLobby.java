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
 * @author Andreea Gheorghe Class that implements the design and functionality
 *         of the game room waiting lobby, where the user will await the start
 *         of the game, which is decided by the host of the game room.
 *
 */

public class GameRoomLobby extends GridPane {

	private GameRoom gameRoom;
	private Client client;
	private int maxPlayers;
	private ArrayList<String> playerNames;
	private VBox playerNamesBox;
	private MenuButton refresh;
	private MenuButton startGame;

	/**
	 * Constructor for the GameRoomLobby class.
	 * 
	 * @param gameRoom
	 *            The current gameRoom.
	 */
	public GameRoomLobby(GameRoom gameRoom) {

		this.setHgap(30);
		this.setVgap(15);

		this.gameRoom = gameRoom;
		maxPlayers = gameRoom.getNoPlayers();

		playerNamesBox = new VBox(10);

		refresh = new MenuButton("REFRESH", 350, 70, 30);
		refresh.setOnMouseClicked(e -> {

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
			} catch (IOException e) {
				System.err.println("GAME WASN'T STARTED");
			}

		});

		// GRID LAYOUT //
		add(refresh, 1, 2);
		add(startGame, 1, 3);

	}

	/**
	 * Method that refreshes the game room lobby, by receiving an updated list
	 * of connected players from the server.
	 */
	public void refresh() {

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

			TextStyle player = new TextStyle(playerNames.get(i) + "                  ", 30);
			Text playerText = player.getTextStyled();

			playerNamesBox.getChildren().add(playerText);
			k++;

		}

		for (int i = k; i < maxPlayers; i++) {

			TextStyle placeholder = new TextStyle("........................................", 30);
			Text placeholderText = placeholder.getTextStyled();

			playerNamesBox.getChildren().add(placeholderText);
		}

		if (!getChildren().contains(playerNamesBox)) {

			add(playerNamesBox, 0, 1);
		}
		GridPane.setRowSpan(playerNamesBox, 6);

	}

	/**
	 * Sets the client to be the current connected client that needs to be
	 * accessed in this class.
	 * 
	 * @param client
	 *            The connected client.
	 */
	public void setClient(Client client) {

		this.client = client;
	}

	/**
	 * Get method for the current game room.
	 * 
	 * @return The current game room.
	 */
	public GameRoom getGameRoom() {

		return this.gameRoom;
	}

}
