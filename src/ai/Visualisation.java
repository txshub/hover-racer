package ai;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JComponent;

import trackDesign.TrackPoint;

/**
 * 
 * @author Reece Bennett
 *
 */
public class Visualisation extends JComponent {

  private static final long serialVersionUID = 1L;

  private TestAI ship;
  private ArrayList<TrackPoint> track;

  private int scale;

  // Drawing variables
  private final int shipWidth = 5;
  private final int shipLength = 10;
  private final int pointSize = 6;
  private final int xOffset = 20;
  private final int yOffset = 50;

  public Visualisation(TestAI ship, ArrayList<TrackPoint> track, int scale) {
    super();
    this.ship = ship;
    this.track = track;
    this.scale = scale;
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;

    g2.setBackground(new Color(250, 250, 250));
    g2.clearRect(0, 0, getWidth(), getHeight());
    
    g2.translate(xOffset, yOffset);

    // Paint the track first
    for (int i = 0; i < track.size(); i++) {
      TrackPoint tp = track.get(i);

      if (ship.getNextPointIndex() == i) {
        g2.setColor(new Color(0, 255, 50));
      } else {
        g2.setColor(new Color(255, 0, 0));
      }
      g2.fillOval((int) tp.x * scale - pointSize / 2, (int) tp.y * scale - pointSize / 2, pointSize,
          pointSize);

      g2.setColor(new Color(0, 50, 255));
      if (i > 0) {
        TrackPoint lp = track.get(i - 1);
        g2.drawLine((int) lp.x * scale, (int) lp.y * scale, (int) tp.x * scale, (int) tp.y * scale);
      }
    }

    // Loop through ships and draw each of them
    Rectangle r = new Rectangle((int) (ship.getPos().x - shipWidth / 2) * scale,
        (int) (ship.getPos().y - shipLength / 2) * scale, shipWidth * scale, shipLength * scale);

    AffineTransform trans = g2.getTransform();

    g2.setColor(new Color(0, 0, 0));
    g2.rotate(Math.toRadians(ship.getRot()), r.getCenterX(), r.getCenterY());
    g2.draw(r);
    g2.fillRect((int) (ship.getPos().x - shipWidth / 2) * scale,
        (int) (ship.getPos().y - shipLength / 2) * scale, shipWidth * scale, 10);

    g2.setTransform(trans);
    
    g2.translate(-xOffset, -yOffset);

    DecimalFormat df = new DecimalFormat("#.####");
    g2.drawString("ax: " + df.format(ship.getAcl().x) + " ay: " + df.format(ship.getAcl().y) + " vx: "
        + df.format(ship.getVel().x) + " vy: " + df.format(ship.getVel().y) + " px: "
        + df.format(ship.getPos().x) + " py: " + df.format(ship.getPos().y), 10, 10);
  }

}
