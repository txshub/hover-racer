package renderEngine;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Main {
  private long window;
  
   public void run() {
     init();
     loop();
     
     // Free the window callbacks and destroy the window
     glfwFreeCallbacks(window);
     glfwDestroyWindow(window);

     // Terminate GLFW and free the error callback
     glfwTerminate();
     glfwSetErrorCallback(null).free();
   }
   
   private void init() {
     // Set the error callback
     GLFWErrorCallback.createPrint(System.err).set();
     
     // Initialise GLFW
     if (!glfwInit()) {
       throw new IllegalStateException("Unable to initialise GLFW");
     }
     
     // Create the game window
     window = glfwCreateWindow(640, 480, "Hover-Racer", NULL, NULL);
     if (window == NULL) {
       glfwTerminate();
       throw new RuntimeException("Failed to create the GLFW window");
     }
     
     // Setup a key callback
     glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
       if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
         glfwSetWindowShouldClose(window, true);
     });
     
     // Create the OpenGL context for rendering
     glfwMakeContextCurrent(window);
     // Enable v-sync
     glfwSwapInterval(1);
   }
   
   private void loop() {
     GL.createCapabilities();
     
     glClearColor(0.57f, 0.32f, 0.75f, 0.0f);
     
     // Run the rendering loop until the user has attempted to close the 
     // window or has pressed the ESCAPE key.
     while ( !glfwWindowShouldClose(window) ) {
       glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

       glfwSwapBuffers(window); // swap the color buffers

       // Poll for window events. The key callback above will only be
       // invoked during this call.
       glfwPollEvents();
     }
   }
   
   public static void main(String[] args) {
     new Main().run();
   }
}
