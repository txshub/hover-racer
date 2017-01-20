package graphics.texture;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

import java.io.IOException;
import java.nio.ByteBuffer;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public class Texture {
  
  public final int id;
  
  public Texture(String fileName) {
    this(loadTexture(fileName));
  }
  
  public Texture(int id) {
    this.id = id;
  }
  
  public int getID() {
    return id;
  }

  private static int loadTexture(String fileName) {
    PNGDecoder decoder = null;
    ByteBuffer buffer = null;
    
    try {
      
      // Load the texture file
      decoder = new PNGDecoder(Texture.class.getResourceAsStream("/textures/" + fileName));
      
      // Load the texture contents into a byte buffer
      buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
      decoder.decode(buffer, decoder.getWidth() * 4, Format.RGBA);
      buffer.flip();
      
    } catch (IOException e) {
      System.err.println("Error loading texture: " + e.getMessage());
    }
    
    // Create a new OpenGL texture and bind it
    int textureID = glGenTextures();
    glBindTexture(GL_TEXTURE_2D, textureID);
    
    // Tell OpenGL how to unpack the bytes. Each component is one byte
    glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
    
    // Upload the texture data
    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(), 
        decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
    
    // Generate a mipmap for scaling of textures
    glGenerateMipmap(GL_TEXTURE_2D);
    
    return textureID;
  }
  
  public void cleanup() {
    glDeleteTextures(id);
  }

}
