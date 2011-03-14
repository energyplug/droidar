package actions;

import gl.GLCamera;
import util.Vec;
import android.view.MotionEvent;

/**
 * This uses the virtual camera rotation to map input from the touchscreen or
 * the trackball etc and do something along the virtual camera axes (eg camera
 * movement or object movement or anything else). without mapping it to the
 * current camera rotation, a x+10 movement would always be along the virtual x
 * axis and not along the current camera x axis.
 * 
 * @author Spobo
 * 
 */
public abstract class ActionDoAlongAxis extends Action {

	protected GLCamera myTargetCamera;
	private float myTrackballFactor;
	private final float myTouchscreenReductionFactor;
	private Vec movementVec = new Vec();

	/**
	 * @param camera
	 * @param trackballFactor
	 *            should be around 2-15
	 * @param touchscreenFactor
	 *            25 would be good value to start.The higher the value the
	 *            slower the movement
	 */
	public ActionDoAlongAxis(GLCamera camera, float trackballFactor,
			float touchscreenFactor) {
		myTargetCamera = camera;
		myTargetCamera.forceAngleCalculation = true;
		myTrackballFactor = trackballFactor;
		myTouchscreenReductionFactor = touchscreenFactor;
	}

	@Override
	public boolean onTrackballEvent(float x, float y, MotionEvent event) {
		AlignAcordingToViewAxes(x * myTrackballFactor, -y * myTrackballFactor);

		return true;
	}

	final float rad2deg = (float) (180.0f / Math.PI);

	@Override
	public boolean onTouchMove(float screenDeltaX, float screenDeltaY) {

		AlignAcordingToViewAxes(screenDeltaX / myTouchscreenReductionFactor,
				-screenDeltaY / myTouchscreenReductionFactor);
		return true;
	}

	/**
	 * This is where the magic happens. The input movement is mapped according
	 * to the virtual camera rotation around the z axis to do the movement
	 * "along the axes"
	 * 
	 * @param x
	 * @param y
	 */
	private void AlignAcordingToViewAxes(float x, float y) {
		movementVec.x = x;
		movementVec.y = y;
		movementVec
				.rotateAroundZAxis(360 - (myTargetCamera.myAnglesInRadians[0] * rad2deg));
		doAlongViewAxis(movementVec.x, movementVec.y);

	}

	public abstract void doAlongViewAxis(float x, float y);

}
