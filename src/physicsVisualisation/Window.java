package physicsVisualisation;

import java.util.ArrayList;

import javax.swing.JFrame;

import physics.Ship;
import physics.Vector3;
// import trackDesign.TrackComponent;
// import trackDesign.TrackPoint;
import placeholders.KeyboardController;

public class Window {

	public static void main(String[] args) {

		// TrackComponent trackComponent = new TrackComponent();
		// ArrayList<TrackPoint> track = trackComponent.getTrack();

		KeyboardController input = new KeyboardController();
		Ship testShip = new Ship(new Vector3(10f, 10f, 10f), input);
		ArrayList<Ship> ships = new ArrayList<Ship>();
		ships.add(testShip);

		Visualisation visualisation = new Visualisation(testShip, 1);

		JFrame frame = new JFrame();
		frame.setSize(800, 800);
		frame.setTitle("Physics Testing");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(visualisation);
		frame.setVisible(true);

		// Add the input listener
		frame.addKeyListener(input);

		boolean running = true;

		long lastTime = System.nanoTime();
		long currTime = lastTime;
		long diff = 0;

		double updateDur = (double) 1000000000 / 60;
		double deltaUPS = 0;
		double renderDur = (double) 1000000000 / 60;
		double deltaFPS = 0;
		testShip.accelerate2d(10, 0);
		double previousX = 0;

		while (running) {
			currTime = System.nanoTime();
			diff = currTime - lastTime;
			deltaUPS += diff / updateDur;
			deltaFPS += diff / renderDur;
			lastTime = currTime;


			// float delta = (float) diff / 1000000000;
			// ships.forEach(s -> s.update(delta / 10000));
			// input.getPressedKeys().forEach(k -> System.out.println(k));
			// Testing
			// testShip.accelerate2d(0.0001f, (float) Math.PI * 1.75f);

			int atSameTime = 0;
			while (deltaUPS >= 1) {
				testShip.update((float) 1 / 60);
				deltaUPS--;
				atSameTime++;
				// System.out.println(diff / updateDur);

			}


			while (deltaFPS >= 1) {
				System.out.println(testShip.getPosition().getX() - previousX);
				previousX = testShip.getPosition().getX();
				// Render
				frame.repaint();

				deltaFPS--;
			}
		}

	}

}
