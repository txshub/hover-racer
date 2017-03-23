package upgrades;

import java.util.Map;
import java.util.function.Function;

/**
 * Container for information about a specific type of upgrade. Contains
 * information about stats raised and costs of buying multiple levels of this
 * upgrade.
 * 
 * @author mxb551
 */
public class UpgradeType {

  /** Name of the upgrade, as shown to the player. Must be unique */
  public String name;
  /**
   * Which piece of the ship does this upgrade correspond to (pick other if it
   * doesn't)
   */
  public Piece piece;
  /** Stats raised (or lowered) by this upgrade, per level of this upgrade. */
  public Map<Stat, Float> stats;
  /**
   * How many levels of this upgrade can a player have. Negative values mean
   * there is no cap.
   */
  public int maxLevel;
  /** Cost of the first level of this upgrade */
  private double baseCost;
  /**
   * Function that returns the cost of nth level of this upgrade. Leave null if
   * cost is always equal to base cost
   */
  private Function<Integer, Double> costFunction;
  // This would also store some sort of an icon

  /**
   * Creates a new upgrade, with cost of each level being the same
   * 
   * @param name
   *          Name of the upgrade, as shown to the player. Must be unique
   * @param stats
   *          Stats raised (or lowered) by this upgrade, per level of this
   *          upgrade.
   * @param maxLevel
   *          How many levels of this upgrade can a player have. Negative values
   *          mean there is no cap.
   * @param cost
   *          Cost per level of this upgrade
   */
  public UpgradeType(String name, Map<Stat, Float> stats, int maxLevel, double cost, Piece piece) {
    super();
    this.name = name;
    this.stats = stats;
    this.maxLevel = maxLevel;
    this.baseCost = cost;
    this.piece = piece;
  }

  /**
   * Creates a new upgrade, with cost of each level being the same
   * 
   * @param name
   *          Name of the upgrade, as shown to the player. Must be unique
   * @param stats
   *          Stats raised (or lowered) by this upgrade, per level of this
   *          upgrade.
   * @param maxLevel
   *          How many levels of this upgrade can a player have. Negative values
   *          mean there is no cap.
   * @param costFunction
   *          Function that returns the cost of nth level of this upgrade. If
   *          cost is always the same use the other constructor.
   */
  public UpgradeType(String name, Map<Stat, Float> stats, int maxLevel,
      Function<Integer, Double> costFunction, Piece piece) {
    super();
    this.name = name;
    this.stats = stats;
    this.maxLevel = maxLevel;
    this.baseCost = costFunction.apply(1);
    this.costFunction = costFunction;
    this.piece = piece;
  }

  /**
   * Returns the cost of a specific level (i.e. cost of upgrading from level
   * (n-1) to n), not rounded. Note that level must be at least 1, otherwise an
   * {@link IllegalArgumentException} will be thrown. Returns -1 if max level is
   * exceeded (i.e. purchase is unavailable)
   */
  public double getCost(int level) {
    if (level < 1)
      throw new IllegalArgumentException(
          "Requested cost of " + name + " at level " + level + ". Level must be at least 1.");

    if (level > maxLevel && maxLevel > 0)
      return -1;
    else if (level == 1 || costFunction == null)
      return baseCost;
    else
      return costFunction.apply(level);
  }

  @Override
  public boolean equals(Object obj) {
    try {
      UpgradeType casted = (UpgradeType) obj;
      return casted.name.equals(this.name);
    } catch (ClassCastException e) {
      return false;
    }
  }

}
