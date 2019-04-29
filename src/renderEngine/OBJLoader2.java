package renderEngine;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;

import org.joml.Vector2f;
import org.joml.Vector3f;

import models.RawModel;

public class OBJLoader2 {
	
	public static RawModel loadFromOBJ(String fileName, Loader loader) {
		
		System.out.println("Loading OBJ file ...");
		
		FileReader fr = null;
		try {
			fr = new FileReader("src/res/" + fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		BufferedReader br = new BufferedReader(fr);
		String line;
		
		ArrayList<Vector3f> verticesList = new ArrayList<Vector3f>();
		ArrayList<Vector3f> normalsList = new ArrayList<Vector3f>();
		ArrayList<Vector2f> texturesList = new ArrayList<Vector2f>();
		ArrayList<Integer> indicesList = new ArrayList<Integer>();
		
		ArrayList<Vector3f> newVerticesList = new ArrayList<Vector3f>();
		ArrayList<Vector3f> newNormalsList = new ArrayList<Vector3f>();
		ArrayList<Vector2f> newTexturesList = new ArrayList<Vector2f>();
		
		float[]	verticesArray = null;
		float[]	texturesArray = null;
		float[]	normalsArray = null;
		int[]	indicesArray = null;
		
		ArrayList<VertexUVCombo> vertexUVComboList = new ArrayList<VertexUVCombo>();
				

		
		try {
			
			while((line = br.readLine()) != null) {
				
				String[] tokens = line.split(" ");
				
				
				if (tokens[0].equals("v")) {
					Vector3f vertex = new Vector3f(
							Float.parseFloat(tokens[1]), 
							Float.parseFloat(tokens[2]),
							Float.parseFloat(tokens[3]));
					verticesList.add(vertex);
					
				} else if (tokens[0].equals("vt")) {
					Vector2f texture = new Vector2f(
							Float.parseFloat(tokens[1]), 
							Float.parseFloat(tokens[2]));
					texturesList.add(texture);
					
				} else if(tokens[0].equals("vn")) {
					Vector3f normal = new Vector3f(
							Float.parseFloat(tokens[1]), 
							Float.parseFloat(tokens[2]),
							Float.parseFloat(tokens[3]));
					normalsList.add(normal);
					
				} else if(tokens[0].equals("f")) {
					break;
				}
			}
			
			verticesArray = new float[verticesList.size()*3];
			texturesArray = new float[texturesList.size()*3];
			normalsArray = new float[normalsList.size()*3];
			indicesArray = new int[verticesList.size()];
			
			
			while(line != null) {
				if(!line.startsWith("f ")) {
					line = br.readLine();
					continue;
				}
				String[] currentLine = line.split(" ");
				String[] vertex1 = currentLine[1].split("/");
				String[] vertex2 = currentLine[2].split("/");
				String[] vertex3 = currentLine[3].split("/");
				
				processVertex(vertex1, indicesList, verticesList, normalsList, texturesList, newVerticesList, newNormalsList, newTexturesList, vertexUVComboList);
				processVertex(vertex2, indicesList, verticesList, normalsList, texturesList, newVerticesList, newNormalsList, newTexturesList, vertexUVComboList);
				processVertex(vertex3, indicesList, verticesList, normalsList, texturesList, newVerticesList, newNormalsList, newTexturesList, vertexUVComboList);
				
				line = br.readLine();
			}
			
			br.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
		// Initialize the arrays to return:
		verticesArray = new float[newVerticesList.size()*3];
		normalsArray = new float[newNormalsList.size()*3];
		texturesArray = new float[newTexturesList.size()*2];
		indicesArray = new int[indicesList.size()];
		
		
		// Convert vertex list to array:
		int vertexPointer = 0;
		for(Vector3f vertex : newVerticesList) {
			verticesArray[vertexPointer++] = vertex.x;
			verticesArray[vertexPointer++] = vertex.y;
			verticesArray[vertexPointer++] = vertex.z;
		}
		
		// Convert normals list to array:
		int normalsPointer = 0;
		for(Vector3f normal : newNormalsList) {
			normalsArray[normalsPointer++] = normal.x;
			normalsArray[normalsPointer++] = normal.y;
			normalsArray[normalsPointer++] = normal.z;
		}
		
		// Convert texture list to array:
		int texturePointer = 0;
		for(Vector2f texture : newTexturesList) {
			texturesArray[texturePointer++] = texture.x;
			texturesArray[texturePointer++] = 1 - texture.y;
		}
		
		// Convert indicesList to array:
		for(int i=0; i<indicesList.size(); i++) {
			indicesArray[i] = indicesList.get(i);
		}
		
		return loader.loadToVAO(verticesArray, texturesArray, normalsArray, indicesArray, fileName);
		
		
	}
	
	private static void processVertex(String[] vertexData, ArrayList<Integer> indicesList,
			ArrayList<Vector3f> verticesList, ArrayList<Vector3f> normalsList, ArrayList<Vector2f> texturesList,
			ArrayList<Vector3f> newVerticesList, ArrayList<Vector3f> newNormalsList,
			ArrayList<Vector2f> newTexturesList, ArrayList<VertexUVCombo> vertexUVComboList) {
		
		// The v/t/n - indices of current vertex being processed:
		int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
		int currentTexturePointer = Integer.parseInt(vertexData[1]) - 1;
		int currentNormalPointer = Integer.parseInt(vertexData[2]) - 1;
		
		VertexUVCombo temp = new VertexUVCombo(currentVertexPointer, currentTexturePointer);
		
		
		// Check if the current vertex - UV combination has already been read in before:
		if(vertexUVComboList.contains(temp)) {
			// It has been read in before, simply add the same index as before:
			int index = vertexUVComboList.indexOf(temp);
			indicesList.add(vertexUVComboList.get(index).getIndicesIndex());

		} else {
			// It has not been read in before. Add the vertex to the newVerticesList, add
			// the texture to the newTexturesList, add the normal to the newNormalsList.
			// Set the index to point at the newly added entry:
			newVerticesList.add(verticesList.get(currentVertexPointer));
			newTexturesList.add(texturesList.get(currentTexturePointer));
			newNormalsList.add(normalsList.get(currentNormalPointer));
			temp.setIndicesIndex(newVerticesList.size()-1);
			indicesList.add(newVerticesList.size()-1);
			vertexUVComboList.add(temp);
		}


	}

}
