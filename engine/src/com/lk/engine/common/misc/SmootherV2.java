/**
 * 
 *  Desc: Template class to help calculate the average value of a history
 *        of values. This can only be used with types that have a 'zero'
 *        value and that have the += and / operators overloaded.
 *
 *        Example: Used to smooth frame rate calculations.
 * 
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.common.misc;

import java.util.ArrayList;
import java.util.List;

import com.lk.engine.common.d2.Vector2D;

public class SmootherV2<T extends Vector2D> {
	// this holds the history
	private List<T> history;

	private final int sampleSize;
	private int nextUpdateSlot;

	// an example of the 'zero' value of the type to be smoothed. This
	// would be something like Vector2D(0,0)
	private T zeroValue;

	// to instantiate a Smoother pass it the number of samples you want
	// to use in the smoothing, and an example of a 'zero' type
	public SmootherV2(final int sampleSize, final T zeroValue) {
		this.sampleSize = sampleSize;
		history = new ArrayList<T>(sampleSize);
		for (int i = 0; i < sampleSize; i++)
			history.add(zeroValue);
		this.zeroValue = zeroValue;
		nextUpdateSlot = 0;
	}

	// each time you want to get a new average, feed it the most recent value
	// and this method will return an average over the last SampleSize updates
	public T Update(final T mostRecentValue) {
		// overwrite the oldest value with the newest
		history.set(nextUpdateSlot++, mostRecentValue);

		// make sure nextUpdateSlot wraps around.
		if (nextUpdateSlot == sampleSize) {
			nextUpdateSlot = 0;
		}

		// now to calculate the average of the history list
		final T sum = zeroValue;

		for (T t : history) {
			sum.add(t);
		}

		sum.div(history.size());
		return sum;
	}
}
