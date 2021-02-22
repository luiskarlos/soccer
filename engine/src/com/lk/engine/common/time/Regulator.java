/**
 *  Desc:   Use this class to regulate code flow (for an update function say)
 *          Instantiate the class with the frequency you would like your code
 *          section to flow (like 10 times per second) and then only allow
 *          the program flow to continue if Ready() returns true
 *
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.common.time;

import static com.lk.engine.common.misc.NumUtils.isEqual;

import com.lk.engine.common.core.Updatable;
import com.lk.engine.common.misc.Active;
import com.lk.engine.common.misc.RandomGenerator;

import java.io.Serializable;

public class Regulator implements Serializable, Updatable {
	// the number of milliseconds the update period can vary per required
	// update-step. This is here to make sure any multiple clients of this class
	// have their updates spread evenly
	private static final double UPDATE_PERIOD_VARIATOR = 10.0; // TODO: configurable?

	// the time period between updates
	private double updatePeriod;
	// the next time the regulator allows code flow
	private long nextUpdateTime;
	private final RandomGenerator random;

	public Regulator(RandomGenerator random,
									 final double numUpdatesPerSecondRqd) {
		this.random = random;
		nextUpdateTime = (long) (System.currentTimeMillis() + random.nextDouble() * 1000);

		if (numUpdatesPerSecondRqd > 0) {
			updatePeriod = 1000.0 / numUpdatesPerSecondRqd;
		} else if (isEqual(0.0, numUpdatesPerSecondRqd)) {
			updatePeriod = 0.0;
		} else if (numUpdatesPerSecondRqd < 0) {
			updatePeriod = -1;
		}
	}

	/**
	 * @return true if the current time exceeds nextUpdateTime
	 */
	public boolean isReady() {
		// if a regulator is instantiated with a zero freq then it goes into stealth
		// mode (doesn't regulate)
		if (isEqual(0.0, updatePeriod)) {
			return true;
		}

		// if the regulator is instantiated with a negative freq then it will
		// never allow the code to flow
		if (updatePeriod < 0) {
			return false;
		}

		final long currentTime = System.currentTimeMillis();
		if (currentTime >= nextUpdateTime) {
			nextUpdateTime = (long) (currentTime + updatePeriod +
					random.randInRange( -UPDATE_PERIOD_VARIATOR, UPDATE_PERIOD_VARIATOR));
			return true;
		}

		return false;
	}

	@Override
	public Active update(long time, int delta) { //TODO: update to consider delta
		return Active.Yes;
	}
}
