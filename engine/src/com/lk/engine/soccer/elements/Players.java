package com.lk.engine.soccer.elements;

import java.util.Collection;

import com.lk.engine.soccer.elements.players.Player;

public interface Players {
	Collection<Player<?>> getPlayers();

	Player<?> getPlayer(final String name);
}
