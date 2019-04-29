package net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.joml.Vector3f;

import entities.Entity;
import entities.Player;
import models.TexturedModel;

public class ServerTransmitter {
	
	private int port;
	private DatagramSocket socket;
	private DatagramPacket sendPacket;
	private DatagramPacket recPacket;
	private byte[] data;
	
	private Map<InetAddress, Player> clients;
	private ArrayList<Player> players;
	TexturedModel playerTexturedModel;
	
	short protocolID = 12345;
	private int recSeqNbr;
	private int rmtSeqNbr = 0;
	private int locSeqNbr = 0;
	private byte[] recBuffer;
	private ByteBuffer byteBuffer;
	
	public enum PacketType{
		CONNECTREQ((byte) 0), CONNECTACC((byte) 1), DISCONNECT((byte) 2), UPDATE((byte) 3);
		
		private byte numVal;
		
		PacketType(byte numVal){
			this.numVal = numVal;
		}
		
		public byte getNumVal() {
			return numVal;
		}	
	}
	
	
	public ServerTransmitter(int port, ArrayList<Player> players, TexturedModel playerTexturedModel) throws SocketException {
		this.port = port;
		this.socket = new DatagramSocket(this.port);
		this.clients = new HashMap<InetAddress, Player>();
		this.players = players;
		this.playerTexturedModel = playerTexturedModel;
		
	}

	public void receivePacket() throws IOException {
		recBuffer = new byte[256];
		recPacket = new DatagramPacket(recBuffer, recBuffer.length);
		socket.receive(recPacket);
		InetAddress sender = recPacket.getAddress();
		data = recPacket.getData();
		byteBuffer = ByteBuffer.wrap(data);

		short rmtProtocolID = byteBuffer.getShort();
		if (rmtProtocolID == protocolID) {
			// Protocols match, continue:

			if ((recSeqNbr = byteBuffer.getInt()) > rmtSeqNbr) {
				// The packet is more recent than the last, accept it:
				rmtSeqNbr = recSeqNbr;
				byte recPacketType = byteBuffer.get();

				if (recPacketType == PacketType.UPDATE.getNumVal()) {
					// Client sending its state:
										
					if (!clients.containsKey(sender)) {
						// New player connecting:
						Player newPlayer = new Player(playerTexturedModel, 
								new Vector3f(byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat()), 
								new Vector3f(byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat()),
								new Vector3f(0, 0, 0));
						players.add(newPlayer);
						clients.put(sender, newPlayer);
						System.out.println("New player connected: " + sender.toString());
					} else {
						Player clientPlayer = clients.get(sender);
						clientPlayer.setPosition(byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat());
						clientPlayer.setVelocity(byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat());

					}
				}
			}
		}
	}
	
	public void sendPacket(InetAddress ip, int port, PacketType p) throws IOException {
		}
		


}
