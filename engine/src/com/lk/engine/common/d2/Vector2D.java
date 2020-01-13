/**
 *   2D vector struct
 *   @author Petr Bilek (http://www.sallyx.org/)
 */
package com.lk.engine.common.d2;

import static com.lk.engine.common.misc.NumUtils.EPSILON_DOUBLE;

import com.lk.engine.common.misc.NumUtils;

public class Vector2D {
	public static final int CLOCKWISE = 1;
	public static final int ANTICLOCKWISE = -1;

	public double x;
	public double y;

	public Vector2D() {
		x = 0.0;
		y = 0.0;
	}

	public Vector2D(final double a, final double b) {
		x = a;
		y = b;
	}

	public Vector2D(final Vector2D v) {
		super();
		this.set(v);
	}

	public Vector2D set(final Vector2D v) {
		this.x = v.x;
		this.y = v.y;
		return this;
	}

	// sets x and y to zero

	public void zero() {
		x = 0.0;
		y = 0.0;
	}

	// returns true if both x and y are zero
	public boolean isZero() {
		return (x * x + y * y) < Float.MIN_VALUE;
	}

	/**
	 * returns the length of a 2D vector
	 */
	public double length() {
		return Math.sqrt(x * x + y * y);
	}

	// returns the squared length of the vector (thereby avoiding the sqrt)
	public double lengthSq() {
		return (x * x + y * y);
	}

	/**
	 * normalizes a 2D Vector
	 */
	public void normalize() {
		final double vectorLength = this.length();

		if (vectorLength > EPSILON_DOUBLE) {
			this.x /= vectorLength;
			this.y /= vectorLength;
		}
	}

	/**
	 * calculates the dot product
	 * 
	 * @param v2
	 * @return dot product
	 */
	public double dot(final Vector2D v2) {
		return x * v2.x + y * v2.y;
	}

	/**
	 * returns positive if v2 is CLOCKWISE of this vector, negative if
	 * ANTICLOCKWISE (assuming the Y axis is pointing down, X axis to right like a
	 * Window app)
	 */
	public int sign(final Vector2D v2) {
		if (y * v2.x > x * v2.y) {
			return ANTICLOCKWISE;
		} else {
			return CLOCKWISE;
		}
	}

	/**
	 * returns the vector that is perpendicular to this one.
	 */
	public Vector2D perp() {
		return new Vector2D(-y, x);
	}

	/**
	 * adjusts x and y so that the length of the vector does not exceed max
	 * truncates a vector so that its length does not exceed max
	 * 
	 * @param max
	 */
	public void truncate(final double max) {
		if (this.length() > max) {
			this.normalize();
			this.mul(max);
		}
	}

	/**
	 * calculates the euclidean distance between two vectors
	 * 
	 * @param v2
	 * @return the distance between this vector and th one passed as a parameter
	 */
	public double distance(final Vector2D v2) {
		return Math.sqrt(distanceSq(v2));
	}

	/**
	 * squared version of distance. calculates the euclidean distance squared
	 * between two vectors
	 * 
	 * @param v2
	 * @return
	 */
	public double distanceSq(final Vector2D v2) {
		final double ySeparation = v2.y - y;
		final double xSeparation = v2.x - x;

		return ySeparation * ySeparation + xSeparation * xSeparation;
	}

	/**
	 * given a normalized vector this method reflects the vector it is operating
	 * upon. (like the path of a ball bouncing off a wall)
	 * 
	 * @param norm
	 */
	public void reflect(final Vector2D norm) {
		this.add(norm.getReverse().mul(2.0 * dot(norm)));
	}

	/**
	 * @return the vector that is the reverse of this vector
	 */
	public Vector2D getReverse() {
		return new Vector2D(-this.x, -this.y);
	}

	// we need some overloaded operators
	public Vector2D add(final Vector2D rhs) {
		x += rhs.x;
		y += rhs.y;

		return this;
	}

	public Vector2D sub(final Vector2D rhs) {
		x -= rhs.x;
		y -= rhs.y;

		return this;
	}

	public Vector2D mul(final double rhs) {
		x *= rhs;
		y *= rhs;

		return this;
	}

	public Vector2D div(final double rhs) {
		x /= rhs;
		y /= rhs;

		return this;
	}

	public boolean isEqual(final Vector2D rhs) {
		return (NumUtils.isEqual(x, rhs.x) && NumUtils.isEqual(y, rhs.y));
	}

	// operator !=

	public boolean notEqual(final Vector2D rhs) {
		return (x != rhs.x) || (y != rhs.y);
	}

	// ------------------------------------------------------------------------some
	// more operator overloads
	public static Vector2D mul(final Vector2D lhs, final double rhs) {
		final Vector2D result = new Vector2D(lhs);
		result.mul(rhs);
		return result;
	}

	public static Vector2D mul(final double lhs, final Vector2D rhs) {
		final Vector2D result = new Vector2D(rhs);
		result.mul(lhs);
		return result;
	}

	// overload the - operator
	public static Vector2D sub(final Vector2D lhs, final Vector2D rhs) {
		final Vector2D result = new Vector2D(lhs);
		result.x -= rhs.x;
		result.y -= rhs.y;

		return result;
	}

	// overload the + operator
	public static Vector2D add(final Vector2D lhs, final Vector2D rhs) {
		final Vector2D result = new Vector2D(lhs);
		result.x += rhs.x;
		result.y += rhs.y;

		return result;
	}

	// overload the / operator
	public static Vector2D div(final Vector2D lhs, final double val) {
		final Vector2D result = new Vector2D(lhs);
		result.x /= val;
		result.y /= val;

		return result;
	}

	// std::ostream& operator<<(std::ostream& os, const Vector2D& rhs)
	@Override
	public String toString() {
		return " " + NumUtils.toString(this.x, 2) + " " + NumUtils.toString(this.y, 2);
	}

	// ------------------------------------------------------------------------non
	// member functions
	public static Vector2D vec2DNormalize(final Vector2D v) {
		final Vector2D vec = new Vector2D(v);

		final double vector_length = vec.length();

		if (vector_length > EPSILON_DOUBLE) {
			vec.x /= vector_length;
			vec.y /= vector_length;
		}

		return vec;
	}

	public static double vec2DDistance(final Vector2D v1, final Vector2D v2) {
		return Math.sqrt(vec2DDistanceSq(v1, v2));
	}

	public static double vec2DDistanceSq(final Vector2D v1, final Vector2D v2) {
		final double ySeparation = v2.y - v1.y;
		final double xSeparation = v2.x - v1.x;

		return ySeparation * ySeparation + xSeparation * xSeparation;
	}

	public static double vec2DLength(final Vector2D v) {
		return Math.sqrt(v.x * v.x + v.y * v.y);
	}

	public static double vec2DLengthSq(final Vector2D v) {
		return (v.x * v.x + v.y * v.y);
	}

	// /////////////////////////////////////////////////////////////////////////////
	// treats a window as a toroid
	public static void wrapAround(final Vector2D pos, final int maxX, final int maxY) {
		if (pos.x > maxX) {
			pos.x = 0.0;
		}

		if (pos.x < 0) {
			pos.x = maxX;
		}

		if (pos.y < 0) {
			pos.y = maxY;
		}

		if (pos.y > maxY) {
			pos.y = 0.0;
		}
	}

	/**
	 * returns true if the point p is not inside the region defined by top_left
	 * and bot_rgt
	 */
	public static boolean notInsideRegion(final Vector2D p, final Vector2D topLeft, final Vector2D botRgt) {
		return (p.x < topLeft.x) || (p.x > botRgt.x) || (p.y < topLeft.y) || (p.y > botRgt.y);
	}

	public static boolean insideRegion(final Vector2D p, final Vector2D topLeft, final Vector2D botRgt) {
		return !((p.x < topLeft.x) || (p.x > botRgt.x) || (p.y < topLeft.y) || (p.y > botRgt.y));
	}

	public static boolean insideRegion(final Vector2D p, final int left, final int top, final int right, final int bottom) {
		return !((p.x < left) || (p.x > right) || (p.y < top) || (p.y > bottom));
	}

	/**
	 * @return true if the target position is in the field of view of the entity
	 *         positioned at posFirst facing in facingFirst
	 */
	public static boolean isSecondInFOVOfFirst(final Vector2D posFirst, final Vector2D facingFirst,
	    final Vector2D posSecond, final double fov) {
		final Vector2D toTarget = vec2DNormalize(sub(posSecond, posFirst));
		return facingFirst.dot(toTarget) >= Math.cos(fov / 2.0);
	}
}