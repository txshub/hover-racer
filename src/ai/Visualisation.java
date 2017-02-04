package ai;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.text.DecimalFormat;

import javax.swing.JComponent;

import trackDesign.TrackComponent;

public class Visualisation extends JComponent {

  private static final long serialVersionUID = 1L;
  
  private ShipManager shipManager;
  private TrackComponent trackComponent;
  
  private int scale;
  
  private final int shipWidth = 10;
  private final int shipLength = 20;

  public Visualisation(ShipManager shipManager, TrackComponent trackComponent, int scale) {
    super();
    this.shipManager = shipManager;
    this.trackComponent = trackComponent;
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
      Rectangle r = new Rectangle((int) (s.getPos().x-shipWidth/2) * scale, (int) (s.getPos().y-shipLength/2) * scale, shipWidth*scale, shipLength*scale);
      
      AffineTransform trans = g2.getTransform();
      
      g2.setColor(new Color(0, 0, 0));
      g2.rotate(Math.toRadians(s.getRot()), r.getCenterX(), r.getCenterY());
      g2.draw(r);
      g2.fillRect((int) (s.getPos().x-shipWidth/2) * scale, (int) (s.getPos().y-shipLength/2) * scale, shipWidth*scale, 10);
      
      g2.setTransform(trans);
      
      DecimalFormat df = new DecimalFormat("#.####");
      g2.drawString("ax: " + df.format(s.getAcl().x) + " ay: " + df.format(s.getAcl().y) + " vx: " + df.format(s.getVel().x) + " vy: " + 
      df.format(s.getVel().y) + " px: " + df.format(s.getPos().x) + " py: " + df.format(s.getPos().y), 10, 10);
    }
  }

}
