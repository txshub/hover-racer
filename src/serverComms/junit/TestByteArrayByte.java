package serverComms.junit;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import serverComms.ByteArrayByte;
import serverComms.ServerComm;

public class TestByteArrayByte {
	
	@Test
	public void testByteArrayByteByteArrayByte() {
		byte type = ServerComm.STATUS;
		byte[] msg = ("JUnit Test").getBytes(ServerComm.charset);
		ByteArrayByte test = new ByteArrayByte(msg, type);
		if(!Arrays.equals(msg, test.getMsg())) fail("Messages don't match");
		if(test.getType() != type) fail("Types don't match");
	}

	@Test
	public void testByteArrayByteByteArray() {
		byte type = ServerComm.CLIENTDISCONNECT;
		byte[] msg = ("JUnit Test").getBytes(ServerComm.charset);
		byte[] out = new byte[msg.length+1];
		out[0] = type;
		for(int i = 0; i < msg.length; i++) {
			out[i+1] = msg[i];
		}
		ByteArrayByte test = new ByteArrayByte(out);
		if(!Arrays.equals(msg, test.getMsg())) fail("Messages don't match");
		if(test.getType() != type) fail("Types don't match"); 
	}

	@Test
	public void testGetMsg() {
		byte type = ServerComm.BADPACKET;
		byte[] msg = ("JUnit Test").getBytes(ServerComm.charset);
		ByteArrayByte test = new ByteArrayByte(msg, type);
		if(!Arrays.equals(msg, test.getMsg())) fail("Messages don't match");
	}

	@Test
	public void testGetType() {
		byte type = ServerComm.MAKEGAME;
		byte[] msg = ("").getBytes(ServerComm.charset);
		ByteArrayByte test = new ByteArrayByte(msg, type);
		if(test.getType() != type) fail("Types don't match");
	}

}
