package physics.placeholders;

import java.util.Optional;

import physics.network.ExportedShip;
import physics.network.ServerShipProvider;

/** ServerShipProvider that does nothing, i.e. acts as if no packets ever arrived from the server. Used internally within the
 * Ship class, otherwise to be only used for testing purposes
 * 
 * @author Maciej Bogacki */
public class FakeServerProvider implements ServerShipProvider {



	public FakeServerProvider() {

	}

	@Override
	public Optional<ExportedShip> getShip() {
		return Optional.empty();
	}

}
