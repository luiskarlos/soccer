package com.lk.engine.common.console;

import com.lk.engine.common.console.params.BallParams;
import com.lk.engine.common.console.params.FieldPlayerParams;
import com.lk.engine.common.console.params.GoalParams;
import com.lk.engine.common.console.params.GoalkeeperParams;
import com.lk.engine.common.console.params.SoccerPitchParams;
import com.lk.engine.common.console.params.SystemParams;
import com.lk.engine.common.console.params.TeamParams;

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
