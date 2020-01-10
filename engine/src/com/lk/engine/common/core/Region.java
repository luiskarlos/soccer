/**
 *  Desc:   Defines a rectangular region. A region has an identifying
 *          number, and four corners.
 * 
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.common.core;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

import com.lk.engine.common.d2.UVector2D;
import com.lk.engine.common.d2.Vector2D;
import com.lk.engine.common.misc.RandomGenerator;

public class Region {
	public enum RegionModifier {
		HALF_SIZE, NORMAL
	}

	protected double top;
	protected double left;
	protected double right;
	protected double bottom;
	protected double width;
	protected double height;
	protected final Vector2D center;
	protected int id;

	public Region() {
		this(0, 0, 0, 0, -1);
	}

	public Region(final double left, final double top, final double right, final double bottom) {
		this(left, top, right, bottom, -1);
	}

	public Region(final double pLeft, final double pTop, final double pRight, final double pBottom, final int pId) {
		top = pTop;
		right = pRight;
		left = pLeft;
		bottom = pBottom;
		id = pId;

		// calculate center of region
		center = new Vector2D((pLeft + pRight) * 0.5, (pTop + pBottom) * 0.5);

		width = abs(pRight - pLeft);
		height = abs(pBottom - pTop);
	}

	public void set(final double left, final double top, final double right, final double bottom) {
		this.top = top;
		this.right = right;
		this.left = left;
		this.bottom = bottom;
	}

	/**
	 * returns true if the given position lays inside the region. The region
	 * modifier can be used to contract the region bounderies
	 */
	public boolean inside(final UVector2D pos) {
		return inside(pos, RegionModifier.NORMAL);
	}

	public boolean inside(final UVector2D pos, final RegionModifier r) {
		if (r == RegionModifier.NORMAL) {
			return ((pos.x() > left) && (pos.x() < right) && (pos.y() > top) && (pos.y() < bottom));
		} else {
			final double marginX = width * 0.25;
			final double marginY = height * 0.25;

			return ((pos.x() > (left + marginX)) && (pos.x() < (right - marginX)) && (pos.y() > (top + marginY)) && (pos.y() < (bottom - marginY)));
		}
	}

	/**
	 * @return a vector representing a random location within the region
	 */
	public Vector2D getRandomPosition(final RandomGenerator random) {
		return new Vector2D(random.randInRange(left, right), random.randInRange(top, bottom));
	}

	// -------------------------------
	public double top() {
		return top;
	}

	public double bottom() {
		return bottom;
	}

	public double left() {
		return left;
	}

	public double right() {
		return right;
	}

	public double width() {
		return abs(right - left);
	}

	public double height() {
		return abs(top - bottom);
	}

	public double length() {
		return max(width(), height());
	}

	public double breadth() {
		return min(width(), height());
	}

	public UVector2D center() {
		return center;
	}

	public int ID() {
		return id;
	}
}