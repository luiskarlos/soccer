package com.lk.engine.soccer.elements.players.fieldplayer.telegrams;

import com.lk.engine.common.fsm.StateMachine;
import com.lk.engine.common.telegraph.Telegram;
import com.lk.engine.common.telegraph.TelegramHandler;
import com.lk.engine.soccer.elements.players.Player;
import com.lk.engine.soccer.elements.players.fieldplayer.states.ReturnToHomeRegion;

public class GoHomeHandler implements TelegramHandler {
	private final StateMachine stateMachine;

	public GoHomeHandler(final StateMachine stateMachine) {
		this.stateMachine = stateMachine;
	}

	@Override
	public Processed handle(final Telegram telegram) {
		final Player<?> player = stateMachine.getOwner();
		if (player.team().controllingPlayer() != player) {
			player.setDefaultHomeRegion();
			stateMachine.changeTo(ReturnToHomeRegion.class);
			return Processed.YES;
		}
		return Processed.NO;
	}
}
