package gameserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameServer {

	public static void main(String[] args) {
		
		Thread.currentThread().setName("MainThread");
		
		short protocolID = 15000;
		Map<InetSocketAddress, ServerPlayer> clients = new HashMap<InetSocketAddress, ServerPlayer>();
		ServerListener nwHandler = new ServerListener(44444, protocolID, clients);
		Thread listenThread = new Thread(nwHandler);
		listenThread.start();
		
		// Set up socket for sending data to all clients:
		DatagramSocket broadcastSocket = null;
		while (broadcastSocket == null) {
			try {
				broadcastSocket = new DatagramSocket();
				System.out.println(Thread.currentThread().getName() +": " + "Created broadcast socket");
			} catch (SocketException e) {
				System.out.println(Thread.currentThread().getName() +": " + "Broadcast socket could not be created");
			}
		}

		double currentTime = System.nanoTime();
		double frameTime = 0;
		double dt = 1.0/30;
		boolean broadcasting = true;
		int seqNbr = 0;
		
		/////////////////////////////////////////////////////////////////////////////////
		// TESTING WITH OLA
//		ArrayList<InetSocketAddress> ipaddresses = new ArrayList<InetSocketAddress>();
//		ArrayList<Integer> ports = new ArrayList<Integer>();
//		ipaddresses.add(new InetSocketAddress("84.55.82.105", 45444));
//		ipaddresses.add(new InetSocketAddress("31.211.243.51", 45444));
//		ipaddresses.add(new InetSocketAddress("31.211.243.51", 46444));
//		System.out.println(ipaddresses.size());
		
		/////////////////////////////////////////////////////////////////////////////////
		
		
		System.out.println(Thread.currentThread().getName() +": " + "Waiting for players to join ...");
		while(clients.size() < 1) {
//			System.out.println(Thread.currentThread().getName() +": " + clients.size() + " clients connected.");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
//			System.out.println("Size = " + clients.size());
		}
		
		System.out.println("Broadcasting!");
		while (broadcasting) {
					
			frameTime = (System.nanoTime() - currentTime) / 1000000000;
			if (frameTime > dt) {
				// Enter here every dt second
				
				byte[] buffer = new byte[40*clients.size()+7];
				ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
				byteBuffer.putShort(protocolID);
				byteBuffer.putInt(seqNbr);
				byte packetType = 0;
				byteBuffer.put(packetType);
				seqNbr++;

				for (InetSocketAddress c : clients.keySet()) {

					float[] playerData = clients.get(c).getData();
					for (int i = 0; i < playerData.length; i++) {
						byteBuffer.putFloat(playerData[i]);
					}
				}

				for (InetSocketAddress c : clients.keySet()) {
					DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, c.getAddress(), c.getPort());
					try {
						broadcastSocket.send(sendPacket);
						System.out.println("Sent packet to: " + c.getAddress() + ":" + c.getPort());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
//				//Sending manually :
//				for (InetSocketAddress address : ipaddresses) {
//					DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, address.getAddress(),
//							address.getPort());
//					try {
//						broadcastSocket.send(sendPacket);
//						System.out.println("Sent packet to: " + address.toString() + ":" + address.getPort());
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
				
				currentTime = System.nanoTime();
			}
			

		}
		
	

	}

}
