package trackDesign;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JComponent;

public class TrackComponent extends JComponent {
	
  private static final long serialVersionUID = 1L;
  
  private ArrayList<TrackPoint> track;
  private int scale;
  
  public TrackComponent() {
    this(TrackMaker.makeTrack(10, 20, 15, 3, 50, 20), 1);
  }
  
  public TrackComponent(int scale) {
    this(TrackMaker.makeTrack(10, 20, 15, 3, 50, 20), scale);
  }
	
	public TrackComponent(ArrayList<TrackPoint> track, int scale) {
		super();
		this.track = track;
    this.scale = scale;
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		for(int i = 0; i < track.size(); i++) {
		  TrackPoint s = track.get(i);
		  TrackPoint f = track.get((i+1) % track.size());
		  
			s.draw(g2);
			g2.drawLine(s.getX() * scale, s.getY() * scale, f.getX() * scale, f.getY() * scale);
			g2.drawString(Integer.toString(i), s.getX() * scale, s.getY() * scale);
		}
	}
}
