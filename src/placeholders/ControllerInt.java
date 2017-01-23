package placeholders;

import java.util.Collection;

public interface ControllerInt {

	/** @return A collections of all keys that are currently pressed */
	public Collection<Action> getPressedKeys();

}
