package com.lk.engine.common.script.instructions;

import com.lk.engine.common.core.Region;
import com.lk.engine.common.misc.Active;
import com.lk.engine.common.script.Environment;
import com.lk.engine.common.script.Evaluator;
import com.lk.engine.soccer.elements.players.Player;
import com.lk.engine.soccer.elements.players.states.Walk;

public class WalkTo extends InstructionTarget {
	public WalkTo(final String name, final String target) {
		super(name, target);
	}

	@Override
	public Active execute(final Evaluator evaluator, final Environment enviroment) {
		final Region pos = enviroment.getVariable(getTarget(enviroment)).container(Region.class);
		for (final Player<?> p : enviroment.getPlayers(getElement(enviroment))) {
			p.getFSM().changeTo(Walk.NAME, getOnExit(), pos);
		}
		return Active.No;
	}

	@Override
	public String toString() {
		return "WalkTo " +  super.toString();
	}
}
