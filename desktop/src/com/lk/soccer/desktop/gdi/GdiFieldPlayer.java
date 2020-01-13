package com.lk.soccer.desktop.gdi;

import static com.lk.engine.common.d2.Transformation.worldTransform;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.lk.engine.common.core.Renderable;
import com.lk.engine.common.d2.Vector2D;
import com.lk.engine.soccer.elements.players.fieldplayer.FieldPlayer;
import com.lk.engine.soccer.elements.team.Team.TeamColor;
import com.lk.soccer.desktop.view.Decorable;

public class GdiFieldPlayer implements Renderable, Decorable {
	private final FieldPlayer player;
	private final Cgdi gdi;
	private final Set<Decorator> decoratos = new HashSet<Decorator>();

	public GdiFieldPlayer(final Cgdi cgdi, final FieldPlayer player) {
		this.player = player;
		this.gdi = cgdi;
	}

	@Override
	public boolean render() {
		if (decoratos.contains(Decorator.HIGHLIGHT)) {
			gdi.yellowBrush();
			gdi.circle(player.pos(), 8);
		}

		gdi.transparentText();
		gdi.textColor(Cgdi.grey);

		// set appropriate team color
		if (player.team().color() == TeamColor.BLUE) {
			gdi.bluePen();
		} else {
			gdi.redPen();
		}

		// render the player's body
		final List<Vector2D> vecPlayerVBTrans = worldTransform(player.vecPlayerVB(), player.pos(), player.heading(),
		    player.side(), player.scale());
		gdi.closedShape(vecPlayerVBTrans);

		gdi.brownBrush();
		if (player.getParams().isHighlightIfThreatened() && player.isControllingPlayer() && player.isThreatened()) {
			gdi.yellowBrush();
		}
		gdi.circle(player.pos(), 6);

		// render the state
		if (player.getParams().isViewStates()) {
			gdi.textColor(0, 170, 0);
			gdi.textAtPos(player.pos().x, player.pos().y - 25, new String(player.getFSM().getCurrentStateName()));
		}

		// show IDs
		if (player.getParams().isViewIDs()) {
			gdi.textColor(0, 170, 0);
			gdi.textAtPos(player.pos().x - 20, player.pos().y - 25, String.valueOf(player.Id()));
		}

		if (player.getParams().isViewTargets()) {
			gdi.redBrush();
			gdi.circle(player.steering().target(), 3);
			gdi.textAtPos(player.steering().target(), String.valueOf(player.Id()));
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
