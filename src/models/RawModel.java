package models;

public class RawModel {
	
	private int vaoID;
	private int vertexCount;
	private String modelFileName;
	
	public RawModel(int vaoID, int vertexCount, String modelFileName) {
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
		this.modelFileName = modelFileName;
	}

	public int getVaoID() {
		return vaoID;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public String getModelFileName() {
		return modelFileName;
	}
	
	
	
}
