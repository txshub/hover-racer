
package userInterface;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import audioEngine.AudioMaster;
import javafx.application.Application;
import javafx.event.EventHandler;
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
 * @author Andreea Gheorghe
 * Class for launching the User Interface.
 *
 */
public class MainMenu extends Application {

  public GameMenu gameMenu;
  public Scene scene;

  /**
   * Method that initializes the primary stage and the current scene.
   * @param primaryStage The primary JavaFX stage.
   */
  public void start(Stage primaryStage) throws Exception {

    // Tudor - start the audio engine
    AudioMaster.init();

    Pane root = new Pane();
    root.setPrefSize(1000, 600);

    // get file from path
    InputStream is = Files.newInputStream(Paths.get("src/resources/img/hover-racerNew.jpg"));
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
  
  public static void main(String[] args) {

    launch(args);

  }

}
