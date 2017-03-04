package input;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controllers;

public class JoystickController extends InputController {

	public JoystickController() {
		super();
	}

	@Override
	public void update() {

	}

	public static void main(String[] args) {
		
		try {
			Controllers.create();
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("It broke.");
			return;
		}
		Controllers.poll();
		System.out.println(Controllers.isCreated());
//		Controllers.getController(0);

//		try {
//			Controllers.destroy();
//			Controllers.create();
//			Controllers.poll();
//			for (int i = 0; i < Controllers.getControllerCount(); i++) {
//				Controller controler = Controllers.getController(i);
//				System.out.println(controler.getName());
//			}
//		} catch (LWJGLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

}
