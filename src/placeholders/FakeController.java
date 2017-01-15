package placeholders;

import java.util.ArrayList;
import java.util.Collection;

/** This controller does literally nothing. Give it to Ship if you just want a ship that's not steered at all. */
public class FakeController implements ControllerInt {


	public FakeController() {}

	@Override
	public Collection<Action> getPressedKeys() {
		return new ArrayList<Action>();
	}

	@Override
	public Collection<Action> getNewlyPressedKeys() {
		return new ArrayList<Action>();
	}

}
