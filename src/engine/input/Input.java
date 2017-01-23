package engine.input;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.*;

import engine.graphics.Window;

public class Input {

	public boolean[] keys = new boolean[68836];
	public boolean[] mods = new boolean[68836];
	
	private GLFWCursorPosCallback cursorPosCallback;
	private GLFWKeyCallback keyCallback;
	private GLFWMouseButtonCallback mouseButtonCallback;
	private GLFWScrollCallback scrollCallback;
	private GLFWWindowFocusCallback windowFocusCallback;
	
	private Window window;
	
	public Input(Window window) {
		this.window = window;
		init();
	}
	
	private void onFocusChanged(boolean focused) {
		
	}
	
	private void onKeyPress(int key, int scancode, int mods) {
		keys[key] = true;
	}
	
	private void onKeyRelease(int key, int scancode, int mods) {
		keys[key] = false;
	}
	
	private void onKeyRepeat(int key, int scancode, int mods) {
		
	}
	
	private void onMouseButtonPress(int button, int mods) {
		
	}
	
	private void onMouseButtonRelease(int button, int mods) {
		
	}
	
	private void onMouseButtonRepeat(int button, int mods) {
		
	}
	
	private void onMouseMove(double xpos, double ypos) {
		
	}
	
	private void onMouseScroll(double xoffset, double yoffset) {
		
	}
	
	private void init() {
		glfwSetCursorPosCallback(window.getWindow(),
				cursorPosCallback = new GLFWCursorPosCallback() {
					
					@Override
					public void invoke(long window, double xpos, double ypos) {
						/*
						 * window - the window that received the event
						 * xpos - the new absolute x-value of the cursor
						 * ypos - the new absolute y-value of the cursor
						 */
						onMouseMove(xpos, ypos);
					}
		});
		
		glfwSetKeyCallback(window.getWindow(),
				keyCallback = new GLFWKeyCallback() {
					
					@Override
					public void invoke(long window, int key, int scancode, int action, int mods) {
						/*
						 * window - the window that received the event
						 * key - the keyboard key that was pressed or released
						 * scancode - the system-specific scancode of the key
						 * action - the key action [GLFW.GLFW_PRESS; GLFW.GLFW_RELEASE; GLFW.GLFW_REPEAT]
						 * mods - bitfield describing which modifier keys were held down
						 */
						switch (action) {
						case GLFW_PRESS:
							onKeyPress(key, scancode, mods);
							break;
						case GLFW_RELEASE:
							onKeyRelease(key, scancode, mods);
							break;
						case GLFW_REPEAT:
							onKeyRepeat(key, scancode, mods);
							break;
						}
					}
		});
		
		glfwSetMouseButtonCallback(window.getWindow(),
				mouseButtonCallback = new GLFWMouseButtonCallback() {

					@Override
					public void invoke(long window, int button, int action, int mods) {
						/*
						 * window - the window that received the event
						 * button - the mouse button that was pressed or released
						 * action - the key action [GLFW.GLFW_PRESS; GLFW.GLFW_RELEASE; GLFW.GLFW_REPEAT]
						 * mods - bitfield describing which modifier keys were held down
						 */
						switch (action) {
						case GLFW_PRESS:
							onMouseButtonPress(button, mods);
							break;
						case GLFW_RELEASE:
							onMouseButtonRelease(button, mods);
							break;
						case GLFW_REPEAT:
							onMouseButtonRepeat(button, mods);
							break;
						}
					}
		});
		
		glfwSetScrollCallback(window.getWindow(),
				scrollCallback = new GLFWScrollCallback() {

					@Override
					public void invoke(long window, double xoffset, double yoffset) {
						/*
						 * window - the window that received the event
						 * xoffset - the scroll offset along the x-axis
						 * yoffset - the scroll offset along the y-axis
						 */
						onMouseScroll(xoffset, yoffset);
					}
		});
		
		glfwSetWindowFocusCallback(window.getWindow(),
				windowFocusCallback = new GLFWWindowFocusCallback() {
					
					@Override
					public void invoke(long window, boolean focused) {
						/*
						 * window - the window that received the event
						 * focused - [GL11.GL_TRUE; GL11.GL_FALSE]
						 */
						if (focused)
							onFocusChanged(true);
						else
							onFocusChanged(false);
					}
		});
	}
	
	public void dispose() {
	  keyCallback.free();
		cursorPosCallback.free();
		keyCallback.free();
		mouseButtonCallback.free();
		scrollCallback.free();
		windowFocusCallback.free();
	}
	
}
