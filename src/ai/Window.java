package ai;

import java.util.ArrayList;

import javax.swing.JFrame;

import com.sun.glass.events.KeyEvent;

import trackDesign.TrackPoint;

public class Window {

  public static void main(String[] args) {
    
    ArrayList<TrackPoint> track = new ArrayList<>();
    track.add(new TrackPoint(50, 50));
    track.add(new TrackPoint(300, 50));
    track.add(new TrackPoint(300, 300));
    track.add(new TrackPoint(50, 300));
    TrackPoint start = track.get(0);
    
    System.out.println(track);
    
    ShipManager shipManager = new ShipManager();
    AIShip player = new AIShip(start.getX(), start.getY(), 0, track);
    player.setRot(0);
    player.setAccel(0.001);
    shipManager.addShip(player);
    
    Visualisation visualisation = new Visualisation(shipManager, track, 2);
    
    JFrame frame = new JFrame();
    frame.setSize(800, 800);
    frame.setTitle("AI Testing");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(visualisation);
    frame.setVisible(true);
    
    // Add the input listener
    Input input = new Input();
    frame.addKeyListener(input);
    
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
//        double a = 0;
//        double t = 0;
//        
//        if (input.isKeyDown(KeyEvent.VK_W)) a += Ship.maxAcceleration;
//        if (input.isKeyDown(KeyEvent.VK_S)) a -= Ship.maxAcceleration;
//        
//        if (input.isKeyDown(KeyEvent.VK_A)) t -= Ship.maxTurnSpeed;
//        if (input.isKeyDown(KeyEvent.VK_D)) t += Ship.maxTurnSpeed;
//        
//        player.setAccel(a);
//        player.setRotV(t);
        
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
