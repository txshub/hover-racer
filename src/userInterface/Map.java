package userInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import trackDesign.SeedTrack;
import trackDesign.TrackMaker;
import trackDesign.TrackPoint;

public class Map extends Application{

	private ArrayList<TrackPoint> track;
	Pane root;
	
	public void start(Stage primaryStage) throws Exception {
		
		root = new Pane();
		root.setPrefSize(400, 300);
		
		Text text = new Text("hello");
		try {
			Font f = Font.loadFont(new FileInputStream(new File("res/fonts/War is Over.ttf")), 20);
			text.setFont(f);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		root.getChildren().add(text);
		
		long seed= 10 ;
		SeedTrack st = TrackMaker.makeTrack(seed, 20, 30, 1, 70, 40, 4, 4);
		track = st.getTrack();
		
		for(int i=0; i< track.size(); i++){
			
			Line line = new Line ();
			line.setStartX((((int)track.get(i).getX()*2)/2)+ seed);
			line.setStartY((((int)track.get(i).getY()*2)/2)+ seed);
			line.setEndX((((int)track.get((i+1)%track.size()).getX()*2)/2) + seed);
			line.setEndY((((int)track.get((i+1)%track.size()).getY()*2)/2) + seed);
			
			root.getChildren().add(line);
		}
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();	
		
		
	}
	
	public static void main(String[] args) {

		launch(args);

	}

}
