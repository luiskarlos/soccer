package com.lk.engine.soccer.elements.players.fieldplayer.telegrams;

import com.lk.engine.common.fsm.StateMachine;
import com.lk.engine.common.telegraph.Telegram;
import com.lk.engine.common.telegraph.TelegramHandler;
import com.lk.engine.soccer.elements.players.Player;
import com.lk.engine.soccer.elements.players.fieldplayer.states.SupportAttacker;

public class SupportAtAtackerHandler implements TelegramHandler {
	private final StateMachine stateMachine;

	public SupportAtAtackerHandler(final StateMachine stateMachine) {
		this.stateMachine = stateMachine;
	}

	@Override
	public Processed handle(final Telegram telegram) {
		final Player<?> player = stateMachine.getOwner();
		// if already supporting just return
		if (stateMachine.isInState(SupportAttacker.NAME)) {
			return Processed.NO;
		}

		// set the target to be the best supporting position
		player.steering().setTarget(player.team().getSupportSpot());
		stateMachine.changeTo(SupportAttacker.NAME);
		return Processed.YES;
	}
}
