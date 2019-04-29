package renderEngine;

public class VertexUVCombo {
	
	private int vertexIndex;
	private int uvIndex;
	private int indicesIndex;
	
	public VertexUVCombo(int vertexIndex, int uvIndex) {
		this.vertexIndex = vertexIndex;
		this.uvIndex = uvIndex;
	}

	public int getVertexIndex() {
		return vertexIndex;
	}

	public int getUVIndex() {
		return uvIndex;
	}
	
	public void setIndicesIndex(int index) {
		this.indicesIndex = index;
	}
	
	public int getIndicesIndex() {
		return indicesIndex;
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof VertexUVCombo) {
			return (this.uvIndex == ((VertexUVCombo) obj).getUVIndex()) && (this.vertexIndex == ((VertexUVCombo) obj).getVertexIndex());
		} else {
			return false;
		}
	}

}
