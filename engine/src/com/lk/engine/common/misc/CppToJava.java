/**
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.common.misc;

import java.util.ArrayList;
import java.util.List;

import com.lk.engine.common.d2.Vector2D;

public final class CppToJava {
	public static class DoubleRef {
		private double t;

		public DoubleRef(final double ref) {
			this.t = ref;
		}

		public double toDouble() {
			return t;
		}

		public void set(double d) {
			this.t = d;
		}
	}

	public static List<Vector2D> clone(final List<Vector2D> list) {
		try {
			final List<Vector2D> c = new ArrayList<Vector2D>(list.size());
			for (final Vector2D t : list) {
				final Vector2D copy = new Vector2D(t);
				c.add(copy);
			}
			return c;
		} catch (final Exception e) {
			throw new RuntimeException("List cloning unsupported", e);
		}
	}
}
