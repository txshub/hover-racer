package physics.network;

import java.nio.ByteBuffer;

import org.joml.Vector3f;

import physics.core.Ship;
import physics.core.Vector3;
import physics.ships.RemoteShip;

/**
 * An object representing a Ship entity, exported for transmission over the
 * network. It serves as an intermediate step between a Ship object and a byte
 * array and does all the conversion necessary.
 * 
 * @author Maciej Bogacki
 */
public class ExportedShip {

  byte id;
  Vector3 position;
  Vector3 velocity;
  Vector3 rotation;
  Vector3 rotationalVelocity;

  public ExportedShip(byte id, Vector3 position, Vector3 velocity, Vector3 rotation,
      Vector3 rotationalVelocity) {
    this.id = id;
    this.position = position;
    this.velocity = velocity;
    this.rotation = rotation;
    this.rotationalVelocity = rotationalVelocity;
  }

  public ExportedShip(byte[] numbers) {
    ByteBuffer buffer = ByteBuffer.wrap(numbers);
    this.id = buffer.get(); // This moves the buffer - order matters
    this.position = makeVector(buffer);
    this.velocity = makeVector(buffer);
    this.rotation = makeVector(buffer);
    this.rotationalVelocity = makeVector(buffer);
  }

  public byte[] toNumbers() {
    ByteBuffer buffer = ByteBuffer.allocate(49); // (4 per float)*(3 per
    // vector)*(4 vectors)
    buffer.put(id);
    addVector(position, buffer);
    addVector(velocity, buffer);
    addVector(rotation, buffer);
    addVector(rotationalVelocity, buffer);
    return buffer.array();
  }

  private void addVector(Vector3 vector, ByteBuffer buffer) {
    buffer.putFloat(vector.x);
    buffer.putFloat(vector.y);
    buffer.putFloat(vector.z);
  }

  private Vector3 makeVector(ByteBuffer buffer) {
    return new Vector3(buffer.getFloat(), buffer.getFloat(), buffer.getFloat());
  }

  public Vector3 getPosition() {
    return position;
  }

  public Vector3 getVelocity() {
    return velocity;
  }

  public Vector3 getRotation() {
    return rotation;
  }

  public Vector3 getRotationalVelocity() {
    return rotationalVelocity;
  }

  public static void main(String[] args) {
    ExportedShip first = new ExportedShip((byte) 42, new Vector3(1, 2, 3), new Vector3(6, 7, 8),
        new Vector3(11, 12, 13), new Vector3(26, 27, 28));
    byte[] numbers = first.toNumbers();
    // System.out.println(numbers[6]);
    // System.out.println(first);
    System.out.println(new ExportedShip(numbers));

    Ship testShip = new RemoteShip((byte) 2, null, new Vector3f(12, 7, 82), null, null, null, null);
    byte[] second = testShip.export();
    Ship receiver = new RemoteShip((byte) 2, null, new Vector3f(3, 4, 45), null, null, null, null);
    receiver.updateFromPacket(second);
    System.out.println(receiver.getPosition());
  }

  public static void printPacket(byte[] packet) {
    if (packet.length == 49)
      System.out.println(new ExportedShip(packet));
    else if (packet.length % 49 == 0) {
      for (int i = 0; i < packet.length / 49; i++) {
        byte[] nextOne = new byte[49];
        System.arraycopy(packet, 49 * i, nextOne, 0, 49);
        System.out.println(new ExportedShip(nextOne));
      }
    } else
      System.err.println("Invalid packet length: " + packet.length);
  }

  @Override
  public String toString() {
    String res = "Id: " + id + "\nPosition: " + position + "\nVelocity: " + velocity
        + "\nRotation: " + rotation + "\nRotational Velocity: " + rotationalVelocity;
    return res;
  }

}
