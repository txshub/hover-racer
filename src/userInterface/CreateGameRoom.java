package userInterface;

import java.io.IOException;

import clientComms.Client;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import physics.placeholders.DataGenerator;
import serverComms.Lobby;

/**
 * 
 * @author Andreea Gheorghe
 *
 */

public class CreateGameRoom extends GridPane {

  public CreateGameRoom() {

    this.setAlignment(Pos.CENTER);
    this.setHgap(40);
    this.setVgap(3);
    this.setPadding(new Insets(20, 10, 0, 30));

    VBox box6 = new VBox(10);
    GridPane.setRowSpan(box6, 2);
    box6.setAlignment(Pos.CENTER);

    TextStyle username = new TextStyle("CHOOSE A USERNAME", 25);
    Text usernameText = username.getTextStyled();

    TextStyle seed = new TextStyle("CHOOSE A SEED", 25);
    Text seedText = seed.getTextStyled();

    TextStyle noAIs = new TextStyle("CHOOSE THE NUMBER OF AI'S", 25);
    Text noAIsText = noAIs.getTextStyled();

    TextStyle noLaps = new TextStyle("CHOOSE THE NUMBER OF LAPS", 25);
    Text noLapsText = noLaps.getTextStyled();

    TextField usernameInput = new TextField();
    TextField seedInput = new TextField();
    TextField noAIsInput = new TextField();
    TextField noLapsInput = new TextField();

    MenuButton generateTrack = new MenuButton("PREVIEW THIS TRACK", 350, 70, 30);

    generateTrack.setOnMouseClicked(event -> {

      if (box6.getChildren().size() > 0) {

        box6.getChildren().remove(0);
      }

      Map track = null;
      try {

        track = new Map(seedInput.getText());
        box6.getChildren().add(track);
      } catch (Exception e) {
        try {
          PopUpWindow.display("NULL SEED");
        } catch (IOException ex1) {
          ex1.printStackTrace();
          System.err.println("POP UP NOT WORKING");
        }
      }
    });

    MenuButton createGameRoom = new MenuButton("START GAME", 350, 70, 30);

    createGameRoom.setOnMouseClicked(event -> {

      // CREATE LOCAL SERVER //

      Lobby localLobby = new Lobby(4445);

      // CREATE SINGLE PLAYER CLIENT //

      Client localClient = new Client(usernameInput.getText(), 4445, "localhost");
      localClient.start();

      // START SINGLE PLAYER GAME //

      try {

        localClient.startSinglePlayerGame(seedInput.getText(),
            Integer.valueOf(noAIsInput.getText()), Integer.valueOf(noLapsInput.getText()),
            DataGenerator.basicShipSetup(usernameInput.getText()));
        Platform.exit();

      } catch (NumberFormatException e) {

        int num = 0;
        for (int i = 0; i < seedInput.getText().length(); i++) {
          num *= (int) seedInput.getText().charAt(i);
        }

      } catch (IOException exp) {

        exp.printStackTrace();
      }
      ((Node) event.getSource()).getScene().getWindow().hide();

    });

    add(usernameText, 0, 1);
    add(usernameInput, 0, 2);

    add(seedText, 0, 3);
    add(seedInput, 0, 4);

    add(noAIsText, 1, 1);
    add(noAIsInput, 1, 2);

    add(noLapsText, 1, 3);
    add(noLapsInput, 1, 4);

    add(box6, 0, 5);

    add(generateTrack, 0, 7);
    add(createGameRoom, 1, 7);

    GridPane.setMargin(usernameInput, new Insets(0, 0, 20, 0));
    GridPane.setMargin(seedInput, new Insets(0, 0, 20, 0));
    GridPane.setMargin(noAIsInput, new Insets(0, 0, 20, 0));
    GridPane.setMargin(generateTrack, new Insets(0, 0, 20, 0));
    GridPane.setMargin(createGameRoom, new Insets(0, 0, 20, 0));

  }

}
