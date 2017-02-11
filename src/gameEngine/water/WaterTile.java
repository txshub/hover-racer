package gameEngine.water;

import gameEngine.terrains.Terrain;

public class WaterTile {

	public static final float TILE_SIZE = (Terrain.SIZE/2);
	
	private float height;
	private float x,z;
	
	public WaterTile(float centerX, float centerZ, float height) {
		this.x = centerX;
		this.z = centerZ;
		this.height = height;
	}

	public float getHeight() {
		return height;
	}

	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}
	
}
