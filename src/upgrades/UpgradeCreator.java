package upgrades;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

import com.google.gson.Gson;

public class UpgradeCreator {

  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    System.out.println("Enter upgrade name");
    String name = in.nextLine();
    Map<Stat, Float> stats = new HashMap<Stat, Float>();
    do {
      System.out.println("Pick a stat or hit enter to finish. Available stats:");
      Arrays.stream(Stat.values()).forEach(s -> System.out.print(s.getName() + ", "));
      System.out.println();
      String inStat = in.nextLine().toLowerCase();
      if (inStat.equals(""))
        break; // If empty line then finish adding stats
      System.out.println(inStat);
      Optional<Stat> stat = Arrays.stream(Stat.values())
          .filter(s -> s.getName().toLowerCase().equals(inStat)).findAny();
      if (stat.isPresent()) {
        System.out.println("Pick a value");
        float value = Float.parseFloat(in.nextLine());
        stats.put(stat.get(), value);
      } else
        System.out.println("Invalid stat. Please try again");

    } while (true);
    System.out.println("Enter base cost");
    double cost = Double.parseDouble(in.nextLine());
    System.out.println("Enter max level (negative for no limit).");
    int maxLevel = Integer.parseInt(in.nextLine());
    if (maxLevel < -1)
      maxLevel = -1;
    UpgradeType upgrade = new UpgradeType(name, stats, maxLevel, cost, Piece.OTHER);
    String json = (new Gson()).toJson(upgrade);
    System.out.println("Created " + json);
    save(name, json);
    System.out.println("Keep going?");
    String ans = in.nextLine();
    if (ans.equals("") || ans.equals("y") || ans.equals("yes") || ans.equals("ok")
        || ans.equals("go"))
      main(null);
  }

  public static void save(String name, String content) {
    try {
      PrintWriter writer = new PrintWriter("src/upgrades/data/" + name + ".txt", "UTF-8");
      writer.println(content);
      writer.close();
    } catch (IOException e) {
      System.err.println("Failed to write to file");
    }
  }

}
