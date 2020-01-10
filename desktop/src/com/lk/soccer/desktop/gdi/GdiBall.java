package com.lk.soccer.desktop.gdi;

import com.lk.engine.common.core.Renderable;
import com.lk.engine.soccer.elements.Ball;

public class GdiBall implements Renderable {
	private final Ball ball;
	private final Cgdi gdi;

	public GdiBall(Cgdi cgdi, Ball ball) {
		this.ball = ball;
		this.gdi = cgdi;
	}

	public boolean render() {
		gdi.blackBrush();
		gdi.circle(ball.pos(), ball.bRadius());
		return true;
	}
}
