package entities;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import models.TexturedModel;
import renderEngine.WindowManager;

import static org.lwjgl.glfw.GLFW.*;


import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;

public class Player {
	
	private TexturedModel playerModel;
	
	private Vector3f position;
	private Vector3f velocity;
	private Vector3f rotation;
	private Vector3f scale;
	
	private Vector3f prevPosition;
	private Vector3f prevRotation;
	
	private Vector3f straightDir;
	private Vector3f sideDir;
	public final float WALKSPEED = 0.1f;
	public final float RUNSPEED = 1f;
	private float movementSpeed = 0;

	public enum PlayerState{
		STANDING, JUMPING, WALKING, RUNNING
	};
	
	private PlayerState playerState;
	
	public boolean reqMoveForward = false;
	public boolean reqMoveBackward = false;
	public boolean reqMoveRight = false;
	public boolean reqMoveLeft = false;
	public boolean reqJump = false;
	public boolean reqMoveDown = false;
	public boolean reqMoveFast;
	public boolean isJumping;
	
	

	public Player(TexturedModel playerModel, Vector3f position, Vector3f velocity, Vector3f rotation) {
		this.playerModel = playerModel;
		this.position = position;
		this.velocity = velocity;
		this.rotation = new Vector3f((float) Math.toRadians(rotation.x), (float) Math.toRadians(rotation.y), (float) Math.toRadians(rotation.z));
		this.scale = new Vector3f(1,1,1);
		
		this.prevPosition = new Vector3f(position.x, position.y, position.z);
		this.prevRotation = new Vector3f(rotation.x, rotation.y, rotation.z);
		
		this.straightDir = new Vector3f((float) Math.sin(rotation.y), 0f, (float) -Math.cos(rotation.y));
		this.sideDir = new Vector3f((float) Math.cos(rotation.y), 0f, (float) Math.sin(rotation.y));	
		
		
	}
	
	public void setState(PlayerState s) {
		this.playerState = s;
	}
	
	public PlayerState getState() {
		return this.playerState;
	}
	
	public void updateDirections() {
		
		this.straightDir.x = (float) Math.sin(rotation.y);
		this.straightDir.y = 0f;
		this.straightDir.z = (float) -Math.cos(rotation.y) ;
		
		this.sideDir.x = (float) Math.cos(rotation.y);
		this.sideDir.y = (float) 0;
		this.sideDir.z = (float) Math.sin(rotation.y);
		
	}
	
	public void moveStraight(float direction) {
		this.position.x += movementSpeed*direction*straightDir.x;
		this.position.y += movementSpeed*direction*straightDir.y;
		this.position.z += movementSpeed*direction*straightDir.z;
	}
	
	public void moveSide(float direction) {
		this.position.x += movementSpeed*direction*sideDir.x;
		this.position.y += movementSpeed*direction*sideDir.y;
		this.position.z += movementSpeed*direction*sideDir.z;
	}
	
	public void moveUp(float direction) {
		this.position.y += 0.1*direction;
	}
	
	public void rotateX(float direction) {
		
		this.rotation.x += (float) Math.toRadians(direction);
		updateDirections();
	}
	
	public void rotateY(float direction) {
		this.rotation.y += direction;
		if(rotation.y > 2*Math.PI) {
			rotation.y -= 2*Math.PI;
		} else if(rotation.y < -2*Math.PI) {
			rotation.y += 2*Math.PI;
		}
		updateDirections();
	}
	
	public void rotateZ(float direction) {
		this.rotation.z += (float) Math.toRadians(direction);
		updateDirections();
	}
	
	public TexturedModel getTexturedModel() {
		return this.playerModel;
	}

	public Vector3f getPosition() {
		return position;
	}
	
	public Vector3f getVelocity() {
		return velocity;
	}
	
	public Vector3f getRotation() {
		return rotation;
	}
	
	public Vector3f getRotationDeg() {
		return new Vector3f((float) Math.toDegrees(rotation.x), (float) Math.toDegrees(rotation.y), (float) Math.toDegrees(rotation.z));
	}
	
	public Vector3f getScale() {
		return this.scale;
	}

	public void setMovementSpeed(float speed) {
		this.movementSpeed = speed;
	}
	
	public void setXVelocity(float vx) {
		this.velocity.x = vx;
	}
	
	public void setYVelocity(float vy) {
		this.velocity.y = vy;
	}
	
	public void setZVelocity(float vz) {
		this.velocity.z = vz;
	}
	
	public void setVelocity(float vx, float vy, float vz) {
		this.velocity.x = vx;
		this.velocity.y = vy;
		this.velocity.z = vz;
		
	}

	public void setPosition(float x, float y, float z) {
		this.position.x = x;
		this.position.y = y;
		this.position.z = z;
	}
	
	private void updatePrevPosition() {
		prevPosition.x = position.x;
		prevPosition.y = position.y;
		prevPosition.z = position.z;
	}
	
	private void updatePrevRotation() {
		prevRotation.x = rotation.x;
		prevRotation.y = rotation.y;
		prevRotation.z = rotation.z;
	}
	
	public void updatePrevState() {
		updatePrevPosition();
		updatePrevRotation();
	}
	
	public void interpolateState(double interpolation) {
		float alpha = (float) interpolation;
		position.x = alpha*(position.x - prevPosition.x) + prevPosition.x;
		position.y = alpha*(position.y - prevPosition.y) + prevPosition.y;
		position.z = alpha*(position.z - prevPosition.z) + prevPosition.z;
		
//		rotation.x = alpha*(rotation.x - prevRotation.x) + prevRotation.x;
//		rotation.y = alpha*(rotation.y - prevRotation.y) + prevRotation.y;
//		rotation.z = alpha*(rotation.z - prevRotation.z) + prevRotation.z;
		
	}
	
	

	
	
}
