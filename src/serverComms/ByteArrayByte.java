package serverComms;

/**
 * Container for Client-Server communications, holds a byte to specify the type of message and also
 * a byte array for the actual message
 * 
 * @author simon
 *
 */
public class ByteArrayByte {

  private byte[] msg;
  private byte type;

  /**
   * Create the object
   * 
   * @param msg
   *          The message to send
   * @param type
   *          The type of message
   */
  public ByteArrayByte(byte[] msg, byte type) {
    this.msg = msg;
    this.type = type;
  }

  /**
   * Creates the message with the message & type in one array
   * 
   * @param typeMessage
   *          The type & message together
   */
  public ByteArrayByte(byte[] typeMessage) {
    this.type = typeMessage[0]; // Get the type
    msg = new byte[typeMessage.length - 1];
    for (int i = 1; i < typeMessage.length; i++) {
      msg[i - 1] = typeMessage[i]; // Get the rest of the msg
    }

  }

  /**
   * Returns the message
   * 
   * @return the message
   */
  public byte[] getMsg() {
    return msg;
  }

  /**
   * Returns the type of message
   * 
   * @return the type of message
   */
  public byte getType() {
    return type;
  }

}
