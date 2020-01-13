package com.lk.engine.soccer.console.params;

public class GoalkeeperParams extends PlayerParams {
	private double interceptRange = 100.0;
	private double tendingDistance = 20.0;

	public GoalkeeperParams() {
		setMinPassDistance(50.0);
	}

	public double getInterceptRange() {
		return interceptRange;
	}

	public void setInterceptRange(final double interceptRange) {
		this.interceptRange = interceptRange;
	}

	public double getInterceptRangeSq() {
		return interceptRange * interceptRange;
	}

	public double getTendingDistance() {
		return tendingDistance;
	}

	public void setTendingDistance(final double tendingDistance) {
		this.tendingDistance = tendingDistance;
	}
}
