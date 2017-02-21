package physics.placeholders;

import java.util.ArrayList;
import java.util.Collection;

import physics.support.Action;

/** This controller does literally nothing. Give it to Ship if you just want a ship that's not steered at all. Used internally within the
 * Ship class, otherwise to be only used for testing purposes
 * 
 * @author Maciej Bogacki */
public class FakeController implements ControllerInt {


	public FakeController() {}

	@Override
	public Collection<Action> getPressedKeys() {
		return new ArrayList<Action>();
	}


}
