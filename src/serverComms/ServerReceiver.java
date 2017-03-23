package serverComms;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class to read any incoming messages from the client and branch it out as required
 * 
 * @author simon
 *
 */
public class ServerReceiver extends Thread {
  private DetectTimeout detect;
  private String clientName;
  private DataInputStream client;
  private Lobby lobby;
  private GameRoom gameRoom = null;
  private int gameNum = -1;

  /**
   * Creates a ServerReceiver object
   * 
   * @param clientName
   *          The client's name
   * @param client
   *          The stream to read from
   * @param lobby
   *          The lobby in use
   */
  public ServerReceiver(String clientName, DataInputStream client, Lobby lobby) {
    this.clientName = clientName;
    this.client = client;
    this.lobby = lobby;
    detect = new DetectTimeout(lobby.clientTable, clientName);
  }

  /**
   * Runs the receiver (Called by ServerReceiver.start())
   */
  public void run() {
    try {
      while (!this.isInterrupted()) {
        int in = client.readInt();
        byte[] messageIn = new byte[in];
        client.readFully(messageIn);
        detect.messageReceived = true;
        ByteArrayByte fullMsg;
        if (messageIn == null || messageIn.length == 0) {
          fullMsg = new ByteArrayByte(new byte[0], ServerComm.BADPACKET);
        } else {
          fullMsg = new ByteArrayByte(messageIn);
        }
        if (fullMsg.getType() == ServerComm.BADPACKET) {
          System.out.println("Got Bad Packet, ignoring it");
        } else if (fullMsg.getType() == ServerComm.USERSENDING) {
          // Not expected at this stage so print error
          System.err.println("UserSending tag used when not expected");
        } else if (fullMsg.getType() == ServerComm.CLIENTDISCONNECT) {
          lobby.remove(clientName);
        } else if (fullMsg.getType() == ServerComm.DONTDISCONNECT) {
          // Do Nothing - It's just making sure we don't disconnect
        } else if (fullMsg.getType() == ServerComm.SENDALLGAMES) {
          ArrayList<GameRoom> rooms = new ArrayList<GameRoom>();
          for (GameRoom room : lobby.games) {
            if (!room.isBusy()) {
              rooms.add(room);
            }
          }
          String out = "";
          for (GameRoom r : rooms) {
            out += r.toString() + System.lineSeparator();
          }
          lobby.clientTable.getQueue(clientName)
              .offer(new ByteArrayByte(out.getBytes(ServerComm.charset), ServerComm.SENDALLGAMES));
        } else if (fullMsg.getType() == ServerComm.MAKEGAME) {
          lobby.clientTable
              .addGame(new GameSettings(new String(fullMsg.getMsg(), ServerComm.charset)));
          lobby.clientTable.getQueue(clientName)
              .offer(new ByteArrayByte(
                  lobby.clientTable.getGame(lobby.clientTable.getGameID(clientName)).toByteArray(),
                  ServerComm.VALIDGAME));
        } else if (fullMsg.getType() == ServerComm.JOINGAME) {
          IDShipData data = new IDShipData(new String(fullMsg.getMsg(), ServerComm.charset));
          if (!lobby.clientTable.joinGame(data.id, data.data)) {
            lobby.clientTable.getQueue(clientName)
                .offer(new ByteArrayByte(new byte[0], ServerComm.INVALIDGAME));
          } else {
            lobby.clientTable.getQueue(clientName).offer(new ByteArrayByte(
                lobby.clientTable.getGame(data.id).toByteArray(), ServerComm.VALIDGAME));
          }
        } else if (fullMsg.getType() == ServerComm.SENDPLAYERDATA) {
          if (gameRoom != null) {
            gameRoom.updateUser(gameNum, fullMsg.getMsg());
          }
        } else if (fullMsg.getType() == ServerComm.STARTGAME) {
          int gameID = lobby.clientTable.getGameID(clientName);
          if (gameID == -1) {
            lobby.clientTable.getQueue(clientName).offer(new ByteArrayByte(
                ("Can't start invalid game").getBytes(ServerComm.charset), ServerComm.INVALIDGAME));
          } else { // Valid Game
            lobby.clientTable.getGame(gameID).startGame(clientName);
          }
        } else if (fullMsg.getType() == ServerComm.REFRESHROOM) {
          int gameID = lobby.clientTable.getGameID(clientName);
          if (gameID == -1) {
            lobby.clientTable.getQueue(clientName)
                .offer(new ByteArrayByte(new byte[0], ServerComm.INVALIDGAME));
          } else {
            lobby.clientTable.getQueue(clientName).offer(new ByteArrayByte(
                lobby.clientTable.getGame(gameID).toByteArray(), ServerComm.VALIDGAME));
          }
        } else {
          System.out.println("Unknown Message Type: " + fullMsg.getType());
        }
      }
    } catch (IOException e) {
      // What to do?
    }
  }

  /**
   * Sets the gameroom for this client and the client's ID within that room
   * 
   * @param gameRoom
   *          The GameRoom
   * @param gameNum
   *          The client's ID within that room
   */
  public void setGame(GameRoom gameRoom, int gameNum) {
    this.gameRoom = gameRoom;
    this.gameNum = gameNum;
  }
}