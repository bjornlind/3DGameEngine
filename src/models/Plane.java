package models;

public class Plane {
	
	private static float[] vertexCoords = new float[]{			
			-0.5f, 0,  0.5f,
			 0.5f, 0, -0.5f,
			-0.5f, 0, -0,5f
			
			-0.5f, 0,  0.5f,
			 0.5f, 0,  0.5f,
			 0.5f, 0, -0.5f
	};
	
	private static int[] indices = new int[] {
			0,1,2,
			3,4,5
	};
	
	private static float[] textureCoords = new float[] {
			0,1,
			1,1,
			1,0,
			
			0,1,
			1,0,
			0,0
			
	};
	
	private static float[] normalCoords = new float[] {
			0,1,0,
			0,1,0,
			0,1,0,
			0,1,0,
			0,1,0,
			0,1,0
	};
	


	public static int[] getIndices() {
		return indices;
	}

	public static float[] getVertexCoords() {
		return vertexCoords;
	}

	public static float[] getTextureCoords() {
		return textureCoords;
	}

	public static float[] getNormalCoords() {
		return normalCoords;
	}

}
