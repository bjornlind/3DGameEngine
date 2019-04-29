package renderEngine;

import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import client.ClientOpponent;
import entities.Entity;
import entities.Player;
import models.ModelGroup;
import models.RawModel;
import models.TexturedModel;
import shaders.StaticShader;
import toobox.PipelineMatrices;

public class EntityRenderer {
	
	private StaticShader shader;

	public EntityRenderer(StaticShader shader){
		this.shader = shader;
	}
	
	/**
	 * Renders static entities (entities that do not change position, velocity,
	 * scale or composition). For each list of entities in the staticEntitiesMap,
	 * the same textured model is used, which is extracted from the
	 * texturedModelsMap.
	 * 
	 * @param staticEntitiesMap - Key = Model name. Value = List of all
	 *                          entities sharing the same ModelName.
	 * @param texturedModelsMap - Key = Model name. Value = TexturedModel belonging
	 *                          to the ModelName.
	 */
	public void renderStaticEntities(Map<String, ModelGroup> staticEntities) {
		TexturedModel activeTexturedModel;
		for(String modelName : staticEntities.keySet()) {
			activeTexturedModel = staticEntities.get(modelName).getTexturedModel();
			prepareTexturedModel(activeTexturedModel);
			for(Entity entity : staticEntities.get(modelName).getEntities()) {
				prepareInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, activeTexturedModel.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
				
			}
			unbindTexturedModel();
			
		}
	}
	
	/**
	 * Renders all players connected to the server.
	 * @param mainPlayer - The player on this client.
	 * @param clients - The map of remote clients connected to the server.
	 */
	public void renderPlayers(Player mainPlayer, Map<Float, ClientOpponent> clients) {
		// Prepare player model:
		TexturedModel playerModel = mainPlayer.getTexturedModel();
		prepareTexturedModel(playerModel);
		
		// Render main player:
		shader.loadTransformationMatrix(PipelineMatrices.createTransformationMatrix(mainPlayer.getPosition(),
				mainPlayer.getRotation(), mainPlayer.getScale()));
		GL11.glDrawElements(GL11.GL_TRIANGLES, mainPlayer.getTexturedModel().getRawModel().getVertexCount(),
				GL11.GL_UNSIGNED_INT, 0);
		
		// Render all remote client players:
		for (Float user : clients.keySet()) {
			ClientOpponent clientOpponent = clients.get(user);
			shader.loadTransformationMatrix(PipelineMatrices.createTransformationMatrix(clientOpponent.getPosition(),
					clientOpponent.getOrientation(), clientOpponent.getScale()));
			GL11.glDrawElements(GL11.GL_TRIANGLES, mainPlayer.getTexturedModel().getRawModel().getVertexCount(), 
					GL11.GL_UNSIGNED_INT, 0);
		}
		
		// Remove player model:
		unbindTexturedModel();
		
	}
	
	/**
	 * Binds the provided textured model to the OpenGL context. 
	 * @param texturedModel - The textured model to bind.
	 */
	private void prepareTexturedModel(TexturedModel texturedModel) {
		RawModel rawModel = texturedModel.getRawModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		shader.loadTextureProperties(texturedModel.getTexture().getReflectivity(), texturedModel.getTexture().getShininess());
		GL30.glActiveTexture(GL30.GL_TEXTURE0);
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, texturedModel.getTexture().getTextureID());
	}
	
	/**
	 * Unbinds the currently bound textured model from the OpenGL context.
	 */
	private void unbindTexturedModel() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	/**
	 * Loads the shader with the transformation matrix defined by the provided entity's current state.
	 * @param entity - The entity whose state is to define the transformation matrix.
	 */
	private void prepareInstance(Entity entity) {
		shader.loadTransformationMatrix(PipelineMatrices.createTransformationMatrix(entity.getPosition(), entity.getRotation(), entity.getScale()));
	}
		
}
