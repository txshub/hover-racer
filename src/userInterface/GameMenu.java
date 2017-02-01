package userInterface;

import gameEngine.engineTester.MainGameLoop;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class GameMenu extends Parent {
	
	
	public GameMenu(){
		
		VBox menu0 = new VBox(10);
		VBox menu1 = new VBox(10);
		VBox menu2 = new VBox(10);
		
		menu0.setTranslateX(100);
		menu0.setTranslateY(160);
		
		menu1.setTranslateX(700);
		menu1.setTranslateY(160);
		
		menu2.setTranslateX(700);
		menu2.setTranslateY(160);
		
		final int offset = 600;
		
		//menu1.setTranslateX(offset);
		
		MenuButton btnPlayGame = new MenuButton("PLAY AGAINST RACERS");
		btnPlayGame.setOnMouseClicked(event -> {
			//FadeTransition ft = new FadeTransition(Duration.seconds(0.5), this);
			//ft.setFromValue(1);
			//ft.setToValue(0);
			//ft.setOnFinished(evt -> {
			//	this.setVisible(true);
			//});
			//ft.play();
			MainGameLoop.main(null);
		});
		
		MenuButton btnPlayAI = new MenuButton("PLAY AGAINST AI");
		btnPlayAI.setOnMouseClicked(event -> {
			//FadeTransition ft = new FadeTransition(Duration.seconds(0.5), this);
			//ft.setFromValue(1);
			//ft.setToValue(0);
			//ft.setOnFinished(evt -> {
			//	this.setVisible(true);
			//});
			//ft.play();
			MainGameLoop.main(null);
		});
		
		MenuButton btnOptions = new MenuButton ("SETTINGS");
		btnOptions.setOnMouseClicked(event -> {
			getChildren().add(menu1);
			
			TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25),menu0);
			trans.setToX(menu0.getTranslateX()-offset);
			
			TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.5),menu1);
			trans1.setToX(menu1.getTranslateX()-offset);
			
			trans.play();
			trans1.play();
			
			trans.setOnFinished(evt -> {
				getChildren().remove(menu0);
			});
			
		});
		
		MenuButton btnExit = new MenuButton ("EXIT");
		btnExit.setOnMouseClicked(event ->{
			System.exit(0);
		});
		
		MenuButton btnBack = new MenuButton ("BACK");
		btnBack.setOnMouseClicked(event -> {
			getChildren().add(menu0);
			
			TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25),menu1);
			trans.setToX(menu1.getTranslateX()+offset);
			
			TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.5),menu0);
			trans1.setToX(menu1.getTranslateX());
			
			trans.play();
			trans1.play();
			
		    trans.setOnFinished(evt ->{
		    	getChildren().remove(menu1);
		    });
		});
		
		MenuButton btnBack2 = new MenuButton ("BACK");
		btnBack2.setOnMouseClicked(event -> {
			getChildren().add(menu1);
			
			TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25),menu2);
			trans.setToX(menu2.getTranslateX()+offset);
			
			TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.5),menu1);
			trans1.setToX(menu2.getTranslateX());
			
			trans.play();
			trans1.play();
			
		    trans.setOnFinished(evt ->{
		    	getChildren().remove(menu2);
		    });
		});
		
		MenuButton btnSound = new MenuButton("SOUND EFFECTS");
		
		btnSound.setOnMouseClicked(event -> {
			getChildren().add(menu2);
			
			TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25),menu1);
			trans.setToX(menu1.getTranslateX()-offset);
			
			TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.5),menu2);
			trans1.setToX(menu2.getTranslateX()-offset);
			
			trans.play();
			trans1.play();
			
			trans.setOnFinished(evt -> {
				getChildren().remove(menu1);
			});
			
		});
		
		
		MenuButton btnMusic = new MenuButton("MUSIC");
		
		btnMusic.setOnMouseClicked(event -> {
			getChildren().add(menu2);
			
			TranslateTransition trans = new TranslateTransition(Duration.seconds(0.25),menu1);
			trans.setToX(menu1.getTranslateX()-offset);
			
			TranslateTransition trans1 = new TranslateTransition(Duration.seconds(0.5),menu2);
			trans1.setToX(menu2.getTranslateX()-offset);
			
			trans.play();
			trans1.play();
			
			trans.setOnFinished(evt -> {
				getChildren().remove(menu1);
			});
			
		});
		
		
		ToggleButton btn1 = new ToggleButton();
		btn1.toggle();
		
		
		//main menu
		menu0.getChildren().addAll(btnPlayGame, btnPlayAI, btnOptions, btnExit);
		//options menu
		menu1.getChildren().addAll(btnSound, btnMusic, btnBack);
		//music and sound effects
		menu2.getChildren().addAll(btn1, btnBack2);
		
		//game menu background
		Rectangle bg = new Rectangle(550,412);
		bg.setFill(Color.GREY);
		bg.setOpacity(0.4);
		
		getChildren().addAll(bg,menu0);
		 

	}

}
