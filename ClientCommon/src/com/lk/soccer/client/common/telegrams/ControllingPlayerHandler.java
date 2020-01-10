package com.lk.soccer.client.common.telegrams;

import com.lk.engine.common.telegraph.Telegram;
import com.lk.engine.common.telegraph.TelegramHandler;
import com.lk.engine.soccer.elements.players.Player;
import com.lk.engine.soccer.elements.team.Team.TeamColor;
import com.lk.soccer.client.common.gui.CoachRender;

public class ControllingPlayerHandler implements TelegramHandler {
	private CoachRender coach;

	public ControllingPlayerHandler(CoachRender coach) {
		this.coach = coach;
	}

	@Override
	public Processed handle(Telegram telegram) {
		coach.setPassFrom(null);
		final Player<?> player = (Player<?>) telegram.getExtraInfo();
		if (player != null && player.team().color() == TeamColor.RED) {
			coach.setPassFrom(player);
		}
		return Processed.YES;
	}
}
