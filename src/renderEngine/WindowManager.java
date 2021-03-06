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

public class WindowManager {
	
	public static long windowID;
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	
	
	public void initWindow() {
		
		
		System.out.println("Using LWJGL version:  " + Version.getVersion());
		
		/* Set up error callback. (Most events are reported through callbacks,
		 * whether it's a key being pressed, a GLFW window being moved, or an
		 * error occurring. Callbacks are simply C functions (or C++ static methods)
		 * that are called by GLFW with arguments describing the event.)
		 * The default implementation will print the error message in System.err.
		 * GLFWErrorCallback is a class
		 * createPrint() is a static method in GLFWErrorCallback that returns an instance of the class
		 * 
		*/
		GLFWErrorCallback.createPrint(System.err).set();
		
		/* Initialize GLFW library. Most GLFW functions will not work before doing this:
		 * ( Returns true if successful, false if failed. If it fails, it will automaticall call
		 * terminate(); )
		 * 
		*/
		if (!glfwInit()) {
			throw new RuntimeException("Failed to initialize GLFW library");
		}
		
		// Configure GLFW. (These are static methods in the glfw class
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE,GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE,GLFW_TRUE);
		
		// Create the window
		windowID = glfwCreateWindow(WIDTH, HEIGHT, "OpenGL Game 1", NULL, NULL);
		
		if (windowID == NULL) {
			throw new RuntimeException("Could not create the GLFW window");
		}
		

		/*
		 * Set up a key callback. Called every time a key is pressed,
		 */
			glfwSetKeyCallback(windowID, new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
					glfwSetWindowShouldClose(window, true);
				}
			}
		});
		
		/*
		 * Get the thread stack and push a new frame:
		 */
		try(MemoryStack stack = stackPush()){
			
			IntBuffer pWidth = stack.mallocInt(1); //int*
			IntBuffer pHeight = stack.mallocInt(1); //int*
			
			// Get the window sized passed earlier to glfwCreateWindow
			glfwGetWindowSize(windowID, pWidth, pHeight);
			
			// Get the resolution of the primary monitor:
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			
			// Center the window:
			glfwSetWindowPos(
				windowID,
				(vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2
			);
		} // The stack frame is popped automatically
		
		/*
		 * Make the OpenGL context current:
		 */
		glfwMakeContextCurrent(windowID);
		
		/*
		 * Enable(1)/Disable(0) VSYNC
		 */
		glfwSwapInterval(1);
		
		/*
		 * Make the window visible
		 */
		glfwShowWindow(windowID);
		
		/*
		 * This line is critical for LWJGL's interoperation with GLFW's OpenGL context,
		 * or any context that is managed externally. LWJGL detects the context that is
		 * current in the current thread, creates the GLCapabilities instance and makes
		 * the OpenGL bindings available for use.
		 */
		GL.createCapabilities();

		glViewport(0, 0, WIDTH, HEIGHT);

		
	}
	
	public void closeWindow() {
		
		// After looping finishes, free the window callbacks and destroy the window:
		glfwFreeCallbacks(windowID);
		glfwDestroyWindow(windowID);
		
		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
		
	}
	
	public boolean isCloseRequested() {
		
		return glfwWindowShouldClose(windowID);
		
	}
	
	public void updateWindow() {

			// clear the frame buffer
//			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			// swap the color buffers
			glfwSwapBuffers(windowID);

			// Poll for window events. The key callback above will only be invoked during
			// this call:
			glfwPollEvents();

		
	}
}
