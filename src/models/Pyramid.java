package models;

public class Pyramid {
	
	private static float[] vertexCoords = new float[] {	
					//Face ABC
					 0.0f,	0.5f,	 0.0f,
					-0.5f, 	0.0f, 	 0.5f,
					 0.5f, 	0.0f, 	 0.5f,
					
					//Face ACD
					 0.0f, 	0.5f, 	 0.0f,
					 0.5f, 	0.0f, 	 0.5f,
					 0.5f, 	0.0f, 	-0.5f,
					
					//Face ADE
					 0.0f, 	0.5f,	 0.0f,
					 0.5f,	0.0f,	-0.5f,
					-0.5f,	0.0f,	-0.5f,
					
					//Face AEB
					 0.0f,	0.5f,	 0.0f,
					-0.5f,	0.0f,	-0.5f,
					-0.5f,	0.0f,	 0.5f,
					
					//Face BED
					-0.5f,	0.0f,	 0.5f,
					-0.5f,	0.0f,	-0.5f,
					 0.5f,	0.0f,	-0.5f,
					
					//Face BDC 
					-0.5f,	0.0f,	 0.5f,
					 0.5f,	0.0f,	-0.5f,
					 0.5f,	0.0f,	 0.5f		
			};
	
	private static int[] indices = new int[] {
			//Face ABC
			0,1,2,

			//Face ACD
			 3,4,5,

			//Face ADE
			 6,7,8,

			//Face AEB
			9,10,11,

			//Face BED
			12,13,14,

			//Face BDC
			15,16,17	
	};
	
	private static float[] textureCoords = new float[] {
			//Face ABC
			0.25f, 0,
			0, 0.5f,
			0.5f, 0.5f,
			
			//Face ACD
			0.25f, 0,
			0, 0.5f,
			0.5f, 0.5f,
			
			//Face ADE
			0.25f, 0,
			0, 0.5f,
			0.5f, 0.5f,
			
			//Face AEB
			0.25f, 0,
			0, 0.5f,
			0.5f, 0.5f,
			
			//Face BED
			0.5f, 0.5f,
			1,1,
			1, 0.5f,
//			0.5f, 0,5f,
//			0,5f, 1,
//			1,1,
			
			//Face BDC 
			0.5f, 0.5f,
			1,1,
			1, 0.5f
	};
	
	
	public static float[] getVertexCoords() {
		return vertexCoords;
	}

	public static int[] getIndices() {
		return indices;
	}

	public static float[] getTextureCoords() {
		return textureCoords;
	}

}
