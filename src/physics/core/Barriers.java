package physics.core;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.joml.Vector3f;

/**
 * Represents the barriers along the track, to be used by the physics engine for collisions.
 * 
 * @author Maciej Bogacki
 */
/** @author mxb551 */
public class Barriers {

  List<Vector3f> track;
  List<ImVector2f> rightPoints;
  List<ImVector2f> leftPoints;

  /**
   * Creates the barriers from points that are used to render them.
   * 
   * @param track
   *          List of the points on the track, as supplied for the rendering engine. Even indexes
   *          are points on the left of the track and odd are points on the right.
   */
  public Barriers(List<Vector3f> track) {
    this.track = track;
    this.rightPoints = new ArrayList<>();
    this.leftPoints = new ArrayList<>();

    // Generate barriers
    for (int i = 0; i < track.size(); i += 2) {
      leftPoints.add(new ImVector2f(track.get(i)));
      rightPoints.add(new ImVector2f(track.get(i + 1)));
    }
  }

  /**
   * Creates all lines a supplied ship collides with (there might occasionally be more than one at a
   * time).
   * 
   * @param ship
   *          Ship to find the collisions for. Only its position and size are used.
   * @return Lines describing walls to collide with. They are all 2d direction vectors, and the x
   *         and z components of the velocity vector should be reflected on them for collisions.
   */
  public List<ImVector2f> allCollisions(Ship ship) {
    List<ImVector2f> res = new LinkedList<>();
    Vector3f pos3d = ship.getPosition();
    ImVector2f pos = new ImVector2f(pos3d.x, pos3d.z);
    for (int i = 0; i < track.size(); i++) {
      res.addAll(collisionVectorsAt(pos, ship.getSize(), i));
    }
    return res;
  }

  /**
   * Returns all collision vectors for a specific point on the track (both left and right walls from
   * that point). For details see {@link allCollisions}.
   */
  private List<ImVector2f> collisionVectorsAt(ImVector2f pos, float size, int point) {
    List<ImVector2f> res = new LinkedList<>();
    collisionVector(pos, left(point + 1), left(point), size).map(v -> res.add(v));
    collisionVector(pos, right(point), right(point + 1), size).map(v -> res.add(v));
    return res;
  }

  /**
   * Returns all collision vectors for a specific wall on the track. For details see
   * {@link allCollisions}.
   */
  private Optional<ImVector2f> collisionVector(ImVector2f pos, ImVector2f start, ImVector2f end,
      float size) {
    if (size < distanceTo(pos, start, end))
      return Optional.empty();
    else
      return Optional.of(end.sub(start));
  }

  /**
   * Finds the distance from a point to a given wall
   * 
   * @param pos
   *          Position of a ship
   * @param start
   *          Point where the wall starts
   * @param end
   *          Point where the wall ends
   * @return Distance the the wall
   */
  private float distanceTo(ImVector2f pos, ImVector2f start, ImVector2f end) {
    float dist2 = start.distanceSquared(end);
    if (dist2 == 0) return pos.distance(start);

    double dist = crossPoint(start, end, pos) / start.distance(end);

    double dot1 = end.sub(start).dot(pos.sub(end));
    if (dot1 > 0) return end.distance(pos);

    double dot2 = start.sub(end).dot(pos.sub(start));
    if (dot2 > 0) return start.distance(pos);

    return (float) Math.abs(dist);

  }

  /** Computes the cross product between ab and ac vectors. */
  private float crossPoint(ImVector2f a, ImVector2f b, ImVector2f c) {
    ImVector2f ab = b.sub(a);
    ImVector2f ac = c.sub(a);
    return ab.getX() * ac.getY() - ab.getY() * ac.getX();
  }

  /**
   * @param i
   *          Index of the point. Values outside the bounds are cast to proper indexes: -1 becomes
   *          the last point etc.
   * @return Point from the left barriers with given index
   */
  private ImVector2f left(int i) {
    return leftPoints.get(i % leftPoints.size());
  }

  /**
   * @param i
   *          Index of the point. Values outside the bounds are cast to proper indexes: -1 becomes
   *          the last point etc.
   * @return Point from the right barriers with given index
   */
  private ImVector2f right(int i) {
    return rightPoints.get(i % rightPoints.size());
  }

}
