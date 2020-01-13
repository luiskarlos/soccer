package com.lk.engine.soccer.elements.team.telegram;

import com.lk.engine.common.fsm.StateMachine;
import com.lk.engine.common.telegraph.Telegram;
import com.lk.engine.common.telegraph.TelegramHandler;
import com.lk.engine.soccer.elements.players.Player;
import com.lk.engine.soccer.elements.team.Team;

public class ControllingPlayerHandler implements TelegramHandler {
	private final StateMachine stateMachine;

	public ControllingPlayerHandler(final StateMachine stateMachine) {
		this.stateMachine = stateMachine;
	}

	@Override
	public Processed handle(final Telegram telegram) {
		final Team team = stateMachine.getOwner();
		final Player<?> player = (Player<?>) telegram.getExtraInfo();
		if (player.team() == team)
			team.setControlling(player);

		return Processed.YES;
	}

}
