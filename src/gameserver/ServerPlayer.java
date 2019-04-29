package gameserver;

import org.joml.Vector3f;

public class ServerPlayer {
	
	private float userCode;
	private Vector3f position;
	private Vector3f velocity;
	private Vector3f orientation;
	private Vector3f scale;
	
	public ServerPlayer(float userCode, Vector3f pos, Vector3f vel, Vector3f ori) {
		this.userCode = userCode;
		this.position = pos;
		this.velocity = vel;
		this.orientation = ori;
		this.scale = new Vector3f(1,1,1);
		
	}

	public synchronized Vector3f getPosition() {
		return position;
	}

	public synchronized void setPosition(float x, float y, float z) {
		this.position.x = x;
		this.position.y = y;
		this.position.z = z;
	}

	public synchronized Vector3f getVelocity() {
		return velocity;
	}

	public synchronized void setVelocity(float vx, float vy, float vz) {
		this.velocity.x = vx;
		this.velocity.y = vy;
		this.velocity.z = vz;
	}

	public synchronized Vector3f getOrientation() {
		return orientation;
	}

	public synchronized void setOrientation(float rx, float ry, float rz) {
		this.orientation.x = rx;
		this.orientation.y = ry;
		this.orientation.z = rz;
	}
	
	public synchronized Vector3f getScale() {
		return scale;
	}
	
	public synchronized void setData(float[] data) {
		this.position.x = data[0];
		this.position.y = data[1];
		this.position.z = data[2];
		this.velocity.x = data[3];
		this.velocity.y = data[4];
		this.velocity.z = data[5];
		this.orientation.x = data[6];
		this.orientation.y = data[7];
		this.orientation.z = data[8];
	}
	
	public synchronized float[] getData() {
		return new float[] {userCode, 
							position.x, position.y, position.z,
							velocity.x, velocity.y, velocity.z,
							orientation.x, orientation.y, orientation.z};
	}
	

	
	
	
	
	

}
