package upgrades;

public enum Piece {

  HULL(1), WING(2), COCKPIT(1), ENGINE(3), OTHER(100);

  private int max;

  private Piece(int max) {
    this.max = max;
  }

  public int getMax() {
    return this.max;
  }

}
