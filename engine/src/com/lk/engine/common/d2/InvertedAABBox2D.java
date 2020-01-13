/**
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.common.d2;

import static com.lk.engine.common.d2.Vector2D.add;

public class InvertedAABBox2D {
	private Vector2D topLeft;
	private Vector2D bottomRight;
	private Vector2D center;

	public InvertedAABBox2D(final Vector2D tl, final Vector2D br) {
		topLeft = tl;
		bottomRight = br;
		center = add(tl, br).div(2.0);
	}

	// returns true if the bbox described by other intersects with this one
	public boolean isOverlappedWith(final InvertedAABBox2D other) {
		return !((other.top() > this.bottom()) || (other.bottom() < this.top()) || (other.left() > this.right()) || (other
		    .right() < this.left()));
	}

	public Vector2D topLeft() {
		return topLeft;
	}

	public Vector2D bottomRight() {
		return bottomRight;
	}

	public double top() {
		return topLeft.y;
	}

	public double left() {
		return topLeft.x;
	}

	public double bottom() {
		return bottomRight.y;
	}

	public double right() {
		return bottomRight.x;
	}

	public Vector2D center() {
		return center;
	}
}