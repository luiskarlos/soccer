package com.lk.engine.soccer.script.instructions;

import java.util.List;

import com.lk.engine.common.fsm.State;
import com.lk.engine.common.fsm.StateMachineOwner;
import com.lk.engine.common.misc.Active;
import com.lk.engine.soccer.elements.players.Player;
import com.lk.engine.soccer.script.Enviroment;
import com.lk.engine.soccer.script.Evaluator;

public class Change extends InstructionTarget {
	public Change(final String name, final String state) {
		super(name, state.toLowerCase());
	}

	@Override
	public Active execute(final Evaluator evaluator, final Enviroment enviroment) {
		final Class<State> state = enviroment.getState(getTarget(enviroment));
		final List<Player<?>> players = enviroment.getPlayers(getElement(enviroment));

		if (players.isEmpty()) {
			StateMachineOwner owner = enviroment.getTeam(getElement(enviroment));
			if (owner != null)
				owner.getFSM().changeTo(state, evaluator, getOnExit(), null);
		} else {
			for (Player<?> p : players) {
				p.getFSM().changeTo(state, evaluator, None.NONE, null);/**/
			}
		}

		return Active.No;
	}

	@Override
	public String toString() {
		return "WalkTo " + super.toString();
	}
}
