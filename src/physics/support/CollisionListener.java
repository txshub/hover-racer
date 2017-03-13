package physics.support;

import physics.core.Ship;

@FunctionalInterface
public interface CollisionListener {

	public void addCollision(Ship first, Ship second);


}
