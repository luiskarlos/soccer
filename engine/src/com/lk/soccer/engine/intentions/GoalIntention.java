package com.lk.soccer.engine.intentions;

import com.lk.engine.soccer.elements.players.Player;

public class GoalIntention {
	public GoalIntention() {
	}

	public Intention process(final Player<?> player, final PlayerKnowledge knowledge) {
		if (knowledge.canKickTheBall()) {

		}

		return new Intention();
	}

}
