package com.lk.engine.soccer.elements.players.fieldplayer.telegrams;

import com.lk.engine.common.d2.Vector2D;
import com.lk.engine.common.fsm.StateMachine;
import com.lk.engine.common.telegraph.Telegram;
import com.lk.engine.common.telegraph.TelegramHandler;
import com.lk.engine.soccer.elements.players.Player;
import com.lk.engine.soccer.elements.players.fieldplayer.states.ReceiveBall;

public class ReceiveBallHandler implements TelegramHandler {
	private final StateMachine stateMachine;

	public ReceiveBallHandler(final StateMachine stateMachine) {
		this.stateMachine = stateMachine;
	}

	@Override
	public Processed handle(final Telegram telegram) {
		final Player<?> player = stateMachine.getOwner();
		player.steering().setTarget((Vector2D) telegram.getExtraInfo());
		stateMachine.changeTo(ReceiveBall.NAME);

		return Processed.YES;
	}
}
