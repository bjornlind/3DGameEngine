package entities;

import org.joml.Vector3f;

public class PointLight {

	private Vector3f position;
	private Vector3f color;
	private Vector3f attenuation = new Vector3f(1, 0.4f, 0);
	private float ambient;
	private float intensity;
	
	public PointLight(Vector3f position, Vector3f color, float ambient, float intensity) {
		this.position = position;
		this.color = color;
		this.ambient = ambient;
		this.intensity = intensity;
	}
	
	public PointLight(Vector3f position, Vector3f color, Vector3f attenuation, float ambient, float intensity) {
		this.position = position;
		this.color = color;
		this.attenuation = attenuation;
		this.ambient = ambient;
		this.intensity = intensity;
	}
	
	

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}
	
	public float getAmbient() {
		return ambient;
	}
	
	public void setAmbient(float ambient) {
		this.ambient = ambient;
	}
	
	public float getIntensity() {
		return this.intensity;
	}
	
	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}

	public Vector3f getAttenuation() {
		return attenuation;
	}

	public void setAttenuation(Vector3f attenuation) {
		this.attenuation = attenuation;
	}
	
	
	
}
