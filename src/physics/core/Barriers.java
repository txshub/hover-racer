package physics.core;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.joml.Vector2f;
import org.joml.Vector3f;

import trackDesign.TrackPoint;

public class Barriers {

	List<TrackPoint> track;
	int points;
	List<Vector2f> middle;
	List<Vector2f> leftPoints;
	List<Vector2f> rightPoints;

	public Barriers(List<TrackPoint> track) {
		this.track = track;
		this.points = track.size();
		if (points == 0) throw new IllegalArgumentException("Track doen't have any points");
		this.middle = new ArrayList<>();
		this.leftPoints = new ArrayList<>();
		this.rightPoints = new ArrayList<>();

		// Generate the middle of the track
		for (int i = 0; i < points; i++) {
			middle.add(point(i + 1).sub(point(i)));
		}

		// Generate barriers
		for (int i = 0; i < points; i++) {
			Vector2f side = new Vector2f(middle.get(i).y, -middle.get(i).x).normalize().mul(track.get(i).getWidth());
			rightPoints.add(point(i).add(side));
			leftPoints.add(point(i).sub(side));
		}
	}

	public List<Vector2f> allCollisions(Ship ship) {
		List<Vector2f> res = new LinkedList<>();
		for (int i = 0; i < points; i++) {
			Vector3f pos = ship.getPosition();
			res.addAll(collisionVectorsAt(new Vector2f(pos.x, pos.z), ship.getSize(), i));
		}
		return res;
	}

	private List<Vector2f> collisionVectorsAt(Vector2f pos, float size, int point) {
		List<Vector2f> res = new LinkedList<>();
		collisionVector(pos, left(point), left(point + 1), size).map(v -> res.add(v));
		collisionVector(pos, right(point), right(point + 1), size).map(v -> res.add(v));
		return res;
	}

	private Optional<Vector2f> collisionVector(Vector2f pos, Vector2f start, Vector2f end, float size) {
		if (size < distanceTo(pos, start, end)) return Optional.empty();
		else return Optional.of(new Vector2f(end).sub(start));
	}

	private float distanceTo(Vector2f pos, Vector2f start, Vector2f end) {
		float dist2 = start.distanceSquared(end);
		if (dist2 == 0) return pos.distance(start);
		float t = Math.max(0f, Math.min(1f, new Vector2f(end).sub(pos).dot(new Vector2f(start).sub(pos)) / dist2));
		Vector2f projection = new Vector2f(pos).add(new Vector2f(end).sub(start).mul(t));
		return end.distance(projection);
	}

	private Vector2f point(int i) {
		return new Vector2f(track.get(i % points));
	}

	private Vector2f left(int i) {
		return new Vector2f(leftPoints.get(i % points));
	}
	private Vector2f right(int i) {
		return new Vector2f(rightPoints.get(i % points));
	}



}
