package gameEngine.renderEngine;

import java.awt.Toolkit;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {

	public static final int WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
	public static final int HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
	public static final int MAX_FPS = 120;

	private static long lastFrameTime;
	private static long delta;
	
	private static long secondTimer = 99l;

	public static void createDisplay() {

		ContextAttribs attribs = new ContextAttribs(3, 2).withForwardCompatible(true).withProfileCore(true);

		try {
			
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create(new PixelFormat(), attribs);
			Display.setTitle("Game Display");
			Display.setVSyncEnabled(true);
			Display.setFullscreen(true);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		lastFrameTime = getCurrentTime();

	}
	
	static int frames = 0;
	static long time = 0;
	public static void updateDisplay() {

		Display.sync(MAX_FPS);
		Display.update();
		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime);
		secondTimer += delta;
		lastFrameTime = currentFrameTime;
		if(secondTimer >= time + 1000){
			System.out.println(frames);
			frames = 0;
			time += 1000;
		}else{
			frames++;
		}

	}
	
	public static float getFrameTimeSeconds(){
		return ((float)delta)/1000f;
	}
	
	public static long getMilliSecondTimer(){
		return secondTimer;
	}

	public static void closeDisplay() {
		Display.destroy();
	}
	
	private static long getCurrentTime(){
		return ((Sys.getTime()*1000)/Sys.getTimerResolution());
	}

}