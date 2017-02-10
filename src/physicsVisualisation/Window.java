package physicsVisualisation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.JFrame;

import physics.Ship;
import physics.Vector3;
// import trackDesign.TrackComponent;
// import trackDesign.TrackPoint;
import placeholders.KeyboardController;

/** Class for handling the basic ship physics visualisation.
 * Redone based on code by Reece Bennett that he used to test his AI. */
public class Window {

	public static void main(String[] args) {

		// TrackComponent trackComponent = new TrackComponent();
		// ArrayList<TrackPoint> track = trackComponent.getTrack();

		KeyboardController input = new KeyboardController();
		Ship testShip = new Ship(new Vector3(10f, 10f, 30f), input);
		ArrayList<Ship> ships = new ArrayList<Ship>();
		ships.add(testShip);

		Visualisation visualisation = new Visualisation(testShip, 2);

		JFrame frame = new JFrame();
		frame.setSize(800, 800);
		frame.setTitle("Physics Testing");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

		// Add the input listener
		frame.addKeyListener(input);

		boolean running = true;

		long lastLoopTime = System.nanoTime();
		final int TARGET_FPS = 60;
		final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
		long lastFpsTime = System.nanoTime();
		long fps = 0;

		Graphics g = frame.getGraphics();
		Image i = frame.createImage(1920, 1080);
		Graphics offg = i.getGraphics();

		// keep looping round til the game ends
		while (running) {
			long now = System.nanoTime();
			long updateLength = now - lastLoopTime;
			lastLoopTime = now;
			double delta = updateLength / ((double) OPTIMAL_TIME);

			// update the frame counter
			lastFpsTime += updateLength;
			fps++;
			if (lastFpsTime >= 1000000000) {
				// System.out.println("(FPS: " + fps + ")");
				lastFpsTime = 0;
				fps = 0;
			}
			// Updating and repainting
			testShip.update((float) delta / 60);
			offg.setColor(Color.black);
			offg.fillRect(0, 0, 1920, 1080);
			offg.setColor(Color.blue);
			offg.fillRect(0, 1020, 1000, 110);
			visualisation.draw(offg);
			g.drawImage(i, 0, 0, null);
			try {
				Thread.sleep((lastLoopTime - System.nanoTime() + OPTIMAL_TIME) / 1000000);
			} catch (Exception e) {}
			;
		}

	}

}