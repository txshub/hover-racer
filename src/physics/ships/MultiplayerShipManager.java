package physics.ships;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.joml.Vector3f;

import gameEngine.entities.Entity;
import gameEngine.models.TexturedModel;
import gameEngine.objConverter.OBJFileLoader;
import gameEngine.renderEngine.Loader;
import gameEngine.textures.ModelTexture;
import input.InputController;
import physics.core.Ship;
import physics.network.RaceSetupData;
import physics.network.ServerShipProvider;
import physics.support.GroundProvider;

/**
 * A class that manages ship on client side while in multiplayer mode. Handles
 * creating them, updating and communicating with the server.
 * 
 * @author Maciej Bogacki
 */
public class MultiplayerShipManager implements ServerShipProvider {

  private Ship player;
  private Collection<Ship> remotes;
  private Map<Byte, byte[]> packets;

  public MultiplayerShipManager(RaceSetupData data, GroundProvider ground, InputController input,
      Loader loader) {
    this(data.getYourId(), input, makeModels(data.getModels(), data.getTextures(), loader),
        data.getStartingPositions(), ground);
  }

  public MultiplayerShipManager(byte playerId, InputController input, TexturedModel playerTexture,
      Collection<TexturedModel> otherTextures, ArrayList<Vector3f> startingPositions,
      GroundProvider ground) {
    this.packets = new HashMap<Byte, byte[]>();

    // Create all RemoteShips
    this.remotes = new ArrayList<Ship>();
    byte id = 0;
    for (TexturedModel texturedModel : otherTextures) {
      if (id == playerId)
        id++; // Don't give them player's id
      remotes.add(new RemoteShip(id, texturedModel, startingPositions.get(id), ground, this));
    }
    // Tell ships about each other
    remotes.forEach(r -> r.addOtherShips(remotes));
    // Finally create the player
    this.player = new PlayerShip(playerId, playerTexture, startingPositions.get(playerId), remotes,
        ground, input);
    remotes.forEach(r -> r.addOtherShip(player));
  }

  public MultiplayerShipManager(byte playerId, InputController input, List<TexturedModel> models,
      List<Vector3f> startingPositions, GroundProvider ground) {
    this.packets = new HashMap<Byte, byte[]>();

    // Create all RemoteShips
    this.remotes = new ArrayList<Ship>();
    byte id = 0;
    for (TexturedModel texturedModel : models) {
      if (id == playerId)
        id++; // Don't give them player's id
      remotes.add(new RemoteShip(id, texturedModel, startingPositions.get(id), ground, this));
    }
    // Tell ships about each other
    remotes.forEach(r -> r.addOtherShips(remotes));
    // Finally create the player
    this.player = new PlayerShip(playerId, models.get(playerId), startingPositions.get(playerId),
        remotes, ground, input);
    remotes.forEach(r -> r.addOtherShip(player));
  }

  public void updateShips(float delta) {
    player.update(delta);
    remotes.forEach(r -> r.update(delta));
  }

  public void addShipsTo(Collection<Entity> entities) {
    entities.addAll(remotes);
    entities.add(player);
  }

  public Ship getPlayerShip() {
    return player;
  }

  public void addPacket(byte[] packet) {
    packets.remove(packet[0]);
    packets.put(packet[0], packet);
  }

  public byte[] getShipPacket() {
    return player.export();
  }

  @Override
  public Optional<byte[]> getShip(byte id) {
    return Optional.ofNullable(packets.remove(id));
  }

  private static List<TexturedModel> makeModels(List<String> models, List<String> textures,
      Loader loader) {
    List<TexturedModel> res = new ArrayList<TexturedModel>();
    for (int i = 0; i < textures.size(); i++) {
      res.add(makeModel(models.get(i), textures.get(i), loader));
    }
    return res;
  }

  private static TexturedModel makeModel(String model, String texture, Loader loader) {
    return new TexturedModel(OBJFileLoader.loadOBJ("newShip", loader),
        new ModelTexture(loader.loadTexture("newShipTexture")));
  }

}
