package com.lk.engine.soccer.script.instructions;

import com.lk.engine.common.core.Region;
import com.lk.engine.common.fsm.State;
import com.lk.engine.common.misc.Active;
import com.lk.engine.soccer.elements.players.Player;
import com.lk.engine.soccer.script.Enviroment;
import com.lk.engine.soccer.script.Evaluator;

public class WalkTo extends InstructionTarget {
	public WalkTo(final String name, final String target) {
		super(name, target);
	}

	@Override
	public Active execute(final Evaluator evaluator, final Enviroment enviroment) {
		final Class<State> state = enviroment.getState("walk");
		final Region pos = enviroment.getVariable(getTarget(enviroment)).container(Region.class);
		for (final Player<?> p : enviroment.getPlayers(getElement(enviroment))) {
			p.getFSM().changeTo(state, evaluator, getOnExit(), pos);
		}
		return Active.No;
	}

	@Override
	public String toString() {
		return "WalkTo " +  super.toString();
	}
}
