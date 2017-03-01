package userInterface;
import java.util.ArrayList;

import audioEngine.AudioMaster;
import clientComms.Client;
import serverComms.ServerComm;
import serverComms.GameNameNumber;
import serverComms.Lobby;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
/**
 * 
 * @author Andreea Gheorghe
 *
 */
public class GameMenu extends Parent {
	
	GridPane initialWindow, settingsWindow; 
	VBox soundWindow, connectMultiWindow, multiOptionsWindow, hostGameRoomWindow, singleGameWindow;
	MenuButton btnPlayGame, btnPlayAI, btnOptions, btnSound, btnMusic, btnExit, btnBackSettings, btnBackMulti, btnBackSingle, btnBack4, btnBack5, btnBack6;
	SoundSlider soundSlider;
	MusicSlider musicSlider;
	CreateGameRoom createGameRoom;
	HostGameRoom hostGameRoom;
	static String usr;
	final int OFFSET = 600;
	
	public GameMenu() {
		
		initialWindow = new GridPane();
		settingsWindow = new GridPane();
		connectMultiWindow = new VBox(10);
		multiOptionsWindow = new VBox(20);
		hostGameRoomWindow = new VBox(10);
		singleGameWindow = new VBox(20);
		
		//initial window
		initialWindow.setTranslateX(100);
		initialWindow.setTranslateY(170);
		initialWindow.setAlignment(Pos.CENTER);
        initialWindow.setHgap(10);
        initialWindow.setVgap(10);
		
		//settings
		settingsWindow.setTranslateX(700);
		settingsWindow.setTranslateY(140);
		settingsWindow.setAlignment(Pos.CENTER);
        settingsWindow.setHgap(10);
        settingsWindow.setVgap(10);
        settingsWindow.setPadding(new Insets(20,30,0,30));
		
		//connect multiplayer
		connectMultiWindow.setTranslateX(700);
		connectMultiWindow.setTranslateY(80);
		
		//connect multiplayer options
		multiOptionsWindow.setTranslateX(700);
		multiOptionsWindow.setTranslateY(200);
		
		//host a game room
		hostGameRoomWindow.setTranslateX(700);
		hostGameRoomWindow.setTranslateY(80);
		
		//create a game room to play with AI
		singleGameWindow.setTranslateX(700);
		singleGameWindow.setTranslateY(50);
		
		
		btnPlayGame = new MenuButton("MULTIPLAYER");
		btnPlayGame.setOnMouseClicked(event -> {
			
			// connect to the server
			getChildren().add(connectMultiWindow);
			
			TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25), initialWindow);
			trans.setToX(initialWindow.getTranslateX() - OFFSET);
			
			TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25), connectMultiWindow);
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
			
			TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25), initialWindow);
			trans.setToX(initialWindow.getTranslateX() - OFFSET);
			
			TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25), singleGameWindow);
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
			});
		});
		
		
		btnBackMulti = new MenuButton("BACK");
		btnBackMulti.setOnMouseClicked(event -> {
			
			getChildren().add(initialWindow);
			
			TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25), connectMultiWindow);
			trans.setToX(connectMultiWindow.getTranslateX() + OFFSET);
			
			TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25), initialWindow);
			trans1.setToX(connectMultiWindow.getTranslateX());
			
			trans.play();
			trans1.play();
			trans.setOnFinished(evt -> {
				getChildren().remove(connectMultiWindow);
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
			});
		});
		
		soundSlider = new SoundSlider();
		musicSlider = new MusicSlider();
		
		hostGameRoom = new HostGameRoom();
		createGameRoom = new CreateGameRoom();
				
		VBox box1Multi = new VBox(10);
		box1Multi.setPadding(new Insets(0, 0, 5, 0));
		
		VBox box2Multi = new VBox(10);
		box2Multi.setPadding(new Insets(0, 0, 5, 0));
		
		VBox box3Multi = new VBox(10);
		box3Multi.setPadding(new Insets(0, 0, 5, 0));
		
		TextStyle usernameM = new TextStyle("USERNAME", 20);
		Text usernameTextM = usernameM.getTextStyled();
		
		TextField usernameInputMulti = new TextField();
		
		TextStyle portM = new TextStyle("PORT", 20);
		Text portTextM = portM.getTextStyled();
		
		TextField portInputMulti = new TextField();
		
		TextStyle machineM = new TextStyle("MACHINE NAME", 20);
		Text machineTextM = machineM.getTextStyled();
		
		TextField machineInputMulti = new TextField();

		box1Multi.setAlignment(Pos.CENTER);
		box1Multi.getChildren().addAll(usernameTextM, usernameInputMulti);
		
		box2Multi.setAlignment(Pos.CENTER);
		box2Multi.getChildren().addAll(portTextM, portInputMulti);
		
		box3Multi.setAlignment(Pos.CENTER);
		box3Multi.getChildren().addAll(machineTextM, machineInputMulti);
		
		VBox box4Multi = new VBox(10);
		box4Multi.setPadding(new Insets(0, 0, 5, 0));
		
		MenuButton startServerMulti = new MenuButton("START A SERVER");

		MenuButton connectMulti = new MenuButton("CONNECT TO THE LOBBY");
		connectMulti.setOnMouseClicked(event->{
			
			usr =  usernameInputMulti.getText();
			int portNo = Integer.valueOf(portInputMulti.getText());
			String machineName = machineInputMulti.getText();
			
			Client client = new Client(usr, portNo, machineName, this);
			
			if(client.serverOn) {
				
				client.start();
				
				getChildren().add(multiOptionsWindow);
				
				TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25), connectMultiWindow);
				trans.setToX(connectMultiWindow.getTranslateX() - OFFSET);
				
				TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25), multiOptionsWindow);
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
			
		startServerMulti.setOnMouseClicked(event-> {
			
			Lobby serverLobby = new Lobby (4444);
			ServerComm server = new ServerComm(4444, serverLobby);
			server.start();
			
			usr =  usernameInputMulti.getText();
			int portNo = Integer.valueOf(portInputMulti.getText());
			String machineName = machineInputMulti.getText();
			
			Client client = new Client(usr, portNo, machineName, this);
			client.start();
			
			getChildren().add(multiOptionsWindow);
			
			TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25), connectMultiWindow);
			trans.setToX(connectMultiWindow.getTranslateX() - OFFSET);
			
			TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25), multiOptionsWindow);
			trans1.setToX(multiOptionsWindow.getTranslateX() - OFFSET);
			
			trans.play();
			trans1.play();
			trans.setOnFinished(evt -> {
				getChildren().remove(connectMultiWindow);
			});
			
		});
		
		
		box4Multi.setAlignment(Pos.CENTER);
		box4Multi.getChildren().add(connectMulti);
		
		//connecting multiplayer options
		MenuButton joinGameRoom = new MenuButton("JOIN A GAME ROOM");
		
		MenuButton hostGR = new MenuButton("HOST A GAME ROOM");
		hostGR.setOnMouseClicked(eventHost -> {
			
			getChildren().add(hostGameRoomWindow);
			
			TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25), multiOptionsWindow);
			trans.setToX(multiOptionsWindow.getTranslateX() - OFFSET);
			
			TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25), hostGameRoomWindow);
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
		settingsWindow.add(soundSlider,0,2);
		settingsWindow.add(btnBackSettings,0,3);	
		
		//connect multiplayer
		connectMultiWindow.getChildren().addAll(box1Multi, box2Multi, box3Multi, box4Multi, btnBackMulti);
		
		//connect multiplayer options
		multiOptionsWindow.getChildren().addAll(joinGameRoom, hostGR);
		
		//host a game room by multiplayer
		hostGameRoomWindow.getChildren().add(hostGameRoom);
		
		//create a game room by single player
		singleGameWindow.getChildren().addAll(createGameRoom, btnBackSingle);
		
		getChildren().addAll(initialWindow);
		
		//trying to improve the speed of TranslateTransition
		this.setCache(true);
		this.setCacheHint(CacheHint.SPEED);
	}
	public void passRooms(ArrayList<GameNameNumber> gameList) {
		// TODO Auto-generated method stub
		
	}
}