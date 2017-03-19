package serverLogic;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.joml.Vector3f;
import org.junit.Before;
import org.junit.Test;

import physics.core.Ship;
import physics.ships.DummyShip;
import serverComms.GameRoom;
import trackDesign.TrackPoint;

public class JUTests {

	private GameLogic logic;
	private ArrayList<Ship> ships;
	private ArrayList<ShipLogicData> players;
	private ArrayList<TrackPoint> trackPoints;
	private GameRoom gameRoom;
	private int laps;
	private int amountOfPlayers;
	
	@Before
	public void init() {
		
		trackPoints = new ArrayList<TrackPoint>();
		trackPoints.add(new TrackPoint(0f, 0f));
		trackPoints.add(new TrackPoint(0f, 1000f));
		trackPoints.add(new TrackPoint(1000f, 1000f));
		trackPoints.add(new TrackPoint(1000f, 0f));
		
		ships = new ArrayList<Ship>();
		ships.add(new DummyShip((byte) 0, null, new Vector3f(0f, 0f, 0f), null, null, trackPoints));
		ships.add(new DummyShip((byte) 1, null, new Vector3f(0f, 0f, 0f), null, null, trackPoints));
		ships.add(new DummyShip((byte) 2, null, new Vector3f(0f, 0f, 0f), null, null, trackPoints));
		players = new ArrayList<ShipLogicData>();
		players.add(new ShipLogicData(ships.get(0)));
		players.add(new ShipLogicData(ships.get(1)));
		players.add(new ShipLogicData(ships.get(2)));
		
		gameRoom = new DummyGameRoom(0, "name", "seed", 3, "name", laps, null);
		
		laps = 3;
		
		amountOfPlayers = 3;
		
		logic = new GameLogic(players, trackPoints, laps, amountOfPlayers, gameRoom);
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testArgs1() {
		logic = new GameLogic(null, trackPoints, laps, amountOfPlayers, gameRoom);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testArgs2() {
		logic = new GameLogic(players, null, laps, amountOfPlayers, gameRoom);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testArgs3() {
		logic = new GameLogic(players, trackPoints, 0, amountOfPlayers, gameRoom);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testArgs4() {
		logic = new GameLogic(players, trackPoints, laps, amountOfPlayers, null);
	}
	
	@Test
	public void testRanking() {
		ships.get(0).setPosition(new Vector3f(0f, 0f, 100f));
		ships.get(1).setPosition(new Vector3f(0f, 0f, 200f));
		ships.get(2).setPosition(new Vector3f(100f, 0f, 1000f));
		logic.update();
		assertEquals(players.get(0).getRanking(), 3);
		assertEquals(players.get(1).getRanking(), 2);
		assertEquals(players.get(2).getRanking(), 1);
	}
	
	@Test
	public void testPlayerFinished() {
		ships.get(0).setPosition(new Vector3f(900f, 0f, 0f));
		logic.update();
		ships.get(0).setPosition(new Vector3f(0f, 0f, 0f));
		players.get(0).setCurrentLap(laps);
		logic.update();
		assertEquals(players.get(0).finished(), true);
	}
	
	@Test
	public void testLap() {
		assertEquals(players.get(0).getCurrentLap(), 1);
		ships.get(0).setPosition(new Vector3f(900f, 0f, 0f));
		logic.update();
		players.get(0).setCurrentLap(1);
		ships.get(0).setPosition(new Vector3f(0f, 0f, 100f));
		logic.update();
		assertEquals(players.get(0).getCurrentLap(), 2);
	}
	
	@Test
	public void testWrongWay() {
		ships.get(0).setPosition(new Vector3f(0f, 0f, 100f));
		players.get(0).setCurrentLap(2);
		logic.update();
		ships.get(0).setPosition(new Vector3f(900f, 0f, 0f));
		logic.update();
		assertEquals(players.get(0).getCurrentLap(), 1);
	}

}
