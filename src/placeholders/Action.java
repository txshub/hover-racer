package placeholders;


/** Enum representing an action that a player can take, each usually triggered by a single button. This serves to provide a layer of
 * abstraction between various input devices and the rest of the system
 * 
 * @author Maciej Bogacki */
public enum Action {
	FORWARD, BREAK, STRAFE_LEFT, STRAFE_RIGHT, TURN_LEFT, TURN_RIGHT, JUMP;

	public static Action[] ordered = new Action[]{FORWARD, BREAK, STRAFE_LEFT, STRAFE_RIGHT, TURN_LEFT, TURN_RIGHT, JUMP};
}
