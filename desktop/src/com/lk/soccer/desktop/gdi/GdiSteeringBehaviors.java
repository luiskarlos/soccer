package com.lk.soccer.desktop.gdi;

import static com.lk.engine.common.d2.Vector2D.add;
import static com.lk.engine.common.d2.Vector2D.mul;

import com.lk.engine.common.core.Renderable;
import com.lk.engine.soccer.elements.players.SteeringBehaviors;

public class GdiSteeringBehaviors implements Renderable {
	private final SteeringBehaviors steeringBehaviors;
	private final Cgdi gdi;

	public GdiSteeringBehaviors(Cgdi cgdi, SteeringBehaviors steeringBehaviors) {
		this.steeringBehaviors = steeringBehaviors;
		this.gdi = cgdi;
	}

	public boolean render() {
		// render the steering force
		gdi.redPen();
		gdi.line(steeringBehaviors.player().pos(),
		    add(steeringBehaviors.player().pos(), mul(steeringBehaviors.steeringForce(), 20)));
		return true;
	}
}
