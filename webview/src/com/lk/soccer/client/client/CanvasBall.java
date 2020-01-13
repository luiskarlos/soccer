package com.lk.soccer.client.client;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.lk.engine.soccer.elements.Ball;

public class CanvasBall implements Renderable {
	private final Ball ball;
	private final CssColor black = CssColor.make("rgb(0, 0, 0)");

	public CanvasBall(Ball ball) {
		this.ball = ball;
	}

	@Override
	public boolean render(final Context2d context) {
		context.setFillStyle(black);
		context.beginPath();
		context.arc(ball.pos().x, ball.pos().y, ball.bRadius(), 0, Math.PI * 2.0, true);
		context.closePath();
		context.fill();
		return true;
	}

}
