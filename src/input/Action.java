package input;

/** Enum representing an action that a player can take, each usually triggered by
 * a single button. This serves to provide a layer of abstraction between
 * various input devices and the rest of the system */
public enum Action {

	FORWARD, BACKWARD, BREAK, STRAFE_LEFT, STRAFE_RIGHT, TURN_LEFT, TURN_RIGHT, JUMP, MUSIC_UP, MUSIC_DOWN, SFX_UP, SFX_DOWN, MUSIC_SKIP, EXIT, MENU;

}
