package serverComms;

public class ByteArrayByte {

	private byte[] msg;
	private byte type;
	
	public ByteArrayByte(byte[] msg, String tag) {
		this.msg = msg;
		this.type = Byte.parseByte(tag, 2);
	}
	
	public ByteArrayByte(byte[] typeMessage) {
		this.type = typeMessage[0];
		msg = new byte[typeMessage.length-1];
		for(int i = 1; i < typeMessage.length; i++) {
			msg[i-1] = typeMessage[i];
		}
		
	}

	public byte[] getMsg() {
		return msg;
	}
	
	public byte getType() {
		return type;
	}
	
}