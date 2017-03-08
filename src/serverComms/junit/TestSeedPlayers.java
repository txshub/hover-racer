package serverComms.junit;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import serverComms.SeedPlayers;
import serverComms.ServerComm;

public class TestSeedPlayers {

  @Test
  public void testToStringWithParams() {
    long seed = 117632345;
    ArrayList<String> players = new ArrayList<String>();
    players.add("Player1");
    players.add("Player2");
    players.add("Player3");
    String expected = seed + "|Player1|Player2|Player3|";
    SeedPlayers test = new SeedPlayers(seed, players);
    if (!test.toString().equals(expected))
      fail("toString not as expected with params");
  }

  @Test
  public void testToStringWithString() {
    long seed = 117632345;
    ArrayList<String> players = new ArrayList<String>();
    players.add("Player1");
    players.add("Player2");
    players.add("Player3");
    String expected = String.valueOf(seed) + "|Player1|Player2|Player3|";
    SeedPlayers test = new SeedPlayers(expected);
    if (!test.toString().equals(expected))
      fail("toString not as expected with string");
  }

  @Test
  public void testToByteArrayWithParams() {
    long seed = 117632345;
    ArrayList<String> players = new ArrayList<String>();
    players.add("Player1");
    players.add("Player2");
    players.add("Player3");
    String expected = seed + "|Player1|Player2|Player3|";
    SeedPlayers test = new SeedPlayers(seed, players);
    if (!Arrays.equals(test.toByteArray(), (expected).getBytes(ServerComm.charset)))
      fail("toString not as expected with params");
  }

  @Test
  public void testToByteArrayWithString() {
    long seed = 117632345;
    ArrayList<String> players = new ArrayList<String>();
    players.add("Player1");
    players.add("Player2");
    players.add("Player3");
    String expected = seed + "|Player1|Player2|Player3|";
    SeedPlayers test = new SeedPlayers(expected);
    if (!Arrays.equals(test.toByteArray(), (expected).getBytes(ServerComm.charset)))
      fail("toString not as expected with string");
  }

}
