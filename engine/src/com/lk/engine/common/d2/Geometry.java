/**
 * Desc:   useful 2D Geometry functions
 *
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.common.d2;

import static com.lk.engine.common.d2.Transformation.pointToLocalSpace;
import static com.lk.engine.common.d2.Vector2D.add;
import static com.lk.engine.common.d2.Vector2D.div;
import static com.lk.engine.common.d2.Vector2D.mul;
import static com.lk.engine.common.d2.Vector2D.sub;
import static com.lk.engine.common.d2.Vector2D.vec2DDistance;
import static com.lk.engine.common.d2.Vector2D.vec2DDistanceSq;
import static com.lk.engine.common.d2.Vector2D.vec2DNormalize;
import static com.lk.engine.common.misc.NumUtils.isEqual;

import java.util.ArrayList;

import com.lk.engine.common.misc.CppToJava.DoubleRef;

public class Geometry {
	/**
	 * given a plane and a ray this function determines how far along the ray an
	 * intersection occurs. Returns negative if the ray is parallel
	 */
	public static double DistanceToRayPlaneIntersection(final UVector2D rayOrigin, final UVector2D rayHeading,
	    final UVector2D planePoint, // any point on the plane
	    final UVector2D planeNormal) {

		final double d = -planeNormal.dot(planePoint);
		final double numer = planeNormal.dot(rayOrigin) + d;
		final double denom = planeNormal.dot(rayHeading);

		// NORMAL is parallel to vector
		if ((denom < 0.000001) && (denom > -0.000001)) {
			return (-1.0);
		}

		return -(numer / denom);
	}

	// ------------------------- WhereIsPoint
	// --------------------------------------
	public static enum SpanType {
		PLANE_BACK_SIDE, PLANE_FRONT, ON_PLANE;
	}

	public static SpanType whereIsPoint(final UVector2D point, final UVector2D pointOnPlane,
																			// any on the plane
	  																	final UVector2D planeNormal) {
		final Vector2D dir = sub(pointOnPlane, point);
		final double d = dir.dot(planeNormal);

		if (d < -0.000001) {
			return SpanType.PLANE_FRONT;
		} else if (d > 0.000001) {
			return SpanType.PLANE_BACK_SIDE;
		}

		return SpanType.ON_PLANE;
	}

	/**
	 * GetRayCircleIntersec
	 */
	public static double getRayCircleIntersect(final Vector2D rayOrigin, final Vector2D rayHeading,
	    final Vector2D circleOrigin, final double radius) {
		final Vector2D toCircle = sub(circleOrigin, rayOrigin);
		final double length = toCircle.length();
		final double v = toCircle.dot(rayHeading);
		final double d = radius * radius - (length * length - v * v);

		// If there was no intersection, return -1
		if (d < 0.0) {
			return (-1.0);
		}

		// Return the distance to the [first] intersecting point
		return (v - Math.sqrt(d));
	}

	/**
	 * DoRayCircleIntersect
	 */
	public static boolean doRayCircleIntersect(final UVector2D rayOrigin, final UVector2D rayHeading,
	    final UVector2D circleOrigin, final double radius) {
		final Vector2D toCircle = sub(circleOrigin, rayOrigin);
		final double length = toCircle.length();
		final double v = toCircle.dot(rayHeading);
		final double d = radius * radius - (length * length - v * v);

		// If there was no intersection, return -1
		return (d < 0.0);
	}

	/**
	 * Given a point P and a circle of radius R centered at C this function
	 * determines the two points on the circle that intersect with the tangents
	 * from P to the circle. Returns false if P is within the circle.
	 *
	 * Thanks to Dave Eberly for this one.
	 */
	public static boolean getTangentPoints(final UVector2D C, final double R, final UVector2D P, final Vector2D T1,
	    final Vector2D T2) {
		final Vector2D PmC = sub(P, C);
		final double sqrLen = PmC.lengthSq();
		final double rSqr = R * R;
		if (sqrLen <= rSqr) {
			// P is inside or on the circle
			return false;
		}

		final double invSqrLen = 1 / sqrLen;
		final double root = Math.sqrt(Math.abs(sqrLen - rSqr));

		T1.x = C.x() + R * (R * PmC.x - PmC.y * root) * invSqrLen;
		T1.y = C.y() + R * (R * PmC.y + PmC.x * root) * invSqrLen;
		T2.x = C.x() + R * (R * PmC.x + PmC.y * root) * invSqrLen;
		T2.y = C.y() + R * (R * PmC.y - PmC.x * root) * invSqrLen;

		return true;
	}

	/**
	 * given a line segment AB and a point P, this function calculates the
	 * perpendicular distance between them TODO: refactoring candidate
	 */
	public static double distToLineSegment(final Vector2D A, final Vector2D B, final Vector2D P) {
		// if the angle is obtuse between PA and AB is obtuse then the closest
		// vertex must be A
		final double dotA = (P.x - A.x) * (B.x - A.x) + (P.y - A.y) * (B.y - A.y);

		if (dotA <= 0) {
			return vec2DDistance(A, P);
		}

		// if the angle is obtuse between PB and AB is obtuse then the closest
		// vertex must be B
		final double dotB = (P.x - B.x) * (A.x - B.x) + (P.y - B.y) * (A.y - B.y);

		if (dotB <= 0) {
			return vec2DDistance(B, P);
		}

		// calculate the point along AB that is the closest to P
		// Vector2D Point = A + ((B - A) * dotA)/(dotA + dotB);
		final Vector2D point = add(A, (div(mul(sub(B, A), dotA), (dotA + dotB))));

		// calculate the distance P-Point
		return vec2DDistance(P, point);
	}

	/**
	 * as above, but avoiding sqrt
	 */
	public static double distToLineSegmentSq(final Vector2D A, final Vector2D B, final Vector2D P) {
		// if the angle is obtuse between PA and AB is obtuse then the closest
		// vertex must be A
		final double dotA = (P.x - A.x) * (B.x - A.x) + (P.y - A.y) * (B.y - A.y);

		if (dotA <= 0) {
			return vec2DDistanceSq(A, P);
		}

		// if the angle is obtuse between PB and AB is obtuse then the closest
		// vertex must be B
		final double dotB = (P.x - B.x) * (A.x - B.x) + (P.y - B.y) * (A.y - B.y);

		if (dotB <= 0) {
			return vec2DDistanceSq(B, P);
		}

		// calculate the point along AB that is the closest to P
		// Vector2D Point = A + ((B - A) * dotA)/(dotA + dotB);
		final Vector2D Point = add(A, (div(mul(sub(B, A), dotA), (dotA + dotB))));

		// calculate the distance P-Point
		return vec2DDistanceSq(P, Point);
	}

	/**
	 * Given 2 lines in 2D space AB, CD this returns true if an intersection
	 * occurs.
	 */
	public static boolean lineIntersection2D(final UVector2D A, final UVector2D B, final UVector2D C, final UVector2D D) {
		final double bot = (B.x() - A.x()) * (D.y() - C.y()) - (B.y() - A.y()) * (D.x() - C.x());
		if (bot == 0)// parallel
		{
			return false;
		}

		final double rTop = (A.y() - C.y()) * (D.x() - C.x()) - (A.x() - C.x()) * (D.y() - C.y());
		final double sTop = (A.y() - C.y()) * (B.x() - A.x()) - (A.x() - C.x()) * (B.y() - A.y());

		final double invBot = 1.0 / bot;
		final double r = rTop * invBot;
		final double s = sTop * invBot;

		if ((r > 0) && (r < 1) && (s > 0) && (s < 1)) {
			// lines intersect
			return true;
		}

		// lines do not intersect
		return false;
	}

	/**
	 * Given 2 lines in 2D space AB, CD this returns true if an intersection
	 * occurs and sets dist to the distance the intersection occurs along AB
	 */
	public static boolean lineIntersection2D(final Vector2D A, final Vector2D B, final Vector2D C, final Vector2D D,
	    final DoubleRef dist) // double
	// &dist
	{
		final double rTop = (A.y - C.y) * (D.x - C.x) - (A.x - C.x) * (D.y - C.y);
		final double sTop = (A.y - C.y) * (B.x - A.x) - (A.x - C.x) * (B.y - A.y);

		final double Bot = (B.x - A.x) * (D.y - C.y) - (B.y - A.y) * (D.x - C.x);

		if (Bot == 0)// parallel
		{
			if (isEqual(rTop, 0) && isEqual(sTop, 0)) {
				return true;
			}
			return false;
		}

		final double r = rTop / Bot;
		final double s = sTop / Bot;

		if ((r > 0) && (r < 1) && (s > 0) && (s < 1)) {
			dist.set(vec2DDistance(A, B) * r);
			return true;
		} else {
			dist.set(0.0);
			return false;
		}
	}

	/**
	 * Given 2 lines in 2D space AB, CD this returns true if an intersection
	 * occurs and sets dist to the distance the intersection occurs along AB. Also
	 * sets the 2d vector point to the point of intersection
	 */
	public static boolean lineIntersection2D(final Vector2D A, final Vector2D B, final Vector2D C, final Vector2D D,
	    final DoubleRef dist, final Vector2D point) {
		final double rBot = (B.x - A.x) * (D.y - C.y) - (B.y - A.y) * (D.x - C.x);
		final double sBot = (B.x - A.x) * (D.y - C.y) - (B.y - A.y) * (D.x - C.x);

		if ((rBot == 0) || (sBot == 0)) {
			// lines are parallel
			return false;
		}

		final double rTop = (A.y - C.y) * (D.x - C.x) - (A.x - C.x) * (D.y - C.y);
		final double sTop = (A.y - C.y) * (B.x - A.x) - (A.x - C.x) * (B.y - A.y);

		final double r = rTop / rBot;
		final double s = sTop / sBot;

		if ((r > 0) && (r < 1) && (s > 0) && (s < 1)) {
			dist.set(vec2DDistance(A, B) * r);
			point.set(add(A, mul(r, sub(B, A))));
			return true;
		} else {
			dist.set(0.0);
			return false;
		}
	}

	/**
	 * tests two polygons for intersection. *Does not check for enclosure*
	 */
	public static boolean objectIntersection2D(final ArrayList<Vector2D> object1, final ArrayList<Vector2D> object2) {
		// test each line segment of object1 against each segment of object2
		for (int r = 0; r < object1.size() - 1; ++r) {
			for (int t = 0; t < object2.size() - 1; ++t) {
				if (lineIntersection2D(object2.get(t), object2.get(t + 1), object1.get(r), object1.get(r + 1))) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * tests a line segment against a polygon for intersection *Does not check for
	 * enclosure*
	 */
	public static boolean segmentObjectIntersection2D(final Vector2D A, final Vector2D B, final ArrayList<Vector2D> object) {
		// test AB against each segment of object
		for (int r = 0; r < object.size() - 1; ++r) {
			if (lineIntersection2D(A, B, object.get(r), object.get(r + 1))) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns true if the two circles overlap
	 */
	public static boolean twoCirclesOverlapped(final double x1, final double y1, final double r1, final double x2,
	    final double y2, final double r2) {
		final double distBetweenCenters = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
		return ((distBetweenCenters < (r1 + r2)) || (distBetweenCenters < Math.abs(r1 - r2)));
	}

	/**
	 * Returns true if the two circles overlap
	 */
	public static boolean twoCirclesOverlapped(final UVector2D c1, final double r1, final UVector2D c2, final double r2) {
		final double DistBetweenCenters = Math.sqrt((c1.x() - c2.x()) * (c1.x() - c2.x()) + (c1.y() - c2.y()) * (c1.y() - c2.y()));
		return ((DistBetweenCenters < (r1 + r2)) || (DistBetweenCenters < Math.abs(r1 - r2)));
	}

	/**
	 * returns true if one circle encloses the other
	 */
	public static boolean twoCirclesEnclosed(final double x1, final double y1, final double r1, final double x2,
	    final double y2, final double r2) {
		final double DistBetweenCenters = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
		return (DistBetweenCenters < Math.abs(r1 - r2));
	}

	/**
	 * Given two circles this function calculates the intersection points of any
	 * overlap.
	 *
	 * returns false if no overlap found
	 *
	 * see http://astronomy.swin.edu.au/~pbourke/Geometry/2circle/
	 */
	public static boolean twoCirclesIntersectionPoints(final double x1, final double y1, final double r1,
	    final double x2, final double y2, final double r2, final DoubleRef p3X, final DoubleRef p3Y, final DoubleRef p4X,
	    final DoubleRef p4Y) {
		// first check to see if they overlap
		if (!twoCirclesOverlapped(x1, y1, r1, x2, y2, r2)) {
			return false;
		}

		// calculate the distance between the circle centers
		final double d = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));

		// Now calculate the distance from the center of each circle to the center
		// of the line which connects the intersection points.
		final double a = (r1 - r2 + (d * d)) / (2 * d);
		// final double b = (r2 - r1 + (d * d)) / (2 * d);

		// MAYBE A TEST FOR EXACT OVERLAP?

		// calculate the point P2 which is the center of the line which
		// connects the intersection points
		double p2X, p2Y;

		p2X = x1 + a * (x2 - x1) / d;
		p2Y = y1 + a * (y2 - y1) / d;

		// calculate first point
		final double h1 = Math.sqrt((r1 * r1) - (a * a));

		p3X.set(p2X - h1 * (y2 - y1) / d);
		p3Y.set(p2Y + h1 * (x2 - x1) / d);

		// calculate second point
		final double h2 = Math.sqrt((r2 * r2) - (a * a));

		p4X.set(p2X + h2 * (y2 - y1) / d);
		p4Y.set(p2Y - h2 * (x2 - x1) / d);

		return true;

	}

	/**
	 * Tests to see if two circles overlap and if so calculates the area defined
	 * by the union
	 *
	 * see http://mathforum.org/library/drmath/view/54785.html
	 */
	public static double twoCirclesIntersectionArea(final double x1, final double y1, final double r1, final double x2,
	    final double y2, final double r2) {
		// first calculate the intersection points
		final double iX1 = 0.0, iY1 = 0.0, iX2 = 0.0, iY2 = 0.0;

		if (!twoCirclesIntersectionPoints(x1, y1, r1, x2, y2, r2, new DoubleRef(iX1), new DoubleRef(iY1),
		    new DoubleRef(iX2), new DoubleRef(iY2))) {
			return 0.0; // no overlap
		}

		// calculate the distance between the circle centers
		final double d = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));

		// find the angles given that A and B are the two circle centers
		// and C and D are the intersection points
		final double CBD = 2 * Math.acos((r2 * r2 + d * d - r1 * r1) / (r2 * d * 2));

		final double CAD = 2 * Math.acos((r1 * r1 + d * d - r2 * r2) / (r1 * d * 2));

		// Then we find the segment of each of the circles cut off by the
		// chord CD, by taking the area of the sector of the circle BCD and
		// subtracting the area of triangle BCD. Similarly we find the area
		// of the sector ACD and subtract the area of triangle ACD.

		final double area = 0.5f * CBD * r2 * r2 - 0.5f * r2 * r2 * Math.sin(CBD) + 0.5f * CAD * r1 * r1 - 0.5f * r1 * r1
		    * Math.sin(CAD);

		return area;
	}

	/**
	 * given the radius, calculates the area of a circle
	 */
	public static double circleArea(final double radius) {
		return Math.PI * radius * radius;
	}

	/**
	 * returns true if the point p is within the radius of the given circle
	 */
	public static boolean pointInCircle(final Vector2D pos, final double radius, final Vector2D p) {
		final double distFromCenterSquared = (sub(p, pos)).lengthSq();
		return (distFromCenterSquared < (radius * radius));
	}

	/**
	 * returns true if the line segemnt AB intersects with a circle at position P
	 * with radius radius
	 */
	public static boolean lineSegmentCircleIntersection(final Vector2D A, final Vector2D B, final Vector2D P,
	    final double radius) {
		// first determine the distance from the center of the circle to
		// the line segment (working in distance squared space)
		final double DistToLineSq = distToLineSegmentSq(A, B, P);
		return (DistToLineSq < radius * radius);
	}

	/**
	 * given a line segment AB and a circle position and radius, this function
	 * determines if there is an intersection and stores the position of the
	 * closest intersection in the reference IntersectionPoint
	 *
	 * returns false if no intersection point is found
	 */
	public static boolean getLineSegmentCircleClosestIntersectionPoint(final Vector2D A, final Vector2D B,
	    final Vector2D pos, final double radius, final Vector2D intersectionPoint) {
		final Vector2D toBNorm = vec2DNormalize(sub(B, A));

		// move the circle into the local space defined by the vector B-A with
		// origin at A
		final Vector2D localPos = pointToLocalSpace(pos, toBNorm, toBNorm.perp(), A);

		// if the local position + the radius is negative then the circle lays
		// behind
		// point A so there is no intersection possible. If the local x pos minus
		// the
		// radius is greater than length A-B then the circle cannot intersect the
		// line segment
		if ((localPos.x + radius >= 0) && ((localPos.x - radius) * (localPos.x - radius) <= vec2DDistanceSq(B, A))) {
			// if the distance from the x axis to the object's position is less
			// than its radius then there is a potential intersection.
			if (Math.abs(localPos.y) < radius) {
				// now to do a line/circle intersection test. The center of the
				// circle is represented by A, B. The intersection points are
				// given by the formulae x = A +/-sqrt(r^2-B^2), y=0. We only
				// need to look at the smallest positive value of x.
				final double a = localPos.x;
				final double b = localPos.y;
				double ip = a - Math.sqrt(radius * radius - b * b);

				if (ip <= 0) {
					ip = a + Math.sqrt(radius * radius - b * b);
				}

				intersectionPoint.set(add(A, mul(toBNorm, ip)));
				return true;
			}
		}

		return false;
	}
}
