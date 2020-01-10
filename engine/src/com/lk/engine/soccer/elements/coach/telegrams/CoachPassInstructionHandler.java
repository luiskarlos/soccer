package com.lk.engine.soccer.elements.coach.telegrams;

import com.lk.engine.common.fsm.StateMachine;
import com.lk.engine.common.telegraph.Telegram;
import com.lk.engine.common.telegraph.TelegramHandler;
import com.lk.engine.soccer.elements.coach.Coach;
import com.lk.engine.soccer.elements.coach.message.PassInstruction;

public class CoachPassInstructionHandler implements TelegramHandler {
	private final StateMachine stateMachine;

	public CoachPassInstructionHandler(final StateMachine stateMachine) {
		this.stateMachine = stateMachine;
	}

	@Override
	public Processed handle(final Telegram telegram) {
		final Coach coach = stateMachine.getOwner();
		final PassInstruction passInstruction = (PassInstruction) telegram.getExtraInfo();

		if (coach.getTeamColor().name().equalsIgnoreCase(passInstruction.getTeamName()))
			coach.setPassIndication(passInstruction.getPos());

		return Processed.YES;
	}
}
