package userInterface;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import audioEngine.AudioMaster;
import clientComms.Client;
import javafx.animation.TranslateTransition;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import physics.placeholders.DataGenerator;
import serverComms.GameRoom;
import serverComms.Lobby;

/**
 * @author Andreea Gheorghe Main class that manages the functionality of the
 *         game menu and the translations between the different windows.
 */
public class GameMenu extends Parent {

  private GridPane initialWindow, settingsWindow, connectMultiWindow, hostGameRoomWindow,
      joinGameRoomWindow, gameRoomLobbyWindow, creditsWindow, keyBindingsWindow,
      shipCustomisationWindowSingle, shipCustomisationWindowMulti;
  private VBox multiOptionsWindow, singleGameWindow;
  private MenuButton btnPlayGame, btnPlayAI, btnOptions, btnExit, btnCredits, btnKeyBindings,
      btnNextSingle, btnNextMulti, btnBackKeyBindings, btnBackCustomisationSingle,
      btnBackCustomisationMulti, btnBackCredits, btnBackSettings, btnBackMulti, btnBackSingle,
      btnBackHost, btnBackMultiOptions, btnBackJoin;
  private SoundSlider soundSlider;
  private MusicSlider musicSlider;
  private Credits credits;
  private CreateGameRoom createGameRoom;
  private HostGameRoom hostGameRoom;
  private JoinGameRoom joinGameRoom;
  private GameRoom gameRoom;
  private GameRoomLobby gameRoomLobby;
  public static String usr;
  public Client client;
  private final int OFFSET = 600;
  private static CustomisationOptions customisations;
  private CustomisationOptions customisations1;
  private MenuButton startServerMulti;
  private MenuButton connectMulti;
  private VBox box4Multi;
  private TextStyle ip;
  private Text ipStyled;

  /**
   * Constructor for the GameMenu class.
   * 
   * @throws IOException
   */
  public GameMenu() throws IOException {

    initialWindow = new GridPane();
    settingsWindow = new GridPane();
    creditsWindow = new GridPane();
    keyBindingsWindow = new GridPane();
    shipCustomisationWindowSingle = new GridPane();
    shipCustomisationWindowMulti = new GridPane();
    connectMultiWindow = new GridPane();
    multiOptionsWindow = new VBox(15);
    hostGameRoomWindow = new GridPane();
    singleGameWindow = new VBox(3);
    joinGameRoomWindow = new GridPane();
    gameRoomLobbyWindow = new GridPane();

    soundSlider = new SoundSlider();
    musicSlider = new MusicSlider();
    credits = new Credits();

    hostGameRoom = new HostGameRoom();
    createGameRoom = new CreateGameRoom();

    customisations = new CustomisationOptions();
    customisations1 = new CustomisationOptions();

    // BUILD THE WINDOWS //

    buildInitialWindow();
    buildSettingsWindow();
    buildCreditsWindow();
    buildKeyBindingsWindow();
    buildShipCustomisationWindowSingle();
    buildShipCustomisationWindowMulti();
    buildConnectMultiWindow();
    buildMultiOptionsWindow();
    buildSingleGameWindow();
    buildHostGameRoomWindow();
    buildJoinGameRoomWindow();
    buildGameRoomLobbyWindow();

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

    // LOAD TUTORIAL IMAGE //

    InputStream is = Files.newInputStream(Paths.get("src/resources/ui/keybindings.png"));
    Image keyBindings = new Image(is);
    is.close();

    ImageView tutorial = new ImageView(keyBindings);
    tutorial.setFitWidth(800);
    tutorial.setFitHeight(420);

    // BUTTON FUNCTIONALITY //

    btnPlayGame = new MenuButton("MULTIPLAYER", 350, 70, 30);
    btnPlayGame.setOnMouseClicked(event -> {

      // CONNECT TO THE SERVER
      // Transition to connectMultiWindow
      try {
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
      } catch (IllegalArgumentException e) {

        try {
          PopUpWindow.display("LOADING");
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    });

    btnPlayAI = new MenuButton("SINGLE PLAYER", 350, 70, 30);
    btnPlayAI.setOnMouseClicked(event -> {

      // SHIP CUSTOMISATION IN SINGLE PLAYER MODE
      // Enter the game with the computer-controlled players
      // Choose a ship for the single player
      try {
        getChildren().add(shipCustomisationWindowSingle);
        getChildren().removeAll(hoverText, racerText, captionText);

        TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25), initialWindow);
        trans.setToX(initialWindow.getTranslateX() - OFFSET);

        TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25),
            shipCustomisationWindowSingle);
        trans1.setToX(shipCustomisationWindowSingle.getTranslateX() - OFFSET);

        trans.play();
        trans1.play();
        trans.setOnFinished(evt -> {
          getChildren().remove(initialWindow);
        });
      } catch (IllegalArgumentException e) {

        try {
          PopUpWindow.display("LOADING");
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    });

    btnOptions = new MenuButton("SETTINGS", 350, 70, 30);
    btnOptions.setOnMouseClicked(event -> {

      // GAME SETTINGS
      // Transition to the settings window
      try {
        getChildren().add(settingsWindow);
        getChildren().removeAll(hoverText, racerText, captionText);

        TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25), initialWindow);
        trans.setToX(initialWindow.getTranslateX() - OFFSET);

        TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25),
            settingsWindow);
        trans1.setToX(settingsWindow.getTranslateX() - OFFSET);

        trans.play();
        trans1.play();
        trans.setOnFinished(evt -> {
          getChildren().remove(initialWindow);
        });
      } catch (IllegalArgumentException e) {

        try {
          PopUpWindow.display("LOADING");
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    });

    btnCredits = new MenuButton("CREDITS", 350, 70, 30);
    btnCredits.setOnMouseClicked(event -> {

      // GAME CREDITS
      // Transition to the credits window
      try {
        getChildren().add(creditsWindow);
        getChildren().removeAll(hoverText, racerText, captionText);

        TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25), initialWindow);
        trans.setToX(initialWindow.getTranslateX() - OFFSET);

        TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25), creditsWindow);
        trans1.setToX(creditsWindow.getTranslateX() - OFFSET);

        trans.play();
        trans1.play();
        trans.setOnFinished(evt -> {
          getChildren().remove(initialWindow);
        });

      } catch (IllegalArgumentException e) {

        try {
          PopUpWindow.display("LOADING");
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    });

    btnKeyBindings = new MenuButton("CONTROLS", 350, 70, 30);
    btnKeyBindings.setOnMouseClicked(event -> {

      // GAME TUTORIAL
      // Transition to the key bindings window
      try {
        getChildren().add(keyBindingsWindow);
        getChildren().removeAll(hoverText, racerText, captionText);

        TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25), initialWindow);
        trans.setToX(initialWindow.getTranslateX() - OFFSET);

        TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25),
            keyBindingsWindow);
        trans1.setToX(keyBindingsWindow.getTranslateX() - OFFSET);

        trans.play();
        trans1.play();
        trans.setOnFinished(evt -> {
          getChildren().remove(initialWindow);
        });

      } catch (IllegalArgumentException e) {

        try {
          PopUpWindow.display("LOADING");
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }

    });

    btnExit = new MenuButton("EXIT", 350, 70, 30);
    btnExit.setOnMouseClicked(event -> {

      // EXIT THE JAVAFX PLATFORM

      // Tudor - Close the audio engine
      AudioMaster.stopMusic();
      AudioMaster.cleanUp();

      System.exit(0);
    });

    btnNextSingle = new MenuButton("NEXT", 350, 70, 30);
    btnNextSingle.setOnMouseClicked(event -> {

      // SINGLE PLAYER MODE
      // Enter the game with the computer-controlled players
      try {
        getChildren().add(singleGameWindow);
        getChildren().removeAll(hoverText, racerText, captionText);

        TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25),
            shipCustomisationWindowSingle);
        trans.setToX(shipCustomisationWindowSingle.getTranslateX() - OFFSET);

        TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25),
            singleGameWindow);
        trans1.setToX(singleGameWindow.getTranslateX() - OFFSET);

        trans.play();
        trans1.play();
        trans.setOnFinished(evt -> {
          getChildren().remove(shipCustomisationWindowSingle);
        });
      } catch (IllegalArgumentException e) {

        try {
          PopUpWindow.display("LOADING");
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }

    });

    btnNextMulti = new MenuButton("NEXT", 350, 70, 30);
    btnNextMulti.setOnMouseClicked(event -> {

      // MULTIPLAYER MODE
      // Enter the multiplayer options window
      try {
        getChildren().add(multiOptionsWindow);

        TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25),
            shipCustomisationWindowMulti);
        trans.setToX(shipCustomisationWindowMulti.getTranslateX() - OFFSET);

        TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25),
            multiOptionsWindow);
        trans1.setToX(multiOptionsWindow.getTranslateX() - OFFSET);

        trans.play();
        trans1.play();
        trans.setOnFinished(evt -> {
          getChildren().remove(shipCustomisationWindowMulti);
        });
      } catch (IllegalArgumentException e) {

        try {
          PopUpWindow.display("LOADING");
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }

    });

    btnBackSettings = new MenuButton("BACK", 350, 70, 30);
    btnBackSettings.setOnMouseClicked(event -> {

      // BACK TO MAIN MENU FROM SETTINGS WINDOW
      // Transition to the initial window

      try {
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
      } catch (IllegalArgumentException e) {

        try {
          PopUpWindow.display("LOADING");
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }

    });

    btnBackCredits = new MenuButton("BACK", 350, 70, 30);
    btnBackCredits.setOnMouseClicked(event -> {

      // BACK TO MAIN MENU FROM CREDITS WINDOW
      // Transition to the initial window

      getChildren().add(initialWindow);

      TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25), creditsWindow);
      trans.setToX(creditsWindow.getTranslateX() + OFFSET);

      TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25), initialWindow);
      trans1.setToX(creditsWindow.getTranslateX());

      trans.play();
      trans1.play();
      trans.setOnFinished(evt -> {
        getChildren().remove(creditsWindow);
        getChildren().addAll(hoverText, racerText, captionText);
      });
    });

    btnBackKeyBindings = new MenuButton("BACK", 350, 70, 30);
    btnBackKeyBindings.setOnMouseClicked(event -> {

      // BACK TO MAIN MENU FROM KEY BINDINGS WINDOW
      // Transition to the initial window

      getChildren().add(initialWindow);

      TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25),
          keyBindingsWindow);
      trans.setToX(keyBindingsWindow.getTranslateX() + OFFSET);

      TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25), initialWindow);
      trans1.setToX(keyBindingsWindow.getTranslateX());

      trans.play();
      trans1.play();
      trans.setOnFinished(evt -> {
        getChildren().remove(keyBindingsWindow);
        getChildren().addAll(hoverText, racerText, captionText);
      });
    });

    btnBackCustomisationSingle = new MenuButton("BACK", 350, 70, 30);
    btnBackCustomisationSingle.setOnMouseClicked(event -> {

      // BACK TO MAIN MENU FROM SHIP CUSTOMISATION WINDOW
      // IN SINGLE PLAYER MODE
      // Transition to the initial window

      getChildren().add(initialWindow);

      TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25),
          shipCustomisationWindowSingle);
      trans.setToX(shipCustomisationWindowSingle.getTranslateX() + OFFSET);

      TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25), initialWindow);
      trans1.setToX(shipCustomisationWindowSingle.getTranslateX());

      trans.play();
      trans1.play();
      trans.setOnFinished(evt -> {
        getChildren().remove(shipCustomisationWindowSingle);
        getChildren().addAll(hoverText, racerText, captionText);
      });
    });

    btnBackCustomisationMulti = new MenuButton("BACK TO MENU", 350, 70, 30);
    btnBackCustomisationMulti.setOnMouseClicked(event -> {

      // BACK TO MAIN MENU FROM SHIP CUSTOMISATION IN MULTIPLAYER MODE
      // Exit the multiplayer mode by disconnecting the client
      // and relaunching the game menu

      client.cleanup();
      getChildren().clear();

      try {
        GameMenu newMenu = new GameMenu();
        getChildren().add(newMenu);
      } catch (IOException e) {
        System.err.println("CANNOT TRANSITION TO MAIN MENU");
      }

    });

    btnBackMulti = new MenuButton("BACK", 350, 70, 30);
    btnBackMulti.setOnMouseClicked(event -> {

      // BACK TO MAIN MENU FROM MULTIPLAYER MODE WINDOW
      // Transition to the initial window

      getChildren().add(initialWindow);

      TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25),
          connectMultiWindow);
      trans.setToX(connectMultiWindow.getTranslateX() + OFFSET);

      TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25), initialWindow);
      trans1.setToX(connectMultiWindow.getTranslateX());

      trans.play();
      trans1.play();
      trans.setOnFinished(evt -> {
        if (box4Multi.getChildren().contains(startServerMulti)) {
          box4Multi.getChildren().remove(startServerMulti);
          box4Multi.getChildren().add(connectMulti);
        }
        getChildren().remove(connectMultiWindow);
        getChildren().addAll(hoverText, racerText, captionText);
      });
    });

    btnBackSingle = new MenuButton("BACK", 350, 70, 30);
    btnBackSingle.setOnMouseClicked(event -> {

      // BACK TO SHIP CUSTOMISATION FROM SINGLE PLAYER WINDOW
      // Transition to the ship customisation window

      getChildren().add(shipCustomisationWindowSingle);

      TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25), singleGameWindow);
      trans.setToX(singleGameWindow.getTranslateX() + OFFSET);

      TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25),
          shipCustomisationWindowSingle);
      trans1.setToX(singleGameWindow.getTranslateX());

      trans.play();
      trans1.play();
      trans.setOnFinished(evt -> {
        getChildren().remove(singleGameWindow);
      });
    });

    btnBackMultiOptions = new MenuButton("BACK", 350, 70, 30);
    btnBackMultiOptions.setOnMouseClicked(event -> {

      // GO TO SHIP CUSTOMISATION IN MULTIPLAYER MODE

      try {
        getChildren().add(shipCustomisationWindowMulti);

        TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25),
            multiOptionsWindow);
        trans.setToX(multiOptionsWindow.getTranslateX() + OFFSET);

        TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25),
            shipCustomisationWindowMulti);
        trans1.setToX(multiOptionsWindow.getTranslateX());

        trans.play();
        trans1.play();
        trans.setOnFinished(evt -> {
          getChildren().remove(multiOptionsWindow);
        });
      } catch (IllegalArgumentException e) {

        try {
          PopUpWindow.display("LOADING");
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }

    });

    btnBackHost = new MenuButton("BACK", 300, 60, 28);
    btnBackHost.setOnMouseClicked(event -> {

      // BACK TO MULTIPLAYER OPTIONS JOIN/HOST FROM HOST GAME WINDOW
      // Transition to multiplayer options window

      getChildren().add(multiOptionsWindow);

      TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25),
          hostGameRoomWindow);
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

      // BACK TO MULTIPLAYER OPTIONS JOIN/HOST FROM JOIN GAME WINDOW
      // Transition to multiplayer options window

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

    // SKIP CURRENT MUSIC TRACK
    MenuButton skipMusic = new MenuButton("SKIP MUSIC", 350, 70, 30);
    skipMusic.setOnMouseClicked(e -> {

      // Tudor - add skip music functionality
      AudioMaster.skipMusic();

    });

    // CONNECT TO THE SERVER //

    TextStyle usernameM = new TextStyle("USERNAME", 30);
    Text usernameTextM = usernameM.getTextStyled();

    TextField usernameInputMulti = new TextField();

    TextStyle machineM = new TextStyle("MACHINE NAME", 30);
    Text machineTextM = machineM.getTextStyled();

    TextField machineInputMulti = new TextField();

    box4Multi = new VBox(10);
    box4Multi.setPadding(new Insets(0, 0, 5, 0));

    startServerMulti = new MenuButton("START A SERVER", 350, 70, 30);

    // If the server is already running, you connect with the settings
    // <username, portNumber, machineName>
    // If the server is not running, add startServerMulti button.

    connectMulti = new MenuButton("CONNECT TO THE LOBBY", 350, 70, 30);
    connectMulti.setOnMouseClicked(event -> {

      try {

        usr = usernameInputMulti.getText();
        int portNo = 4444;
        String machineName = machineInputMulti.getText();

        client = new Client(usr, portNo, machineName);

        if (client.serverOn) {

          client.start();

          String ipAddress = client.waitForIP();
          System.out.println("Test: " + ipAddress);
          ip = new TextStyle("IP: " + ipAddress, 30);
          ipStyled = ip.getTextStyled();

          ipStyled.setY(100);
          ipStyled.setX(multiOptionsWindow.getLayoutX());

          multiOptionsWindow.getChildren().add(ipStyled);

          MainMenu.allThreads.add(0, client);

          getChildren().add(shipCustomisationWindowMulti);

          TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25),
              connectMultiWindow);
          trans.setToX(connectMultiWindow.getTranslateX() - OFFSET);

          TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25),
              shipCustomisationWindowMulti);
          trans1.setToX(shipCustomisationWindowMulti.getTranslateX() - OFFSET);

          trans.play();
          trans1.play();
          trans.setOnFinished(evt -> {
            getChildren().remove(connectMultiWindow);
          });

        } else {

          // The server was not running
          if (box4Multi.getChildren().size() > 0) {

            box4Multi.getChildren().remove(0);
          }

          box4Multi.getChildren().add(startServerMulti);
        }
      } catch (Exception e) {

        System.err.println("COULD NOT CONNECT TO THE SERVER");
      }

    });

    // If the server was not running -
    // Start a server on <4444, localhost>
    // Start the client with <username, 4444, localhost>
    startServerMulti.setOnMouseClicked(event -> {

      new Lobby(4444);

      usr = usernameInputMulti.getText();
      int portNo = 4444;
      String machineName = machineInputMulti.getText();

      client = new Client(usr, portNo, machineName);
      client.start();

      String ipAddress = client.waitForIP();
      System.out.println("Test2: " + ipAddress);
      ip = new TextStyle("IP: " + ipAddress, 25);
      ipStyled = ip.getTextStyled();

      ipStyled.setY(100);
      ipStyled.setX(multiOptionsWindow.getLayoutX());

      multiOptionsWindow.getChildren().add(ipStyled);

      MainMenu.allThreads.add(0, client);

      getChildren().add(shipCustomisationWindowMulti);

      TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25),
          connectMultiWindow);
      trans.setToX(connectMultiWindow.getTranslateX() - OFFSET);

      TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25),
          shipCustomisationWindowMulti);
      trans1.setToX(shipCustomisationWindowMulti.getTranslateX() - OFFSET);

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

        // Get the list of all the available game rooms
        gameRoomList = client.requestAllGames();
        joinGameRoom.setGameList(gameRoomList);
        joinGameRoom.refresh();

      } catch (IOException e) {

        System.err.println("Didn't receive list of available game rooms");
      }

      TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25),
          multiOptionsWindow);
      trans.setToX(multiOptionsWindow.getTranslateX() - OFFSET);

      TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25),
          joinGameRoomWindow);
      trans1.setToX(joinGameRoomWindow.getTranslateX() - OFFSET);

      trans.play();
      trans1.play();
      trans.setOnFinished(evt -> {
        getChildren().remove(multiOptionsWindow);
      });

    });

    // JOIN THE SELECTED GAME ROOM //

    MenuButton joinChosenGR = new MenuButton("JOIN THIS GAME ROOM", 350, 70, 30);
    joinChosenGR.setOnMouseClicked(eventjoin -> {

      try {

        // The user has not selected a game room
        // from the available ones
        if (joinGameRoom.getChosenGRid() == -1) {

          PopUpWindow.display("PLEASE SELECT A GAME ROOM");

        } else {
          GameRoom gameRoomChosen = client.joinGame(joinGameRoom.getChosenGRid(),
              DataGenerator.basicShipSetup(GameMenu.usr, customisations1.getTypeId()));
          // Create a game lobby of the chosen game room
          // that is received from the server
          gameRoomLobby = new GameRoomLobby(gameRoomChosen);
          gameRoomLobby.setClient(client);
          gameRoomLobby.refresh();

          gameRoomLobbyWindow.add(gameRoomLobby, 0, 0);
          getChildren().add(gameRoomLobbyWindow);

          TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25),
              joinGameRoomWindow);
          trans.setToX(joinGameRoomWindow.getTranslateX() - OFFSET);

          TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25),
              gameRoomLobbyWindow);
          trans1.setToX(gameRoomLobbyWindow.getTranslateX() - OFFSET);

          trans.play();
          trans1.play();
          trans.setOnFinished(evt -> {
            getChildren().remove(joinGameRoomWindow);
          });
        }
      } catch (IOException e) {

        System.err.println("JOIN DIDN'T WORK");
      } catch (Exception e) {
        e.printStackTrace();
      }
    });

    // HOST A GAME ROOM IN MULTIPLAYER MODE //

    MenuButton hostGR = new MenuButton("HOST A GAME ROOM", 350, 70, 30);
    hostGR.setOnMouseClicked(eventHost -> {

      getChildren().add(hostGameRoomWindow);

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

      try {

        hostGameRoom.setSettings();

        gameRoom = client.createGame(hostGameRoom.getSeed(), hostGameRoom.getMaxPlayers(),
            hostGameRoom.getNoLaps(), hostGameRoom.getName(),
            DataGenerator.basicShipSetup(client.clientName, customisations1.getTypeId()));
        // Create a game lobby of the game room
        // that is received from the server
        gameRoomLobby = new GameRoomLobby(gameRoom);
        gameRoomLobby.setClient(client);
        gameRoomLobby.refresh();

        gameRoomLobbyWindow.add(gameRoomLobby, 0, 0);
        getChildren().add(gameRoomLobbyWindow);

        TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25),
            hostGameRoomWindow);
        trans.setToX(hostGameRoomWindow.getTranslateX() - OFFSET);

        TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25),
            gameRoomLobbyWindow);
        trans1.setToX(gameRoomLobbyWindow.getTranslateX() - OFFSET);

        trans.play();
        trans1.play();
        trans.setOnFinished(evt -> {
          getChildren().remove(hostGameRoomWindow);
        });

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

        System.err.println("HOSTING A GAME ROOM DID NOT WORK");
      }

    });

    // MAIN MENU WINDOW CHILDREN //

    initialWindow.add(btnPlayGame, 0, 1);
    initialWindow.add(btnPlayAI, 0, 2);
    initialWindow.add(btnOptions, 0, 3);
    initialWindow.add(btnKeyBindings, 0, 4);
    initialWindow.add(btnCredits, 0, 5);
    initialWindow.add(btnExit, 0, 6);

    // SETTINGS WINDOW CHILDREN //

    settingsWindow.add(musicSlider, 0, 1);
    settingsWindow.add(soundSlider, 0, 2);
    settingsWindow.add(skipMusic, 0, 3);
    settingsWindow.add(btnBackSettings, 0, 4);

    // CREDITS WINDOW CHILDREN //

    creditsWindow.add(credits, 0, 0);
    creditsWindow.add(btnBackCredits, 0, 1);

    // KEY BINDINGS CHILDREN //

    keyBindingsWindow.add(tutorial, 0, 0);
    keyBindingsWindow.add(btnBackKeyBindings, 0, 1);

    // SHIP CUSTOMISATION SINGLE CHILDREN //

    shipCustomisationWindowSingle.add(customisations, 0, 0);
    shipCustomisationWindowSingle.add(btnNextSingle, 0, 1);
    shipCustomisationWindowSingle.add(btnBackCustomisationSingle, 0, 2);

    // SHIP CUSTOMISATION MULTI CHILDREN //

    shipCustomisationWindowMulti.add(customisations1, 0, 0);
    shipCustomisationWindowMulti.add(btnNextMulti, 0, 1);
    shipCustomisationWindowMulti.add(btnBackCustomisationMulti, 0, 2);

    // CONNECT IN MULTIPLAYER MODE CHILDREN //

    connectMultiWindow.add(usernameTextM, 0, 1);
    connectMultiWindow.add(usernameInputMulti, 0, 2);
    GridPane.setMargin(usernameInputMulti, new Insets(0, 0, 10, 0));

    connectMultiWindow.add(machineTextM, 0, 5);
    connectMultiWindow.add(machineInputMulti, 0, 6);
    GridPane.setMargin(machineInputMulti, new Insets(0, 0, 10, 0));

    connectMultiWindow.add(box4Multi, 0, 7);
    connectMultiWindow.add(btnBackMulti, 0, 8);

    GridPane.setHalignment(usernameTextM, HPos.CENTER);
    GridPane.setHalignment(machineTextM, HPos.CENTER);

    // MULTIPLAYER OPTIONS WINDOW CHILDREN //

    multiOptionsWindow.getChildren().addAll(joinGR, hostGR, btnBackMultiOptions);

    // SINGLE PLAYER MODE CHILDREN //

    singleGameWindow.getChildren().addAll(createGameRoom, btnBackSingle);

    // HOST GAME ROOM WINDOW //

    hostGameRoomWindow.add(hostGameRoom, 0, 0);
    hostGameRoomWindow.add(mapBox, 1, 0);
    hostGameRoomWindow.add(generateTrack, 1, 1);
    hostGameRoomWindow.add(hostGameRoomButton, 1, 2);
    hostGameRoomWindow.add(btnBackHost, 0, 1);

    // JOIN GAME ROOM WINDOW //

    joinGameRoomWindow.add(joinGameRoom, 0, 0);
    GridPane.setHalignment(btnBackJoin, HPos.RIGHT);
    joinGameRoomWindow.add(btnBackJoin, 0, 2);
    joinGameRoomWindow.add(joinChosenGR, 0, 1);

    // GAME MENU CHILDREN //

    getChildren().addAll(initialWindow, hoverText, racerText, captionText);

    // IMPROVE THE SPEED OF TRANSLATE TRANSITIONS //

    this.setCache(true);
    this.setCacheHint(CacheHint.SPEED);
  }

  /** Sets the design of the initial game menu window. */
  public void buildInitialWindow() {

    initialWindow.setTranslateX(100);
    initialWindow.setTranslateY(110);
    initialWindow.setAlignment(Pos.CENTER);
    initialWindow.setHgap(10);
    initialWindow.setVgap(6);

  }

  /** Sets the design of the settings window. */
  public void buildSettingsWindow() {

    settingsWindow.setTranslateX(700);
    settingsWindow.setTranslateY(110);
    settingsWindow.setAlignment(Pos.CENTER);
    settingsWindow.setHgap(10);
    settingsWindow.setVgap(10);
    settingsWindow.setPadding(new Insets(20, 30, 0, 30));

  }

  /** Sets the design of the credits window. */
  public void buildCreditsWindow() {

    creditsWindow.setTranslateX(700);
    creditsWindow.setTranslateY(100);
    creditsWindow.setAlignment(Pos.CENTER);
    creditsWindow.setVgap(20);

  }

  /**
   * Sets the design of the key bindings window, where the user can access a
   * tutorial of the controls used in the game.
   */
  public void buildKeyBindingsWindow() {

    keyBindingsWindow.setTranslateX(700);
    keyBindingsWindow.setTranslateY(60);
    keyBindingsWindow.setPadding(new Insets(0, 70, 0, 0));
    keyBindingsWindow.setVgap(20);

  }

  /** Sets the design of the ship customisation window in single player mode. */
  public void buildShipCustomisationWindowSingle() {

    shipCustomisationWindowSingle.setTranslateX(700);
    shipCustomisationWindowSingle.setTranslateY(60);
    shipCustomisationWindowSingle.setVgap(10);
    shipCustomisationWindowSingle.setHgap(10);

  }

  /** Sets the design of the ship customisation window in multiplayer mode. */
  public void buildShipCustomisationWindowMulti() {

    shipCustomisationWindowMulti.setTranslateX(700);
    shipCustomisationWindowMulti.setTranslateY(60);
    shipCustomisationWindowMulti.setVgap(10);
    shipCustomisationWindowMulti.setHgap(10);

  }

  /**
   * Sets the design of the window where the user connects to the lobby in
   * multiplayer mode.
   */
  public void buildConnectMultiWindow() {

    connectMultiWindow.setTranslateX(700);
    connectMultiWindow.setTranslateY(120);
    connectMultiWindow.setAlignment(Pos.CENTER);
    connectMultiWindow.setHgap(10);
    connectMultiWindow.setVgap(10);
    connectMultiWindow.setPadding(new Insets(0, 30, 0, 30));

  }

  /**
   * Sets the design of the window that provides options for the multiplayer
   * mode - (host a game, join a game).
   */
  public void buildMultiOptionsWindow() {

    multiOptionsWindow.setTranslateX(700);
    multiOptionsWindow.setTranslateY(240);

  }

  /** Sets the design for the single player mode window. */
  public void buildSingleGameWindow() {

    singleGameWindow.setTranslateX(700);
    singleGameWindow.setTranslateY(80);

  }

  /**
   * Sets the design for the host game room window, where the user inputs the
   * game settings.
   */
  private void buildHostGameRoomWindow() {

    hostGameRoomWindow.setTranslateX(700);
    hostGameRoomWindow.setTranslateY(80);
    hostGameRoomWindow.setHgap(40);
    hostGameRoomWindow.setVgap(10);
    hostGameRoomWindow.setPadding(new Insets(0, 30, 0, 30));

  }

  /**
   * Sets the design for the join game room window, where the user can join an
   * existing game room.
   */
  private void buildJoinGameRoomWindow() {

    joinGameRoomWindow.setTranslateX(700);
    joinGameRoomWindow.setTranslateY(80);
    joinGameRoomWindow.setVgap(10);
    joinGameRoomWindow.setPadding(new Insets(0, 20, 0, 0));

  }

  /**
   * Sets the design for the game room lobby window, where the user can see who
   * is currently connected to the game room and the host can start the game at
   * any point.
   */
  private void buildGameRoomLobbyWindow() {

    gameRoomLobbyWindow.setTranslateX(700);
    gameRoomLobbyWindow.setTranslateY(130);
    gameRoomLobbyWindow.setVgap(10);
    gameRoomLobbyWindow.setHgap(30);
    gameRoomLobbyWindow.setPadding(new Insets(0, 40, 0, 0));

  }

  public static int getTypeId() {
    return customisations.getTypeId();
  }

}