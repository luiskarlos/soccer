package com.lk.engine.common.misc;

import java.util.Random;

public class RandomGenerator extends Random {
	private static final long serialVersionUID = 1L;
	private double y2 = 0;
	private boolean use_last = false;

	public int randInt(final int x, final int y) {
		assert y >= x : "<RandInt>: y is less than x";
		return nextInt(Integer.MAX_VALUE - x) % (y - x + 1) + x;
	}

	public double randInRange(final double x, final double y) {
		return x + nextDouble() * (y - x);
	}

	public double randomClamped() {
		return nextDouble() - nextDouble();
	}

	// returns a random number with a NORMAL distribution. See method at
	// http://www.taygeta.com/random/gaussian.html
	public double randGaussian() {
		return randGaussian(0, 1);
	}

	public double randGaussian(final double mean, final double standard_deviation) {
		double x1, x2, w, y1;

		if (use_last) /* use value from previous call */
		{
			y1 = y2;
			use_last = false;
		} else {
			do {
				x1 = 2.0 * nextDouble() - 1.0;
				x2 = 2.0 * nextDouble() - 1.0;
				w = x1 * x1 + x2 * x2;
			} while (w >= 1.0);

			w = Math.sqrt((-2.0 * Math.log(w)) / w);
			y1 = x1 * w;
			y2 = x2 * w;
			use_last = true;
		}

		return (mean + y1 * standard_deviation);
	}
}
