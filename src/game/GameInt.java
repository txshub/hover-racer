package game;


/** Temporary interface for moving between Game and MultiplayerGame
 * 
 * @author Maciej Bogacki */
public interface GameInt {

	public boolean shouldClose();
	public void update(float delta);
	public void render();
	public void cleanUp();

}
