package trackDesign;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JComponent;

public class TrackComponent extends JComponent {
	
	private ArrayList<TrackPoint> track;
	
	public TrackComponent() {
		super();
		track = TrackMaker.makeTrack(10, 20, 15, 3, 70, 40, 4);
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(Color.BLUE);
		for(int i = 0; i < track.size(); i++) {
			track.get(i).draw(g2);
			g2.drawLine((int)track.get(i).getX()*2, (int)track.get(i).getY()*2, (int)track.get((i+1)%track.size()).getX()*2, (int)track.get((i+1)%track.size()).getY()*2);
		}
	}
}
