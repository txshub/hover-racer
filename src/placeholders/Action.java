package placeholders;


public enum Action {
	FORWARD, BREAK, STRAFE_LEFT, STRAFE_RIGHT, TURN_LEFT, TURN_RIGHT, JUMP;

	public static Action[] ordered = new Action[]{FORWARD, BREAK, STRAFE_LEFT, STRAFE_RIGHT, TURN_LEFT, TURN_RIGHT, JUMP};
}
