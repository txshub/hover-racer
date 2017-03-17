package physics.core;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.joml.Vector3f;

import trackDesign.TrackPoint;

// Added a comment here

public class Barriers {

  List<TrackPoint> track;
  int points;
  List<ImVector2f> middle;
  List<ImVector2f> rightPoints;
  List<ImVector2f> leftPoints;

  public Barriers(List<TrackPoint> track) {
    this.track = track;
    this.points = track.size();
    if (points == 0)
      throw new IllegalArgumentException("Track doen't have any points");
    this.middle = new ArrayList<>();
    this.rightPoints = new ArrayList<>();
    this.leftPoints = new ArrayList<>();

    // Generate the middle of the track
    for (int i = 0; i < points; i++) {
      middle.add(point(i + 1).sub(point(i)));
    }

    // Generate barriers
    for (int i = 0; i < points; i++) {
      ImVector2f side = new ImVector2f(middle.get(i).getY(), -middle.get(i).getX()).normalize()
          .mul(track.get(i).getWidth() / 2);
      leftPoints.add(point(i).add(side));
      rightPoints.add(point(i).sub(side));
    }
  }

  public List<ImVector2f> allCollisions(Ship ship) {
    List<ImVector2f> res = new LinkedList<>();
    Vector3f pos3d = ship.getPosition();
    ImVector2f pos = new ImVector2f(pos3d.x, pos3d.z);
    for (int i = 0; i < points; i++) {
      res.addAll(collisionVectorsAt(pos, ship.getSize(), i));
    }

    // TODO temporary debug data
    if (ship.getId() == 0) {
      // System.out.println(ship.getPosition());
      // System.out.println(pos.distance(left(0)));
      // System.out.println(ship);
      // System.out.println(distanceTo(pos, left(0), left(1), true));
    }
    return res;
  }

  private List<ImVector2f> collisionVectorsAt(ImVector2f pos, float size, int point) {
    List<ImVector2f> res = new LinkedList<>();
    collisionVector(pos, left(point + 1), left(point), size).map(v -> res.add(v));
    collisionVector(pos, right(point), right(point + 1), size).map(v -> res.add(v));
    return res;
  }

  private Optional<ImVector2f> collisionVector(ImVector2f pos, ImVector2f start, ImVector2f end,
      float size) {
    if (size < distanceTo(pos, start, end, false))
      return Optional.empty();
    else
      return Optional.of(end.sub(start));
  }

  private float distanceTo(ImVector2f pos, ImVector2f start, ImVector2f end, boolean printit) {
    float dist2 = start.distanceSquared(end);
    if (dist2 == 0)
      return pos.distance(start);
    /*
     * float t = Math.max(0f, Math.min(1f, new ImVector2f(end).sub(pos).dot(new
     * ImVector2f(start).sub(pos)) / dist2)); ImVector2f projection = new
     * ImVector2f(end).sub(new ImVector2f(start).mul(t)); if (printit)
     * System.out.println("Projection at " + t + ":" + projection + ", between "
     * + start + " and " + end);
     */
    // return end.distance(projection);

    double dist = crossPoint(start, end, pos) / start.distance(end);

    double dot1 = end.sub(start).dot(pos.sub(end));
    if (dot1 > 0)
      return end.distance(pos);

    double dot2 = start.sub(end).dot(pos.sub(start));
    if (dot2 > 0)
      return start.distance(pos);

    return (float) Math.abs(dist);

  }

  private float crossPoint(ImVector2f a, ImVector2f b, ImVector2f c) {
    ImVector2f ab = b.sub(a);
    ImVector2f ac = c.sub(a);
    return ab.getX() * ac.getY() - ab.getY() * ac.getX();
  }

  private ImVector2f point(int i) {
    return new ImVector2f(track.get(i % points));
  }

  private ImVector2f left(int i) {
    return leftPoints.get(i % points);
  }

  private ImVector2f right(int i) {
    return rightPoints.get(i % points);
  }

}
