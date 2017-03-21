package serverComms;

import physics.network.ShipSetupData;
import physics.placeholders.DataGenerator;

/**
 * Container for a user's ID and their ship data
 * 
 * @author simon
 *
 */
public class IDShipData {

  final int id;
  final ShipSetupData data;

  /**
   * Creates an IDShipData object
   * 
   * @param id
   *          The user's ID
   * @param data
   *          The user's ship data
   */
  public IDShipData(int id, ShipSetupData data) {
    this.id = id;
    this.data = data;
  }

  /**
   * Creates the object from a string passed over the network
   * 
   * @param in
   *          The string passed over the network
   */
  public IDShipData(String in) {
    String collected = "";
    while (in.charAt(0) != '|') {
      collected += in.charAt(0);
      in = in.substring(1);
    }
    id = Integer.parseInt(collected);
    in = in.substring(1);
    data = DataGenerator.fromJson(in);

  }

  /**
   * Returns a representation of this container
   */
  public String toString() {
    return id + "|" + data.toString();
  }

  /**
   * Returns a byte array of the toString() method
   * 
   * @return A byte array of the toString() method
   */
  public byte[] toByteArray() {
    return toString().getBytes(ServerComm.charset);
  }

}
