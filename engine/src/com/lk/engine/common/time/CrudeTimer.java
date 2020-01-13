/**
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.common.time;

public class CrudeTimer {
	// set to the time (in seconds) when class is instantiated
	private double startTime;

	// set the start time
	private CrudeTimer() {
		startTime = System.currentTimeMillis() * 0.001;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException("Cloning not allowed");
	}

	// returns how much time has elapsed since the timer was started
	public double getCurrentTime() {
		// return System.currentTimeMillis() * 0.001 - startTime;
		// The truncation of the results was added to produce results similar to
		// what is produced by the C++ code
		// Improved by A.Rick Anderson
		return ((double) (Math.round((System.currentTimeMillis() * 0.001 - startTime) * 1000))) / 1000;
	}
}
