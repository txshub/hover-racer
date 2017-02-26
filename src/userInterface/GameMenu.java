package userInterface;

import audioEngine.AudioMaster;
import clientComms.Client;
import game.MainGameLoop;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * 
 * @author Andreea Gheorghe
 *
 */

public class GameMenu extends Parent {

	VBox menu0, menu1, menu2, menu3, menu4, menu5, menu6;
	MenuButton btnPlayGame, btnPlayAI, btnOptions, btnSound, btnMusic, btnExit, btnBack, btnBack2, btnBack3, btnBack4, btnBack5, btnBack6;
	SoundSlider soundSlider;
	MusicSlider musicSlider;
	final int OFFSET = 600;

	public GameMenu() {

		menu0 = new VBox(10);
		menu1 = new VBox(10);
		menu2 = new VBox(10);
		menu3 = new VBox(10);
		menu4 = new VBox(20);
		menu5 = new VBox(20);
		menu6 = new VBox(10);

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
		
		
		btnPlayGame = new MenuButton("MULTIPLAYER");
		btnPlayGame.setOnMouseClicked(event -> {
			// enter the server lobby in the actual game
			getChildren().add(menu4);
			
			TranslateTransition trans = new TranslateTransition(Duration.seconds(0.15), menu0);
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
			
			TranslateTransition trans = new TranslateTransition(Duration.seconds(0.15), menu0);
			trans.setToX(menu0.getTranslateX() - OFFSET);
			
			TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25), menu5);
			trans1.setToX(menu5.getTranslateX() - OFFSET);

			trans.play();
			trans1.play();

			trans.setOnFinished(evt -> {
				getChildren().remove(menu0);
			});

			
			//AudioMaster.stopMusic();
			//new MainGameLoop().main();
			//((Node) event.getSource()).getScene().getWindow().hide();
		});

		btnOptions = new MenuButton("SETTINGS");
		btnOptions.setOnMouseClicked(event -> {
			getChildren().add(menu1);

			TranslateTransition trans = new TranslateTransition(Duration.seconds(0.15), menu0);
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

			TranslateTransition trans = new TranslateTransition(Duration.seconds(0.15), menu1);
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

			TranslateTransition trans = new TranslateTransition(Duration.seconds(0.15), menu2);
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

			TranslateTransition trans = new TranslateTransition(Duration.seconds(0.15), menu3);
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

			TranslateTransition trans = new TranslateTransition(Duration.seconds(0.15), menu4);
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

			TranslateTransition trans = new TranslateTransition(Duration.seconds(0.15), menu5);
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

			TranslateTransition trans = new TranslateTransition(Duration.seconds(0.15), menu1);
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

			TranslateTransition trans = new TranslateTransition(Duration.seconds(0.15), menu1);
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
		
		menu4.setAlignment(Pos.CENTER);
		menu5.setAlignment(Pos.CENTER);
		
		VBox box1 = new VBox(10);
		box1.setPadding(new Insets(0, 0, 5, 0));
		
		VBox box2 = new VBox(10);
		box2.setPadding(new Insets(0, 0, 5, 0));
		
		VBox box3 = new VBox(10);
		box3.setPadding(new Insets(0, 0, 5, 0));
		
		VBox box1a = new VBox(10);
		box1.setPadding(new Insets(0, 0, 5, 0));
		
		VBox box2a = new VBox(10);
		box2.setPadding(new Insets(0, 0, 5, 0));
		
		VBox box3a = new VBox(10);
		box3.setPadding(new Insets(0, 0, 5, 0));
		
		VBox box4Multi = new VBox(10);
		box4Multi.setPadding(new Insets(0, 0, 5, 0));
		
		VBox box4Single = new VBox(10);
		box4Single.setPadding(new Insets(0, 0, 5, 0));
		
		TextStyle username = new TextStyle("USERNAME", 20);
		Text usernameText = username.getTextStyled();
		
		TextField usernameInput = new TextField();
		
		TextStyle port = new TextStyle("PORT", 20);
		Text portText = port.getTextStyled();
		
		TextField portInput = new TextField();
		
		TextStyle machine = new TextStyle("MACHINE NAME", 20);
		Text machineText = machine.getTextStyled();
		
		TextField machineInput = new TextField();
		
		MenuButton connectSingle = new MenuButton("CONNECT TO THE LOBBY");
		connectSingle.setOnMouseClicked(event->{
			
			String usr = usernameInput.getText();
			int portNo = Integer.valueOf(portInput.getText());
			String machineName = machineInput.getText();
			
			Client client = new Client(usr, portNo, machineName, this);
			client.start();
			
		});
		
		MenuButton connectMulti = new MenuButton("CONNECT TO THE LOBBY");
		connectMulti.setOnMouseClicked(event->{
			
			String usr = usernameInput.getText();
			int portNo = Integer.valueOf(portInput.getText());
			String machineName = machineInput.getText();
			
			Client client = new Client(usr, portNo, machineName, this);
			client.start();
		
			getChildren().add(menu6);

			TranslateTransition trans = new TranslateTransition(Duration.seconds(0.15), menu4);
			trans.setToX(menu4.getTranslateX() - OFFSET);

			TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.25), menu6);
			trans1.setToX(menu6.getTranslateX() - OFFSET);

			trans.play();
			trans1.play();

			trans.setOnFinished(evt -> {
				getChildren().remove(menu4);
			});
	
		});
		
		box1.setAlignment(Pos.CENTER);
		box1.getChildren().addAll(usernameText, usernameInput);
		
		box2.setAlignment(Pos.CENTER);
		box2.getChildren().addAll(portText, portInput);
		
		box3.setAlignment(Pos.CENTER);
		box3.getChildren().addAll(machineText, machineInput);
		
		box1a.setAlignment(Pos.CENTER);
		box1a.getChildren().addAll(usernameText, usernameInput);
		
		box2a.setAlignment(Pos.CENTER);
		box2a.getChildren().addAll(portText, portInput);
		
		box3a.setAlignment(Pos.CENTER);
		box3a.getChildren().addAll(machineText, machineInput);
		
		box4Single.setAlignment(Pos.CENTER);
		box4Single.getChildren().add(connectSingle);
		
		box4Multi.setAlignment(Pos.CENTER);
		box4Multi.getChildren().add(connectMulti);
		
		//connecting multiplayer options
		MenuButton joinGameRoom = new MenuButton("JOIN A GAME ROOM");
		MenuButton hostGameRoom = new MenuButton("HOST A GAME ROOM");
		
		// main menu
		menu0.getChildren().addAll(btnPlayGame, btnPlayAI, btnOptions, btnExit);
		// options menu
		menu1.getChildren().addAll(btnSound, btnMusic, btnBack);
		// sound effects
		menu2.getChildren().addAll(soundSlider, btnBack2);
		// background music
		menu3.getChildren().addAll(musicSlider, btnBack3);
		//connect multiplayer
		menu4.getChildren().addAll(box1a, box2a, box3a, box4Multi, btnBack4);
		//connect single player
		menu5.getChildren().addAll(box1, box2, box3, box4Single,btnBack5);
		//connect multiplayer options
		menu6.getChildren().addAll(joinGameRoom, hostGameRoom);
		
		// game menu background
		Rectangle bg2 = new Rectangle(800, 500);
		bg2.setFill(Color.BLACK);
		bg2.setOpacity(0.2);

		getChildren().addAll(bg2, menu0);

	}

	public static void serverOff() {
		// TODO Auto-generated method stub
		
	}

}
