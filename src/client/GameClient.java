package client;

import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.joml.Vector3f;

import entities.CameraFPP;
import entities.Entity;
import entities.Player;
import entities.PointLight;
import models.ModelGroup;
import models.TexturedModel;
import physics.PhysicsEngine;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader2;
import renderEngine.WindowManager;
import textures.Texture;

public class GameClient {
	
	private WindowManager wm;
	private Loader loader;
		
	private PointLight light;
	private MasterRenderer renderer;
	private Map<String, ModelGroup> staticEntities;

	private Player player;
	private CameraFPP camera;
	private double dt = 1.0/60.0;
	private PhysicsEngine physicsEngine;
	
	private Map<Float, ClientOpponent> clients;
	private ClientTransmitter clientTransmitter;
	private ClientListener clientListener;
	private final short protocolID = 15000;
	
	private enum Connection {
		DISCONNECTED ((byte) 0),
		CONNECTED ((byte) 1);		
		
		private byte connectionState;
		Connection(byte connectionState){
			this.connectionState = connectionState;
		}	
	}
	
	
	//////////////////////////////////////////////
	
	public synchronized void setConnection(Connection connectionState){
		this.connectionState = connectionState;
		
	}
	
	public void initNetwork() {
		

		
		// Init map of clients connected:
		clients = new HashMap<Float, ClientOpponent>();
		
		// Init clientListener in a new thread:
		clientListener = new ClientListener(clients, protocolID);
		String publicIP = clientListener.getPublicIP();
		System.out.println(publicIP);
		int listenPort = clientListener.getListenPort();
		Thread listenThread = new Thread(clientListener, "ClientListenThread");
		listenThread.setDaemon(true);
		listenThread.start();
				
		// Init client transmitter:
		String serverIP = "31.211.243.51";
		int serverPort = 44444;
		clientTransmitter = new ClientTransmitter(publicIP, listenPort, serverIP, serverPort, protocolID);
		
		// Init connection state:
		
		
		// Connect to server:
		while(connectionState != Connection.CONNECTED) {
			clientTransmitter.connectToServer();
		}

		
	}

	public void initGraphics() {
		wm = new WindowManager();
		
		wm.initWindow();
		loader = new Loader();
		physicsEngine = new PhysicsEngine(WindowManager.windowID, WindowManager.WIDTH, WindowManager.HEIGHT);
		
		// Static entities map:
		staticEntities = new HashMap<String, ModelGroup>();

		//Player entity:
		Texture cubeTexture = new Texture("PlayerModel_blob_texture.png",loader);
		player = new Player(new TexturedModel(OBJLoader2.loadFromOBJ("PlayerModel_blob.obj", loader), cubeTexture),
				new Vector3f(0, 50, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
		camera = new CameraFPP(WindowManager.windowID, player.getPosition(), new Vector3f(player.getRotation().x, player.getRotation().y, player.getRotation().z));
			
		//Renderer & Light
		light = new PointLight(new Vector3f(0, 50, 0), new Vector3f(0.9f, 0.9f, 1), 0.05f, 70f);
		renderer = new MasterRenderer(player, clients, staticEntities);
		
	}
		
	public void initStaticEntities() {
		System.out.println("Loading map ...");
				
		try {
			BufferedReader reader = new BufferedReader(new FileReader("src/maps/treeMap.txt"));
			String line; 
			while((line = reader.readLine()) != null) {
				line = line.trim();
				if(line.startsWith("m")) {
					String[] contents = line.split(";");
					String modelName = contents[1];
					String modelFileName = contents[2];
					String textureFileName = contents[3];
					
					if(!staticEntities.containsKey(modelName)) {
						staticEntities.put(modelName,
								new ModelGroup(new TexturedModel(OBJLoader2.loadFromOBJ(modelFileName, loader),
										new Texture(textureFileName, loader))));
					}
										
				} else if (line.startsWith("e")) {
					String[] contents = line.split(";");
					String modelName = contents[1];
					String[] pos = contents[2].split(",");
					String[] vel = contents[3].split(",");
					String[] rot = contents[4].split(",");
					String[] scale = contents[5].split(",");
					
					try {
						staticEntities.get(modelName).addEntity(new Entity(staticEntities.get(modelName).getTexturedModel(),
								new Vector3f(Float.parseFloat(pos[0]), Float.parseFloat(pos[1]), Float.parseFloat(pos[2])),
								new Vector3f(Float.parseFloat(vel[0]), Float.parseFloat(vel[1]), Float.parseFloat(vel[2])),
								new Vector3f(Float.parseFloat(rot[0]), Float.parseFloat(rot[1]), Float.parseFloat(rot[2])),
								new Vector3f(Float.parseFloat(scale[0]), Float.parseFloat(scale[1]),Float.parseFloat(scale[2]))));
					} catch(NullPointerException e) {
						System.out.println("No model with this name, could not add entity");
					}
				}
			}
			
			reader.close();
			System.out.println("Map load complete.");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void runGameLoop() {
		
		// Initialize time related variables:
		double currentTime = System.nanoTime();
		double accumulator = 0.0;
		double t = 0;
		int counter = 0;
		double frameNbr = 0;

		// Render loop:
		while (!wm.isCloseRequested()) {

			glfwSwapBuffers(WindowManager.windowID);

			double newTime = System.nanoTime();
			double frameTime = (newTime - currentTime) / 1000000000;
			currentTime = newTime;
			accumulator += frameTime;

			// Physics loop:
			while (accumulator >= dt) {

				player.updatePrevState();
				physicsEngine.handleInputs(player, camera);
				physicsEngine.integratePlayer(player, dt);
				camera.alignCameraWithPlayer(player);
				camera.updateCameraSettings();
				
				accumulator -= dt;
				t += dt;
				counter++;
			}
			
//			// Interpolate player position & rotation:
//			if(counter > 0) {
//				double alpha = accumulator/dt;
//				player.interpolateState(alpha);
//				camera.alignCameraWithPlayer(player);
//			}
			
			// Transmit data to server:
			if(frameNbr%3 == 0) {
				clientTransmitter.transmitPlayerData(player);
			}
			
			// Print stuff:
			printPlayerData();
//			System.out.printf("Time = %.2f\n", t);
			System.out.printf("FPS = %.2f\n", frameNbr / t);
			System.out.println("# Physics loops: " + counter);
			System.out.println();
			counter = 0;
			
			// Increment frame number:
			frameNbr++;
			
			// Render:
			renderer.render(camera, light);


			}

	
	}
		
	private void printPlayerData() {
		System.out.println("Position:\t" + player.getPosition().toString());
		System.out.println("Velocity:\t" + player.getVelocity().toString());
		System.out.println("Rotation:\t" + player.getRotation().toString());
	}

	public void cleanUp() {
		renderer.cleanUp();
		loader.cleanUp();
		wm.closeWindow();		
	}
	
	public void saveMap(){
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter map name: ");
		String fileName = scan.nextLine();
		
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter("src/maps/" + fileName + ".txt"));
			for(String modelName : staticEntities.keySet()) {
				writer.write("m;" + modelName);
				writer.write(";" +  staticEntities.get(modelName).getTexturedModel().getRawModel().getModelFileName());
				writer.write(";" + staticEntities.get(modelName).getTexturedModel().getTexture().getTextureFileName());
				writer.newLine();
			}
			for(String modelName : staticEntities.keySet()) {
				for(Entity entity : staticEntities.get(modelName).getEntities()) {
					writer.write("e;" + modelName + ";");
					
					writer.write(Float.toString(entity.getPosition().x) + "," + 
								 Float.toString(entity.getPosition().y) + "," + 
								 Float.toString(entity.getPosition().z) + ";");
					
					writer.write(Float.toString(entity.getVelocity().x) + "," + 
							 	 Float.toString(entity.getVelocity().y) + "," + 
							 	 Float.toString(entity.getVelocity().z) + ";");
					
					writer.write(Float.toString(entity.getRotation().x) + "," + 
								 Float.toString(entity.getRotation().y) + "," + 
								 Float.toString(entity.getRotation().z) + ";");
					
					writer.write(Float.toString(entity.getScale().x) + "," + 
							 	 Float.toString(entity.getScale().y) + "," + 
							 	 Float.toString(entity.getScale().z) + ";");
					
					writer.newLine();
				}
			}
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		scan.close();
		
	}
	
	public static void main(String[] args) {
		
		GameClient gc = new GameClient();
		gc.initNetwork();
		gc.initGraphics();
		gc.initStaticEntities();
		gc.runGameLoop();
		gc.cleanUp();
//		gc.saveMap();
		System.out.println("Good bye");

	}

}
