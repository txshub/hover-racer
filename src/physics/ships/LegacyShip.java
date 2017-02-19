package physics.ships;

import java.util.Collection;

import org.joml.Vector3f;

import gameEngine.models.TexturedModel;
import physics.core.Ship;
import physics.placeholders.ControllerInt;
import physics.support.Action;
import physics.support.GroundProvider;

public class LegacyShip extends Ship {

	private ControllerInt input;

	public LegacyShip(TexturedModel model, Vector3f startingPosition, Collection<Ship> otherShips, GroundProvider ground,
		ControllerInt input) {
		super(model, startingPosition, otherShips, ground);
		this.input = input;
	}

	@Override
	public void update(float delta) {
		float thrust = 0, turn = 0, strafe = 0, jump = 0;
		Collection<Action> keys = input.getPressedKeys();
		// Handle inputs
		if (keys.contains(Action.FORWARD)) thrust++;
		if (keys.contains(Action.BREAK)) thrust--;
		if (keys.contains(Action.TURN_RIGHT)) turn++;
		if (keys.contains(Action.TURN_LEFT)) turn--;
		if (keys.contains(Action.STRAFE_RIGHT)) strafe++;
		if (keys.contains(Action.STRAFE_LEFT)) strafe--;
		if (keys.contains(Action.JUMP)) jump++;
		// Steer and update ship
		super.steer(thrust, turn, strafe, jump, delta);
		super.updatePhysics(delta);

	}

}
