package serverComms;

import physics.network.ShipSetupData;
import physics.placeholders.DataGenerator;

/**
 * Container for all relevant game settings
 * 
 * @author simon
 *
 */
public class GameSettings {

  final String seed;
  final int maxPlayers;
  final int lapCount;
  final String lobbyName;
  final ShipSetupData setupData;

  /**
   * Creates a GameSettings object
   * 
   * @param seed
   *          The seed for track generation
   * @param maxPlayers
   *          The maximum number of players
   * @param lapCount
   *          The number of laps
   * @param lobbyName
   *          The lobby name
   * @param setupData
   *          The ship data for the host
   * @throws IllegalArgumentException
   *           If the lobby name contains a new line symbol (will break the
   *           communications)
   */
  public GameSettings(String seed, int maxPlayers, int lapCount, String lobbyName,
      ShipSetupData setupData) throws IllegalArgumentException {
    this.seed = seed;
    this.maxPlayers = maxPlayers;
    this.lobbyName = lobbyName;
    this.lapCount = lapCount;
    this.setupData = setupData;
    if (lobbyName.contains(System.lineSeparator()))
      throw new IllegalArgumentException("Name shouldn't contain new line symbol");
  }

  /**
   * Creates the object from a GameSettings string passed over the network
   * 
   * @param in
   *          The string passed across the network
   */
  public GameSettings(String in) {
    String collected = "";
    while (in.charAt(0) != '|') {
      collected += in.charAt(0);
      in = in.substring(1);
    }
    seed = collected;
    in = in.substring(1);
    collected = "";
    while (in.charAt(0) != '|') {
      collected += in.charAt(0);
      in = in.substring(1);
    }
    maxPlayers = Integer.valueOf(collected);
    in = in.substring(1);
    collected = "";
    while (in.charAt(0) != '|') {
      collected += in.charAt(0);
      in = in.substring(1);
    }
    lapCount = Integer.valueOf(collected);
    in = in.substring(1);
    collected = "";
    while (in.charAt(0) != '|') {
      collected += in.charAt(0);
      in = in.substring(1);
    }
    lobbyName = collected;
    setupData = DataGenerator.fromJson(in.substring(1));
  }

  /**
   * Returns a string representing the data
   */
  public String toString() {
    return seed + "|" + maxPlayers + "|" + lapCount + "|" + lobbyName + "|" + setupData.toString();
  }

  /**
   * Returns a byte array from the toString() method
   * 
   * @return A byte array from the toString() method
   */
  public byte[] toByteArray() {
    return toString().getBytes(ServerComm.charset);
  }
}
