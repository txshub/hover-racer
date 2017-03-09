package serverComms.junit;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import clientComms.Client;
import clientComms.ClientReceiver;
import serverComms.ServerComm;
import userInterface.GameMenu;

public class DummyClient extends Client {

  ClientReceiver receiver;
  String clientName;
  boolean testsPassed = false;

  public DummyClient(String clientName, int portNumber, String machineName, GameMenu gameMenu) {
    super(clientName, portNumber, machineName, gameMenu);
    this.clientName = clientName;
  }

  @Override
  public void run() {
    Socket server;
    DataInputStream fromServer;
    try {
      server = new Socket("localhost", 5153);
      toServer = new DataOutputStream(new BufferedOutputStream(server.getOutputStream()));
      fromServer = new DataInputStream(new BufferedInputStream(server.getInputStream()));
      receiver = new DummyReceiver(fromServer, this);
      receiver.start();
      super.sendByteMessage(clientName.getBytes(ServerComm.charset), ServerComm.USERSENDING);
      Thread.sleep(1000);
      testsPassed = ((DummyReceiver) receiver).testsPassed;
    } catch (IOException e) {

    } catch (InterruptedException e) {

    }

  }

}
