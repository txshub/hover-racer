package placeholders;

import java.util.Collection;

/** Interface for an object that provides input from the player
 * 
 * @author Maciej Bogacki */
public interface ControllerInt {

	/** @return A collections of all keys that are currently pressed */
	public Collection<Action> getPressedKeys();

}
