package trackDesign;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JComponent;

/**
 * Component to hold a track for displaying
 * 
 * @author simon
 *
 */
public class TrackComponent extends JComponent {

  private static final long serialVersionUID = 1L;
  private ArrayList<TrackPoint> track; // The arraylist of points that make up
                                       // the track
  private ArrayList<TrackPoint> track2;
  private long seed;

  /**
   * Testing method to input a predefined track
   * 
   * @param track
   */
  public TrackComponent(ArrayList<TrackPoint> track) {
    super();
    this.track = track;
    this.seed = -1;
  }

  /**
   * Creates the TrackComponent object
   */
  public TrackComponent() {
    super();
    SeedTrack st = TrackMaker.makeTrack(100, 1000, 10, 50, 0, 50, 10); // Generate
                                                                       // a
                                                                       // random
                                                                       // track
    track = st.getTrack();
    seed = st.getSeed();
    System.out.println("TrackComponent.TrackComponent(), " + seed);
    track2 = TrackMaker.makeTrack(seed, 10, 20, 30, 1, 30, 40, 1).getTrack();
  }

  /**
   * Return the track represented by this TrackComponent
   * 
   * @return the ArrayList of TrackPoints
   */
  public ArrayList<TrackPoint> getTrack() {
    return track;
  }

  /**
   * Paints the track onto the component
   * 
   * @param g
   *          The graphics object
   */
  public void paintComponent(Graphics g) {

    Graphics2D g2 = (Graphics2D) g;
    g2.setColor(Color.BLUE);
    for (int i = 0; i < track.size(); i++) {
      // track.get(i).draw(g2);
      g2.drawLine((int) track.get(i).getX() * 2, (int) track.get(i).getY() * 2,
          (int) track.get((i + 1) % track.size()).getX() * 2,
          (int) track.get((i + 1) % track.size()).getY() * 2);
    }
    g2.setColor(Color.RED);
    for (int i = 0; i < track2.size(); i++) {
      // g2.drawLine((int)track2.get(i).getX()*2, (int)track2.get(i).getY()*2,
      // (int)track2.get((i+1)%track2.size()).getX()*2,
      // (int)track2.get((i+1)%track2.size()).getY()*2);
    }
  }
}
