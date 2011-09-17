package actions;

import gl.GLCamera;
import util.Vec;
import worldData.Updateable;
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
public class ActionWASDMovement extends Action implements Updateable {

	protected GLCamera myTargetCamera;
	private final float xReduction;
	private final float yReduction;
	private Vec accelerationVec = new Vec();
	private float myMaxSpeed;

	private float yFactor;
	private float xFactor;

	/**
	 * @param camera
	 * @param xReduction
	 * @param yReduction
	 * @param maxSpeed
	 */
	public ActionWASDMovement(GLCamera camera, float xReduction,
			float yReduction, float maxSpeed) {
		myTargetCamera = camera;
		myTargetCamera.forceAngleCalculation = true;
		this.xReduction = xReduction;
		this.yReduction = yReduction;
		myMaxSpeed = maxSpeed;
	}

	final float rad2deg = (float) (180.0f / Math.PI);

	@Override
	public boolean onTouchMove(MotionEvent e1, MotionEvent e2,
			float screenDeltaX, float screenDeltaY) {

		yFactor = (-e2.getX() + e1.getX()) / yReduction;
		xFactor = (e1.getY() - e2.getY()) / xReduction;

		return true;
	}

	@Override
	public boolean onReleaseTouchMove() {
		xFactor = 0;
		yFactor = 0;
		return true;
	}

	@Override
	public boolean update(float timeDelta) {
		if (xFactor != 0 || yFactor != 0) {

			float[] rayDir = new float[4];
			myTargetCamera.getCameraViewDirectionRay(null, rayDir);

			accelerationVec.x = rayDir[0];
			accelerationVec.y = rayDir[1];
			accelerationVec.z = rayDir[2];

			accelerationVec.normalize();
			accelerationVec.mult(xFactor);
			/*
			 * now the yFactor which has to be added orthogonal to the x
			 * direction (with z value 0)
			 */
			// Vec normalizedOrtoh =
			// Vec.getOrthogonalHorizontal(accelerationVec);

			Vec yDir = new Vec(yFactor, 0, 0);
			yDir.rotateAroundZAxis(180 - myTargetCamera.myAnglesInRadians[0]
					* rad2deg);

			// System.out.println("yDir="+yDir);

			accelerationVec.add(yDir);

			if (accelerationVec.getLength() > myMaxSpeed)
				accelerationVec.setLength(myMaxSpeed);

			myTargetCamera.changeNewPosition(accelerationVec.x * timeDelta,
					accelerationVec.y * timeDelta, accelerationVec.z
							* timeDelta);

		}

		return true;
	}
}
