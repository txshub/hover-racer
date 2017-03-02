package serverComms.junit;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import serverComms.GameNameNumber;
import serverComms.GameSettings;
import serverComms.ServerComm;

public class TestGameSettings {

	@Test
	public void testToStringWithParams() {
		long seed = 12432;
		int maxPlayers = 3;
		int lapCount = 7;
		String lobbyName = "Testing";
		String hostName = "Tester";
		String expected = seed + "|" + maxPlayers + "|" + lapCount + "|" + lobbyName + "|" + hostName;
		GameSettings gs = new GameSettings(seed, maxPlayers, lapCount, lobbyName, hostName);
		if(!gs.toString().equals(expected)) fail("String with params wasn't as expected");
	}
	
	@Test
	public void testToStringWithString() {
		long seed = 12432;
		int maxPlayers = 3;
		int lapCount = 7;
		String lobbyName = "Testing";
		String hostName = "Tester";
		String expected = seed + "|" + maxPlayers + "|" + lapCount + "|" + lobbyName + "|" + hostName;
		GameSettings gs = new GameSettings(expected);
		if(!gs.toString().equals(expected)) fail("String with string wasn't as expected");
	}

	@Test
	public void testToByteArrayWithParams() {
		long seed = 12432;
		int maxPlayers = 3;
		int numAI = 4;
		int lapCount = 7;
		String lobbyName = "Testing";
		String hostName = "Tester";
		byte[] expected = (seed + "|" + maxPlayers + "|" + lapCount + "|" + lobbyName + "|" + hostName).getBytes(ServerComm.charset);
		GameSettings gs = new GameSettings(seed, maxPlayers, lapCount, lobbyName, hostName);
		if(!Arrays.equals(gs.toByteArray(), expected)) fail("Byte Array with params wasn't as expected");
	}
	
	@Test
	public void testToByteArrayWithString() {
		long seed = 12432;
		int maxPlayers = 3;
		int numAI = 4;
		int lapCount = 7;
		String lobbyName = "Testing";
		String hostName = "Tester";
		String expected = seed + "|" + maxPlayers + "|" + lapCount + "|" + lobbyName + "|" + hostName;
		GameSettings gs = new GameSettings(expected);
		if(!Arrays.equals(gs.toByteArray(), expected.getBytes(ServerComm.charset))) fail("Byte Array with params wasn't as expected");
	}

}
