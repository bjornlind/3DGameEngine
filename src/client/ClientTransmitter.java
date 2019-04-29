package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import entities.Player;

public class ClientTransmitter {
	
	private DatagramSocket socket;
	private InetAddress serverIP;
	private int serverPort;
	private Short[] ipArray;
	private int listenPort;
	private short protocolID;
	private int seqNbr;
	
	public ClientTransmitter(String publicIP, int listenPort, String serverIP, int serverPort, short protocolID) {
		this.seqNbr = 0;
		this.listenPort = listenPort;
		
		String[] ipStringArray = publicIP.split("\\.");
		ipArray = new Short[ipStringArray.length];
		for(int i = 0; i < ipStringArray.length; i++) {
			ipArray[i] = Short.parseShort(ipStringArray[i]);
		}
		
		try {
			this.serverIP = InetAddress.getByName(serverIP);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		
		this.serverPort = serverPort;
		this.protocolID = protocolID;
		
		socket = null;
		while (socket == null) {
			try {
				socket = new DatagramSocket();
				System.out.println("Created broadcast socket");
			} catch (SocketException e) {
				System.out.println("Broadcast socket could not be created");
			}
		}
		 
		
		
	}
	
	public void transmitPlayerData(Player player) {
		
		// Serialize data:
		byte[] buffer = new byte[55];
		ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
		byteBuffer.putShort(protocolID);
		
		byteBuffer.putShort(ipArray[0]);
		byteBuffer.putShort(ipArray[1]);
		byteBuffer.putShort(ipArray[2]);
		byteBuffer.putShort(ipArray[3]);
		
		byteBuffer.putInt(listenPort);
		byteBuffer.putInt(seqNbr);
		byteBuffer.put((byte)0);
		
		byteBuffer.putFloat(player.getPosition().x);
		byteBuffer.putFloat(player.getPosition().y);
		byteBuffer.putFloat(player.getPosition().z);
		byteBuffer.putFloat(player.getVelocity().x);
		byteBuffer.putFloat(player.getVelocity().y);
		byteBuffer.putFloat(player.getVelocity().z);
		byteBuffer.putFloat(player.getRotation().x);
		byteBuffer.putFloat(player.getRotation().y);
		byteBuffer.putFloat(player.getRotation().z);
		
		// Send data:
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverIP, serverPort);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Increment the sequence number:
		seqNbr++;
		
	}

	public void connectToServer() {
		// TODO Auto-generated method stub
		
	}

}
