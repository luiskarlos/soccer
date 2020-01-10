package com.lk.engine.soccer.elements.players.telegrams;

import com.lk.engine.common.telegraph.TelegramCheckin;
import com.lk.engine.common.telegraph.Telegraph;
import com.lk.engine.soccer.elements.players.Player;

public abstract class PlayerTelegramBuilder implements TelegramCheckin {
	private final Player<?> player;

	public PlayerTelegramBuilder(Player<?> player) {
		this.player = player;
	}

	protected Player<?> getPlayer() {
		return player;
	}

	@Override
	public void checkin(final Telegraph dispatcher) {

	}
}
