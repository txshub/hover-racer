package physics.ships;

import java.util.ArrayList;
import java.util.Collection;

import org.joml.Vector3f;

import gameEngine.models.TexturedModel;
import physics.core.Ship;
import physics.support.Action;
import physics.support.GroundProvider;
import physics.support.InputController;
import physics.support.ShipSounds;

/** Represents a Ship controlled with inputs from the Player
 * 
 * @author Maciej Bogacki */
public class PlayerShip extends Ship {

	InputController input;
	ShipSounds sound;

	public PlayerShip(byte id, TexturedModel model, Vector3f startingPosition, Collection<Ship> otherShips, GroundProvider ground,
		InputController input) {
		super(id, model, startingPosition, ground);
		super.addOtherShips(otherShips);
		this.input = input; // Deal with input
		this.sound = new ShipSounds(this, otherShips != null ? otherShips : new ArrayList<Ship>()); // Create ShipSounds
	}

	@Override
	public void update(float delta) {
		float thrust = 0, turn = 0, strafe = 0, jump = 0;
		// Handle inputs
		if (input.checkAction(Action.FORWARD)) thrust++;
		if (input.checkAction(Action.BREAK)) thrust = -1;
		if (input.checkAction(Action.TURN_RIGHT)) turn++;
		if (input.checkAction(Action.TURN_LEFT)) turn--;
		if (input.checkAction(Action.STRAFE_RIGHT)) strafe++;
		if (input.checkAction(Action.STRAFE_LEFT)) strafe--;
		if (input.checkAction(Action.JUMP)) jump++;
		// Steer and update ship
		super.steer(thrust, turn, strafe, jump, delta);
		super.updatePhysics(delta);
		sound.update(delta);
	}

	public void cleanUp() {
		sound.cleanUp();
	}

}
