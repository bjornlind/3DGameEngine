package renderEngine;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;

import models.RawModel;

import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.stb.STBImage.*;

public class Loader {
	
	private ArrayList<Integer> vaos = new ArrayList<Integer>();
	private ArrayList<Integer> vbos = new ArrayList<Integer>();
	private ArrayList<Integer> textures = new ArrayList<Integer>();
	
	
	/**
	 * Loads vertex data, index data, texture data etc. to a new VAO
	 * @param vertices - Vertex position array
	 * @param indices - Vertex index array
	 * @return - RawModel with the VAO and index length (vertex count)
	 */
	public RawModel loadToVAO(float[] vertices, float[] textureCoords, float[] normals, int[] indices, String modelFileName) {
		
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0, vertices, 3);
		storeDataInAttributeList(1, textureCoords, 2);
		storeDataInAttributeList(2, normals, 3);
		unbindVAO();
		return new RawModel(vaoID, indices.length, modelFileName);
	}
	
	/**
	 * Creates a vertex array object and binds it to the current context.
	 * The VAO-ID is also added to the list of current VAOs.
	 * @return - The VAO handle (VAO ID)
	 */
	private int createVAO() {
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}
	
	/**
	 * Creates a new VBO to hold vertex data. Adds the VBO to the list of
	 * current VBOs. Binds the VBO to the OpenGL array buffer. Creates a float
	 * buffer and feeds the OpenGL array buffer with the float buffer data.
	 * Specifies the location in the VAO to store the VBO and its organization 
	 * (how many data values per vertex). Unbinds the VBO.
	 * 
	 * @param attributeNbr - The VAO index to add the data in
	 * @param data         - The supplied vertex data (could be position, color,
	 *                     etc)
	 */
	private void storeDataInAttributeList(int attributeNbr, float[] data, int coordinateSize) {
		
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = floatArrayDataToFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNbr, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	/**
	 * Creates a VBO to feed the index array. Adds the VBO to the list of current
	 * VBOs. The VBO is bound to the element array buffer of OpenGL. The element
	 * array buffer of OpenGL (which is now our VBO) is fed an Integer buffer
	 * containing the indices in the glBufferData method
	 * 
	 * @param indices - The vertex indices
	 */
	private void bindIndicesBuffer(int[] indices) {
		int vboIDElement = GL15.glGenBuffers();
		vbos.add(vboIDElement);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboIDElement);
		IntBuffer buffer = intArrayDataToIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	
	/**
	 * Reads texture data. Creates VBO and attaches the texture data. Adds VBO to VAO.
	 * @param fileName - Name of the image file (incl. file extension)
	 * @return The textureID
	 */
	public int loadTexture(String fileName) {
		
		System.out.println("Loading texture ...");
		
		// Loading the image:
		MemoryStack stack = stackPush();
		IntBuffer w = stack.mallocInt(1);
		IntBuffer h = stack.mallocInt(1);
		IntBuffer comp = stack.mallocInt(1);
		ByteBuffer buffer = stbi_load("src/res/" + fileName, w, h, comp, 0);
		if(buffer == null) {
			throw new RuntimeException("Failed to load texture file." + System.lineSeparator() + stbi_failure_reason());
		}
		int width = w.get();
		int height = h.get();
	
		// Create texture. Add to list. Bind texture:
		int textureID = GL30.glGenTextures();
		textures.add(textureID);
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, textureID);
		
		// Wrapping:
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_WRAP_S, GL30.GL_REPEAT);
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_WRAP_T, GL30.GL_REPEAT);
		
//		// Filtering:
//		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_NEAREST);
//		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_NEAREST);
		
		// Store image on GPU:
		GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0, GL30.GL_RGBA, width, height, 0, GL30.GL_RGBA, GL30.GL_UNSIGNED_BYTE, buffer);
		
		// MipMap:
		GL30.glGenerateMipmap(GL30.GL_TEXTURE_2D);
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_LINEAR_MIPMAP_LINEAR);
		GL30.glTexParameterf(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_LOD_BIAS, 0.0f);
		
		// Unbind and return texture handle
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, 0);
		return textureID;
		
	}
		
	/**
	 * Creates an integer buffer to hold the supplied data array. Flips the buffer
	 * so it's ready to be read.
	 * 
	 * @param data
	 * @return An integer buffer containing the parameter data array.
	 */
	private IntBuffer intArrayDataToIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
		
	}
	
	/**
	 * Creates a float buffer to store the supplied float array data 
	 * @param data - The float array
	 * @return - The float buffer
	 */
	private FloatBuffer floatArrayDataToFloatBuffer(float[] data) {
		FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(data.length);
		floatBuffer.put(data);
		floatBuffer.flip();
		return floatBuffer;
	}
	
	/**
	 * Unbinds the currently bound VAO
	 */
	private void unbindVAO() {
		GL30.glBindVertexArray(0);
	}
	
	/**
	 * Clears the VAOs and VBOs lists. Called at the end of a program.
	 */
	public void cleanUp() {
		for(int vao : vaos) {
			GL30.glDeleteVertexArrays(vao);
		}
		for(int vbo: vbos) {
			GL15.glDeleteBuffers(vbo);
		}
		for(int texture: textures) {
			GL15.glDeleteTextures(texture);
		}
	}
		

}
