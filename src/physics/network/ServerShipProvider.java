package physics.network;

import java.util.Optional;

/** Provides information received from the server about one ship.
 * 
 * @author Maciej Bogacki */
public interface ServerShipProvider {

	/** @return Newest data for this ship as provided by the server. If no new data was received since last call of this method, return and
	 *         empty Optional. */
	public Optional<byte[]> getShip();

}
