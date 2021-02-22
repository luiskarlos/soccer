package com.lk.engine.common.debug;

public interface OldConsole {
	OldConsole print(final Object T);

	OldConsole print(final Object... T);

	boolean isShowInfoEnabled();

	boolean isPlayerStateInfoOn();

	boolean isDebugTeamStatesOn();

	boolean isGoalyStateInfoOn();

	void debugOn();

	void debugOff();
}