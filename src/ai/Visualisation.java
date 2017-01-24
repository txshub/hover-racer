package ai;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JComponent;

import trackDesign.TrackComponent;

public class Visualisation extends JComponent {

  private static final long serialVersionUID = 1L;
  
  private ShipManager shipManager;
  private TrackComponent track;
  
  private final int shipWidth = 20;
  private final int shipLength = 40;

  public Visualisation(ShipManager shipManager) {
    super();
    this.shipManager = shipManager;
    track = new TrackComponent();
  }
  
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;
    
    g2.setBackground(new Color(250, 250, 250));
    g2.clearRect(0, 0, getWidth(), getHeight());
    
    // Paint the track first
    track.paintComponent(g);
    
    // Loop through ships and draw each of them
    for (Ship s : shipManager.getShips()) {
      Rectangle r = new Rectangle(s.getX(), s.getY(), shipWidth, shipLength);
      
      g2.setColor(new Color(0, 0, 0));
      g2.rotate(Math.toRadians(s.getRot()), r.getCenterX(), r.getCenterY());
      g2.draw(r);
    }
  }

}
