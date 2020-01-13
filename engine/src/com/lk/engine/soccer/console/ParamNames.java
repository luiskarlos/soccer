package com.lk.engine.soccer.console;

import com.lk.engine.soccer.console.params.BallParams;
import com.lk.engine.soccer.console.params.FieldPlayerParams;
import com.lk.engine.soccer.console.params.GoalParams;
import com.lk.engine.soccer.console.params.GoalkeeperParams;
import com.lk.engine.soccer.console.params.SoccerPitchParams;
import com.lk.engine.soccer.console.params.SystemParams;
import com.lk.engine.soccer.console.params.TeamParams;

public enum ParamNames {
	PLAYER(FieldPlayerParams.class), BLUE_PLAYER(FieldPlayerParams.class), RED_PLAYER(FieldPlayerParams.class), GOALKEEPER(
	    GoalkeeperParams.class), BLUE_GOALKEEPER(GoalkeeperParams.class), RED_GOALKEEPER(GoalkeeperParams.class), SYSTEM(
	    SystemParams.class), SOCCERPITCH(SoccerPitchParams.class), TEAM(TeamParams.class), GOAL(GoalParams.class), BALL(
	    BallParams.class);

	private Class<?> clazz;

	private ParamNames(Class<?> clazz) {
		this.clazz = clazz;
	}

	public String id() {
		return name().toLowerCase().replace('_', '.');
	}

	public Class<?> getClazz() {
		return clazz;
	}
}
