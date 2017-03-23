
package userInterface;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

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
  public static ImageView imgView;
  public static Rectangle bg;

  public static ArrayList<Thread> allThreads = new ArrayList<Thread>();

  /**
   * Method that initializes the primary stage and the current scene.
   * 
   * @param primaryStage
   *          The primary JavaFX stage.
   */
  public void start(Stage primaryStage) {

    // Tudor - start the audio engine
    AudioMaster.init();

    root = new Pane();
    root.setPrefSize(1000, 600);

    this.primaryStage = primaryStage;

    // get file from path
    try {
      InputStream is = Files.newInputStream(Paths.get("src/resources/img/hover-racer.jpg"));
      Image background = new Image(is);
      is.close();

      imgView = new ImageView(background);
      imgView.setFitWidth(1000);
      imgView.setFitHeight(600);
    } catch (Exception e) {
      e.printStackTrace();
    }

    try {
      gameMenu = new GameMenu();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    gameMenu.setVisible(true);

    bg = new Rectangle(1000, 600);
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
        for (Thread t : allThreads) {
          if (t.isAlive()) t.interrupt();
        }
        System.exit(0);
      }
    });

    // Tudor - start the music
    AudioMaster.playMusic();

  }

  public static ObservableList<Node> getMenuChildren() {
    return root.getChildren();
  }

  public static void hideScene() {

    primaryStage.hide();

  }

  public static void reloadScene() throws IOException {

    AudioMaster.init();
    AudioMaster.playMusic();
    for (int i = 0; i < root.getChildren().size(); i++) {
      if (!root.getChildren().get(i).equals(imgView) && !root.getChildren().get(i).equals(bg)) {
        root.getChildren().remove(i);
      }
    }

    GameMenu newMenu = new GameMenu();
    root.getChildren().add(newMenu);

    primaryStage.show();
  }

  public static void main(String[] args) {

    launch(args);

  }

}
