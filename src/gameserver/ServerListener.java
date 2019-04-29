package gameserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Map;

import org.joml.Vector3f;

public class ServerListener implements Runnable {

	private int serverPort;
	private DatagramSocket socket;
	private Map<InetSocketAddress, ServerPlayer> clients;

	private boolean running = false;
	private final short protocolID;
	private int rmtSeqNbr;

	public ServerListener(int serverPort, short protocolID, Map<InetSocketAddress, ServerPlayer> clients) {
		this.serverPort = serverPort;
		this.protocolID = protocolID;
		this.clients = clients;
		this.rmtSeqNbr = 0;
		try {
			this.socket = new DatagramSocket(serverPort);
			System.out.println("Created listen socket");
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		
	}

	@Override
	public void run() {

		running = true;
		System.out.println("Listening!");
		while (running) {
			try {
				listen();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void listen() throws IOException {
		byte[] buffer = new byte[55];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		socket.receive(packet);
		System.out.println("Received packet from: " + packet.getAddress() + ":" + packet.getPort());
		

		processPacket(packet);

	}

	private void processPacket(DatagramPacket packet) {

		
		byte[] data = packet.getData();
		ByteBuffer byteBuffer = ByteBuffer.wrap(data);
		short protID = byteBuffer.getShort();

		if (protID == protocolID) {
			
			InetSocketAddress sender = null;
			try {
				InetAddress senderIP = InetAddress.getByName(byteBuffer.getShort() + "." + byteBuffer.getShort() + "." + byteBuffer.getShort() + "." + byteBuffer.getShort());
				int clientListenPort = byteBuffer.getInt();	
				sender = new InetSocketAddress(senderIP, clientListenPort);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}		
					
			int seqNbr = byteBuffer.getInt();
			byte packetType = byteBuffer.get();
						
			System.out.println("Remote usercode: " + sender.hashCode());

			float[] playerData = new float[9];
			for (int i = 0; i < playerData.length; i++) {
				playerData[i] = byteBuffer.getFloat();
			}

			if (clients.containsKey(sender)) {
				clients.get(sender).setData(playerData);

			} else {
				// New client, add:
				clients.put(sender,
						new ServerPlayer(sender.hashCode(),
								new Vector3f(playerData[0], playerData[1], playerData[2]),
								new Vector3f(playerData[3], playerData[4], playerData[5]),
								new Vector3f(playerData[6], playerData[7], playerData[8])));

				System.out.println(
						Thread.currentThread().getName() + ": " + "Added new player. #players = " + clients.size());
				System.out.println(Thread.currentThread().getName() + ": " + "Players: " + clients.keySet().toString());
			}

		}

	}

}
