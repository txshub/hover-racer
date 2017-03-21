package ai;

import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JFrame;

import trackDesign.SeedTrack;
import trackDesign.TrackMaker;
import trackDesign.TrackPoint;

/**
 * 
 * @author Reece Bennett
 *
 */
public class Window {

  public static void main(String[] args) {

    ArrayList<TrackPoint> track = new ArrayList<>();
    // track.add(new TrackPoint(50, 50));
    // track.add(new TrackPoint(80, 60));
    // track.add(new TrackPoint(130, 100));
    // track.add(new TrackPoint(160, 170));
    // track.add(new TrackPoint(180, 180));
    // track.add(new TrackPoint(200, 165));
    // track.add(new TrackPoint(210, 120));
    // track.add(new TrackPoint(220, 80));
    // track.add(new TrackPoint(240, 60));
    // track.add(new TrackPoint(290, 80));
    // track.add(new TrackPoint(300, 300));
    // track.add(new TrackPoint(50, 300));

    track.add(new TrackPoint(50, 50));
    track.add(new TrackPoint(80, 60));
    track.add(new TrackPoint(110, 50));
    track.add(new TrackPoint(120, 140));
    track.add(new TrackPoint(80, 200));

    SeedTrack seedTrack = TrackMaker.makeTrack();
    track = seedTrack.getTrack();

    TestAI ship = new TestAI(track.get(0).getX(), track.get(0).getY(), 0, track);
    ship.setRot(0);
    ship.setAccel(0.001);

    Visualisation visualisation = new Visualisation(ship, track, 2);

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
      
      if (input.isKeyDown(KeyEvent.VK_ESCAPE)) {
        running = false;
      }

      while (deltaUPS >= 1) {
        // Update
        // double a = 0;
        // double t = 0;
        //
        // if (input.isKeyDown(KeyEvent.VK_W)) a += Ship.maxAcceleration;
        // if (input.isKeyDown(KeyEvent.VK_S)) a -= Ship.maxAcceleration;
        //
        // if (input.isKeyDown(KeyEvent.VK_A)) t -= Ship.maxTurnSpeed;
        // if (input.isKeyDown(KeyEvent.VK_D)) t += Ship.maxTurnSpeed;
        //
        // player.setAccel(a);
        // player.setRotV(t);

        ship.doNextInput();
        ship.updatePos();

        deltaUPS--;
      }

      while (deltaFPS >= 1) {
        // Render
        frame.repaint();

        deltaFPS--;
      }
    }
    
    // Close the JFrame
    frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
  }

}
