
package userInterface;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import audioEngine.AudioMaster;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * 
 * @author Andreea Gheorghe Class for launching the User Interface.
 *
 */
public class MainMenu extends Application {

	public GameMenu gameMenu;
	public Scene scene;
	public static Pane root;
	public static Stage primaryStage;
	/**
	 * Method that initializes the primary stage and the current scene.
	 * 
	 * @param primaryStage
	 *            The primary JavaFX stage.
	 */
	public void start(Stage primaryStage) throws Exception {

		// Tudor - start the audio engine
		AudioMaster.init();
		
		root = new Pane();
		root.setPrefSize(1000, 600);
		
		this.primaryStage = primaryStage;

		// get file from path
		InputStream is = Files.newInputStream(Paths.get("src/resources/img/hover-racer.jpg"));
		Image background = new Image(is);
		is.close();

		ImageView imgView = new ImageView(background);
		imgView.setFitWidth(1000);
		imgView.setFitHeight(600);

		gameMenu = new GameMenu();
		gameMenu.setVisible(true);

		Rectangle bg = new Rectangle(1000, 600);
		bg.setOpacity(0.5);
		bg.setFill(Color.BLACK);

		root.getChildren().addAll(imgView, bg, gameMenu);

		scene = new Scene(root);

		primaryStage.setResizable(false);
		primaryStage.sizeToScene();

		primaryStage.setScene(scene);
		primaryStage.show();
		
		Platform.setImplicitExit(false);

		// Handle closing the window by pressing 'X'
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			public void handle(WindowEvent we) {
				AudioMaster.stopMusic();
				AudioMaster.cleanUp();
				System.exit(0);
			}
		});

		// Tudor - start the music
		AudioMaster.playMusic();

	}
	
	public static ObservableList<Node> getMenuChildren(){
		return root.getChildren();
	}
	
	public static void hideScene(){
		
		primaryStage.hide();
		
	}

	public static void reloadScene(){
		
		root.getChildren().clear();
		GameMenu newMenu;
		try {
			newMenu = new GameMenu();
			root.getChildren().add(newMenu);
			root.setVisible(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		primaryStage.show();
		

	}
	public static void main(String[] args) {

		launch(args);

	}

}
