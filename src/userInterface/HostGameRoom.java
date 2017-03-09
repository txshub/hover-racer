package userInterface;

import java.io.IOException;

import clientComms.Client;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import serverComms.GameRoom;
import physics.placeholders.DataGenerator;

/**
 * 
 * @author Andreea Gheorghe
 *
 */

public class HostGameRoom extends GridPane {

  private Client client;
  private int gameRoomSeed;
  private int maxPlayers;
  private int lapNo;
  private String gameRoomName;
  private TextField seedInput;
  private TextField noPlayersInput;
  private TextField noLapsInput;
  private TextField nameInput;
  

  public HostGameRoom() {
	  
    this.setAlignment(Pos.CENTER);
    this.setHgap(30);
    this.setVgap(3);
    this.setPadding(new Insets(20, 10, 0, 10));

    VBox box6 = new VBox(10);
    GridPane.setRowSpan(box6, 2);
    box6.setAlignment(Pos.CENTER);

    TextStyle name = new TextStyle("CHOOSE GAME NAME", 25);
    Text nameText = name.getTextStyled();

    TextStyle seed = new TextStyle("CHOOSE TRACK SEED", 25);
    Text seedText = seed.getTextStyled();

    TextStyle noPlayers = new TextStyle("CHOOSE NR OF PLAYERS", 25);
    Text noPlayersText = noPlayers.getTextStyled();

    TextStyle noLaps = new TextStyle("CHOOSE NR OF LAPS", 25);
    Text noLapsText = noLaps.getTextStyled();

    nameInput = new TextField();
    seedInput = new TextField();
    noPlayersInput = new TextField();
    noLapsInput = new TextField();

    MenuButton generateTrack = new MenuButton("PREVIEW TRACK", 200, 50, 20);

    generateTrack.setOnMouseClicked(event -> {

      if (box6.getChildren().size() > 0) {

        box6.getChildren().remove(0);
      }

      Map track = new Map(Integer.valueOf(seedInput.getText()));
      box6.getChildren().add(track);

    });

    
    add(nameText, 0, 1);
    add(nameInput, 0, 2);

    add(seedText, 0, 3);
    add(seedInput, 0, 4);

    add(noPlayersText, 1, 1);
    add(noPlayersInput, 1, 2);

    add(noLapsText, 1, 3);
    add(noLapsInput, 1, 4);

    add(box6, 0, 5);

    add(generateTrack, 0, 8);

    GridPane.setMargin(nameInput, new Insets(0, 0, 20, 0));
    GridPane.setMargin(seedInput, new Insets(0, 0, 20, 0));
    GridPane.setMargin(noPlayersInput, new Insets(0, 0, 20, 0));
    GridPane.setMargin(generateTrack, new Insets(0, 0, 20, 0));

  }

  public void setClient(Client client) {

    this.client = client;

  }
  
  public void setSeed(){
	  
	  this.gameRoomSeed = Integer.valueOf(seedInput.getText());
  }
  
  public void setMaxPlayers(){
	  
	  this.maxPlayers = Integer.valueOf(noPlayersInput.getText());
  }
  
  public void setNoLaps(){
	  
	  this.lapNo = Integer.valueOf(noLapsInput.getText());
  }
  
  public void setName(){
	  
	  this.gameRoomName = nameInput.getText();
  }
  
  public int getSeed(){
	  
	  return this.gameRoomSeed;
  }
  
  public int getMaxPlayers(){
	  
	 return this.maxPlayers; 
  }
  
  public int getNoLaps(){
	  
	 return this.lapNo;
  }
  
  public String getName(){
	  
	 return this.gameRoomName;
  }
}
