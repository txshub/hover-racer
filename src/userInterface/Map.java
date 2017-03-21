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
 * @author Andreea Gheorghe Class that implements the dynamic track map. Will be
 *         used to preview the track when the user inputs a seed.
 *
 */
public class Map extends Canvas {

	private ArrayList<TrackPoint> track;
	private String seed;

	/**
	 * Constructor for the Map class that generates the track according to a
	 * given seed.
	 * 
	 * @param seed
	 *            The given seed that will generate the track.
	 */
	public Map(String seed) {

		this.seed = seed;

		this.setWidth(160);
		this.setHeight(160);

		GraphicsContext gc = this.getGraphicsContext2D();
		drawShapes(gc);

	}

	/**
	 * Method that draws the map.
	 * 
	 * @param gc
	 *            The GraphicsContext needed to display the elements.
	 */
	public void drawShapes(GraphicsContext gc) {

		SeedTrack st = TrackMaker.makeTrack(seed);
		track = st.getTrack();
		for(TrackPoint tp: track) {
			tp.mul(1f/20f);
		}

		// Go through each track point, connect them
		// Resize so that it will fit in the canvas size
		for (int i = 0; i < track.size(); i++) {

			double x1 = (((track.get(i).getX() * 2) / 3.3));
			double y1 = (((track.get(i).getY() * 2) / 3.3));
			double x2 = (((track.get((i + 1) % track.size()).getX() * 2) / 3.3));
			double y2 = (((track.get((i + 1) % track.size()).getY() * 2) / 3.3));

			gc.setStroke(Color.WHITE);
			gc.setLineWidth(2);
			gc.strokeLine(x1, y1, x2, y2);
		}

	}
}
