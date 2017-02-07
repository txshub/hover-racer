package placeholders;

import java.util.Optional;

public class FakeServerProvider implements ServerShipProvider {



	public FakeServerProvider() {

	}

	@Override
	public Optional<ExportedShip> getShip() {
		return Optional.empty();
	}

}
