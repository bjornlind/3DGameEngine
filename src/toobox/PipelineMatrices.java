package toobox;

import org.joml.Matrix4f;
import org.joml.Quaterniond;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import entities.Camera;
import shaders.StaticShader;

public class PipelineMatrices {
	
	/**
	 * Creates a 4x4 homogeneous matrix containing translation, rotation and scale
	 * transformations.
	 * 
	 * @param translation - A 3x1 translation vector
	 * @param rotation    - A 3x1 rotation vector
	 * @param scale       - A uniform scale constant
	 * @return The 4x4 homogeneous transformation matrix
	 */
	public static Matrix4f createTransformationMatrix(Vector3f translation, Vector3f rotation, Vector3f scale) {
		
		Matrix4f transformMatrix = new Matrix4f();
		transformMatrix.translate(translation, transformMatrix);
		transformMatrix.rotateX(rotation.x);
		
		// Negative because the rotateY function is counter-clockwise around Y (in a RON-system):
		transformMatrix.rotateY(-rotation.y);
		
		transformMatrix.rotateZ(rotation.z);
		transformMatrix.scale(scale);
		return transformMatrix;
	}
	
	public static Matrix4f createViewMatrix(Camera cam) {
		
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.rotateX((float) cam.getRotation().x);
		viewMatrix.rotateY((float) cam.getRotation().y);
		viewMatrix.rotateZ((float) cam.getRotation().z);
		Vector3f camPos = cam.getPosition();
		viewMatrix.translate(-camPos.x, -camPos.y, -camPos.z);
		return viewMatrix;
	}

	public static Matrix4f createProjectionMatrix(Camera cam, float aspectRatio) {
		
		float y_scale = (float) (1f / Math.tan(Math.toRadians(cam.getFOV())) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = cam.getFAR() - cam.getNEAR();
		
		Matrix4f projMatrix = new Matrix4f();
		projMatrix.m00(x_scale);
		projMatrix.m11(y_scale);
		projMatrix.m22(- ( (cam.getFAR() + cam.getNEAR()) / frustum_length) );
		projMatrix.m23(-1);
		projMatrix.m32(- ( (2* cam.getNEAR() * cam.getFAR()) / frustum_length) );
		projMatrix.m33(0);
		return projMatrix;
	}
	


}
