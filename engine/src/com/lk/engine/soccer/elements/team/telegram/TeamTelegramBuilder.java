package com.lk.engine.soccer.elements.team.telegram;

import com.lk.engine.common.telegraph.Message;
import com.lk.engine.common.telegraph.TelegramCheckin;
import com.lk.engine.common.telegraph.Telegraph;
import com.lk.engine.soccer.elements.team.Team;

public class TeamTelegramBuilder implements TelegramCheckin {
	private final Team team;

	public TeamTelegramBuilder(final Team team) {
		this.team = team;
	}

	@Override
	public void checkin(final Telegraph dispatcher) {
		dispatcher.checkin(Message.NEW_PLAYER, new NewPlayerHandler(team.getFSM()));
		dispatcher.checkin(Message.CONTROLLING_PLAYER, new ControllingPlayerHandler(team.getFSM()));
	}
}
