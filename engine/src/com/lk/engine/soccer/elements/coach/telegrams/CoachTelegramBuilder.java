package com.lk.engine.soccer.elements.coach.telegrams;

import com.lk.engine.common.telegraph.Message;
import com.lk.engine.common.telegraph.TelegramCheckin;
import com.lk.engine.common.telegraph.Telegraph;
import com.lk.engine.soccer.elements.coach.Coach;

public class CoachTelegramBuilder implements TelegramCheckin {
	private final Coach coach;

	public CoachTelegramBuilder(final Coach coach) {
		this.coach = coach;
	}

	@Override
	public void checkin(final Telegraph dispatcher) {
		dispatcher.checkin(Message.COACH_PASS_INSTRUCTION, new CoachPassInstructionHandler(coach.getFSM()));
	}
}
