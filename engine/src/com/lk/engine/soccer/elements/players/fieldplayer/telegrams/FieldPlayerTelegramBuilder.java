package com.lk.engine.soccer.elements.players.fieldplayer.telegrams;

import com.lk.engine.common.telegraph.Message;
import com.lk.engine.common.telegraph.Telegraph;
import com.lk.engine.soccer.elements.players.fieldplayer.FieldPlayer;
import com.lk.engine.soccer.elements.players.telegrams.PlayerTelegramBuilder;

public class FieldPlayerTelegramBuilder extends PlayerTelegramBuilder {
	public FieldPlayerTelegramBuilder(final FieldPlayer player) {
		super(player);
	}

	@Override
	public void checkin(final Telegraph dispatcher) {
		super.checkin(dispatcher);
		dispatcher.register(Message.RECEIVE_BALL, new ReceiveBallHandler(getPlayer().getFSM()), getPlayer().Id());
		dispatcher.register(Message.SUPPORT_ATTACKER, new SupportAtAtackerHandler(getPlayer().getFSM()), getPlayer().Id());
		dispatcher.register(Message.PASS_TO_ME, new PassToMeHandler(getPlayer().getFSM()), getPlayer().Id());

		final GoHomeHandler goHomeHandler = new GoHomeHandler(getPlayer().getFSM());
		dispatcher.register(Message.GO_HOME, goHomeHandler, getPlayer().Id());
		dispatcher.checkin(Message.GO_HOME, goHomeHandler);

	}
}
