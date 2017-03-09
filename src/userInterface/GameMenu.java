package userInterface;

import java.io.IOException;

import audioEngine.AudioMaster;
import clientComms.Client;
import javafx.animation.TranslateTransition;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import serverComms.Lobby;
import serverComms.ServerComm;

/**
 * 
 * @author Andreea Gheorghe
 *
 */
public class GameMenu extends Parent {

  GridPane initialWindow, settingsWindow, connectMultiWindow;
  VBox multiOptionsWindow, hostGameRoomWindow, singleGameWindow, joinGameRoomWindow;
  MenuButton btnPlayGame, btnPlayAI, btnOptions, btnSound, btnMusic, btnExit, btnBackSettings,
      btnBackMulti, btnBackSingle, btnBackHost, btnBackMultiOptions, btnBackJoin;
  SoundSlider soundSlider;
  MusicSlider musicSlider;
  CreateGameRoom createGameRoom;
  HostGameRoom hostGameRoom;
  JoinGameRoom joinGameRoom;
  static String usr;
  Client client;
  final int OFFSET = 600;

  public GameMenu() throws IOException {

    initialWindow = new GridPane();
    settingsWindow = new GridPane();
    connectMultiWindow = new GridPane();
    multiOptionsWindow = new VBox(15);
    hostGameRoomWindow = new VBox(3);
    singleGameWindow = new VBox(3);
    joinGameRoomWindow = new VBox(3);

    // initial window
    initialWindow.setTranslateX(100);
    initialWindow.setTranslateY(180);
    initialWindow.setAlignment(Pos.CENTER);
    initialWindow.setHgap(10);
    initialWindow.setVgap(10);

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

    // settings
    settingsWindow.setTranslateX(700);
    settingsWindow.setTranslateY(140);
    settingsWindow.setAlignment(Pos.CENTER);
    settingsWindow.setHgap(10);
    settingsWindow.setVgap(10);
    settingsWindow.setPadding(new Insets(20, 30, 0, 30));

    // connect multiplayer
    connectMultiWindow.setTranslateX(700);
    connectMultiWindow.setTranslateY(120);
    connectMultiWindow.setAlignment(Pos.CENTER);
    connectMultiWindow.setHgap(10);
    connectMultiWindow.setVgap(10);
    connectMultiWindow.setPadding(new Insets(0, 30, 0, 30));

    // connect multiplayer options
    multiOptionsWindow.setTranslateX(700);
    multiOptionsWindow.setTranslateY(240);

    // host a game room
    hostGameRoomWindow.setTranslateX(700);
    hostGameRoomWindow.setTranslateY(80);

    // create a game room to play with AI
    singleGameWindow.setTranslateX(700);
    singleGameWindow.setTranslateY(50);

    joinGameRoomWindow.setTranslateX(700);
    joinGameRoomWindow.setTranslateY(50);

    btnPlayGame = new MenuButton("MULTIPLAYER");
    btnPlayGame.setOnMouseClicked(event -> {

      // connect to the server
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

    btnPlayAI = new MenuButton("SINGLE PLAYER");
    btnPlayAI.setOnMouseClicked(event -> {
      // enter the game with the computer-controlled players

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

    btnOptions = new MenuButton("SETTINGS");
    btnOptions.setOnMouseClicked(event -> {

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

    btnExit = new MenuButton("EXIT");
    btnExit.setOnMouseClicked(event -> {

      // Tudor - Close the audio engine
      AudioMaster.stopMusic();
      AudioMaster.cleanUp();

      System.exit(0);
    });

    btnBackSettings = new MenuButton("BACK");

    btnBackSettings.setOnMouseClicked(event -> {
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

    btnBackMulti = new MenuButton("BACK");
    btnBackMulti.setOnMouseClicked(event -> {

      getChildren().add(initialWindow);

      TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25),
          connectMultiWindow);
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

    btnBackSingle = new MenuButton("BACK");
    btnBackSingle.setOnMouseClicked(event -> {

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

    btnBackMultiOptions = new MenuButton("BACK TO MENU");
    btnBackMultiOptions.setOnMouseClicked(event -> {

      getChildren().add(initialWindow);

      TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25),
          multiOptionsWindow);
      trans.setToX(multiOptionsWindow.getTranslateX() + OFFSET);

      TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25), initialWindow);
      trans1.setToX(multiOptionsWindow.getTranslateX());

      trans.play();
      trans1.play();

      trans.setOnFinished(evt -> {

        // disconnect client when you return to the main menu
        client.cleanup();

        getChildren().remove(multiOptionsWindow);
        getChildren().addAll(hoverText, racerText, captionText);

      });
    });

    btnBackHost = new MenuButton("BACK");
    btnBackHost.setOnMouseClicked(event -> {

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

    btnBackJoin = new MenuButton("BACK");
    btnBackJoin.setOnMouseClicked(event -> {

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

    soundSlider = new SoundSlider();
    musicSlider = new MusicSlider();

    hostGameRoom = new HostGameRoom();
    createGameRoom = new CreateGameRoom();

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

    MenuButton startServerMulti = new MenuButton("START A SERVER");

    MenuButton connectMulti = new MenuButton("CONNECT TO THE LOBBY");
    connectMulti.setOnMouseClicked(event -> {

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

        if (box4Multi.getChildren().size() > 0) {

          box4Multi.getChildren().remove(0);
        }

        box4Multi.getChildren().add(startServerMulti);
      }
    });

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

    // connecting multiplayer options
    MenuButton joinGR = new MenuButton("JOIN A GAME ROOM");

    joinGameRoom = new JoinGameRoom();

    joinGR.setOnMouseClicked(eventHost -> {

      try {
        joinGameRoom.setClient(client);
        joinGameRoom.refresh();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      getChildren().add(joinGameRoomWindow);

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

    MenuButton hostGR = new MenuButton("HOST A GAME ROOM");
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

    // initial window
    initialWindow.add(btnPlayGame, 0, 1);
    initialWindow.add(btnPlayAI, 0, 2);
    initialWindow.add(btnOptions, 0, 3);
    initialWindow.add(btnExit, 0, 4);

    // settings window
    settingsWindow.add(musicSlider, 0, 1);
    settingsWindow.add(soundSlider, 0, 2);
    settingsWindow.add(btnBackSettings, 0, 3);

    // connect multiplayer
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

    // connect multiplayer options
    multiOptionsWindow.getChildren().addAll(joinGR, hostGR, btnBackMultiOptions);

    // host a game room by multiplayer
    hostGameRoomWindow.getChildren().addAll(hostGameRoom, btnBackHost);

    // create a game room by single player
    singleGameWindow.getChildren().addAll(createGameRoom, btnBackSingle);

    // join a game room in multiplayer mode
    joinGameRoomWindow.getChildren().addAll(joinGameRoom, btnBackJoin);

    getChildren().addAll(initialWindow, hoverText, racerText, captionText);

    // trying to improve the speed of TranslateTransition
    this.setCache(true);
    this.setCacheHint(CacheHint.SPEED);
  }
}