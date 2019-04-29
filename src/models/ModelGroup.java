package models;

import java.util.ArrayList;
import java.util.List;

import entities.Entity;

public class ModelGroup {
	
	private TexturedModel texturedModel;
	private List<Entity> entities;
	
	public ModelGroup(TexturedModel texturedModel) {
		this.texturedModel = texturedModel;
		this.entities = new ArrayList<Entity>();
		
	}
	
	public List<Entity> getEntities(){
		return entities;
	}
	
	public TexturedModel getTexturedModel() {
		return texturedModel;
	}
	
	public void addEntity(Entity entity) {
		entities.add(entity);
	}
	

}
