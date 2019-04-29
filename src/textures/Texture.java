package textures;

import renderEngine.Loader;

public class Texture {

	private int textureID;
	private String textureFileName;
	private float reflectivity = 0;
	private float shininess = 1;
	
	public Texture(String fileName, Loader loader) {
		this.textureFileName = fileName;
		this.textureID = loader.loadTexture(fileName);
	}

	public int getTextureID() {
		return textureID;
	}
	
	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}

	public float getShininess() {
		return shininess;
	}

	public void setShininess(float shininess) {
		this.shininess = shininess;
	}

	public String getTextureFileName() {
		return textureFileName;
	}
	
}
