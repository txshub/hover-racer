package ai;

import java.util.ArrayList;

import javax.swing.JFrame;

import trackDesign.TrackMaker;
import trackDesign.TrackPoint;

public class Window {

  public static void main(String[] args) {
    
    ArrayList<TrackPoint> track = TrackMaker.makeTrack(10, 20, 15, 3, 50, 20);
    TrackPoint start = track.get(1);
    
    System.out.println(track);
    
    ShipManager shipManager = new ShipManager();
    Ship player = new Ship(start.getX(), start.getY(), 0);
    shipManager.addShip(player);
    
    Visualisation visualisation = new Visualisation(shipManager, track, 2);
    
    JFrame frame = new JFrame();
    frame.setSize(800, 800);
    frame.setTitle("AI Testing");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(visualisation);
    frame.setVisible(true);
    
    boolean running = true;
    
    long lastTime = System.nanoTime();
    long currTime = lastTime;
    long diff = 0;
    
    double updateDur = 1000000000 / 60;
    double deltaUPS = 0;
    double renderDur = 1000000000 / 60;
    double deltaFPS = 0;
    
    while (running) {
      currTime = System.nanoTime();
      diff = currTime - lastTime;
      deltaUPS += diff / updateDur;
      deltaFPS += diff / renderDur;
      lastTime = currTime;
      
      while (deltaUPS >= 1) {
        // Update
        shipManager.updateShips();
        
        deltaUPS--;
      }
      
      while (deltaFPS >= 1) {
        // Render
        frame.repaint();
        
        deltaFPS--;
      }
    }
    
  }

}
