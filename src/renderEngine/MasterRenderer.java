package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL40;

import client.ClientOpponent;
import entities.Camera;
import entities.Entity;
import entities.Player;
import entities.PointLight;
import models.ModelGroup;
import models.TexturedModel;
import shaders.StaticShader;
import toobox.PipelineMatrices;

public class MasterRenderer {

	private StaticShader entityStaticShader;
	private EntityRenderer entityRenderer;
	private Map<String, ModelGroup> staticEntities; 
	private Map<Float, ClientOpponent> clients;
	private Player player;
	
	private final float WIDTH = (float) WindowManager.WIDTH;
	private final float HEIGHT = (float) WindowManager.HEIGHT;
		
	public MasterRenderer(Player player, Map<Float, ClientOpponent> clients, Map<String, ModelGroup> staticEntities) {
		GL40.glEnable(GL40.GL_CULL_FACE);
		GL40.glCullFace(GL40.GL_BACK);
		
		this.player = player;
		this.clients = clients;
		this.entityStaticShader = new StaticShader();
		this.entityRenderer = new EntityRenderer(entityStaticShader);
		this.staticEntities = staticEntities;
		
	}
	
	public void render(Camera camera, PointLight light) {
		prepare();
		renderEntities(camera, light);
		renderTerrain(camera, light);

	}

	/**
	 * Sets the clear color Clears the color and depth buffer.
	 */
	private void prepare() {
		GL40.glEnable(GL40.GL_DEPTH_TEST);
		GL40.glClearColor(0,0,0,1);
		GL40.glClear(GL40.GL_COLOR_BUFFER_BIT | GL40.GL_DEPTH_BUFFER_BIT);
	}
	
	private void renderEntities(Camera camera, PointLight light) {
		entityStaticShader.start();
		entityStaticShader.loadLight(light);
		entityStaticShader.loadProjectionMatrix(PipelineMatrices.createProjectionMatrix(
				camera, WIDTH / HEIGHT));
		entityStaticShader.loadViewMatrix(PipelineMatrices.createViewMatrix(camera));
		entityRenderer.renderStaticEntities(staticEntities);
		entityRenderer.renderPlayers(player, clients);
		entityStaticShader.stop();

	}
	
	private void renderTerrain(Camera camera, PointLight light) {
		// TODO Auto-generated method stub
		
	}

	public void cleanUp() {
		entityStaticShader.cleanUp();
	}
	
}
