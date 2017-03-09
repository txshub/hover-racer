package serverComms;

import physics.network.ShipSetupData;
import physics.placeholders.DataGenerator;

public class GameSettings {

  final long seed;
  final int maxPlayers;
  final int lapCount;
  final String lobbyName;
  final String hostName;
  final ShipSetupData setupData;

  public GameSettings(long seed, int maxPlayers, int lapCount, String lobbyName, ShipSetupData setupData)
      throws IllegalArgumentException {
    this.seed = seed;
    this.maxPlayers = maxPlayers;
    this.lobbyName = lobbyName;
    this.lapCount = lapCount;
    this.hostName = setupData.nickname;
    this.setupData = setupData;
    if (lobbyName.contains(System.lineSeparator()))
      throw new IllegalArgumentException("Name shouldn't contain new line symbol");
  }

  public GameSettings(String in) {
    String collected = "";
    while (in.charAt(0) != '|') {
      collected += in.charAt(0);
      in = in.substring(1);
    }
    seed = Long.valueOf(collected);
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
    in = in.substring(1);
    collected = "";
    while (in.charAt(0) != '|') {
    	collected += in.charAt(0);
    	in = in.substring(1);
    }
    hostName = collected;
    setupData = DataGenerator.fromJson(in.substring(1));
  }

  public String toString() {
    return seed + "|" + maxPlayers + "|" + lapCount + "|" + lobbyName + "|" + hostName + "|" + setupData.toString();
  }

  public byte[] toByteArray() {
    return toString().getBytes(ServerComm.charset);
  }
}
