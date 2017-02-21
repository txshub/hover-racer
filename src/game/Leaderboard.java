package game;

import java.util.ArrayList;

import physics.Ship;

public class Leaderboard {
	
	private ArrayList<Ship>ships;
	
	/**
	 * Constructor
	 * @param ships The list of ships in the game
	 */
	public Leaderboard(ArrayList<Ship>ships){
		this.ships = ships;
	}
	
	/**
	 * Updates the leaderboard
	 */
	public void update() {
		ArrayList<Ship> ordered = new ArrayList<Ship>();
		for (Ship ship : ships) {
			for (int i = 0; i < ordered.size(); i++) {
				
				// TODO - implement int Ship.surpassedPoints()
				// TODO - implement float Ship.distanceToNextPoint()
				
				if (ship.SurpassedPoints() < ordered.get(i).SurpassedPoints()) {
					ordered.add(i, ship);
					break;
				} else if (ship.SurpassedPoints() == ordered.get(i).SurpassedPoints()){
					if (ship.distanceToNextPoint() < ordered.get(i).distanceToNextPoint()) {
						ordered.add(i, ship);
						break;
					}
				}
				
			}
			
			if (!ordered.contains(ship)) {
				ordered.add(ship);
			}
		}
		ships = ordered;
	}
	
	/**
	 * Get the current leaderboard
	 * @return The list of ships, sorted by the percentage of the track they covered (first element = last ship in the race)
	 */
	public ArrayList<Ship> getStandings() {
		return ships;
	}
}
