package placeholders;

import java.util.Optional;

public interface ServerShipProvider {

	/** @return Newest data for this ship as provided by the server. If no new data was received since last call of this method, return and
	 *         empty Optional. */
	public Optional<ExportedShip> getShip();

}
