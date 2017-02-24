package game;

import java.util.ArrayList;
import java.util.HashMap;

import gameEngine.entities.Player;
import physics.core.Ship;
import trackDesign.SeedTrack;

/**
 * @author rtm592
 *
 */
public class GameLogic {

	private float playerDist;
	private Player player;
	private ArrayList<Ship> opponents;
	private SeedTrack track;
	private HashMap<Integer,Integer> ranking;
	
	public GameLogic(Player player, ArrayList<Ship> opponents, SeedTrack track) {

		this.player = player;
		this.opponents = opponents;
		this.track = track;
		playerDist = 0;
		ranking = new HashMap<>();
		
	}
	
	public void update(){
		// calculate distance here
		
		// get rankings from the server
	}
	
	public void setRankings(HashMap<Integer,Integer> ranking){
		this.ranking = ranking;
	}
	
	public float getPlayerDist(){
		return playerDist;
	}
	
	public HashMap<Integer, Integer> getRankings(){
		return ranking;
	}
	
}
