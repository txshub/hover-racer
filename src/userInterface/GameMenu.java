package userInterface;

import java.io.IOException;
import java.util.ArrayList;

import audioEngine.AudioMaster;
import clientComms.Client;
import javafx.animation.TranslateTransition;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.CacheHint;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import physics.placeholders.DataGenerator;
import serverComms.GameRoom;
import serverComms.Lobby;
import serverComms.ServerComm;

/**
 * 
 * @author Andreea Gheorghe
 *
 */
public class GameMenu extends Parent {

  private GridPane initialWindow, settingsWindow, connectMultiWindow, hostGameRoomWindow;
  private VBox multiOptionsWindow, singleGameWindow, joinGameRoomWindow;
  private MenuButton btnPlayGame, btnPlayAI, btnOptions, btnSound, btnMusic, btnExit, btnBackSettings,
  			 		 btnBackMulti, btnBackSingle, btnBackHost, btnBackMultiOptions, btnBackJoin;
  private SoundSlider soundSlider;
  private MusicSlider musicSlider;
  private CreateGameRoom createGameRoom;
  private HostGameRoom hostGameRoom;
  private JoinGameRoom joinGameRoom;
  private GameRoom gameRoom;
  private GameRoomLobby gameRoomLobby;
  public static String usr;
  public Client client;
  private final int OFFSET = 600;

  public GameMenu() throws IOException {

    initialWindow = new GridPane();
    settingsWindow = new GridPane();
    connectMultiWindow = new GridPane();
    multiOptionsWindow = new VBox(15);
    hostGameRoomWindow = new GridPane();
    singleGameWindow = new VBox(3);
    joinGameRoomWindow = new VBox(3);
    
    soundSlider = new SoundSlider();
    musicSlider = new MusicSlider();

    hostGameRoom = new HostGameRoom();
    createGameRoom = new CreateGameRoom();

    // BUILD THE WINDOWS //
    
    buildInitialWindow();
    buildSettingsWindow();
    buildConnectMultiWindow();
    buildMultiOptionsWindow();
    buildSingleGameWindow();
    buildHostGameRoomWindow();
    buildJoinGameRoomWindow();
    
    
    // GAME MENU CAPTION //
    
    TextStyle hover = new TextStyle("HOVER", 90);
    Text hoverText = hover.getTextStyled();
    hoverText.setX(650);
    hoverText.setY(300);

    TextStyle racer = new TextStyle("RACER", 90);
    Text racerText = racer.getTextStyled();
    racerText.setX(740);
    racerText.setY(380);

    TextStyle caption = new TextStyle("DARE TO WIN THE SPACE RACE", 29);
    Text captionText = caption.getTextStyled();
    captionText.setX(650);
    captionText.setY(450);

    // BUTTON FUNCTIONALITY //

    btnPlayGame = new MenuButton("MULTIPLAYER", 350, 70, 30);
    btnPlayGame.setOnMouseClicked(event -> {

      // CONNECT TO THE SERVER
      // Transition to connectMultiWindow
    	
      getChildren().add(connectMultiWindow);
      getChildren().removeAll(hoverText, racerText, captionText);

      TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25), initialWindow);
      trans.setToX(initialWindow.getTranslateX() - OFFSET);

      TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25),
          connectMultiWindow);
      trans1.setToX(connectMultiWindow.getTranslateX() - OFFSET);

      trans.play();
      trans1.play();
      trans.setOnFinished(evt -> {
        getChildren().remove(initialWindow);

      });
    });

    btnPlayAI = new MenuButton("SINGLE PLAYER", 350, 70, 30);
    btnPlayAI.setOnMouseClicked(event -> {
    
     // SINGLE PLAYER MODE 
     // Enter the game with the computer-controlled players

      getChildren().add(singleGameWindow);
      getChildren().removeAll(hoverText, racerText, captionText);

      TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25), initialWindow);
      trans.setToX(initialWindow.getTranslateX() - OFFSET);

      TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25),
          singleGameWindow);
      trans1.setToX(singleGameWindow.getTranslateX() - OFFSET);

      trans.play();
      trans1.play();
      trans.setOnFinished(evt -> {
        getChildren().remove(initialWindow);
      });
    });

    btnOptions = new MenuButton("SETTINGS", 350, 70, 30);
    btnOptions.setOnMouseClicked(event -> {

      // GAME SETTINGS  	
      // Transition to the settings window
    	
      getChildren().add(settingsWindow);
      getChildren().removeAll(hoverText, racerText, captionText);

      TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25), initialWindow);
      trans.setToX(initialWindow.getTranslateX() - OFFSET);

      TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25), settingsWindow);
      trans1.setToX(settingsWindow.getTranslateX() - OFFSET);

      trans.play();
      trans1.play();
      trans.setOnFinished(evt -> {
        getChildren().remove(initialWindow);
      });
    });

    btnExit = new MenuButton("EXIT", 350, 70, 30);
    btnExit.setOnMouseClicked(event -> {
    	
      // EXIT THE JAVAFX PLATFORM
    	
      // Tudor - Close the audio engine
      AudioMaster.stopMusic();
      AudioMaster.cleanUp();

      System.exit(0);
    });
    
    btnBackSettings = new MenuButton("BACK", 350, 70, 30);
    btnBackSettings.setOnMouseClicked(event -> {
    	
      //BACK TO MAIN MENU FROM SETTINGS WINDOW
    	
      getChildren().add(initialWindow);

      TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25), settingsWindow);
      trans.setToX(settingsWindow.getTranslateX() + OFFSET);

      TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25), initialWindow);
      trans1.setToX(settingsWindow.getTranslateX());

      trans.play();
      trans1.play();
      trans.setOnFinished(evt -> {
        getChildren().remove(settingsWindow);
        getChildren().addAll(hoverText, racerText, captionText);
      });
    });

    btnBackMulti = new MenuButton("BACK", 350, 70, 30);
    btnBackMulti.setOnMouseClicked(event -> {

      // BACK TO MAIN MENU FROM MULTIPLAYER MODE WINDOW
    	
      getChildren().add(initialWindow);

      TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25), connectMultiWindow);
      trans.setToX(connectMultiWindow.getTranslateX() + OFFSET);

      TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25), initialWindow);
      trans1.setToX(connectMultiWindow.getTranslateX());

      trans.play();
      trans1.play();
      trans.setOnFinished(evt -> {
        getChildren().remove(connectMultiWindow);
        getChildren().addAll(hoverText, racerText, captionText);
      });
    });

    btnBackSingle = new MenuButton("BACK", 350, 70, 30);
    btnBackSingle.setOnMouseClicked(event -> {

      //BACK TO MAIN MENU FROM SINGLE PLAYER WINDOW
    	
      getChildren().add(initialWindow);

      TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25), singleGameWindow);
      trans.setToX(singleGameWindow.getTranslateX() + OFFSET);

      TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25), initialWindow);
      trans1.setToX(singleGameWindow.getTranslateX());

      trans.play();
      trans1.play();
      trans.setOnFinished(evt -> {
        getChildren().remove(singleGameWindow);
        getChildren().addAll(hoverText, racerText, captionText);

      });
    });

    btnBackMultiOptions = new MenuButton("BACK TO MENU", 350, 70, 30);
    btnBackMultiOptions.setOnMouseClicked(event -> {

      // BACK TO MAIN MENU FROM MULTIPLAYER MODE
    	
      getChildren().add(initialWindow);

      TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25), multiOptionsWindow);
      trans.setToX(multiOptionsWindow.getTranslateX() + OFFSET);

      TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25), initialWindow);
      trans1.setToX(multiOptionsWindow.getTranslateX());

      trans.play();
      trans1.play();

      trans.setOnFinished(evt -> {

        // Disconnect client when you return to the main menu
        client.cleanup();

        getChildren().remove(multiOptionsWindow);
        getChildren().addAll(hoverText, racerText, captionText);

      });
    });

    btnBackHost = new MenuButton("BACK", 300, 60, 28);
    btnBackHost.setOnMouseClicked(event -> {

      //BACK TO MULTIPLAYER OPTIONS JOIN/HOST FROM HOST	GAME WINDOW
    	
      getChildren().add(multiOptionsWindow);

      TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25), hostGameRoomWindow);
      trans.setToX(hostGameRoomWindow.getTranslateX() + OFFSET);

      TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25),
          multiOptionsWindow);
      trans1.setToX(hostGameRoomWindow.getTranslateX());

      trans.play();
      trans1.play();

      trans.setOnFinished(evt -> {
        getChildren().remove(hostGameRoomWindow);

      });
    });

    btnBackJoin = new MenuButton("BACK", 350, 70, 30);
    btnBackJoin.setOnMouseClicked(event -> {

      //BACK TO MULTIPLAYER OPTIONS JOIN/HOST FROM JOIN GAME WINDOW
    	
      getChildren().add(multiOptionsWindow);

      TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25),
          joinGameRoomWindow);
      trans.setToX(joinGameRoomWindow.getTranslateX() + OFFSET);

      TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25),
          multiOptionsWindow);
      trans1.setToX(joinGameRoomWindow.getTranslateX());

      trans.play();
      trans1.play();

      trans.setOnFinished(evt -> {
        getChildren().remove(joinGameRoomWindow);

      });
    });

    // CONNECT TO THE SERVER //
    
    TextStyle usernameM = new TextStyle("USERNAME", 30);
    Text usernameTextM = usernameM.getTextStyled();

    TextField usernameInputMulti = new TextField();

    TextStyle portM = new TextStyle("PORT", 30);
    Text portTextM = portM.getTextStyled();

    TextField portInputMulti = new TextField();

    TextStyle machineM = new TextStyle("MACHINE NAME", 30);
    Text machineTextM = machineM.getTextStyled();

    TextField machineInputMulti = new TextField();

    VBox box4Multi = new VBox(10);
    box4Multi.setPadding(new Insets(0, 0, 5, 0));
    
    MenuButton startServerMulti = new MenuButton("START A SERVER", 350, 70, 30);
    
    //If the server is already running, you connect with the settings
    // <username, portNumber, machineName>
    //If the server is not running, add startServerMulti button.
    
    MenuButton connectMulti = new MenuButton("CONNECT TO THE LOBBY", 350, 70, 30);
    connectMulti.setOnMouseClicked(event -> {

    try {	
    	
      usr = usernameInputMulti.getText();
      int portNo = Integer.valueOf(portInputMulti.getText());
      String machineName = machineInputMulti.getText();

      client = new Client(usr, portNo, machineName, this);

      if (client.serverOn) {

        client.start();

        getChildren().add(multiOptionsWindow);

        TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25),
            connectMultiWindow);
        trans.setToX(connectMultiWindow.getTranslateX() - OFFSET);

        TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25),
            multiOptionsWindow);
        trans1.setToX(multiOptionsWindow.getTranslateX() - OFFSET);

        trans.play();
        trans1.play();
        trans.setOnFinished(evt -> {
          getChildren().remove(connectMultiWindow);
        });

      } else {
    	  
    	//The server was not running
        if (box4Multi.getChildren().size() > 0) {

          box4Multi.getChildren().remove(0);
        }
       
        box4Multi.getChildren().add(startServerMulti);
      }
    }
    catch(Exception e){
    	
    	PopUpWindow popUp = new PopUpWindow("INVALID INPUT");
		popUp.display();
		
    }
    
    });
    

    //If the server was not running - 
    //Start a server on <4444, localhost>
    //Start the client with <username, 4444, localhost>
    startServerMulti.setOnMouseClicked(event -> {

      Lobby serverLobby = new Lobby(4444);
      ServerComm server = new ServerComm(4444, serverLobby);
      server.start();

      usr = usernameInputMulti.getText();
      int portNo = Integer.valueOf(portInputMulti.getText());
      String machineName = machineInputMulti.getText();

      client = new Client(usr, portNo, machineName, this);
      client.start();

      getChildren().add(multiOptionsWindow);

      TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25),
          connectMultiWindow);
      trans.setToX(connectMultiWindow.getTranslateX() - OFFSET);

      TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25),
          multiOptionsWindow);
      trans1.setToX(multiOptionsWindow.getTranslateX() - OFFSET);

      trans.play();
      trans1.play();
      trans.setOnFinished(evt -> {
        getChildren().remove(connectMultiWindow);
      });

    });

    box4Multi.setAlignment(Pos.CENTER);
    box4Multi.getChildren().add(connectMulti);

    // JOIN A GAME ROOM IN MULTIPLAYER MODE //
    joinGameRoom = new JoinGameRoom();

    MenuButton joinGR = new MenuButton("JOIN A GAME ROOM", 350, 70, 30);
    
    joinGR.setOnMouseClicked(event -> {
        
      getChildren().add(joinGameRoomWindow);
      joinGameRoom.setClient(client);

      ArrayList<GameRoom> gameRoomList;
      try {
    	  gameRoomList = client.requestAllGames();
    	  joinGameRoom.setGameList(gameRoomList);
		
      } catch (IOException e) {
		
    	  System.err.println("Didn't receive list of available game rooms");
      }
      
      TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25), multiOptionsWindow);
      trans.setToX(multiOptionsWindow.getTranslateX() - OFFSET);

      TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25), joinGameRoomWindow);
      trans1.setToX(joinGameRoomWindow.getTranslateX() - OFFSET);

      trans.play();
      trans1.play();
      trans.setOnFinished(evt -> {
        getChildren().remove(multiOptionsWindow);
      });

    });

    //HOST A GAME ROOM IN MULTIPLAYER MODE //

    MenuButton hostGR = new MenuButton("HOST A GAME ROOM", 350, 70, 30);
    hostGR.setOnMouseClicked(eventHost -> {

      getChildren().add(hostGameRoomWindow);
      hostGameRoom.setClient(client);

      TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25),
          multiOptionsWindow);
      trans.setToX(multiOptionsWindow.getTranslateX() - OFFSET);

      TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25),
          hostGameRoomWindow);
      trans1.setToX(hostGameRoomWindow.getTranslateX() - OFFSET);

      trans.play();
      trans1.play();
      trans.setOnFinished(evt -> {
        getChildren().remove(multiOptionsWindow);
      });

    });
    
    // PREVIEW THE TRACK //
    
    VBox mapBox = new VBox(10);
    GridPane.setRowSpan(mapBox, 3);
    mapBox.setAlignment(Pos.CENTER);
    
    MenuButton generateTrack = new MenuButton("PREVIEW TRACK", 300, 60, 28);
    
    generateTrack.setOnMouseClicked(event -> {

      if (mapBox.getChildren().size() > 0) {

        mapBox.getChildren().remove(0);
      }
      
      hostGameRoom.setSeed();
      
      Map track = new Map(hostGameRoom.getSeed());
      mapBox.getChildren().add(track);

    });
    
    
    // CREATE THE GAME ROOM //
    
    MenuButton hostGameRoomButton = new MenuButton("CREATE THE GAME ROOM", 300, 60, 28);

    hostGameRoomButton.setOnMouseClicked(event -> {
    	
      // create a game room
      hostGameRoom.setSeed();
      hostGameRoom.setMaxPlayers();
      hostGameRoom.setNoLaps();
      hostGameRoom.setName();
      
      try {
  
       gameRoom =  client.createGame(hostGameRoom.getSeed(), hostGameRoom.getMaxPlayers(), hostGameRoom.getNoLaps(), 
    		       hostGameRoom.getName(), DataGenerator.basicShipSetup(client.clientName));
       gameRoomLobby = new GameRoomLobby(gameRoom);
       System.out.println(client.requestAllGames().size());
       gameRoomLobby.setClient(client);
       
       getChildren().add(gameRoomLobby);

       TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25), hostGameRoomWindow);
       trans.setToX(hostGameRoomWindow.getTranslateX() - OFFSET);

       TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25), gameRoomLobby);
       trans1.setToX(gameRoomLobby.getTranslateX() - OFFSET);

       trans.play();
       trans1.play();
       trans.setOnFinished(evt -> {
         getChildren().remove(hostGameRoomWindow);
       });
      

      } catch (IOException e) {

        e.printStackTrace();
      }

    });


    // MAIN MENU WINDOW CHILDREN
    initialWindow.add(btnPlayGame, 0, 1);
    initialWindow.add(btnPlayAI, 0, 2);
    initialWindow.add(btnOptions, 0, 3);
    initialWindow.add(btnExit, 0, 4);

    // SETTINGS WINDOW CHILDREN
    settingsWindow.add(musicSlider, 0, 1);
    settingsWindow.add(soundSlider, 0, 2);
    settingsWindow.add(btnBackSettings, 0, 3);

    // CONNECT IN MULTIPLAYER MODE CHILDREN
    connectMultiWindow.add(usernameTextM, 0, 1);
    connectMultiWindow.add(usernameInputMulti, 0, 2);
    GridPane.setMargin(usernameInputMulti, new Insets(0, 0, 10, 0));

    connectMultiWindow.add(portTextM, 0, 3);
    connectMultiWindow.add(portInputMulti, 0, 4);
    GridPane.setMargin(portInputMulti, new Insets(0, 0, 10, 0));

    connectMultiWindow.add(machineTextM, 0, 5);
    connectMultiWindow.add(machineInputMulti, 0, 6);
    GridPane.setMargin(machineInputMulti, new Insets(0, 0, 10, 0));

    connectMultiWindow.add(box4Multi, 0, 7);
    connectMultiWindow.add(btnBackMulti, 0, 8);

    GridPane.setHalignment(usernameTextM, HPos.CENTER);
    GridPane.setHalignment(portTextM, HPos.CENTER);
    GridPane.setHalignment(machineTextM, HPos.CENTER);

    // MULTIPLAYER OPTIONS WINDOW CHILDREN
    multiOptionsWindow.getChildren().addAll(joinGR, hostGR, btnBackMultiOptions);
    
    // SINGLE PLAYER MODE CHILDREN
    singleGameWindow.getChildren().addAll(createGameRoom, btnBackSingle);

    // HOST GAME ROOM WINDOW
    hostGameRoomWindow.add(hostGameRoom, 0, 0);
    hostGameRoomWindow.add(mapBox, 1, 0);
    hostGameRoomWindow.add(generateTrack, 1, 1);
    hostGameRoomWindow.add(hostGameRoomButton, 1, 2);
    hostGameRoomWindow.add(btnBackHost, 0, 1);
    
    // JOIN GAME ROOM WINDOW 
    joinGameRoomWindow.getChildren().addAll(btnBackJoin, joinGameRoom);

    // GAME MENU CHILDREN
    getChildren().addAll(initialWindow, hoverText, racerText, captionText);

    // IMPROVE THE SPEED OF TRANSLATE TRANSITIONS
    this.setCache(true);
    this.setCacheHint(CacheHint.SPEED);
  }

/**
   * Sets the design of the initial game menu window.
   */
  public void buildInitialWindow(){
	  
	  initialWindow.setTranslateX(100);
	  initialWindow.setTranslateY(180);
	  initialWindow.setAlignment(Pos.CENTER);
	  initialWindow.setHgap(10);
	  initialWindow.setVgap(10);
	  
  }
  
  /**
   * Sets the design of the settings window.
   */
  public void buildSettingsWindow(){
	  
	  settingsWindow.setTranslateX(700);
	  settingsWindow.setTranslateY(140);
	  settingsWindow.setAlignment(Pos.CENTER);
	  settingsWindow.setHgap(10);
	  settingsWindow.setVgap(10);
	  settingsWindow.setPadding(new Insets(20, 30, 0, 30));
	  
  }
  
  /**
   * Sets the design of the window where the user connects to the lobby
   * in multiplayer mode.
   */
  public void buildConnectMultiWindow(){
	  
	  connectMultiWindow.setTranslateX(700);
	  connectMultiWindow.setTranslateY(120);
	  connectMultiWindow.setAlignment(Pos.CENTER);
	  connectMultiWindow.setHgap(10);
	  connectMultiWindow.setVgap(10);
	  connectMultiWindow.setPadding(new Insets(0, 30, 0, 30));
	  
  }
  
  /**
   * Sets the design of the window that provides options 
   * for the multiplayer mode - (host a game, join a game).
   */
  public void buildMultiOptionsWindow(){
	  
	  multiOptionsWindow.setTranslateX(700);
	  multiOptionsWindow.setTranslateY(240);
	  
  }
  
  /**
   * Sets the design for the single player mode window.
   */
  public void buildSingleGameWindow(){
	  
	  singleGameWindow.setTranslateX(700);
	  singleGameWindow.setTranslateY(50);
	  
  }
  
  /**
   * Sets the design for the host game room window,
   * where the user inputs the game settings.
   */
  private void buildHostGameRoomWindow() {
		
	  hostGameRoomWindow.setTranslateX(700);
	  hostGameRoomWindow.setTranslateY(80);
	  hostGameRoomWindow.setHgap(40);
	  hostGameRoomWindow.setVgap(10);
	  hostGameRoomWindow.setPadding(new Insets(0,30,0,30));

  }
  
  /**
   * Sets the design for the join game room window,
   * where the user can join an existing game room.
   */
  private void buildJoinGameRoomWindow() {
	
	  joinGameRoomWindow.setTranslateX(700);
	  joinGameRoomWindow.setTranslateY(50);
  }

}