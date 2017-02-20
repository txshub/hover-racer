package gameEngine.entities;

import org.joml.Vector3f;

import gameEngine.models.TexturedModel;
import physics.support.Action;
import physics.support.InputController;


/** @author rtm592 */
public class Player extends Ship {

	private InputController input;

	public Player(TexturedModel model, Vector3f position, Vector3f rotation, float scale, InputController input) {
		super(model, position, rotation, scale);
		this.input = input;
	}

	public void update(float dt) {
		// Check inputs
		thrust = 0;
		if (input.checkAction(Action.FORWARD)) thrust += 1;
		if (input.checkAction(Action.BREAK)) thrust -= 1;

		rotThrust = 0;
		if (input.checkAction(Action.TURN_LEFT)) rotThrust += 1;
		if (input.checkAction(Action.TURN_RIGHT)) rotThrust -= 1;

		super.update(1f / 60f);
	}

}
