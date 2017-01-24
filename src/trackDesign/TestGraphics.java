package trackDesign;
import javax.swing.JFrame;

public class TestGraphics {
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setSize(600,600);
		frame.setTitle("Track Making Graphics Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		TrackComponent track = new TrackComponent(2);
		frame.add(track);
		frame.setVisible(true);
	}
}