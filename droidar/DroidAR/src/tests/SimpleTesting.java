package tests;

import system.EventManager;
import android.app.Activity;
import android.content.Context;
import android.hardware.SensorManager;
import android.location.LocationManager;

public abstract class SimpleTesting {

	public SimpleTesting() {
		// TODO execute run automatically
	}

	public abstract void run() throws Exception;

	public void assertTrue(boolean b) throws Exception {
		if (!b) {
			throw new Exception("A result was not true!");
		}
	}

	public void assertFalse(boolean b) throws Exception {
		if (b) {
			throw new Exception("A result was not false!");
		}
	}

	public void assertNotNull(Object o) throws Exception {
		if (o == null) {
			throw new Exception("An object was null!");
		}
	}

	public void assertEquals(Object a, Object b) throws Exception {
		if (!a.equals(b)) {
			throw new Exception("Two objects were not equal!");
		}
	}

	/**
	 * add all tests you want to be executed on startup here:
	 * 
	 * @param main
	 * 
	 * @throws Exception
	 */
	public static void runAllTests(Activity myTargetActivity) throws Exception {

		EventManager.getInstance().registerListeners(myTargetActivity, true);

		// new ThreadTest().run();
		// new MemoryAllocationTests().run();
		// new NetworkTests().run();

		new SystemTests().run();
		new EfficientListTests().run();
		new GeoTests().run();
		new IOTests(myTargetActivity).run();

	}
}