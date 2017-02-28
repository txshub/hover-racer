package userInterface;

import audioEngine.AudioMaster;
import clientComms.Client;
import serverComms.ServerComm;
import serverComms.Lobby;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * 
 * @author Andreea Gheorghe
 *
 */

public class GameMenu extends Parent {

	VBox menu0, menu1, menu2, menu3, menu4, menu5, menu6, menu7, menu8;
	MenuButton btnPlayGame, btnPlayAI, btnOptions, btnSound, btnMusic, btnExit, btnBack, btnBack2, btnBack3, btnBack4, btnBack5, btnBack6;
	SoundSlider soundSlider;
	MusicSlider musicSlider;
	static String usr;
	final int OFFSET = 600;

	public GameMenu() {

		menu0 = new VBox(10);
		menu1 = new VBox(10);
		menu2 = new VBox(10);
		menu3 = new VBox(10);
		menu4 = new VBox(20);
		menu5 = new VBox(20);
		menu6 = new VBox(10);
		menu7 = new VBox(20);
		menu8 = new VBox(20);

		//initial window
		menu0.setTranslateX(100);
		menu0.setTranslateY(160);
		menu0.setPadding(new Insets(20.0));

		//settings
		menu1.setTranslateX(700);
		menu1.setTranslateY(160);
		menu1.setPadding(new Insets(20.0));

		//sound effects
		menu2.setTranslateX(700);
		menu2.setTranslateY(200);
		menu2.setPadding(new Insets(20.0));

		//music
		menu3.setTranslateX(700);
		menu3.setTranslateY(200);
		menu3.setPadding(new Insets(20.0));

		//connect multiplayer
		menu4.setTranslateX(700);
		menu4.setTranslateY(80);
		
		//connect single player
		menu5.setTranslateX(700);
		menu5.setTranslateY(80);
		
		//connect multiplayer options
		menu6.setTranslateX(700);
		menu6.setTranslateY(200);
		
		//host a game room
		menu7.setTranslateX(700);
		menu7.setTranslateY(80);
		
		//create a game room to play with AI
		menu8.setTranslateX(700);
		menu8.setTranslateY(80);
		
		
		btnPlayGame = new MenuButton("MULTIPLAYER");
		btnPlayGame.setOnMouseClicked(event -> {
			// enter the server lobby in the actual game
			getChildren().add(menu4);
			
			TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25), menu0);
			trans.setToX(menu0.getTranslateX() - OFFSET);

			TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25), menu4);
			trans1.setToX(menu4.getTranslateX() - OFFSET);

			trans.play();
			trans1.play();

			trans.setOnFinished(evt -> {
				getChildren().remove(menu0);
			});

		});

		btnPlayAI = new MenuButton("SINGLE PLAYER");
		btnPlayAI.setOnMouseClicked(event -> {
			// enter the game with the computer-controlled players
			
			getChildren().add(menu5);
			
			TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25), menu0);
			trans.setToX(menu0.getTranslateX() - OFFSET);
			
			TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25), menu5);
			trans1.setToX(menu5.getTranslateX() - OFFSET);

			trans.play();
			trans1.play();

			trans.setOnFinished(evt -> {
				getChildren().remove(menu0);
			});

		});

		btnOptions = new MenuButton("SETTINGS");
		btnOptions.setOnMouseClicked(event -> {
			getChildren().add(menu1);

			TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25), menu0);
			trans.setToX(menu0.getTranslateX() - OFFSET);

			TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25), menu1);
			trans1.setToX(menu1.getTranslateX() - OFFSET);

			trans.play();
			trans1.play();

			trans.setOnFinished(evt -> {
				getChildren().remove(menu0);
			});

		});

		btnExit = new MenuButton("EXIT");
		btnExit.setOnMouseClicked(event -> {

			// Tudor - Close the audio engine
			AudioMaster.stopMusic();
			AudioMaster.cleanUp();
			
			System.exit(0);
		});

		btnBack = new MenuButton("BACK");
		btnBack.setOnMouseClicked(event -> {
			getChildren().add(menu0);

			TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25), menu1);
			trans.setToX(menu1.getTranslateX() + OFFSET);

			TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25), menu0);
			trans1.setToX(menu1.getTranslateX());

			trans.play();
			trans1.play();

			trans.setOnFinished(evt -> {
				getChildren().remove(menu1);
			});
		});

		btnBack2 = new MenuButton("BACK");
		btnBack2.setOnMouseClicked(event -> {
			getChildren().add(menu1);

			TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25), menu2);
			trans.setToX(menu2.getTranslateX() + OFFSET);

			TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25), menu1);
			trans1.setToX(menu2.getTranslateX());

			trans.play();
			trans1.play();

			trans.setOnFinished(evt -> {
				getChildren().remove(menu2);
			});
		});

		btnBack3 = new MenuButton("BACK");
		btnBack3.setOnMouseClicked(event -> {
			getChildren().add(menu1);

			TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25), menu3);
			trans.setToX(menu3.getTranslateX() + OFFSET);

			TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25), menu1);
			trans1.setToX(menu3.getTranslateX());

			trans.play();
			trans1.play();

			trans.setOnFinished(evt -> {
				getChildren().remove(menu3);
			});
		});
		
		btnBack4 = new MenuButton("BACK");
		btnBack4.setOnMouseClicked(event -> {
			getChildren().add(menu0);

			TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25), menu4);
			trans.setToX(menu4.getTranslateX() + OFFSET);

			TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25), menu0);
			trans1.setToX(menu4.getTranslateX());

			trans.play();
			trans1.play();

			trans.setOnFinished(evt -> {
				getChildren().remove(menu4);
			});
		});
		
		btnBack5 = new MenuButton("BACK");
		btnBack5.setOnMouseClicked(event -> {
			getChildren().add(menu0);

			TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25), menu5);
			trans.setToX(menu5.getTranslateX() + OFFSET);

			TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25), menu0);
			trans1.setToX(menu5.getTranslateX());

			trans.play();
			trans1.play();

			trans.setOnFinished(evt -> {
				getChildren().remove(menu5);
			});
		});

		btnSound = new MenuButton("SOUND EFFECTS");
		btnSound.setOnMouseClicked(event -> {
			getChildren().add(menu2);

			TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25), menu1);
			trans.setToX(menu1.getTranslateX() - OFFSET);

			TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25), menu2);
			trans1.setToX(menu2.getTranslateX() - OFFSET);

			trans.play();
			trans1.play();

			trans.setOnFinished(evt -> {
				getChildren().remove(menu1);
			});

		});

		btnMusic = new MenuButton("MUSIC");
		btnMusic.setOnMouseClicked(event -> {
			getChildren().add(menu3);

			TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25), menu1);
			trans.setToX(menu1.getTranslateX() - OFFSET);

			TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25), menu3);
			trans1.setToX(menu3.getTranslateX() - OFFSET);

			trans.play();
			trans1.play();

			trans.setOnFinished(evt -> {
				getChildren().remove(menu1);
			});

		});

		soundSlider = new SoundSlider();
		musicSlider = new MusicSlider();
		
		HostGameRoom hostGR = new HostGameRoom();
		CreateGameRoom createGR = new CreateGameRoom();
		
		menu4.setAlignment(Pos.CENTER);
		menu5.setAlignment(Pos.CENTER);
		
		VBox box1Multi = new VBox(10);
		box1Multi.setPadding(new Insets(0, 0, 5, 0));
		
		VBox box2Multi = new VBox(10);
		box2Multi.setPadding(new Insets(0, 0, 5, 0));
		
		VBox box3Multi = new VBox(10);
		box3Multi.setPadding(new Insets(0, 0, 5, 0));
		
		VBox box1Single = new VBox(10);
		box1Single.setPadding(new Insets(0, 0, 5, 0));
		
		VBox box2Single = new VBox(10);
		box2Single.setPadding(new Insets(0, 0, 5, 0));
		
		VBox box3Single = new VBox(10);
		box3Single.setPadding(new Insets(0, 0, 5, 0));
		
		VBox box4Multi = new VBox(10);
		box4Multi.setPadding(new Insets(0, 0, 5, 0));
		
		VBox box4Single = new VBox(10);
		box4Single.setPadding(new Insets(0, 0, 5, 0));
		
		TextStyle usernameM = new TextStyle("USERNAME", 20);
		Text usernameTextM = usernameM.getTextStyled();
		
		TextStyle usernameS = new TextStyle("USERNAME", 20);
		Text usernameTextS = usernameS.getTextStyled();
		
		TextField usernameInputSingle = new TextField();
		TextField usernameInputMulti = new TextField();
		
		TextStyle portM = new TextStyle("PORT", 20);
		Text portTextM = portM.getTextStyled();
		
		TextStyle portS = new TextStyle("PORT", 20);
		Text portTextS = portS.getTextStyled();
		
		TextField portInputSingle = new TextField();
		TextField portInputMulti = new TextField();
		
		TextStyle machineM = new TextStyle("MACHINE NAME", 20);
		Text machineTextM = machineM.getTextStyled();
		
		TextStyle machineS = new TextStyle("MACHINE NAME", 20);
		Text machineTextS = machineS.getTextStyled();
		
		TextField machineInputSingle = new TextField();
		TextField machineInputMulti = new TextField();
		
		MenuButton startServerSingle = new MenuButton("START A SERVER");
		MenuButton startServerMulti = new MenuButton("START A SERVER");
		
		MenuButton connectSingle = new MenuButton("CONNECT TO THE LOBBY");
		connectSingle.setOnMouseClicked(event->{
			
			String usr = usernameInputSingle.getText();
			int portNo = Integer.valueOf(portInputSingle.getText());
			String machineName = machineInputSingle.getText();
			
			Client client = new Client(usr, portNo, machineName);
			
			if(client.serverOn) {
				client.start();
				
				getChildren().add(menu8);

				TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25), menu5);
				trans.setToX(menu5.getTranslateX() - OFFSET);

				TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25), menu8);
				trans1.setToX(menu8.getTranslateX() - OFFSET);

				trans.play();
				trans1.play();

				trans.setOnFinished(evt -> {
					getChildren().remove(menu5);
				});
				
			} else {
			
				if (box4Single.getChildren().size() > 0) {
						
					box4Single.getChildren().remove(0);
				}
					
				box4Single.getChildren().add(startServerSingle);
				
			}
		});
			
		startServerSingle.setOnMouseClicked(event-> {
			
			Lobby serverLobby = new Lobby (4444);
			ServerComm server = new ServerComm(4444, serverLobby);
			server.start();
			
			String usr = usernameInputSingle.getText();
			int portNo = Integer.valueOf(portInputSingle.getText());
			String machineName = machineInputSingle.getText();
			
			Client client = new Client(usr, portNo, machineName);
			client.start();
			
			getChildren().add(menu8);

			TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25), menu5);
			trans.setToX(menu5.getTranslateX() - OFFSET);

			TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25), menu8);
			trans1.setToX(menu8.getTranslateX() - OFFSET);

			trans.play();
			trans1.play();

			trans.setOnFinished(evt -> {
				getChildren().remove(menu5);
			});
			
		});
		
		
		MenuButton connectMulti = new MenuButton("CONNECT TO THE LOBBY");
		connectMulti.setOnMouseClicked(event->{
			
			usr =  usernameInputMulti.getText();
			int portNo = Integer.valueOf(portInputMulti.getText());
			String machineName = machineInputMulti.getText();
			
			Client client = new Client(usr, portNo, machineName);
			
			if(client.serverOn) {
				
				client.start();
				
				getChildren().add(menu6);

				TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25), menu4);
				trans.setToX(menu4.getTranslateX() - OFFSET);

				TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25), menu6);
				trans1.setToX(menu6.getTranslateX() - OFFSET);

				trans.play();
				trans1.play();

				trans.setOnFinished(evt -> {
					getChildren().remove(menu4);
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
			
			String usr = usernameInputMulti.getText();
			int portNo = Integer.valueOf(portInputMulti.getText());
			String machineName = machineInputMulti.getText();
			
			Client client = new Client(usr, portNo, machineName);
			client.start();
			
			getChildren().add(menu6);

			TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25), menu4);
			trans.setToX(menu4.getTranslateX() - OFFSET);

			TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25), menu6);
			trans1.setToX(menu6.getTranslateX() - OFFSET);

			trans.play();
			trans1.play();

			trans.setOnFinished(evt -> {
				getChildren().remove(menu4);
			});
			
		});
		
		box1Multi.setAlignment(Pos.CENTER);
		box1Multi.getChildren().addAll(usernameTextM, usernameInputMulti);
		
		box2Multi.setAlignment(Pos.CENTER);
		box2Multi.getChildren().addAll(portTextM, portInputMulti);
		
		box3Multi.setAlignment(Pos.CENTER);
		box3Multi.getChildren().addAll(machineTextM, machineInputMulti);
		
		box1Single.setAlignment(Pos.CENTER);
		box1Single.getChildren().addAll(usernameTextS, usernameInputSingle);
		
		box2Single.setAlignment(Pos.CENTER);
		box2Single.getChildren().addAll(portTextS, portInputSingle);
		
		box3Single.setAlignment(Pos.CENTER);
		box3Single.getChildren().addAll(machineTextS, machineInputSingle);
		
		box4Single.setAlignment(Pos.CENTER);
		box4Single.getChildren().add(connectSingle);
		
		box4Multi.setAlignment(Pos.CENTER);
		box4Multi.getChildren().add(connectMulti);
		
		
		//connecting multiplayer options
		MenuButton joinGameRoom = new MenuButton("JOIN A GAME ROOM");
		
		MenuButton hostGameRoom = new MenuButton("HOST A GAME ROOM");
		hostGameRoom.setOnMouseClicked(eventHost -> {
			
			getChildren().add(menu7);

			TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25), menu6);
			trans.setToX(menu6.getTranslateX() - OFFSET);

			TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25), menu7);
			trans1.setToX(menu7.getTranslateX() - OFFSET);

			trans.play();
			trans1.play();

			trans.setOnFinished(evt -> {
				getChildren().remove(menu6);
			});
			
		});
		
		// main menu
		menu0.getChildren().addAll(btnPlayGame, btnPlayAI, btnOptions, btnExit);
		// options menu
		menu1.getChildren().addAll(btnSound, btnMusic, btnBack);
		// sound effects
		menu2.getChildren().addAll(soundSlider, btnBack2);
		// background music
		menu3.getChildren().addAll(musicSlider, btnBack3);
		//connect multiplayer
		menu4.getChildren().addAll(box1Multi, box2Multi, box3Multi, box4Multi, btnBack4);
		//connect single player
		menu5.getChildren().addAll(box1Single, box2Single, box3Single, box4Single, btnBack5);
		//connect multiplayer options
		menu6.getChildren().addAll(joinGameRoom, hostGameRoom);
		//host a game room by multiplayer
		menu7.getChildren().add(hostGR);
		//host a game room by single player
		menu8.getChildren().add(createGR);

		getChildren().addAll(menu0);

		//trying to improve the speed of TranslateTransition
		this.setCache(true);
		this.setCacheHint(CacheHint.SPEED);
	}

}
