package renderEngine;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.GL;

public class Main {
  
  long window;

  public Main() {
    if (!glfwInit()) {
      throw new IllegalStateException("Unable to initialize GLFW");
    }
    
    window = glfwCreateWindow(640, 480, "Hover-Racer", NULL, NULL);
    
    glfwShowWindow(window);
    
    glfwMakeContextCurrent(window);
    GL.createCapabilities();
    
    while(!glfwWindowShouldClose(window)) {
      glfwPollEvents();
      
      glClear(GL_COLOR_BUFFER_BIT);
      
      glBegin(GL_QUADS);
        glColor4f(1, 0, 0, 0);
        glVertex2f(-0.5f, 0.5f);
        
        glColor4f(0, 1, 0, 0);
        glVertex2f(0.5f, 0.5f);

        glColor4f(0, 0, 1, 0);
        glVertex2f(0.5f, -0.5f);

        glColor4f(1, 1, 1, 0);
        glVertex2f(-0.5f, -0.5f);
      glEnd();
      
      glfwSwapBuffers(window);
    }
    
    glfwTerminate();
  }

  public static void main(String[] args) {
    new Main();
  }

}
