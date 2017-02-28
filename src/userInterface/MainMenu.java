
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
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * 
 * @author Andreea Gheorghe
 *
 */
public class MainMenu extends Application {

  private GameMenu gameMenu;

  @Override
  public void start(Stage primaryStage) throws Exception {

    // Tudor - start the audio engine
    AudioMaster.init();

    Pane root = new Pane();
    root.setPrefSize(800, 500);

    // get file from path
    InputStream is = Files.newInputStream(Paths.get("res/img/hover-racer.jpg"));
    Image background = new Image(is);
    is.close();

    ImageView imgView = new ImageView(background);
    imgView.setFitWidth(800);
    imgView.setFitHeight(500);

    gameMenu = new GameMenu();
    gameMenu.setVisible(true);

    root.getChildren().addAll(imgView, gameMenu);

    // create a scene
    Scene scene = new Scene(root);

    primaryStage.setResizable(false);
    primaryStage.sizeToScene();

    primaryStage.setScene(scene);
    primaryStage.show();

    primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

      public void handle(WindowEvent we) {
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
