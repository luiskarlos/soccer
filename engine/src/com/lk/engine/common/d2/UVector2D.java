package com.lk.engine.common.d2;

import jsinterop.annotations.JsType;

@JsType
public interface UVector2D {

	double x();

	double y();

	boolean isZero();

	double length();

	double lengthSq();

	double dot(final UVector2D v2);

	int sign(final UVector2D v2);

	Vector2D perp();

	double distance(final UVector2D v2);

	double distanceSq(final UVector2D v2);

	Vector2D getReverse();

	boolean isEqual(final UVector2D rhs);

	boolean notEqual(final UVector2D rhs);
}
