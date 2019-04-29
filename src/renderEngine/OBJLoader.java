package renderEngine;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector3f;

import models.RawModel;

public class OBJLoader {
	
	public static RawModel loadFromOBJ(String fileName, Loader loader) {
		
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
				
		float[]	verticesArray = null;
		float[]	texturesArray = null;
		float[]	normalsArray = null;
		int[]	indicesArray = null;
		
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
					normalsArray = new float[verticesList.size()*3];
					texturesArray = new float[verticesList.size()*2];
					break;
				}
			}
			

			while(line != null) {
				if(!line.startsWith("f ")) {
					line = br.readLine();
					continue;
				}
				String[] currentLine = line.split(" ");
				String[] vertex1 = currentLine[1].split("/");
				String[] vertex2 = currentLine[2].split("/");
				String[] vertex3 = currentLine[3].split("/");
				
				processVertex(vertex1, indicesList, normalsList, texturesList, texturesArray, normalsArray);
				processVertex(vertex2, indicesList, normalsList, texturesList, texturesArray, normalsArray);
				processVertex(vertex3, indicesList, normalsList, texturesList, texturesArray, normalsArray);
				
				line = br.readLine();
			}
			
			br.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
		
		verticesArray = new float[verticesList.size()*3];
		indicesArray = new int[indicesList.size()];
		
		int vertexPointer = 0;
		for(Vector3f vertex : verticesList) {
			verticesArray[vertexPointer++] = vertex.x;
			verticesArray[vertexPointer++] = vertex.y;
			verticesArray[vertexPointer++] = vertex.z;
		}
		
		for(int i=0; i<indicesList.size(); i++) {
			indicesArray[i] = indicesList.get(i);
		}
		
		return loader.loadToVAO(verticesArray, texturesArray, indicesArray);
		
		
	}
	
	private static void processVertex(String[] vertexData, ArrayList<Integer> indicesList, ArrayList<Vector3f> normalsList,
			ArrayList<Vector2f> texturesList, float[] texturesArray, float[] normalsArray) {
		
		int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
		indicesList.add(currentVertexPointer);
		
		Vector2f currentTex = texturesList.get(Integer.parseInt(vertexData[1])-1);
		texturesArray[currentVertexPointer*2] = currentTex.x;
		texturesArray[currentVertexPointer*2+1] = 1-currentTex.y;
				
		Vector3f currentNorm = normalsList.get(Integer.parseInt(vertexData[2])-1);
		normalsArray[currentVertexPointer*3] = currentNorm.x;
		normalsArray[currentVertexPointer*3+1] = currentNorm.y;
		normalsArray[currentVertexPointer*3+2] = currentNorm.z;
		
		
		
		
		
		
		
		
		
		
		
	}

}
