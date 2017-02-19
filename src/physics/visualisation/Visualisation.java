package physics.visualisation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collection;

import physics.core.Ship;

/** Basic visualisation for testing ship physics. Placeholder until physics is connected to the main engine.
 * Adjusted from code by Reece Bennett that he used to test his AI. */
public class Visualisation {

	private Collection<Ship> ships;
	// private TrackComponent trackComponent;

	private int scale;

	private final int shipWidth = 10;
	private final int shipLength = 20;


	public Visualisation(Ship ship, int scale) {
		this.ships = new ArrayList<Ship>();
		ships.add(ship);
		// this.trackComponent = null;
		this.scale = scale;
	}

	/* public Visualisation(Ship ships, TrackComponent trackComponent, int scale) {
	 * super();
	 * this.ships = ships;
	 * this.trackComponent = trackComponent;
	 * this.scale = scale;
	 * } */

	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;


		// Paint the track first
		// if (trackComponent != null) trackComponent.paintComponent(g);

		// Loop through ships and draw each of them
		for (Ship s : ships) {
			// float x = s.testX;
			float x = -s.getPosition().x + 120;
			float z = -s.getPosition().z + 120; // Use this line to get a top-down view
			// float z = (500 - s.getPosition().y * 2); // Use this line to get a sideways view
			Rectangle r =
				new Rectangle((int) (x - shipWidth / 2) * scale, (int) (z - shipLength / 2) * scale, shipWidth * scale, shipLength * scale);

			AffineTransform trans = g2.getTransform();
			//
			g2.setColor(Color.white);
			g2.rotate(-Math.toRadians(s.getRotation().y), r.getCenterX(), r.getCenterY());
			g2.fillRect((int) (x - shipWidth / 2) * scale, (int) (z - shipLength / 2) * scale, shipWidth * scale, 10);
			g2.draw(r);
			g2.fillRect((int) (x - shipWidth / 2) * scale, (int) (z - shipLength / 2) * scale, shipWidth * scale, 10);
			//
			g2.setTransform(trans);

			// DecimalFormat df = new DecimalFormat("#.####");
			// g2.drawString("ax: " + df.format(s.getAcl().x) + " ay: " + df.format(s.getAcl().y) + " vx: " + df.format(s.getVel().x) + "
			// vy: "
			// + df.format(s.getVel().y) + " px: " + df.format(s.getPos().x) + " py: " + df.format(s.getPos().y), 10, 10);
		}
	}

}
