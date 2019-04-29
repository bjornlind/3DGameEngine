package models;


public class Cube {
	
	float[] vertexCoords;
	int[] indices; 
	float[] textureCoords;
	
	
	public Cube() {
		
		
		vertexCoords = new float[]{			
				-0.5f,0.5f,-0.5f,	
				-0.5f,-0.5f,-0.5f,	
				0.5f,-0.5f,-0.5f,	
				0.5f,0.5f,-0.5f,		
				
				-0.5f,0.5f,0.5f,	
				-0.5f,-0.5f,0.5f,	
				0.5f,-0.5f,0.5f,	
				0.5f,0.5f,0.5f,
				
				0.5f,0.5f,-0.5f,	
				0.5f,-0.5f,-0.5f,	
				0.5f,-0.5f,0.5f,	
				0.5f,0.5f,0.5f,
				
				-0.5f,0.5f,-0.5f,	
				-0.5f,-0.5f,-0.5f,	
				-0.5f,-0.5f,0.5f,	
				-0.5f,0.5f,0.5f,
				
				-0.5f,0.5f,0.5f,
				-0.5f,0.5f,-0.5f,
				0.5f,0.5f,-0.5f,
				0.5f,0.5f,0.5f,
				
				-0.5f,-0.5f,0.5f,
				-0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,0.5f
				
		};
		
		textureCoords = new float[]{
				
				0,0,
				0,1,
				1,1,
				1,0,			
				0,0,
				0,1,
				1,1,
				1,0,			
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0

				
		};
		
		indices = new int[]{
				0,1,3,	
				3,1,2,	
				4,5,7,
				7,5,6,
				8,9,11,
				11,9,10,
				12,13,15,
				15,13,14,	
				16,17,19,
				19,17,18,
				20,21,23,
				23,21,22

		};
		
		
		
//		vertexCoords = new float[] {
//				-1,  1,  1,
//				-1, -1,  1,
//				 1, -1,  1,
//				 1,  1,  1,
//				 
//				 1,  1, -1,
//				 1, -1, -1,
//				-1,  1, -1,
//				-1, -1, -1		
//		};
//		
//		indices = new float[]{
//				1, 2, 3,
//				1, 3, 4,
//				4, 3, 5,
//				3, 6, 5,
//				5, 6, 8,
//				5, 8, 7,
//				7, 2, 1,
//				7, 8, 2,
//				1, 4, 5,
//				1, 5, 7,
//				6, 3, 2,
//				6, 2, 8
//		};
		
	}


	
	public float[] getVertexCoords() {
		return vertexCoords;
	}


	
	public int[] getIndices() {
		return indices;
	}


	
	public float[] getTextureCoords() {
		return textureCoords;
	}

	
	
	
//	fABC = new Face(vA, vB, vC, new Color(Math.random(),Math.random(),Math.random(),1));
//	fACD = new Face(vA, vC, vD, new Color(Math.random(),Math.random(),Math.random(),1));
//	fDCE = new Face(vD, vC, vE, new Color(Math.random(),Math.random(),Math.random(),1));
//	fECF = new Face(vE, vC, vF, new Color(Math.random(),Math.random(),Math.random(),1));
//	fGHE = new Face(vG, vH, vE, new Color(Math.random(),Math.random(),Math.random(),1));
//	fEHF = new Face(vE, vH, vF, new Color(Math.random(),Math.random(),Math.random(),1));
//	fGHB = new Face(vG, vH, vB, new Color(Math.random(),Math.random(),Math.random(),1));
//	fGBA = new Face(vG, vB, vA, new Color(Math.random(),Math.random(),Math.random(),1));
//	fADE = new Face(vA, vD, vE, new Color(Math.random(),Math.random(),Math.random(),1));
//	fAEG = new Face(vA, vE, vG, new Color(Math.random(),Math.random(),Math.random(),1));
//	fCBF = new Face(vC, vB, vF, new Color(Math.random(),Math.random(),Math.random(),1));
//	fBHF = new Face(vB, vH, vF, new Color(Math.random(),Math.random(),Math.random(),1));		
	
//	vA = new Vertex(-1, 1, -1, x, y, z);
//	vB = new Vertex(-1, -1, -1, x, y, z);
//	vC = new Vertex(1, -1, -1, x, y, z);
//	vD = new Vertex(1, 1, -1, x, y, z);
//	vE= new Vertex(1, 1, 1, x, y, z);
//	vF= new Vertex(1, -1, 1, x, y, z);
//	vG= new Vertex(-1, 1, 1, x, y, z);
//	vH= new Vertex(-1, -1, 1, x, y, z);

}
