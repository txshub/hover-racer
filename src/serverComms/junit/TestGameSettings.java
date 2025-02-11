package serverComms.junit;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import physics.placeholders.DataGenerator;
import serverComms.GameSettings;
import serverComms.ServerComm;

public class TestGameSettings {

  @Test
  public void testNewLineInLobbyName() {
    String seed = "12432";
    int maxPlayers = 3;
    int lapCount = 7;
    String lobbyName = "New" + System.lineSeparator() + "Line Test";
    String hostName = "Tester";
    try {
      new GameSettings(seed, maxPlayers, lapCount, lobbyName,
          DataGenerator.basicShipSetup(hostName));
      // Constructor should throw exception
      fail("IllegalArgumentException not raised");
    } catch (IllegalArgumentException e) {
      // Should reach here
    }
  }

  @Test
  public void testToStringWithParams() {
    String seed = "12432";
    int maxPlayers = 3;
    int lapCount = 7;
    String lobbyName = "Testing";
    String hostName = "Tester";
    String expected = seed + "|" + maxPlayers + "|" + lapCount + "|" + lobbyName + "|"
        + DataGenerator.basicShipSetup(hostName).toString();
    GameSettings gs = new GameSettings(seed, maxPlayers, lapCount, lobbyName,
        DataGenerator.basicShipSetup(hostName));
    if (!gs.toString().equals(expected))
      fail("String with params wasn't as expected");
  }

  @Test
  public void testToStringWithString() {
    String seed = "12432";
    int maxPlayers = 3;
    int lapCount = 7;
    String lobbyName = "Testing";
    String hostName = "Tester";
    String expected = seed + "|" + maxPlayers + "|" + lapCount + "|" + lobbyName + "|"
        + DataGenerator.basicShipSetup(hostName).toString();
    GameSettings gs = new GameSettings(expected);
    if (!gs.toString().equals(expected))
      fail("String with string wasn't as expected");
  }

  @Test
  public void testToByteArrayWithParams() {
    String seed = "12432";
    int maxPlayers = 3;
    int lapCount = 7;
    String lobbyName = "Testing";
    String hostName = "Tester";
    byte[] expected = (seed + "|" + maxPlayers + "|" + lapCount + "|" + lobbyName + "|"
        + DataGenerator.basicShipSetup(hostName).toString()).getBytes(ServerComm.charset);
    GameSettings gs = new GameSettings(seed, maxPlayers, lapCount, lobbyName,
        DataGenerator.basicShipSetup(hostName));
    if (!Arrays.equals(gs.toByteArray(), expected))
      fail("Byte Array with params wasn't as expected");
  }

  @Test
  public void testToByteArrayWithString() {
    String seed = "12432";
    int maxPlayers = 3;
    int lapCount = 7;
    String lobbyName = "Testing";
    String hostName = "Tester";
    String expected = seed + "|" + maxPlayers + "|" + lapCount + "|" + lobbyName + "|"
        + DataGenerator.basicShipSetup(hostName).toString();
    GameSettings gs = new GameSettings(expected);
    if (!Arrays.equals(gs.toByteArray(), expected.getBytes(ServerComm.charset)))
      fail("Byte Array with params wasn't as expected");
  }

}
