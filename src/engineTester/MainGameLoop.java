package engineTester;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;

import java.io.IOException;
import java.util.ArrayList;

import entities.*;
import entities.Player.PlayerState;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import models.*;
import physics.PhysicsEngine;
import renderEngine.*;
import shaders.StaticShader;
import textures.Texture;


public class MainGameLoop {
	
	private static double dt = 1.0/60.0;
	
	

	public static void main(String[] args) throws IOException {
		
		

		
		WindowManager wm = new WindowManager();
		wm.initWindow();
		Loader loader = new Loader();
		
		PhysicsEngine physicsEngine = new PhysicsEngine(WindowManager.windowID, WindowManager.WIDTH, WindowManager.HEIGHT);
		
		ArrayList<Player> players = new ArrayList<Player>();
		ArrayList<Camera> cameras = new ArrayList<Camera>();
				
		Texture cubeTexture = new Texture(loader.loadTexture("cubeUV.png"));
		Player player1 = new Player(new TexturedModel(OBJLoader2.loadFromOBJ("mayaCube.obj", loader), cubeTexture),
				new Vector3f(0,5,0), new Vector3f(0,0,0), new Vector3f(0,0,0));
		players.add(player1);
		Camera camera1 = new CameraFPP(WindowManager.windowID, player1);
		cameras.add(camera1);
		
		Player player2 = new Player(new TexturedModel(OBJLoader2.loadFromOBJ("mayaCube.obj", loader), cubeTexture),
				new Vector3f(5,5,5), new Vector3f(0,0,0), new Vector3f(0,0,0));
		players.add(player2);
		Camera camera2 = new CameraFPP(WindowManager.windowID, player2);
		cameras.add(camera2);
		
		
		PointLight light = new PointLight(new Vector3f(0, 10, 0), new Vector3f(0.9f, 0.9f, 1), 0.05f, 10f);
		MasterRenderer renderer = new MasterRenderer(players);
		
		ArrayList<Entity> staticEntities = new ArrayList<Entity>();
		
		Texture texture = new Texture(loader.loadTexture("buildingTexture.png"));
		TexturedModel texturedTestObj = new TexturedModel(OBJLoader2.loadFromOBJ("building.obj", loader), texture);
//		entities.add(new Entity(texturedTestObj, new Vector3f(0,25,0), new Vector3f(0,0,0), new Vector3f(0,0,0), new Vector3f(1,1,1)));
		
		Texture sphereTexture = new Texture(loader.loadTexture("sphereTexture.png"));
		Entity sphereEntity = new Entity(new TexturedModel(OBJLoader2.loadFromOBJ("sphere.obj", loader), sphereTexture),
				new Vector3f(0, 2, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
//		entities.add(sphereEntity);
		
		Texture treeTexture = new Texture(loader.loadTexture("Lowpoly_tree_texture.png"));
		TexturedModel treeModel = new TexturedModel(OBJLoader2.loadFromOBJ("Lowpoly_tree_sample_triangulated.obj", loader), treeTexture);
		Entity tree = new Entity(new TexturedModel(OBJLoader2.loadFromOBJ("Lowpoly_tree_sample_triangulated.obj", loader), treeTexture),
				new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
//		entities.add(tree);
		
		Texture dragonTexture = new Texture(loader.loadTexture("/dragonv4/texture/Body-TM_u0_v0.png"));
		dragonTexture.setReflectivity(1);
		dragonTexture.setShininess(10);
		TexturedModel dragonModel = new TexturedModel(OBJLoader2.loadFromOBJ("/dragonv4/dragon.obj", loader), dragonTexture);
		
		System.out.println("Generating dragons ...");
		
		for (int i = -10; i <= 10; i++) {
			for (int j = -10; j <= 10; j++) {
				if (i != 0 || j != 0) {
					Entity dragon = new Entity(dragonModel, new Vector3f(i * 25, 0, j * 25), new Vector3f(0, 0, 0),
							new Vector3f(0, (float) (Math.random() * 360), 0), new Vector3f(0.01f, 0.01f, 0.01f));
					staticEntities.add(dragon);
				}
			}
		}


		
//		// City:
		for (int i = -30; i <= 30; i++) {
			for (int j = -30; j <= 30; j++) {
				if (i != 0 || j != 0) {
					Entity treeEntity = new Entity(treeModel, 
							new Vector3f(30 * j, 0, -30 * i),
							new Vector3f(0, 0, 0),
							new Vector3f(0, (float)Math.random()*360, 0), 
							new Vector3f(1, 1, 1));
					staticEntities.add(treeEntity);
				}
			}
		}
		


		Texture planeTexture = new Texture(loader.loadTexture("chessboard.png"));
		TexturedModel texturedPlane = new TexturedModel(OBJLoader2.loadFromOBJ("plane.obj", loader), planeTexture);
		Entity planeEntity = new Entity(texturedPlane, new Vector3f(0,0,0), new Vector3f(0,0,0), new Vector3f(0,0,0), new Vector3f(1000,1,1000));
		staticEntities.add(planeEntity); 
		
		double currentTime = System.nanoTime();
		double accumulator = 0.0;
		double t = 0;
		int counter = 0;
		double frameNbr = 0;
		
		// Render loop:
		while(!wm.isCloseRequested()) {
			
			glfwSwapBuffers(WindowManager.windowID);

			double newTime = System.nanoTime();
			double frameTime = (newTime - currentTime)/1000000000;
			currentTime = newTime;	
			accumulator += frameTime;
			
			// Physics loop:
			while(accumulator >= dt) {
				
				physicsEngine.handleInputs(players, cameras);			
				physicsEngine.integratePlayer(dt, players);
//				physicsEngine.updateEntityPhysics(dt, entities);
				cameras.get(physicsEngine.getActivePlayer()).updateCamera();
				cameras.get(physicsEngine.getActivePlayer()).updateCameraSettings();
				

				accumulator -= dt;
				t += dt;
				counter ++;
			}
			
			light.setPosition(players.get(physicsEngine.getActivePlayer()).getPosition());
			
			for(Entity entity : staticEntities) {
				renderer.processEntity(entity);
			}
			renderer.render(cameras.get(physicsEngine.getActivePlayer()), light);
			
			frameNbr++;
			
			System.out.println("Position:\t" + player1.getPosition().toString());
			System.out.println("Velocity:\t" + player1.getVelocity().toString());
			System.out.println("Rotation:\t" + player1.getRotationDeg().toString());
			System.out.printf("Time = %.2f\n", t);
			System.out.printf("FPS = %.2f\n", frameNbr/t);
			System.out.println("# Physics loops: " + counter);
			System.out.println();
			counter = 0;
			

			

			
		}
		
		
		renderer.cleanUp();
		loader.cleanUp();
		wm.closeWindow();
	}


}
