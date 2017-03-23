package physics.core;

import java.util.Map;

import upgrades.ShipTemplate;
import upgrades.Stat;

public class StatManager {

  Map<Stat, Float> stats;
  public static final float SCALE = 3;

  /**
   * A class that stores stats for one Ship, created with a ShipTemplate.
   * 
   * @param template
   *          Template to initialise the stats with
   */
  public StatManager(ShipTemplate template) {
    super();
    this.stats = template.getStats();
    this.MASS = stats.get(Stat.MASS);
    this.ACCELERATION = SCALE * stats.get(Stat.ACCELERATION) / MASS;
    this.BREAK_POWER = SCALE * stats.get(Stat.BREAK_POWER) / MASS;
    this.TURN_SPEED = stats.get(Stat.TURN_SPEED) / MASS;
    this.AIR_RESISTANCE = stats.get(Stat.DRAG);
    this.AIR_CUSHION = 15 * stats.get(Stat.MASS);
    this.JUMP_POWER = stats.get(Stat.JUMP_POWER);
    this.SIZE = stats.get(Stat.SIZE);

    if (stats.get(Stat.WALL_ELASTICITY) != null)
      this.WALL_ELASTICITY = stats.get(Stat.WALL_ELASTICITY);
    if (stats.get(Stat.SHIP_ELASTICITY) != null)
      this.SHIP_COLLISION_ELASTICITY = stats.get(Stat.SHIP_ELASTICITY);

    this.ROTATIONAL_RESISTANCE = AIR_RESISTANCE / 2;
    this.MAX_SPEED = ACCELERATION * ACCELERATION / AIR_RESISTANCE;
    // this = stats.get(Stat.);
  }

  public final float VERTICAL_SCALE = 10;
  public final float ACCELERATION; // How fast does the ship accelerate
  public final float BREAK_POWER; // How fast does it break
  public final float TURN_SPEED; // How fast does it turn
  public final float AIR_RESISTANCE; // How fast do ships slow down (this and
  // acceleration determines the max speed)
  public final float ROTATIONAL_RESISTANCE; // How fast does rotating slow down
  public final float GRAVITY = 15; // The force of gravity affecting the ship
  public final float AIR_CUSHION; // The base force of the ait cushion keeping
  // the hovercraft in the air
  public final float CUSHION_SCALE = 0.8f;
  public final float JUMP_POWER; // Jumping, for science! (testing vertical
  // stuff)
  public final float LEVELLING_SPEED = 0.2f;
  public final float SPEED_OF_ROTATION_WHILE_TURNING = 1.25f;
  public final float MAX_SPEED; // Used for the sound engine
  public final float MASS;
  public final float SIZE;

  // How much energy is retained during ship-wall collisions
  public static float WALL_ELASTICITY = 0.35f;
  // How much energy is retained during ship-ship collisions
  public static float SHIP_COLLISION_ELASTICITY = 0.8f;

}
