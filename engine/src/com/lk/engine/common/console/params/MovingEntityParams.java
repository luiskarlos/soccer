package com.lk.engine.common.console.params;

public class MovingEntityParams {
	private double mass;
	private double radius;
	private double maxForce;
	private double maxTurnRate;
	private double maxSpeed;

	public double getMass() {
		return mass;
	}

	public void setMass(final double mass) {
		this.mass = mass;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(final double radius) {
		this.radius = radius;
	}

	public double getMaxTurnRate() {
		return maxTurnRate;
	}

	public void setMaxTurnRate(final double maxTurnRate) {
		this.maxTurnRate = maxTurnRate;
	}

	public double getMaxForce() {
		return maxForce;
	}

	public void setMaxForce(final double maxForce) {
		this.maxForce = maxForce;
	}

	public double getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(final double maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

}
