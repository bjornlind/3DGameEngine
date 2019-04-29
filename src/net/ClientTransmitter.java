package net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import entities.Player;

public class ClientTransmitter {
	
	private int localPort;
	private int serverPort;
	private InetAddress serverIP;
	private DatagramSocket socket;
	private DatagramPacket sendPacket;
	private DatagramPacket recPacket;

	short protocolID = 12345;
	private int recSeqNbr;
	private int rmtSeqNbr = 0;
	private int locSeqNbr = 0;
	private byte[] sendBuffer;
	private byte[] recBuffer;
	private ByteBuffer byteBuffer;
	boolean connected = false;

	public enum PacketType {
		CONNECTREQ((byte) 0), CONNECTACC((byte) 1), DISCONNECT((byte) 2), UPDATE((byte) 3);

		private byte numVal;

		PacketType(byte numVal) {
			this.numVal = numVal;
		}

		public byte getNumVal() {
			return numVal;
		}

	}

	public ClientTransmitter(int serverPort, String serverIP) throws SocketException, UnknownHostException {
		this.serverPort = serverPort;
		this.socket = new DatagramSocket();
		this.localPort = socket.getLocalPort();
		this.serverIP = InetAddress.getByName(serverIP);
	}
		
	/**
	 * Send player state to the server, i.e. position, velocity.
	 * @param player - This clients player
	 * @throws IOException
	 */
	public void sendPlayerData(Player player) throws IOException {
		sendPacket(PacketType.UPDATE, player);

	}

	/**
	 * Send a packet to the server.
	 * @param PT - The type of packet to send.
	 * @param player - The player whose data is sent.
	 * @throws IOException
	 */
	private void sendPacket(PacketType PT, Player player) throws IOException {

		sendBuffer = new byte[256];
		byteBuffer = ByteBuffer.wrap(sendBuffer);

		// Add protocolID, sequence number, packet type to buffer:
		byteBuffer.putShort(protocolID);
		byteBuffer.putInt(locSeqNbr);
		byteBuffer.put(PT.getNumVal());

		if (PT == PacketType.UPDATE) {
			// Add position and velocity to buffer:
			byteBuffer.putFloat(player.getPosition().x);
			byteBuffer.putFloat(player.getPosition().y);
			byteBuffer.putFloat(player.getPosition().z);
			byteBuffer.putFloat(player.getVelocity().x);
			byteBuffer.putFloat(player.getVelocity().y);
			byteBuffer.putFloat(player.getVelocity().z);
		}

		// Increment the sequence number:
		locSeqNbr++;

		// Create and send the packet:
		sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, serverIP, serverPort);
		socket.send(sendPacket);

		System.out.println("Sent packet");

	}

	public void receivePacket() throws IOException {

	}

//	public void connectToServer(Player player) throws IOException {
//		while (!connected) {
//			sendPacket(PacketType.CONNECTREQ, player);
//			receivePacket();
//		}
//	}
	
}
