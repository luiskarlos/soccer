/**
 *
 *  Desc: Windows timer class.
 *
 *        nb. this only uses the high performance timer. There is no
 *        support for ancient computers. I know, I know, I should add
 *        support, but hey, I have shares in AMD and Intel... Go upgrade ;o)
 * 
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.common.time;

public class PrecisionTimer {
	private long currentTime, lastTime, lastTimeInTimeElapsed, nextTime, startTime, frameTime;
	private final long perfCountFreq;
	private double timeElapsed, lastTimeElapsed;
	private final double timeScale;
	private final double normalFPS;
	// private double m_SlowFPS;
	private boolean started;
	// if true a call to TimeElapsed() will return 0 if the current
	// time elapsed is much smaller than the previous. Used to counter
	// the problems associated with the user using menus/resizing/moving
	// a window etc
	private boolean smoothUpdates;

	public PrecisionTimer() {
		this(0d);
	}

	public PrecisionTimer(final double fps) {
		normalFPS = fps;
		// how many ticks per sec do we get
		// QueryPerformanceFrequency((LARGE_INTEGER *) & perfCountFreq);
		// using System.nanoSecond() it is obviously 1 000 millis second per
		// second
		perfCountFreq = 1000L;
		timeScale = 1.0 / perfCountFreq;

		// calculate ticks per frame
		if (normalFPS > 0)
			frameTime = (long) (perfCountFreq / normalFPS);
	}

	/**
	 * whatdayaknow, this starts the timer call this immediately prior to game
	 * loop. Starts the timer (obviously!)
	 * 
	 */
	public void start() {
		started = true;
		timeElapsed = 0.0;

		// get the time QueryPerformanceCounter((LARGE_INTEGER *) & lastTime);
		lastTime = System.currentTimeMillis();

		// keep a record of when the timer was started
		startTime = lastTimeInTimeElapsed = lastTime;

		// update time to render next frame
		nextTime = lastTime + frameTime;
	}

	// determines if enough time has passed to move onto next frame
	// public boolean ReadyForNextFrame();
	// only use this after a call to the above.
	// double GetTimeElapsed(){return timeElapsed;}
	// public double TimeElapsed();
	public double currentTime() {
		// QueryPerformanceCounter((LARGE_INTEGER *) & currentTime);
		currentTime = System.currentTimeMillis();
		return (currentTime - startTime) * timeScale;
	}

	public boolean started() {
		return started;
	}

	public void smoothUpdatesOn() {
		smoothUpdates = true;
	}

	public void smoothUpdatesOff() {
		smoothUpdates = false;
	}

	/**
	 * returns true if it is time to move on to the next frame step. To be used if
	 * FPS is set.
	 */
	public boolean readyForNextFrame() {
		assert normalFPS != 0 : "PrecisionTimer::ReadyForNextFrame<No FPS set in timer>";

		// QueryPerformanceCounter((LARGE_INTEGER *) & currentTime);
		currentTime = System.currentTimeMillis();

		if (currentTime > nextTime) {

			timeElapsed = (currentTime - lastTime) * timeScale;
			lastTime = currentTime;

			// update time to render next frame
			nextTime = currentTime + frameTime;

			return true;
		}

		return false;
	}

	/**
	 * returns time elapsed since last call to this function.
	 */
	public double timeElapsed() {
		lastTimeElapsed = timeElapsed;

		// QueryPerformanceCounter((LARGE_INTEGER *) & currentTime);
		currentTime = System.currentTimeMillis();

		timeElapsed = (currentTime - lastTimeInTimeElapsed) * timeScale;

		lastTimeInTimeElapsed = currentTime;

		final double Smoothness = 5.0;

		if (smoothUpdates) {
			if (timeElapsed < (lastTimeElapsed * Smoothness)) {
				return timeElapsed;
			} else {
				return 0.0;
			}
		} else {
			return timeElapsed;
		}
	}
}