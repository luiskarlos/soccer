/**
 * 
 *  Desc:   class to create and render 2D walls. Defined as the two 
 *          vectors A - B with a perpendicular NORMAL. 
 *
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.common.d2;

import static com.lk.engine.common.d2.Vector2D.add;
import static com.lk.engine.common.d2.Vector2D.div;
import static com.lk.engine.common.d2.Vector2D.sub;
import static com.lk.engine.common.d2.Vector2D.vec2DNormalize;

public class Wall2D {
	protected Vector2D a = new Vector2D();
	protected Vector2D b = new Vector2D();
	protected Vector2D n = new Vector2D();

	protected void calculateNormal() {
		final Vector2D temp = vec2DNormalize(sub(b, a));
		n.x = -temp.y;
		n.y = temp.x;
	}

	public Wall2D() {
	}

	public Wall2D(final Vector2D A, final Vector2D B) {
		a = A;
		b = B;
		calculateNormal();
	}

	public Wall2D(final Vector2D A, final Vector2D B, final Vector2D N) {
		a = A;
		b = B;
		n = N;
	}

	public Vector2D from() {
		return a;
	}

	public void setFrom(final Vector2D v) {
		a = v;
		calculateNormal();
	}

	public Vector2D to() {
		return b;
	}

	public void setTo(final Vector2D v) {
		b = v;
		calculateNormal();
	}

	public Vector2D normal() {
		return n;
	}

	public void setNormal(final Vector2D n) {
		this.n = n;
	}

	public Vector2D center() {
		return div(add(a, b), 2.0);
	}
}