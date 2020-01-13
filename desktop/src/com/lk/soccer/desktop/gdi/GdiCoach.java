package com.lk.soccer.desktop.gdi;

import com.lk.engine.common.core.Renderable;
import com.lk.engine.common.d2.Vector2D;
import com.lk.engine.soccer.elements.players.Player;
import com.lk.soccer.client.common.gui.CoachRender;

public class GdiCoach implements Renderable, CoachRender {
	private final Cgdi gdi;
	private Vector2D passMark = null;
	private Player<?> player = null;

	public GdiCoach(Cgdi cgdi) {
		this.gdi = cgdi;
	}

	@Override
	public boolean render() {
		if (passMark != null) {
			gdi.orangeBrush();
			gdi.circle(passMark, 3);

			if (player != null) {
				final Vector2D origin = player.pos();
				gdi.line(origin.x, origin.y, passMark.x, passMark.y);
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
