package com.lk.soccer.desktop.gdi;

import static com.lk.engine.common.d2.Transformation.worldTransform;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.lk.engine.common.core.Renderable;
import com.lk.engine.common.d2.Vector2D;
import com.lk.engine.soccer.elements.players.goalkeeper.Goalkeeper;
import com.lk.engine.soccer.elements.team.Team.TeamColor;
import com.lk.soccer.desktop.view.Decorable;

public class GdiGoalKeeper implements Renderable, Decorable {
	private final Goalkeeper player;
	private final Cgdi gdi;
	private final Set<Decorator> decoratos = new HashSet<Decorator>();

	public GdiGoalKeeper(final Cgdi cgdi, final Goalkeeper player) {
		this.player = player;
		this.gdi = cgdi;
	}

	@Override
	public boolean render() {
		if (decoratos.contains(Decorator.HIGHLIGHT)) {
			gdi.yellowBrush();
			gdi.circle(player.pos(), 8);
		}

		if (player.team().color() == TeamColor.BLUE) {
			gdi.bluePen();
		} else {
			gdi.redPen();
		}

		final List<Vector2D> vecPlayerVBTrans = worldTransform(player.vecPlayerVB(), player.pos(), player.lookAt(), player
		    .lookAt().perp(), player.scale());
		gdi.closedShape(vecPlayerVBTrans);

		// draw the head
		gdi.brownBrush();
		gdi.circle(player.pos(), 6);

		// draw the ID
		if (player.getParams().isViewIDs()) {
			gdi.textColor(0, 170, 0);
			gdi.textAtPos(player.pos().x() - 20, player.pos().y() - 25, String.valueOf(player.Id()));
		}

		// draw the state
		if (player.getParams().isViewStates()) {
			gdi.textColor(0, 170, 0);
			gdi.transparentText();
			gdi.textAtPos(player.pos().x(), player.pos().y() - 25, new String(player.getFSM().getCurrentStateName()));
		}

		return true;
	}

	@Override
	public void add(Decorator... decorators) {
		for (final Decorator d : decorators)
			this.decoratos.add(d);
	}

	@Override
	public void remove(Decorator... decorators) {
		for (final Decorator d : decorators)
			this.decoratos.remove(d);
	}
}
