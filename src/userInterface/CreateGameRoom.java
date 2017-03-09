package userInterface;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * 
 * @author Andreea Gheorghe
 *
 */

public class CreateGameRoom extends GridPane {

  public CreateGameRoom() {

    this.setAlignment(Pos.CENTER);
    this.setHgap(30);
    this.setVgap(3);
    this.setPadding(new Insets(20, 10, 0, 10));

    VBox box6 = new VBox(10);
    GridPane.setRowSpan(box6, 2);
    box6.setAlignment(Pos.CENTER);

    TextStyle username = new TextStyle("CHOOSE A USERNAME", 25);
    Text usernameText = username.getTextStyled();

    TextStyle name = new TextStyle("CHOOSE A GAME NAME", 25);
    Text nameText = name.getTextStyled();

    TextStyle seed = new TextStyle("CHOOSE A SEED", 25);
    Text seedText = seed.getTextStyled();

    TextStyle noAIs = new TextStyle("CHOOSE THE NUMBER OF AI'S", 25);
    Text noAIsText = noAIs.getTextStyled();

    TextStyle noLaps = new TextStyle("CHOOSE THE NUMBER OF LAPS", 25);
    Text noLapsText = noLaps.getTextStyled();

    TextField usernameInput = new TextField();
    TextField nameInput = new TextField();
    TextField seedInput = new TextField();
    TextField noAIsInput = new TextField();
    TextField noLapsInput = new TextField();

    MenuButton generateTrack = new MenuButton("PREVIEW THIS TRACK", 350, 70, 30);

    generateTrack.setOnMouseClicked(event -> {

      if (box6.getChildren().size() > 0) {

        box6.getChildren().remove(0);
      }

      Map track = new Map(Integer.valueOf(seedInput.getText()));
      box6.getChildren().add(track);

    });

    MenuButton createGameRoom = new MenuButton("START GAME", 350, 70, 30);

    createGameRoom.setOnMouseClicked(event -> {

      // create a game room
      // Simon?? - single player startGame method
      // GameRoom gameRoom = new GameRoom(0, usernameInput.getText(),
      // Integer.valueOf(seedInput.getText()),
      // (Integer.valueOf(noAIsInput.getText()) + 1) , "", new ClientTable());
      // gameRoom.startGame(usernameInput.getText());
      // ((Node) event.getSource()).getScene().getWindow().hide();

    });

    add(usernameText, 0, 1);
    add(usernameInput, 0, 2);

    add(nameText, 1, 1);
    add(nameInput, 1, 2);

    add(seedText, 0, 3);
    add(seedInput, 0, 4);

    add(noAIsText, 1, 3);
    add(noAIsInput, 1, 4);

    add(noLapsText, 1, 5);
    add(noLapsInput, 1, 6);

    add(box6, 0, 6);

    add(generateTrack, 0, 8);
    add(createGameRoom, 1, 8);

    GridPane.setMargin(usernameInput, new Insets(0, 0, 20, 0));
    GridPane.setMargin(nameInput, new Insets(0, 0, 20, 0));
    GridPane.setMargin(seedInput, new Insets(0, 0, 20, 0));
    GridPane.setMargin(noAIsInput, new Insets(0, 0, 20, 0));
    GridPane.setMargin(generateTrack, new Insets(0, 0, 20, 0));
    GridPane.setMargin(createGameRoom, new Insets(0, 0, 20, 0));

  }

}
