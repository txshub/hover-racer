package physics.network;

import com.google.gson.Gson;

import upgrades.ShipTemplate;

/** Data about a ship, sent to the server from each client before the start of
 * the game.
 * 
 * @author Maciej Bogacki */
public class ShipSetupData {

	public String nickname;
	public String model;
	public String texture;
	public ShipTemplate stats;

	public ShipSetupData(String nickname, String model, String texture, ShipTemplate stats) {
		this.nickname = nickname;
		this.model = model;
		this.texture = texture;
		this.stats = stats;
	}

	public String getNickname() {
		return nickname;
	}

	public String getModel() {
		return model;
	}

	public String getTexture() {
		return texture;
	}

	public ShipTemplate getStats() {
		return stats;
	}

	@Override
	public String toString() {
		return (new Gson()).toJson(this);
	}

}
