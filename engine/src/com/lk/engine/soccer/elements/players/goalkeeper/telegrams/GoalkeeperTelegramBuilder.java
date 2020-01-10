package com.lk.engine.soccer.elements.players.goalkeeper.telegrams;

import com.lk.engine.common.telegraph.Telegraph;
import com.lk.engine.soccer.elements.players.goalkeeper.Goalkeeper;
import com.lk.engine.soccer.elements.players.telegrams.PlayerTelegramBuilder;

public class GoalkeeperTelegramBuilder extends PlayerTelegramBuilder {
	public GoalkeeperTelegramBuilder(final Goalkeeper player) {
		super(player);
	}

	@Override
	public void checkin(final Telegraph dispatcher) {
		super.checkin(dispatcher);
	}
}
