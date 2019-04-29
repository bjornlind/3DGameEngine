package shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL40; 

public abstract class ShaderProgram {
	
	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(4*4);
	
	/**
	 * Creates a vertex and fragment shader. Creates the shader program object. Attaches
	 * the shaders to the shader program object. Links the program object.
	 * BindAttributes: associates a generic vertex attribute indes with a named attribute variable. 
	 * 
	 * @param vertexShaderFile
	 * @param fragmentShaderFile
	 */
	public ShaderProgram(String vertexShaderFile, String fragmentShaderFile) {
		vertexShaderID = loadShader(vertexShaderFile, GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentShaderFile, GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		bindAttributes();
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		getAllUniformLocations();
		

	}
	
	
	protected abstract void getAllUniformLocations();
	
	/**
	 * Returns the location of the uniform variable with name uniformName that is
	 * used in the vertex AND/OR fragment shader.
	 * 
	 * @param uniformName - The name of the uniform variable
	 * @return The location of the uniform variable.
	 */
	protected int getUniformLocation(String uniformName) {
		return GL40.glGetUniformLocation(programID, uniformName);
	}
	
	/**
	 * Sets the float uniform variable specified by the "location" parameter equal to
	 * "value"
	 * 
	 * @param location - The uniform variable location
	 * @param value - The float value to store in the uniform variable
	 */
	protected void loadFloat(int location, float value) {
		GL40.glUniform1f(location, value);
	}
	
	/**
	 * Sets the vec3 uniform variable specified by the "location" parameter equal to
	 * the Vector3f specified by the "vector" parameter.
	 * @param location - The uniform variable location
	 * @param vector - The Vector3f to store in the uniform variable
	 */
	protected void loadVector(int location, Vector3f vector) {
		GL20.glUniform3f(location, vector.x, vector.y, vector.z);
	}
	
	/**
	 * Sets the float uniform variable specified by the "location" parameter equal to
	 * the float value derived from the boolean parameter "value".
	 * Value = true -> 1
	 * Value = false -> 0
	 * 
	 * @param location - The uniform variable location
	 * @param value - The float value to store in the uniform variable
	 */
	protected void loadBoolean(int location, boolean value) {
		GL40.glUniform1f(location, value? 1:0);
	}
	
	/**
	 * Sets the mat4 uniform variable specified by the "location" parameter equal to
	 * the Matrix4f "matrix" parameter
	 * 
	 * @param location - The uniform variable location
	 * @param matrix - The Matrix4f to store in the uniform variable
	 */
	protected void loadMatrix(int location, Matrix4f matrix) {
		matrix.get(matrixBuffer);
		GL40.glUniformMatrix4fv(location, false, matrixBuffer);
	}
	
	/**
	 * Overwritten in subclasses
	 */
	protected abstract void bindAttributes();
	
	/**
	 * Associates the VAO index "attribute" with the shader language variable name "variable name
	 * @param attribute
	 * @param variableName
	 */
	protected void bindAttribute(int attribute, String variableName) {
		GL20.glBindAttribLocation(programID, attribute, variableName);
		
	}

	/**
	 * Creates a new shader. Reads the source code from a file. Compiles the shader.
	 * Makes sure the compilation was successful. Returns the shader handle (shaderID).
	 * 
	 * @param file - The filename of the file that contains the shader source code
	 * @param type - The type of shader that's loaded. Vertex/Fragment/Geometry/etc
	 * @return int - The shader handle (shaderID)
	 */
	private int loadShader(String file, int type) {

		StringBuilder shaderSourceCode = new StringBuilder();

		try {
			InputStream in = Class.class.getResourceAsStream(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = reader.readLine()) != null) {
				shaderSourceCode.append(line).append("\n");
			}
			reader.close();
		} catch (IOException e) {
			System.err.println("Could not read shader file");
			e.printStackTrace();
			System.exit(-1);
		}

		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSourceCode);
		GL20.glCompileShader(shaderID);
		if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			System.err.println("Could not compile shader");
			System.exit(-1);
		}

		return shaderID;

	}

	/**
	 * Makes it possible to use the shader program.
	 */
	public void start() {
		GL20.glUseProgram(programID);
	}
	
	/**
	 * Makes it not possible to use the shader program.
	 */
	public void stop() {
		GL20.glUseProgram(0);
	}
	
	/**
	 * Detaches the shaders from the program.
	 * Deletes the shaders.
	 * Deletes the program from the OpenGL context.
	 */
	public void cleanUp() {
		stop();
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(programID);
	}
	

	

	


}
