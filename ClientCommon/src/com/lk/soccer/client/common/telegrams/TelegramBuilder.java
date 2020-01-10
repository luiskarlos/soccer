package com.lk.soccer.client.common.telegrams;

import com.lk.engine.common.telegraph.Message;
import com.lk.engine.common.telegraph.TelegramCheckin;
import com.lk.engine.common.telegraph.Telegraph;
import com.lk.soccer.client.common.gui.CoachRender;

public class TelegramBuilder implements TelegramCheckin {
	private final CoachRender coach;

	public TelegramBuilder(final CoachRender coach) {
		this.coach = coach;
	}

	@Override
	public void checkin(final Telegraph telegraph) {

		final ControllingPlayerHandler player = new ControllingPlayerHandler(coach);
		telegraph.checkin(Message.CONTROLING_PLAYER, player);

		final CoachPassInstructionHandler passHandler = new CoachPassInstructionHandler(coach);
		telegraph.checkin(Message.COACH_PASS_INSTRUCTION, passHandler);
		telegraph.checkin(Message.COACH_PASS_FULFILLED, passHandler);

	}
}
