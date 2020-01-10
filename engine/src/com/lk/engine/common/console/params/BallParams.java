package com.lk.engine.common.console.params;

public class BallParams extends MovingEntityParams {
	private double friction = -0.015;

	public BallParams() {
		setRadius(3.0);
		setMass(1.0);
	}

	public double getFriction() {
		return friction;
	}

	public void setFriction(final double friction) {
		this.friction = friction;
	}
}
