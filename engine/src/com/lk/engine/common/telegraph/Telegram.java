package com.lk.engine.common.telegraph;

public interface Telegram {

	// these telegrams will be stored in a priority queue. Therefore the >
	// operator needs to be overloaded so that the PQ can sort the telegrams
	// by time priority. Note how the times must be smaller than
	// SmallestDelay apart before two Telegrams are considered unique.
	public final static double SMALLEST_DELAY = 0.25;

	public abstract int getReceiver();

	public abstract Message getMsg();

	public abstract Object getExtraInfo();

}