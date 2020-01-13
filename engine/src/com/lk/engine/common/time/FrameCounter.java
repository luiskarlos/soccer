/**
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.common.time;

public class FrameCounter {
	private long count = 0;
	private int framesElapsed = 0;

	public FrameCounter() {
	}

	public void update() {
		++count;
		++framesElapsed;
	}

	public long getCurrentFrame() {
		return count;
	}

	public void reset() {
		count = 0;
	}

	public void start() {
		framesElapsed = 0;
	}

	public int framesElapsedSinceStartCalled() {
		return framesElapsed;
	}
}