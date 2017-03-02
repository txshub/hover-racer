package trackDesign;

import javax.swing.JFrame;

/**
 * Graphics test to ensure proper track generation
 * 
 * @author simon
 *
 */
public class TestGraphics {

  /**
   * Creates a JFrame and displays a TrackComponent on it
   * 
   * @param args
   *          Any input arguments to be ignored
   */
  public static void main(String[] args) {
    JFrame frame = new JFrame();
    frame.setSize(600, 600);
    frame.setTitle("Track Making Graphics Test");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    TrackComponent track = new TrackComponent();
    frame.add(track);
    frame.setVisible(true);
  }
}
