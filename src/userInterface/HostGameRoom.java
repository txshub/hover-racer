package userInterface;

import java.util.Random;

import clientComms.Client;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 * 
 * @author Andreea Gheorghe Class that implements the design and functionality of hosting a game
 *         room, option that is provided in the multiplayer mode options.
 *
 */

public class HostGameRoom extends GridPane {

  private Client client;
  private String gameRoomSeed;
  private int maxPlayers;
  private int lapNo;
  private String gameRoomName;
  private TextField seedInput;
  private TextField noPlayersInput;
  private TextField noLapsInput;
  private TextField nameInput;

  /**
   * Constructor for the HostGameRoom class.
   */
  public HostGameRoom() {

    this.setAlignment(Pos.CENTER);
    this.setVgap(5);
    this.setPadding(new Insets(20, 10, 0, 10));

    TextStyle name = new TextStyle("CHOOSE THE GAME NAME", 25);
    Text nameText = name.getTextStyled();

    TextStyle seed = new TextStyle("CHOOSE THE TRACK SEED", 25);
    Text seedText = seed.getTextStyled();

    TextStyle noPlayers = new TextStyle("CHOOSE NUMBER OF PLAYERS", 25);
    Text noPlayersText = noPlayers.getTextStyled();

    TextStyle noLaps = new TextStyle("CHOOSE NUMBER OF LAPS", 25);
    Text noLapsText = noLaps.getTextStyled();

    nameInput = new TextField();
    nameInput.setPrefSize(300, 20);

    seedInput = new TextField();
    seedInput.setPrefSize(300, 20);

    noPlayersInput = new TextField();
    noPlayersInput.setPrefSize(300, 20);

    noLapsInput = new TextField();
    noLapsInput.setPrefSize(300, 20);

    // GRID LAYOUT //

    add(nameText, 0, 1);
    add(nameInput, 0, 2);

    add(seedText, 0, 3);
    add(seedInput, 0, 4);

    add(noPlayersText, 0, 5);
    add(noPlayersInput, 0, 6);

    add(noLapsText, 0, 7);
    add(noLapsInput, 0, 8);

    GridPane.setMargin(nameInput, new Insets(0, 0, 20, 0));
    GridPane.setMargin(seedInput, new Insets(0, 0, 20, 0));
    GridPane.setMargin(noPlayersInput, new Insets(0, 0, 20, 0));

    this.setCache(true);
    this.setCacheHint(CacheHint.SPEED);

  }

  /**
   * Sets the client to be the current connected client that needs to be accessed in this class.
   * 
   * @param client
   *          The connected client.
   */
  public void setClient(Client client) {

    this.client = client;

  }

  /**
   * Sets the seed, maximum number of players, number of laps and game room name by taking the input
   * of the user.
   */

  public void setSettings() throws InvalidPlayerNumberException {

    if (nameInput.getText().isEmpty() || noPlayersInput.getText().isEmpty()
        || noLapsInput.getText().isEmpty()) {
      throw new NullPointerException();
    }

    if (!seedInput.getText().isEmpty()) {
      this.gameRoomSeed = seedInput.getText();
    } else if (gameRoomSeed == null) {
      this.gameRoomSeed = String.valueOf((new Random()).nextLong());
    }
    this.maxPlayers = Integer.valueOf(noPlayersInput.getText());
    this.lapNo = Integer.valueOf(noLapsInput.getText());
    this.gameRoomName = nameInput.getText();

    if (this.maxPlayers < 1 || this.maxPlayers > 8) {
      throw new InvalidPlayerNumberException();
    }

  }

  /**
   * Sets the seed that is used to preview the track.
   */
  public void setSeed() {
    if (seedInput.getText().isEmpty()) {
      this.gameRoomSeed = String.valueOf((new Random()).nextLong());
    } else {
      this.gameRoomSeed = seedInput.getText();
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
   * Get method for the maximum number of players.
   * 
   * @return The maximum number of players.
   */
  public int getMaxPlayers() {
    return this.maxPlayers;
  }

  /**
   * Get method for the number of laps.
   * 
   * @return The number of laps.
   */
  public int getNoLaps() {
    return this.lapNo;
  }

  /**
   * Get method for the game room name.
   * 
   * @return The game room name.
   */
  public String getName() {
    return this.gameRoomName;
  }

}
