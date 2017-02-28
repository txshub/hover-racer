package userInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import trackDesign.SeedTrack;
import trackDesign.TrackMaker;
import trackDesign.TrackPoint;

/**
 * 
 * @author Andreea Gheorghe
 *
 */
public class Map extends Canvas {

	private ArrayList<TrackPoint> track;
	private long seed;
	
//	private Canvas canvas;
	
	public Map(long seed){
		
		this.seed= seed ;
		
		this.setWidth(200);
		this.setHeight(200);
		
		GraphicsContext gc = this.getGraphicsContext2D();
        drawShapes(gc);
        
		
	}
	
	public void drawShapes(GraphicsContext gc) {
		
		SeedTrack st = TrackMaker.makeTrack(seed);
		track = st.getTrack();
		
		for(int i=0; i< track.size(); i++){
			
			double x1 = (((track.get(i).getX()*2)/4));
			double y1 =(((track.get(i).getY()*2)/4));
			double x2 = (((track.get((i+1)%track.size()).getX()*2)/4));
			double y2 = (((track.get((i+1)%track.size()).getY()*2)/4));
			
			gc.setStroke(Color.WHITE);
			gc.setLineWidth(1.5);
			gc.strokeLine(x1,y1,x2,y2);
		}
		
	}
}
