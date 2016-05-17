package renderEngine;


import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;


public class DisplayManager {

	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	private static final int FPS_CAP = 60;
	
	private static long lastFrameTime;
	private static float delta;
	
	
	public static void createDisplay() {
		
		ContextAttribs attribs = new ContextAttribs(3,3)
				.withForwardCompatible(true).withProfileCore(true);
		
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create( new PixelFormat(), attribs);
			Display.setTitle("First Display");
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		//tell Opengl to use all Display Window
		GL11.glViewport( 0, 0, WIDTH, HEIGHT);
		lastFrameTime = getCurrentTime();
	}
	
	public static void updateDisplay() {
		Display.sync(FPS_CAP);
		Display.update();
		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime)/1000f;
		lastFrameTime = currentFrameTime;
	}
	
	public static float getFrameTimeSeconds(){
		return delta;
	}
	
	public static void closeDisplay() {
		Display.destroy();
		
	}
	
	private static long getCurrentTime() {
		//sys.gettime = x tick
		//gettimeR = tick/second
		//sys/get = tick/tick/second = y second
		//milisecond * 1000 = y * 1000 milisecond
		return Sys.getTime()*1000/ Sys.getTimerResolution();
	}
	
}
