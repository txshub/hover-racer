package placeholders;

import java.util.Collection;

public interface ControllerInt {

	/** @return A collections of all keys that are currently pressed */
	public Collection<Action> getPressedKeys();

	/** @return A collection of all keys that have just been pressed since the last call of this function */
	public Collection<Action> getNewlyPressedKeys();


}
