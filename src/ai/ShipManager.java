package ai;

import java.util.ArrayList;

/**
 * 
 * @author Reece Bennett
 *
 */
public class ShipManager {

  private ArrayList<Ship> ships;
  
  public ShipManager() {
    ships = new ArrayList<>();
  }
  
  public void addShip(Ship ship) {
    ships.add(ship);
  }
  
  public Ship getShip(int index) {
    return ships.get(index);
  }
  
  public ArrayList<Ship> getShips() {
    return ships;
  }
  
  public void updateShips() {
    for (Ship s : ships) {
      if (s instanceof AIShip) {
        ((AIShip) s).doNextInput();
      }
      s.updatePos();
    }
  }
}
