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
 
  }

  public void setClient(Client client) {

    this.client = client;

  }
  
  public void setSettings(){
	  
	  try {
		  this.gameRoomSeed = Integer.valueOf(seedInput.getText());
		  this.maxPlayers = Integer.valueOf(noPlayersInput.getText());
		  this.lapNo = Integer.valueOf(noLapsInput.getText());
		  this.gameRoomName = nameInput.getText();
	  }
	  catch(Exception e){
	    	
	    	try {
	    		PopUpWindow.display("NULL INPUT");
	    	}
	    	catch (IOException ex){
	    		System.err.println("POP UP NOT WORKING");
	    	}
			
	    }
  }
  
  public void setSeed(){
	  
	 try {
		 this.gameRoomSeed = Integer.valueOf(seedInput.getText());
	 }
	 catch (Exception e){
		 
		 try {
	    		PopUpWindow.display("NULL SEED");
	    	}
	    	catch (IOException ex){
	    		System.err.println("POP UP NOT WORKING");
	    	}
	 }
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
