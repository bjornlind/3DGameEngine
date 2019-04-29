package entities;

import org.joml.Vector3f;

public class CameraFPP extends Camera {
	

	/**
	 * 
	 * @param windowID - Window handle for the OpenGL context
	 * @param player - The player that this FPP Camera should attach to.
	 */
	public CameraFPP(long windowID, Vector3f startPosition, Vector3f startRotation) {
		super(windowID, startPosition, startRotation);
	}
	
	@Override
	public void alignCameraWithPlayer(Player player) {
		setPosition(player.getPosition());
		rotation.y = player.getRotation().y;
	}



	
}
