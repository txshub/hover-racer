package physics.junit;

import org.joml.Vector3f;
import org.junit.Test;

import physics.core.Ship;
import physics.core.Vector3;
import physics.network.ExportedShip;
import physics.ships.DummyShip;

public class TestShipExporting {

  @Test
  public void testExportedShipCreation() {
    ExportedShip ship = new ExportedShip((byte) 0, new Vector3(1, 2, 3), new Vector3(4, 5, 6),
        new Vector3(7, 8, 9), new Vector3(10, 11, 12));

    assert ship.getPosition().x == 1;
    assert ship.getPosition().y == 2;
    assert ship.getPosition().z == 3;

    assert ship.getVelocity().x == 4;
    assert ship.getRotation().y == 8;
    assert ship.getRotationalVelocity().z == 12;
  }

  @Test
  public void testExportedShipConversion() {
    ExportedShip firstShip = new ExportedShip((byte) 0, new Vector3(1, 2, 3), new Vector3(4, 5, 6),
        new Vector3(7, 8, 9), new Vector3(10, 11, 12));

    byte[] exported = firstShip.toNumbers();

    ExportedShip secondShip = new ExportedShip(exported);

    assert firstShip.getPosition().equals(secondShip.getPosition());
    assert firstShip.getVelocity().equals(secondShip.getVelocity());
    assert firstShip.getRotation().equals(secondShip.getRotation());
    assert firstShip.getRotationalVelocity().equals(secondShip.getRotationalVelocity());
  }

  @Test
  public void testFullShipTransportation() {

    Ship firstShip = new DummyShip((byte) 0, null, new Vector3f(1, 2, 3), null, null, null);
    firstShip.setRotation(new Vector3f(4, 5, 6));

    byte[] exported = firstShip.export();

    Ship secondShip = new DummyShip((byte) 0, null, new Vector3f(10, 20, 30), null, null, null);
    secondShip.updateFromPacket(exported);

    assert firstShip.getPosition().equals(secondShip.getPosition());
    assert firstShip.getRotation().equals(secondShip.getRotation());

  }

}