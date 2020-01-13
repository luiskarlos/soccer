package com.lk.soccer.client.client;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.lk.engine.common.d2.Vector2D;
import com.lk.engine.soccer.elements.players.Player;
import com.lk.soccer.client.common.gui.CoachRender;

public class CanvasCoach implements Renderable, CoachRender {
	private final CssColor color = CssColor.make("rgb(250, 0, 0)");
	private Vector2D passMark;
	private Player<?> player;

	@Override
	public boolean render(final Context2d context) {
		if (passMark != null) {
			context.setFillStyle(color);
			context.beginPath();
			context.arc(passMark.x, passMark.y, 10, 0, Math.PI * 2.0, true);
			context.closePath();
			context.fill();

			if (player != null) {
				final Vector2D origin = player.pos();
				context.beginPath();
				context.moveTo(origin.x, origin.y);
				context.lineTo(passMark.x, passMark.y);
				context.setStrokeStyle(CssColor.make(255, 0, 0));
				context.stroke();
			}
		}
		return true;
	}

	public void setPassMark(final Vector2D passMark) {
		this.passMark = passMark;
	}

	public void setPassFrom(final Player<?> player) {
		this.player = player;
	}
}
