package ai;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JComponent;

import trackDesign.TrackComponent;
import trackDesign.TrackPoint;

public class Visualisation extends JComponent {

  private static final long serialVersionUID = 1L;
  
  private ShipManager shipManager;
  private TrackComponent trackComponent;
  
  private int scale;
  
  private final int shipWidth = 20;
  private final int shipLength = 40;

  public Visualisation(ShipManager shipManager, ArrayList<TrackPoint> track, int scale) {
    super();
    this.shipManager = shipManager;
    trackComponent = new TrackComponent(track, scale);
    this.scale = scale;
  }
  
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;
    
    g2.setBackground(new Color(250, 250, 250));
    g2.clearRect(0, 0, getWidth(), getHeight());
    
    // Paint the track first
    trackComponent.paintComponent(g);
    
    // Loop through ships and draw each of them
    for (Ship s : shipManager.getShips()) {
      Rectangle r = new Rectangle(s.getX() * scale, s.getY() * scale, shipWidth, shipLength);
      
      g2.setColor(new Color(0, 0, 0));
      g2.rotate(Math.toRadians(s.getRot()), r.getCenterX(), r.getCenterY());
      g2.draw(r);
    }
  }

}
