package shaders;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import entities.PointLight;

public class StaticShader extends ShaderProgram {

	private static final String VERTEX_FILE = "/shaders/vertexShader.txt";
	private static final String FRAGMENT_FILE = "/shaders/fragmentShader.txt";
	
	private int location_uniform_transformationMatrix;
	private int location_uniform_viewMatrix;
	private int location_uniform_projectionMatrix;
	private int location_uniform_lightPosition;
	private int location_uniform_lightColor;
	private int location_uniform_shininess;
	private int location_uniform_reflectivity;
	private int location_uniform_ambient;
	private int location_uniform_lightIntensity;
	private int location_uniform_lightAttenuation;
	

	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);

	}

	/**
	 * Binds the shader attribute name to the VAO index
	 */
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");

	}

	/**
	 * Retrieves the location of all the uniform variables used by this shader.
	 */
	@Override
	protected void getAllUniformLocations() {
		location_uniform_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_uniform_viewMatrix = super.getUniformLocation("viewMatrix");
		location_uniform_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_uniform_lightPosition = super.getUniformLocation("lightPosition");
		location_uniform_lightColor = super.getUniformLocation("lightColor");
		location_uniform_shininess = super.getUniformLocation("shininess");
		location_uniform_reflectivity = super.getUniformLocation("reflectivity");
		location_uniform_ambient = super.getUniformLocation("ambient");
		location_uniform_lightIntensity = super.getUniformLocation("lightIntensity");
		location_uniform_lightAttenuation = super.getUniformLocation("lightAttenuation");
	}
	
	/**
	 * Stores a Matrix4f into the "transformationMatrix" uniform variable
	 * @param matrix - The Matrix4f to store in the "transformationMatrix" uniform variable
	 */
	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(location_uniform_transformationMatrix, matrix);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix) {
		super.loadMatrix(location_uniform_projectionMatrix, matrix);
	}
	
	public void loadViewMatrix(Matrix4f matrix) {
		super.loadMatrix(location_uniform_viewMatrix, matrix);
	}
	
	public void loadLight(PointLight light) {
		super.loadVector(location_uniform_lightPosition, light.getPosition());
		super.loadVector(location_uniform_lightColor, light.getColor());
		super.loadFloat(location_uniform_ambient, light.getAmbient());
		super.loadFloat(location_uniform_lightIntensity, light.getIntensity());
		super.loadVector(location_uniform_lightAttenuation, light.getAttenuation());
	}
	
	public void loadTextureProperties(float reflectivity, float shininess) {
		super.loadFloat(location_uniform_reflectivity, reflectivity);
		super.loadFloat(location_uniform_shininess, shininess);
	}
	
	
	
	

}
