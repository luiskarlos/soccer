/**
 *
 *  Desc:   class to create and render 2D walls. Defined as the two
 *          vectors A - B with a perpendicular NORMAL.
 *
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.common.d2;

import java.io.Serializable;

import static com.lk.engine.common.d2.Vector2D.add;
import static com.lk.engine.common.d2.Vector2D.div;
import static com.lk.engine.common.d2.Vector2D.sub;
import static com.lk.engine.common.d2.Vector2D.vec2DNormalize;

public class Wall2D implements Serializable {
	protected final Vector2D a = new Vector2D();
	protected final Vector2D b = new Vector2D();
	protected final Vector2D n = new Vector2D();

	protected void calculateNormal() {
		final Vector2D temp = vec2DNormalize(sub(b, a));
		n.x = -temp.y;
		n.y = temp.x;
	}

	public Wall2D() {
	}

	public Wall2D(final UVector2D A, final UVector2D B) {
		a.set(A);
		b.set(B);
		calculateNormal();
	}

	public Wall2D(final UVector2D A, final UVector2D B, final UVector2D N) {
		a.set(A);
		b.set(B);
		n.set(N);
	}

	public UVector2D from() {
		return a;
	}

	public void setFrom(final UVector2D v) {
		a.set(v);
		calculateNormal();
	}

	public UVector2D to() {
		return b;
	}

	public void setTo(final UVector2D v) {
		b.set(v);
		calculateNormal();
	}

	public UVector2D normal() {
		return n;
	}

	public void setNormal(final UVector2D n) {
		this.n.set(n);
	}

	public UVector2D center() {
		return div(add(a, b), 2.0);
	}
}
