package physics.support;

import org.joml.Vector3f;

import gameEngine.terrains.Terrain;

public class TerrainProvider implements GroundProvider{

	Terrain terrain;

	public TerrainProvider(Terrain terrain) {
		this.terrain = terrain;
	}

	@Override
	public float distanceToGround(Vector3f position, Vector3f direction) {
		//TODO directionality
		try{
			float groundHeight = terrain.getHeightOfTerrain(position.x, position.z);
			return position.y - groundHeight;
		}catch(ArrayIndexOutOfBoundsException e){
			System.err.println("Vehicle out of terrain.");
			return 5;
		}
	}

	@Override
	public Vector3f normalToGround(Vector3f point) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
	
	
}
