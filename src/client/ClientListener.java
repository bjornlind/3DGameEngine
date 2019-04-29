package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Map;

import org.joml.Vector3f;

public class ClientListener implements Runnable {
	
	Map<Float, ClientOpponent> clients;
	DatagramSocket socket;
	private int rmtSeqNbr;
	private short protocolID;;
	private boolean running = false;
	private float localUserCode;
	private int listenPort;
	private String publicIP;
	
	
	public ClientListener(Map<Float, ClientOpponent> clients, short protocolID) {
		this.clients = clients;
		this.protocolID = protocolID;
		this.rmtSeqNbr = 0;

		// Initialize socket:		
		listenPort = 45444;
		socket = null;
		while (socket == null) {
			try {
				socket = new DatagramSocket(listenPort);
				System.out.println("Created listen socket");
			} catch (SocketException e) {
				System.out.println("Listen socket could not be created. Increasing port number by 1000");
				listenPort += 1000;
			}
		}
		
		// Get public IP:
		publicIP = null;
		while(publicIP == null) {
			try {
				URL url = new URL("http://bot.whatismyipaddress.com");
				BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
				publicIP = reader.readLine().trim();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
		// Get the local userCode
		InetSocketAddress publicAddress = new InetSocketAddress(publicIP, socket.getLocalPort());
		localUserCode = publicAddress.hashCode();
		System.out.println("Local user code = " + localUserCode);		
	}
	
	public int getListenPort() {
		return listenPort;
	}
	
	public String getPublicIP() {
		return publicIP;
	}
	
	@Override
	public void run() {
		
		running = true;

		System.out.println("Listening!");
		while (running) {
			listen();
		}
		
	}
	
	private void listen() {
		byte[] buffer = new byte[320];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		
		try {
//			System.out.println("Waiting for packet:");
			socket.receive(packet);
			System.out.println("Received packet");
		} catch (IOException e) {
			e.printStackTrace();
		}

		processPacket(packet);
	}
	
	private void processPacket(DatagramPacket packet) {
//		System.out.println("processing packet");
		
		
		byte[] data = packet.getData();
		int dataLength = packet.getLength();
		ByteBuffer byteBuffer = ByteBuffer.wrap(data);

		short protID = byteBuffer.getShort();
		if(protID == protocolID) {
			
			int seqNbr = byteBuffer.getInt();
			if (seqNbr >= rmtSeqNbr) {
				rmtSeqNbr = seqNbr;

				byte packetType = byteBuffer.get();

				while (byteBuffer.position() < dataLength) {

					float userCode = byteBuffer.getFloat();
					System.out.println("Received usercode: " + userCode);
					if (userCode != localUserCode) {

						float[] playerData = new float[10];
						playerData[0] = userCode;
						for (int i = 1; i < playerData.length; i++) {
							playerData[i] = byteBuffer.getFloat();
						}

						if (clients.containsKey(userCode)) {
							clients.get(userCode).setData(playerData);

						} else {
							// New client, add:
							clients.put(userCode,
									new ClientOpponent(userCode,
											new Vector3f(playerData[1], playerData[2], playerData[3]),
											new Vector3f(playerData[4], playerData[5], playerData[6]),
											new Vector3f(playerData[7], playerData[8], playerData[9])));

							System.out.println("Added new client: " + userCode);
						}
					} else {
						byteBuffer.position(byteBuffer.position() + 36);
					}
				}
//				for(ClientOpponent clientOpponent : clients.values()) {
//					System.out.println("x: " + clientOpponent.getPosition().x + "\t" +
//									   "y: " + clientOpponent.getPosition().y + "\t" +
//									   "z: " + clientOpponent.getPosition().z + "\t");
//				}
				
			}
			
			
		}
		
	}
	
	
	}
