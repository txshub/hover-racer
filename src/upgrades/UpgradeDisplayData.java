package upgrades;

import java.util.Map;

public class UpgradeDisplayData {

  public String name;
  public int cost;
  public int level;
  public int maxLevel;
  public Map<String, Float> statGains;

  public UpgradeDisplayData(String name, int cost, int level, int maxLevel,
      Map<String, Float> statGains) {
    super();
    this.name = name;
    this.cost = cost;
    this.level = level;
    this.maxLevel = maxLevel;
    this.statGains = statGains;
  }

}
