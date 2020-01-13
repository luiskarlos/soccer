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

public class Smoother<T extends Number> {
	// this holds the history

	private List<T> history;
	private final int sampleSize;
	private int nextUpdateSlot;
	// an example of the 'zero' value of the type to be smoothed. This
	// would be something like Vector2D(0,0)
	private T zeroValue;

	/**
	 * to instantiate a Smoother pass it the number of samples you want to use in
	 * the smoothing, and an example of a 'zero' type
	 */
	public Smoother(final int sampleSize, final T pZeroValue) {
		this.sampleSize = sampleSize;
		history = new ArrayList<T>(sampleSize);
		for (int i = 0; i < sampleSize; i++)
			history.add(pZeroValue);

		zeroValue = pZeroValue;
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
		T sum = zeroValue;

		for (final T t : history) {
			sum = add(sum, t);
		}

		sum = div(sum, history.size());
		return sum;
	}

	@SuppressWarnings("unchecked")
	public T add(final T a, final T b) {
		return (T) new Double(a.doubleValue() + b.doubleValue());
	}

	@SuppressWarnings("unchecked")
	public T div(final T a, final double b) {
		return (T) new Double(a.doubleValue() / b);
	}
}
