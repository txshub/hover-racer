package serverComms.junit;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import serverComms.GameNameNumber;
import serverComms.ServerComm;

public class TestGameNameNumber {

  @Test
  public void testToStringWithInt() {
    String name = "Testing";
    int num = 77;
    String expected = num + "|" + name;
    GameNameNumber gnn = new GameNameNumber(name, num);
    if (!gnn.toString().equals(expected))
      fail("toString with int failed");
  }

  @Test
  public void testToStringNoInt() {
    String name = "Testing";
    int num = 77;
    String construct = num + "|" + name;
    GameNameNumber gnn = new GameNameNumber(construct);
    if (!gnn.toString().equals(construct))
      fail("toString no int failed");
  }

  @Test
  public void testToByteArrayWithInt() {
    String name = "Testing";
    int num = 77;
    byte[] expected = (num + "|" + name).getBytes(ServerComm.charset);
    GameNameNumber gnn = new GameNameNumber(name, num);
    if (!Arrays.equals(gnn.toByteArray(), expected))
      fail("Byte arrays don't match with int");
  }

  @Test
  public void testToByteArrayNoInt() {
    String name = "Testing";
    int num = 77;
    String construct = num + "|" + name;
    byte[] expected = (construct).getBytes(ServerComm.charset);
    GameNameNumber gnn = new GameNameNumber(construct);
    if (!Arrays.equals(gnn.toByteArray(), expected))
      fail("Byte arrays don't match with int");
  }

}
