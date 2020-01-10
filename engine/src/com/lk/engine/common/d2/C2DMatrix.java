/**
 * Desc:   2D Matrix class 
 * 
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.common.d2;

import java.util.List;

public class C2DMatrix {
	private class Matrix {
		double _11, _12, _13;
		double _21, _22, _23;
		double _31, _32, _33;
	}

	private Matrix matrix = new Matrix();

	public C2DMatrix() {
		identity(); // initialize the matrix to an identity matrix
	}

	// Accessories to the matrix elements
	public void _11(final double val) {
		matrix._11 = val;
	}

	public void _12(final double val) {
		matrix._12 = val;
	}

	public void _13(final double val) {
		matrix._13 = val;
	}

	public void _21(final double val) {
		matrix._21 = val;
	}

	public void _22(final double val) {
		matrix._22 = val;
	}

	public void _23(final double val) {
		matrix._23 = val;
	}

	public void _31(final double val) {
		matrix._31 = val;
	}

	public void _32(final double val) {
		matrix._32 = val;
	}

	public void _33(final double val) {
		matrix._33 = val;
	}

	// multiply two matrices together
	private void matrixMultiply(final Matrix mIn) {
		final Matrix matTemp = new Matrix();

		// first row
		matTemp._11 = (matrix._11 * mIn._11) + (matrix._12 * mIn._21) + (matrix._13 * mIn._31);
		matTemp._12 = (matrix._11 * mIn._12) + (matrix._12 * mIn._22) + (matrix._13 * mIn._32);
		matTemp._13 = (matrix._11 * mIn._13) + (matrix._12 * mIn._23) + (matrix._13 * mIn._33);

		// second
		matTemp._21 = (matrix._21 * mIn._11) + (matrix._22 * mIn._21) + (matrix._23 * mIn._31);
		matTemp._22 = (matrix._21 * mIn._12) + (matrix._22 * mIn._22) + (matrix._23 * mIn._32);
		matTemp._23 = (matrix._21 * mIn._13) + (matrix._22 * mIn._23) + (matrix._23 * mIn._33);

		// third
		matTemp._31 = (matrix._31 * mIn._11) + (matrix._32 * mIn._21) + (matrix._33 * mIn._31);
		matTemp._32 = (matrix._31 * mIn._12) + (matrix._32 * mIn._22) + (matrix._33 * mIn._32);
		matTemp._33 = (matrix._31 * mIn._13) + (matrix._32 * mIn._23) + (matrix._33 * mIn._33);

		matrix = matTemp;
	}

	// applies a 2D transformation matrix to a std::vector of Vector2Ds
	public void transformVector2Ds(final List<Vector2D> vPoints) {
		for (final Vector2D vPoint : vPoints) {
			transformVector2Ds(vPoint);
		}
	}

	// applies a 2D transformation matrix to a single Vector2D
	public void transformVector2Ds(final Vector2D vPoint) {
		final double x = (matrix._11 * vPoint.x()) + (matrix._21 * vPoint.y()) + (matrix._31);
		final double y = (matrix._12 * vPoint.x()) + (matrix._22 * vPoint.y()) + (matrix._32);

		vPoint.x = x;
		vPoint.y = y;
	}

	// create an identity matrix
	public void identity() {
		matrix._11 = 1;
		matrix._12 = 0;
		matrix._13 = 0;

		matrix._21 = 0;
		matrix._22 = 1;
		matrix._23 = 0;

		matrix._31 = 0;
		matrix._32 = 0;
		matrix._33 = 1;

	}

	// create a transformation matrix
	public void translate(final double x, final double y) {
		final Matrix mat = new Matrix();

		mat._11 = 1;
		mat._12 = 0;
		mat._13 = 0;

		mat._21 = 0;
		mat._22 = 1;
		mat._23 = 0;

		mat._31 = x;
		mat._32 = y;
		mat._33 = 1;

		// and multiply
		matrixMultiply(mat);
	}

	// create a scale matrix
	public void scale(final double xScale, final double yScale) {
		final Matrix mat = new Matrix();

		mat._11 = xScale;
		mat._12 = 0;
		mat._13 = 0;

		mat._21 = 0;
		mat._22 = yScale;
		mat._23 = 0;

		mat._31 = 0;
		mat._32 = 0;
		mat._33 = 1;

		// and multiply
		matrixMultiply(mat);
	}

	// create a rotation matrix
	public void rotate(final double rot) {
		final Matrix mat = new Matrix();

		final double Sin = Math.sin(rot);
		final double Cos = Math.cos(rot);

		mat._11 = Cos;
		mat._12 = Sin;
		mat._13 = 0;
		mat._21 = -Sin;
		mat._22 = Cos;
		mat._23 = 0;
		mat._31 = 0;
		mat._32 = 0;
		mat._33 = 1;

		// and multiply
		matrixMultiply(mat);
	}

	// create a rotation matrix from a 2D vector
	public void rotate(final UVector2D fwd, final UVector2D side) {
		final Matrix mat = new Matrix();

		mat._11 = fwd.x();
		mat._12 = fwd.y();
		mat._13 = 0;
		mat._21 = side.x();
		mat._22 = side.y();
		mat._23 = 0;
		mat._31 = 0;
		mat._32 = 0;
		mat._33 = 1;

		// and multiply
		matrixMultiply(mat);
	}
}