package com.lk.soccer.desktop.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lk.engine.common.core.Renderable;
import com.lk.engine.soccer.GameBuilderListener;
import com.lk.engine.soccer.elements.Ball;
import com.lk.engine.soccer.elements.PlayRegions;
import com.lk.engine.soccer.elements.players.fieldplayer.FieldPlayer;
import com.lk.engine.soccer.elements.players.goalkeeper.Goalkeeper;
import com.lk.engine.soccer.elements.referee.Referee;
import com.lk.engine.soccer.elements.team.Team;
import com.lk.soccer.desktop.gdi.Cgdi;
import com.lk.soccer.desktop.gdi.GdiBall;
import com.lk.soccer.desktop.gdi.GdiCoach;
import com.lk.soccer.desktop.gdi.GdiFieldPlayer;
import com.lk.soccer.desktop.gdi.GdiGoalKeeper;
import com.lk.soccer.desktop.gdi.GdiPlayRegions;
import com.lk.soccer.desktop.gdi.GdiReferee;
import com.lk.soccer.desktop.gdi.GdiTeam;

public class CgiRender {
	private final Cgdi cgdi = new Cgdi();
	private final GdiCoach gdiCoach = new GdiCoach(cgdi);
	private final Map<Object, Renderable> objtToRenderable = new HashMap<>();

	private final List<Renderable> renderables = new ArrayList<>();
	private final List<Renderable> toRender = new ArrayList<>();

	public CgiRender() {
		renderables.add(gdiCoach);
	}

	public Cgdi getGdi() {
		return cgdi;
	}

	public GdiCoach getGdiCoach() {
		return gdiCoach;
	}

	public void render() {
		toRender.clear();
		toRender.addAll(renderables);
		for (final Renderable u : toRender) {
			u.render();
		}
	}

	public <T> T getGidObject(Class<T> t, Object owner) {
		return t.cast(objtToRenderable.get(owner));
	}

	public void clear() {
		renderables.clear();
	}

	private void add(final Object key, final Renderable renderable) {
		renderables.add(renderable);
		objtToRenderable.put(key, renderable);
	}

	public GameBuilderListener getListener() {
		return new GameBuilderListener() {
			@Override
			public void onCreated(Ball ball) {
				add(ball, new GdiBall(cgdi, ball));
			}

			@Override
			public void onCreated(FieldPlayer fieldPlayer) {
				add(fieldPlayer, new GdiFieldPlayer(cgdi, fieldPlayer));
			}

			@Override
			public void onCreated(Goalkeeper goalkeeper) {
				add(goalkeeper, new GdiGoalKeeper(cgdi, goalkeeper));
			}

			@Override
			public void onCreated(Team team) {
				add(team, new GdiTeam(cgdi, team));
			}

			@Override
			public void onCreated(Referee soccerPitch) {
				renderables.add(0, new GdiReferee(cgdi, soccerPitch));
			}

			@Override
			public void onCreated(PlayRegions playRegions) {
				renderables.add(new GdiPlayRegions(cgdi, playRegions));
			}
		};
	}

}
