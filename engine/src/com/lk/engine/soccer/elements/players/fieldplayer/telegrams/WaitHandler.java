package com.lk.engine.soccer.elements.players.fieldplayer.telegrams;

import com.lk.engine.common.fsm.StateMachine;
import com.lk.engine.common.telegraph.Telegram;
import com.lk.engine.common.telegraph.TelegramHandler;
import com.lk.engine.soccer.elements.players.fieldplayer.states.Wait;

public class WaitHandler implements TelegramHandler {
	private final StateMachine stateMachine;

	public WaitHandler(final StateMachine stateMachine) {
		this.stateMachine = stateMachine;
	}

	@Override
	public Processed handle(final Telegram telegram) {
		stateMachine.changeTo(Wait.class);
		return Processed.YES;
	}
}
