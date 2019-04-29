package entities;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT_CONTROL;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;

import org.joml.Vector3f;

import renderEngine.WindowManager;

public abstract class Camera {
	
	protected Vector3f position;
	protected Vector3f rotation;
	protected long windowID;
	protected float FOV = 50f;
	protected float NEAR = 0.01f;
	protected float FAR = 3000;
	protected final double WIDTH = (double) WindowManager.WIDTH;
	protected final double HEIGHT = (double) WindowManager.HEIGHT;
	
	/**
	 * 
	 * @param windowID - Window handle for the OpenGL context
	 * @param startPosition - Initial position of camera, world coordinates.
	 * @param startRotation - Initial rotation of camera, in radians.
	 */
	protected Camera(long windowID, Vector3f startPosition, Vector3f startRotation) {
		
		this.windowID = windowID;
		this.position = startPosition;
		this.rotation = startRotation;
	}
	
	/**
	 * Rotate the camera about the x-axis.	
	 * @param angle - The angle, in radians, to rotate the camera.
	 */
	public void rotateX(float angle) {
		rotation.x += angle;
	}
	
	/**
	 * Rotate the camera about the y-axis.	
	 * @param angle - The angle, in radians, to rotate the camera.
	 */
	public void rotateY(float angle) {
		this.rotation.y += angle;
	}

	/**
	 * Rotate the camera about the z-axis.	
	 * @param angle - The angle, in radians, to rotate the camera.
	 */
	public void rotateZ(float angle) {
		this.rotation.z += angle;
	}

	protected void increaseFOV(float FOV) {
		this.FOV += Math.toRadians(FOV);
	}
	
	public float getFOV() {
		return FOV;
	}

	public float getNEAR() {
		return NEAR;
	}

	public float getFAR() {
		return FAR;
	}

	public abstract void alignCameraWithPlayer(Player player);

	public void updateCameraSettings() {
		
		// Get events:
		glfwPollEvents();
	
		int stateRSHIFT = glfwGetKey(windowID, GLFW_KEY_RIGHT_SHIFT);
		if(stateRSHIFT == GLFW_PRESS) {
			increaseFOV(10);
		} 
		
		int stateRCTRL = glfwGetKey(windowID, GLFW_KEY_RIGHT_CONTROL);
		if(stateRCTRL == GLFW_PRESS) {
			increaseFOV(-10);
		} 
	}
	
	public Vector3f getRotation() {
		return rotation;
	}

	public Vector3f getPosition() {
		return position;
	}
	
	protected void setPosition(Vector3f position) {
		this.position = position;
	}

	
	
}
