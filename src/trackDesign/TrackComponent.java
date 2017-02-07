package trackDesign;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JComponent;

/**
 * Component to hold a track for displaying
 * @author simon
 *
 */
public class TrackComponent extends JComponent {
	
	private ArrayList<TrackPoint> track; //The arraylist of points that make up the track
	
	/**
	 * Creates the TrackComponent object
	 */
	public TrackComponent() {
		super();
		track = TrackMaker.makeTrack(10, 20, 30, 1, 70, 40, 4); //Get a random track object
	}
	
	/**
	 * Paints the track onto the component
	 * @param g The graphics object
	 */
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(Color.BLUE);
		for(int i = 0; i < track.size(); i++) {
			//track.get(i).draw(g2);
			g2.drawLine((int)track.get(i).getX()*2, (int)track.get(i).getY()*2, (int)track.get((i+1)%track.size()).getX()*2, (int)track.get((i+1)%track.size()).getY()*2);
		}
	}
}
