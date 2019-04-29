package entities;

import org.joml.Vector3f;

import models.TexturedModel;

public class Entity {

	private TexturedModel model;
	private Vector3f position;
	private Vector3f velocity;
	private Vector3f rotation;
	private Vector3f scale;
	
	public Entity(TexturedModel model, Vector3f pos, Vector3f vel, Vector3f rot, Vector3f scale) {
		this.model = model;
		this.position = pos;
		this.velocity = vel;
		this.rotation = new Vector3f(0,0,0);
		this.rotation.x = (float) Math.toRadians(rot.x);
		this.rotation.y = (float) Math.toRadians(rot.y);
		this.rotation.z = (float) Math.toRadians(rot.z);
		this.scale = scale;
	}

	public void translate(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}
	
	public void rotate(float drX, float drY, float drZ) {
		this.rotation.x += Math.toRadians(drX);
		this.rotation.y += Math.toRadians(drY);;
		this.rotation.z += Math.toRadians(drZ);;
	}
	
	public void scale(float scaleX, float scaleY, float scaleZ) {
		this.scale.x = scaleX;
		this.scale.y = scaleY;
		this.scale.z = scaleZ;
	}
	
	public TexturedModel getTexturedModel() {
		return model;
	}

	public void setTexturedModel(TexturedModel model) {
		this.model = model;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public void setX(float x) {
		this.position.x = x;
	}
	
	public void setY(float y) {
		this.position.y = y;
	}

	public void setZ(float z) {
		this.position.z = z;
	}
	
	public void setPosition(float x, float y, float z) {
		this.position.x = x;
		this.position.y = y;
		this.position.z = z;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	public Vector3f getScale() {
		return scale;
	}

	public Vector3f getVelocity() {
		return velocity;
	}

	public void setVelocity(float vx, float vy, float vz) {
		this.velocity.x = vx;
		this.velocity.y = vy;
		this.velocity.z = vz;
		
		
	}
	
	


	
	
	
	
}
