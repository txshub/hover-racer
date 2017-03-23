package userInterface;

import java.io.IOException;
import java.util.ArrayList;

import clientComms.Client;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import serverComms.GameRoom;

/**
 * 
 * @author Andreea Gheorghe Class that implements the design and functionality
 *         of joining a game room, option that is provided in the multiplayer
 *         mode options.
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

  /**
   * Constructor for the JoinGameRoom class.
   */
  public JoinGameRoom() {

    setHgap(30);
    setVgap(5);

    // No game room has been selected yet
    chosenGRid = -1;

    refresh = new MenuButton("REFRESH LIST", 320, 60, 30);
    add(refresh, 1, 7);

    gameRoomData = new VBox(5);
    gameRoomData.setAlignment(Pos.CENTER);

    refresh.setOnMouseClicked(eventRefresh -> {

      refresh();

    });

    this.setCache(true);
    this.setCacheHint(CacheHint.SPEED);

  }

  /**
   * Method that refreshes the window, to display updated information of the
   * game room and the number of connected players.
   */
  public void refresh() {

    getChildren().clear();
    add(refresh, 1, 7);

    int column = 0;
    int row = 1;

    // REQUEST UPDATED GAME ROOM LIST //
    try {
      setGameList(client.requestAllGames());
    } catch (IOException e1) {
      System.err.println("Didn't receive game room list");
    }

    // DISPLAY OPTIONS //
    for (int i = 0; i < gameRoomList.size(); i++) {

      GameRoom gameRoom = gameRoomList.get(i);
      String roomName = gameRoom.getName();
      MenuButton btnRoom = new MenuButton(roomName, 350, 70, 30);
      this.add(btnRoom, column, row);
      row++;

      btnRoom.setOnMouseClicked(event -> {

        gameRoomData.getChildren().clear();

        String joinedText = "CURRENTLY " + gameRoom.getPlayers().size() + " OUT OF "
            + gameRoom.getNoPlayers() + " PLAYERS JOINED";
        TextStyle joinedPlayers = new TextStyle(joinedText, 25);
        Text joinedPlayersStyled = joinedPlayers.getTextStyled();

        String seed = gameRoom.getSeed();

        Map track = new Map(seed);

        MenuButton selectGR = new MenuButton("SELECT THIS GAME ROOM", 320, 60, 30);
        selectGR.setOnMouseClicked(ev -> {

          setChosenGRId(gameRoom.id);
          selectGR.setClicked(true);

        });

        gameRoomData.getChildren().addAll(joinedPlayersStyled, track, selectGR);

        if (!getChildren().contains(gameRoomData)) {

          add(gameRoomData, 1, 0);
        }
        GridPane.setRowSpan(gameRoomData, 6);

      });
    }

  }

  /**
   * Sets the client to be the current connected client that needs to be
   * accessed in this class.
   * 
   * @param client
   *          The connected client.
   */
  public void setClient(Client client) {

    this.client = client;
  }

  /**
   * Sets the game room list to be an updated game room list, received from the
   * server.
   * 
   * @param gameRoomList
   *          The updated game room list.
   */
  public void setGameList(ArrayList<GameRoom> gameRoomList) {

    this.gameRoomList = gameRoomList;
  }

  /**
   * Sets the game room id after the user has selected a certain game room from
   * the available game room list.
   * 
   * @param chosenGRid
   *          The chosen game room id.
   */
  public void setChosenGRId(int chosenGRid) {

    this.chosenGRid = chosenGRid;

  }

  /**
   * Get method for the chosen game room id.
   * 
   * @return The chosen game room id.
   */
  public int getChosenGRid() {

    return this.chosenGRid;

  }

}
