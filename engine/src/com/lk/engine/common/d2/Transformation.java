/**
 *  Desc:   Functions for converting 2D vectors between World and Local
 *          space.
 *
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.common.d2;

import static com.lk.engine.common.d2.Vector2D.add;
import static com.lk.engine.common.d2.Vector2D.mul;

import java.util.ArrayList;
import java.util.List;

import com.lk.engine.common.misc.CppToJava;

public class Transformation {
	// --------------------------- WorldTransform -----------------------------
	//
	// given a std::vector of 2D vectors, a position, orientation and scale,
	// this function transforms the 2D vectors into the object's world space
	// ------------------------------------------------------------------------
	public static List<Vector2D> worldTransform(final List<Vector2D> points, final UVector2D pos, final UVector2D forward,
	    final UVector2D side, final UVector2D scale) {
		// create a transformation matrix
		final C2DMatrix matTransform = new C2DMatrix();

		// scale
		if ((scale.x() != 1.0) || (scale.y() != 1.0)) {
			matTransform.scale(scale.x(), scale.y());
		}
		// rotate
		matTransform.rotate(forward, side);
		// and translate
		matTransform.translate(pos.x(), pos.y());

		// copy the original vertices into the buffer about to be transformed
		final List<Vector2D> tranVector2Ds = CppToJava.clone(points);

		// now transform the object's vertices
		matTransform.transformVector2Ds(tranVector2Ds);

		return tranVector2Ds;
	}

	/**
	 * given a std::vector of 2D vectors, a position and orientation this function
	 * transforms the 2D vectors into the object's world space
	 */
	public static List<Vector2D> worldTransform(final List<Vector2D> points, final Vector2D pos, final Vector2D forward,
	    final Vector2D side) {
		// copy the original vertices into the buffer about to be transformed
		final List<Vector2D> tranVector2Ds = CppToJava.clone(points);
		for (final Vector2D v : points) {
			tranVector2Ds.add(v);
		}

		// create a transformation matrix
		final C2DMatrix matTransform = new C2DMatrix();
		// rotate
		matTransform.rotate(forward, side);
		// and translate
		matTransform.translate(pos.x, pos.y);
		// now transform the object's vertices
		matTransform.transformVector2Ds(tranVector2Ds);

		return tranVector2Ds;
	}

	// --------------------- PointToWorldSpace --------------------------------
	//
	// Transforms a point from the agent's local space into world space
	// ------------------------------------------------------------------------
	public static Vector2D pointToWorldSpace(final Vector2D point, final Vector2D AgentHeading, final Vector2D AgentSide,
	    final Vector2D AgentPosition) {
		// create a transformation matrix
		final C2DMatrix matTransform = new C2DMatrix();
		// rotate
		matTransform.rotate(AgentHeading, AgentSide);
		// and translate
		matTransform.translate(AgentPosition.x, AgentPosition.y);

		// make a copy of the point
		final Vector2D transPoint = new Vector2D(point);
		// now transform the vertices
		matTransform.transformVector2Ds(transPoint);

		return transPoint;
	}

	// --------------------- VectorToWorldSpace --------------------------------
	//
	// Transforms a vector from the agent's local space into world space
	// ------------------------------------------------------------------------
	public static Vector2D vectorToWorldSpace(final Vector2D vec, final Vector2D AgentHeading, final Vector2D AgentSide) {

		// create a transformation matrix
		final C2DMatrix matTransform = new C2DMatrix();
		// rotate
		matTransform.rotate(AgentHeading, AgentSide);

		// make a copy of the point
		final Vector2D transVec = new Vector2D(vec);
		// now transform the vertices
		matTransform.transformVector2Ds(transVec);

		return transVec;
	}

	// --------------------- PointToLocalSpace --------------------------------
	//
	// ------------------------------------------------------------------------
	public static Vector2D pointToLocalSpace(final UVector2D point, final UVector2D AgentHeading, final UVector2D AgentSide,
	    final UVector2D AgentPosition) {
		// create a transformation matrix
		final C2DMatrix matTransform = new C2DMatrix();

		final double Tx = -AgentPosition.dot(AgentHeading);
		final double Ty = -AgentPosition.dot(AgentSide);

		// create the transformation matrix
		matTransform._11(AgentHeading.x());
		matTransform._12(AgentSide.x());
		matTransform._21(AgentHeading.y());
		matTransform._22(AgentSide.y());
		matTransform._31(Tx);
		matTransform._32(Ty);

		// make a copy of the point
		final Vector2D transPoint = new Vector2D(point);
		// now transform the vertices
		matTransform.transformVector2Ds(transPoint);

		return transPoint;
	}

	// --------------------- VectorToLocalSpace --------------------------------
	//
	// ------------------------------------------------------------------------
	public static Vector2D vectorToLocalSpace(final Vector2D vec, final Vector2D AgentHeading, final Vector2D AgentSide) {
		// create a transformation matrix
		final C2DMatrix matTransform = new C2DMatrix();

		// create the transformation matrix
		matTransform._11(AgentHeading.x);
		matTransform._12(AgentSide.x);
		matTransform._21(AgentHeading.y);
		matTransform._22(AgentSide.y);

		// make a copy of the point
		final Vector2D transPoint = new Vector2D(vec);

		// now transform the vertices
		matTransform.transformVector2Ds(transPoint);

		return transPoint;
	}

	// -------------------------- Vec2DRotateAroundOrigin
	// --------------------------
	//
	// rotates a vector ang rads around the origin
	// -----------------------------------------------------------------------------
	public static void vec2DRotateAroundOrigin(final Vector2D v, final double ang) {
		// create a transformation matrix
		final C2DMatrix mat = new C2DMatrix();
		// rotate
		mat.rotate(ang);
		// now transform the object's vertices
		mat.transformVector2Ds(v);
	}

	// ------------------------ CreateWhiskers
	// ------------------------------------
	//
	// given an origin, a facing direction, a 'field of view' describing the
	// limit of the outer whiskers, a whisker length and the number of whiskers
	// this method returns a vector containing the end positions of a series
	// of whiskers radiating away from the origin and with equal distance between
	// them. (like the spokes of a wheel clipped to a specific segment size)
	// ----------------------------------------------------------------------------
	public static List<Vector2D> createWhiskers(final int numWhiskers, final double WhiskerLength, final double fov,
	    final Vector2D facing, final Vector2D origin) {
		// this is the magnitude of the angle separating each whisker
		final double sectorSize = fov / (numWhiskers - 1);

		final List<Vector2D> whiskers = new ArrayList<Vector2D>(numWhiskers);
		Vector2D temp; // TODO: this maybe a bug
		double angle = -fov * 0.5;

		for (int w = 0; w < numWhiskers; ++w) {
			// create the whisker extending outwards at this angle
			temp = facing;
			vec2DRotateAroundOrigin(temp, angle);
			whiskers.add(add(origin, mul(WhiskerLength, temp)));

			angle += sectorSize;
		}

		return whiskers;
	}
}