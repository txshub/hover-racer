package userInterface;

import audioEngine.AudioMaster;
import game.Main;
import gameEngine.engineTester.MainGameLoop;
import javafx.animation.TranslateTransition;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class GameMenu extends Parent {
	
	VBox menu0, menu1, menu2,menu3;
	MenuButton btnPlayGame, btnPlayAI, btnOptions, btnSound, btnMusic, btnExit, btnBack, btnBack2, btnBack3;
	SoundSlider soundSlider;
	MusicSlider musicSlider;
	final int OFFSET = 600;
	
	public GameMenu(){
		
		menu0 = new VBox(10);
		menu1 = new VBox(10);
		menu2 = new VBox(10);
		menu3 = new VBox(10);
		
		menu0.setTranslateX(100);
		menu0.setTranslateY(160);
		
		menu1.setTranslateX(700);
		menu1.setTranslateY(160);
		
		menu2.setTranslateX(700);
		menu2.setTranslateY(160);
		
		menu3.setTranslateX(700);
		menu3.setTranslateY(160);
			
		btnPlayGame = new MenuButton("PLAY AGAINST RACERS");
		btnPlayGame.setOnMouseClicked(event -> {
			//enter the server lobby in the actual game
			Main.main(null);
		});
		
		btnPlayAI = new MenuButton("PLAY AGAINST AI");
		btnPlayAI.setOnMouseClicked(event -> {
			//enter the game with the computer-controlled players
			MainGameLoop.main(null);
		});
		
		btnOptions = new MenuButton ("SETTINGS");
		btnOptions.setOnMouseClicked(event -> {
			getChildren().add(menu1);
			
			TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25),menu0);
			trans.setToX(menu0.getTranslateX() - OFFSET);
			
			TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.5),menu1);
			trans1.setToX(menu1.getTranslateX() - OFFSET);
			
			trans.play();
			trans1.play();
			
			trans.setOnFinished(evt -> {
				getChildren().remove(menu0);
			});
			
		});
		
		btnExit = new MenuButton ("EXIT");
		btnExit.setOnMouseClicked(event ->{
			
			// Tudor - Close the audio engine
			AudioMaster.stopMusic();
			AudioMaster.cleanUP();
			
			System.exit(0);
		});
		
		btnBack = new MenuButton ("BACK");
		btnBack.setOnMouseClicked(event -> {
			getChildren().add(menu0);
			
			TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25),menu1);
			trans.setToX(menu1.getTranslateX() + OFFSET);
			
			TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.5),menu0);
			trans1.setToX(menu1.getTranslateX());
			
			trans.play();
			trans1.play();
			
		    trans.setOnFinished(evt ->{
		    	getChildren().remove(menu1);
		    });
		});
		
		btnBack2 = new MenuButton ("BACK");
		btnBack2.setOnMouseClicked(event -> {
			getChildren().add(menu1);
			
			TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25),menu2);
			trans.setToX(menu2.getTranslateX() + OFFSET);
			
			TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.5),menu1);
			trans1.setToX(menu2.getTranslateX());
			
			trans.play();
			trans1.play();
			
		    trans.setOnFinished(evt ->{
		    	getChildren().remove(menu2);
		    });
		});
		
		btnBack3 = new MenuButton ("BACK");
		btnBack3.setOnMouseClicked(event -> {
			getChildren().add(menu1);
			
			TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25),menu3);
			trans.setToX(menu3.getTranslateX() + OFFSET);
			
			TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.5),menu1);
			trans1.setToX(menu3.getTranslateX());
			
			trans.play();
			trans1.play();
			
		    trans.setOnFinished(evt ->{
		    	getChildren().remove(menu3);
		    });
		});
		
		btnSound = new MenuButton("SOUND EFFECTS");
		btnSound.setOnMouseClicked(event -> {
			getChildren().add(menu2);
			
			TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25),menu1);
			trans.setToX(menu1.getTranslateX() - OFFSET);
			
			TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.5),menu2);
			trans1.setToX(menu2.getTranslateX()- OFFSET);
			
			trans.play();
			trans1.play();
			
			trans.setOnFinished(evt -> {
				getChildren().remove(menu1);
			});
			
		});
		
		
		btnMusic = new MenuButton("MUSIC");		
		btnMusic.setOnMouseClicked(event -> {
			getChildren().add(menu3);
			
			TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25),menu1);
			trans.setToX(menu1.getTranslateX() - OFFSET);
			
			TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.5),menu3);
			trans1.setToX(menu3.getTranslateX() - OFFSET);
			
			trans.play();
			trans1.play();
			
			trans.setOnFinished(evt -> {
				getChildren().remove(menu1);
			});
			
		});
		
		
		soundSlider = new SoundSlider();
		musicSlider = new MusicSlider();
		
		//main menu
		menu0.getChildren().addAll(btnPlayGame, btnPlayAI, btnOptions, btnExit);
		//options menu
		menu1.getChildren().addAll(btnSound, btnMusic, btnBack);
		//sound effects
		menu2.getChildren().addAll(soundSlider,btnBack2);
		//background music
		menu3.getChildren().addAll(musicSlider,btnBack3);
		
		//game menu background
		Rectangle bg2 = new Rectangle(550,412);
		bg2.setFill(Color.GREY);
		bg2.setOpacity(0.4);
		
		getChildren().addAll(bg2,menu0);
		 
	}

}
