package physics;

import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_DISABLED;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_INSERT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;

import java.nio.DoubleBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;

import entities.Camera;
import entities.CameraFPP;
import entities.Entity;
import entities.Player;
import renderEngine.WindowManager;

public class PhysicsEngine {

	private long windowID;
	private int WIDTH;
	private int HEIGHT;
	
	private DoubleBuffer mouseXPos;
	private DoubleBuffer mouseYPos;
	private double deltaMouseX;
	private double deltaMouseY;
	private double mouseSens = 0.1;

	private double gY = -9.81;
	
	public PhysicsEngine(long windowID, int WIDTH, int HEIGHT) {
		this.windowID = windowID;
		this.WIDTH = WIDTH;
		this.HEIGHT = HEIGHT;
		mouseXPos = BufferUtils.createDoubleBuffer(1);
		mouseYPos = BufferUtils.createDoubleBuffer(1);
		glfwSetInputMode(windowID, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
		glfwSetCursorPos(windowID, WIDTH / 2, HEIGHT / 2);
	}
	
	public void integratePlayer(Player player, double dt) {
		
	
			updatePlayerMovement(player, dt);
			
			float x = player.getPosition().x;
			float y = player.getPosition().y;
			float z = player.getPosition().z;
			float vx = player.getVelocity().x;
			float vy = player.getVelocity().y;
			float vz = player.getVelocity().z;
			
			
			vy += gY*dt;	
			y += vy*dt;

			
			if(collisionDetectPlayerY(y, 1.8f)) {
				y = 1.8f;
				vy = 0;
				player.isJumping = false;
			};
			
			player.setVelocity(vx, vy, vz);
			player.setPosition(x, y, z);
			
		
		
	}
	
	private boolean collisionDetectPlayerY(float y, float threshold) {
		if (y < threshold) {
			return true;
		}
		return false;
	}
	
	private boolean collisionDetectEntityY(float y, float threshold) {
		if (y < threshold) {
			return true;
		}
		return false;
	}

	public void handleInputs(Player player, Camera camera) {

		// Get events:
		glfwPollEvents();
		
		// Mouse events:
		int stateInsert = glfwGetKey(windowID, GLFW_KEY_INSERT);
		if(stateInsert != GLFW_PRESS) {
			glfwSetInputMode(windowID, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
			
			glfwGetCursorPos(WindowManager.windowID, mouseXPos, mouseYPos);
			deltaMouseX = mouseXPos.get(0) - WIDTH / 2;
			deltaMouseY = mouseYPos.get(0) - HEIGHT / 2;

			camera.rotateX((float) Math.toRadians(deltaMouseY * mouseSens));
			player.rotateY((float) Math.toRadians(deltaMouseX * mouseSens));
			glfwSetCursorPos(windowID, WIDTH / 2, HEIGHT / 2);
			
		} else {
			glfwSetInputMode(windowID, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
		}
		
		// Keyboard inputs:
		int stateLSHIFT = glfwGetKey(windowID, GLFW_KEY_LEFT_SHIFT);
		if (stateLSHIFT == GLFW_PRESS) {
			player.reqMoveFast = true;
		} else {
			player.reqMoveFast = false;
		}

		int stateW = glfwGetKey(windowID, GLFW_KEY_W);
		if (stateW == GLFW_PRESS) {
			player.reqMoveForward = true;
		} else {
			player.reqMoveForward = false;
		}

		int stateS = glfwGetKey(windowID, GLFW_KEY_S);
		if (stateS == GLFW_PRESS) {
			player.reqMoveBackward = true;
		} else {
			player.reqMoveBackward = false;
		}

		int stateD = glfwGetKey(windowID, GLFW_KEY_D);
		if (stateD == GLFW_PRESS) {
			player.reqMoveRight = true;
		} else {
			player.reqMoveRight = false;
		}

		int stateA = glfwGetKey(windowID, GLFW_KEY_A);
		if (stateA == GLFW_PRESS) {
			player.reqMoveLeft = true;
		} else {
			player.reqMoveLeft = false;
		}

		int stateSpace = glfwGetKey(windowID, GLFW_KEY_SPACE);
		if (stateSpace == GLFW_PRESS) {
			player.reqJump = true;
		} else {
			player.reqJump = false;
		}
		
		int stateONE = glfwGetKey(windowID, GLFW_KEY_1);
		if (stateONE == GLFW_PRESS) {
			mouseSens += 0.005;
		} 
		
		int stateTWO = glfwGetKey(windowID, GLFW_KEY_2);
		if (stateTWO == GLFW_PRESS) {
			mouseSens -= 0.005;
		}
		
	}

	private void updatePlayerMovement(Player player, double dt) {

		if(player.reqMoveFast) {
			player.setMovementSpeed(player.WALKSPEED*3);
		} else {
			player.setMovementSpeed(player.WALKSPEED);
		}
		
		if (player.reqMoveForward) {player.moveStraight(1);}
		if (player.reqMoveBackward) {player.moveStraight(-1);}
		if (player.reqMoveRight) {player.moveSide(1);}
		if (player.reqMoveLeft) {player.moveSide(-1);}

		if (player.reqJump) {
			if(!player.isJumping) {
				player.setYVelocity(20f);
//				player.isJumping = true;
			}
		}
	}

	public void updateDynamicEntityPhysics(double dt, ArrayList<Entity> entities) {
		
		for(Entity entity : entities) {
			float x = entity.getPosition().x;
			float y = entity.getPosition().y;
			float z = entity.getPosition().z;
			
			float vx = entity.getVelocity().x;
			float vy = entity.getVelocity().y;
			float vz = entity.getVelocity().z;
			
			float sy = entity.getScale().y;
			
//			vy += gY*dt;	
//			y += vy*dt;
			
			if(collisionDetectEntityY(y, 0.5f*sy)) {
				y = sy*0.5f;
				vy = 0;
			};
			
			entity.setVelocity(vx, vy, vz);
			entity.setPosition(x, y, z);
			
		}
		
	}


}
