package ai;

import javax.swing.JFrame;

public class Window {

  public static void main(String[] args) {
    
    ShipManager shipManager = new ShipManager();
    Ship player = new Ship(100, 200, 40);
    shipManager.addShip(player);
    player.setVX(1);
    
    Visualisation visualisation = new Visualisation(shipManager);
    
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
