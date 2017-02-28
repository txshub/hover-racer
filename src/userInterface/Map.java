package userInterface;

import java.util.ArrayList;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
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

  // private Canvas canvas;

  public Map(long seed) {

    this.seed = seed;

    this.setWidth(200);
    this.setHeight(200);

    GraphicsContext gc = this.getGraphicsContext2D();
    drawShapes(gc);

  }

  public void drawShapes(GraphicsContext gc) {

    SeedTrack st = TrackMaker.makeTrack(seed, 20, 30, 1, 70, 40, 4, 4);
    track = st.getTrack();

    for (int i = 0; i < track.size(); i++) {

      double x1 = (((track.get(i).getX() * 2) / 4));
      double y1 = (((track.get(i).getY() * 2) / 4));
      double x2 = (((track.get((i + 1) % track.size()).getX() * 2) / 4));
      double y2 = (((track.get((i + 1) % track.size()).getY() * 2) / 4));

      gc.setStroke(Color.WHITE);
      gc.strokeLine(x1, y1, x2, y2);
    }

  }

}
