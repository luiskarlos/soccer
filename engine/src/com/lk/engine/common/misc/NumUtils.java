/*
 * Desc: misc utility functions and Constants
 * 
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.common.misc;

/**
 * 
 * @author Petr
 */
public final class NumUtils {
	// a few useful finalants

	public static final double TWO_PI = Math.PI * 2;
	public static final double HALF_PI = Math.PI / 2;
	public static final double QUARTER_PI = Math.PI / 4;

	public static final double EPSILON_DOUBLE = Double.MIN_NORMAL;

	static public double DegsToRads(final double degs) {
		return TWO_PI * (degs / 360.0);
	}

	// compares two real numbers. Returns true if they are equal
	static public boolean isEqual(final float a, final float b) {
		return Math.abs(a - b) < 1E-12;
	}

	// -----------------------------------------------------------------------
	//
	// some handy little functions
	// -----------------------------------------------------------------------
	public static double sigmoid(final double input) {
		return sigmoid(input, 1.0);
	}

	public static double sigmoid(final double input, final double response) {
		return (1.0 / (1.0 + Math.exp(-input / response)));
	}

	// returns the maximum of two values
	public static <T extends Comparable<T>> T maxOf(final T a, final T b) {
		if (a.compareTo(b) > 0) {
			return a;
		}
		return b;
	}

	// returns the minimum of two values
	public static <T extends Comparable<T>> T minOf(final T a, final T b) {
		if (a.compareTo(b) < 0) {
			return a;
		}
		return b;
	}

	/**
	 * clamps the first argument between the second two
	 */
	public static <T extends Number> T clamp(final T arg, final T minVal, final T maxVal) {
		assert (minVal.doubleValue() < maxVal.doubleValue()) : "<Clamp>MaxVal < MinVal!";

		if (arg.doubleValue() < minVal.doubleValue()) {
			return minVal;
		}

		if (arg.doubleValue() > maxVal.doubleValue()) {
			return maxVal;
		}
		return arg;
	}

	public static boolean isEqual(final double a, final double b) {
		return (Math.abs(a - b) < 1E-12);
	}

	public static <T extends Number> String toString(final T t, final int precision) {
		String s = t.toString();
		if (s.indexOf('.') > 0) {
			return s.substring(0, s.indexOf('.')+precision);
		}
		return s;
	}
}
