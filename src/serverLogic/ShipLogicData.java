package serverLogic;

import org.joml.Vector3f;

import physics.core.Ship;

public class ShipLogicData {

	private Ship ship;
	private int ranking;
	private boolean finished;
	private int currentLap;
	public ShipLogicData(Ship ship) {
		super();
		this.ship = ship;
	}

	public int getRanking() {
		return ranking;
	}
	public void setRanking(int ranking) {
		this.ranking = ranking;
	}
	public boolean finished() {
		return finished;
	}
	public void setFinished(boolean finished) {
		this.finished = finished;
	}
	public int getCurrentLap() {
		return currentLap;
	}
	public void setCurrentLap(int currentLap) {
		this.currentLap = currentLap;
	}
	public Vector3f getPosition() {
		return ship.getPosition();
	}
	public byte getId() {
		return ship.getId();
	}



}
