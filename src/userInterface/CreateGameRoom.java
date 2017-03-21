package userInterface;

import java.io.IOException;
import java.util.Random;

import clientComms.Client;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import physics.placeholders.DataGenerator;
import serverComms.Lobby;

/**
 * 
 * @author Andreea Gheorghe Class that implements the design and functionality
 *         of the single player mode window.
 *
 */

public class CreateGameRoom extends GridPane {

	private String gameRoomSeed;
	private int maxAIs;
	private int lapNo;
	private String username;
	private TextField usernameInput;
	private TextField seedInput;
	private TextField noAIsInput;
	private TextField noLapsInput;

	/**
	 * Constructor for the CreateGameRoom class.
	 */
	public CreateGameRoom() {

		this.setAlignment(Pos.CENTER);
		this.setHgap(40);
		this.setVgap(3);
		this.setPadding(new Insets(20, 10, 0, 30));

		VBox mapBox = new VBox(10);
		GridPane.setRowSpan(mapBox, 2);
		mapBox.setAlignment(Pos.CENTER);

		TextStyle username = new TextStyle("CHOOSE A USERNAME", 25);
		Text usernameText = username.getTextStyled();

		TextStyle seed = new TextStyle("CHOOSE A SEED", 25);
		Text seedText = seed.getTextStyled();

		TextStyle noAIs = new TextStyle("CHOOSE THE NUMBER OF AI'S", 25);
		Text noAIsText = noAIs.getTextStyled();

		TextStyle noLaps = new TextStyle("CHOOSE THE NUMBER OF LAPS", 25);
		Text noLapsText = noLaps.getTextStyled();

		usernameInput = new TextField();
		seedInput = new TextField();
		noAIsInput = new TextField();
		noLapsInput = new TextField();

		MenuButton generateTrack = new MenuButton("PREVIEW THIS TRACK", 350, 70, 30);
		generateTrack.setOnMouseClicked(event -> {

			if (mapBox.getChildren().size() > 0) {

				mapBox.getChildren().remove(0);
			}

			setSeed();
			Map track = new Map(getSeed());
			mapBox.getChildren().add(track);

		});

		MenuButton createGameRoom = new MenuButton("START GAME", 350, 70, 30);
		createGameRoom.setOnMouseClicked(event -> {

			try {
				setSettings();

				// CREATE LOCAL SERVER //
				Lobby localLobby = new Lobby(4445);

				// CREATE SINGLE PLAYER CLIENT //
				Client localClient = new Client(getUsername(), 4445, "localhost");
				localClient.start();

				localClient.startSinglePlayerGame(getSeed(), getMaxAIs(), getNoLaps(),
						DataGenerator.basicShipSetup(getUsername()));

			} catch (InvalidPlayerNumberException ex) {

				try {
					PopUpWindow.display("CHOOSE A NUMBER BETWEEN 1 AND 8");
				} catch (Exception e) {
					System.err.println("POP UP NOT WORKING");
				}
			} catch (NullPointerException exp) {

				try {
					PopUpWindow.display("NULL INPUT");
				} catch (Exception e) {
					System.err.println("POP UP NOT WORKING");
				}

			} catch (IOException e) {

				System.err.println("SINGLE PLAYER MODE DOES NOT WORK");
			}

		});

		// GRID LAYOUT //

		add(usernameText, 0, 1);
		add(usernameInput, 0, 2);

		add(seedText, 0, 3);
		add(seedInput, 0, 4);

		add(noAIsText, 1, 1);
		add(noAIsInput, 1, 2);

		add(noLapsText, 1, 3);
		add(noLapsInput, 1, 4);

		add(mapBox, 0, 5);

		add(generateTrack, 0, 7);
		add(createGameRoom, 1, 7);

		GridPane.setMargin(usernameInput, new Insets(0, 0, 20, 0));
		GridPane.setMargin(seedInput, new Insets(0, 0, 20, 0));
		GridPane.setMargin(noAIsInput, new Insets(0, 0, 20, 0));
		GridPane.setMargin(generateTrack, new Insets(0, 0, 20, 0));
		GridPane.setMargin(createGameRoom, new Insets(0, 0, 20, 0));

	}

	/**
	 * Sets the seed that is used to preview the track.
	 */
	public void setSeed() {

		if(seedInput.getText().isEmpty()) {
			this.gameRoomSeed = String.valueOf((new Random()).nextLong());
		} else {
			this.gameRoomSeed = seedInput.getText();
		}
	}

	/**
	 * Sets the seed, maximum number of AIs, number of laps and username by
	 * taking the input of the user.
	 * 
	 * @throws InvalidPlayerNumberException
	 */
	public void setSettings() throws InvalidPlayerNumberException {

		if (usernameInput.getText().isEmpty() || noAIsInput.getText().isEmpty() || noLapsInput.getText().isEmpty()) {
			throw new NullPointerException();
		}
		
		if(!seedInput.getText().isEmpty()) {
			this.gameRoomSeed = seedInput.getText();
		} else if(gameRoomSeed==null) {
			this.gameRoomSeed = String.valueOf((new Random()).nextLong());
		}
		this.maxAIs = Integer.valueOf(noAIsInput.getText());
		this.lapNo = Integer.valueOf(noLapsInput.getText());
		this.username = usernameInput.getText();

		if (this.maxAIs < 1 || this.maxAIs > 8) {
			throw new InvalidPlayerNumberException();
		}

	}

	/**
	 * Get method for the game room seed.
	 * 
	 * @return The game room seed.
	 */
	public String getSeed() {
		return this.gameRoomSeed;
	}

	/**
	 * Get method for the maximum number of AI players.
	 * 
	 * @return The maximum number of AI players.
	 */
	public int getMaxAIs() {
		return this.maxAIs;
	}

	/**
	 * Get method for the username.
	 * 
	 * @return The chosen username.
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * Get method for the number of laps.
	 * 
	 * @return The number of laps.
	 */
	public int getNoLaps() {
		return this.lapNo;
	}

}