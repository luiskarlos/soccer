package com.lk.engine.common.script.instructions;

import java.util.List;

import com.lk.engine.common.fsm.StateMachineOwner;
import com.lk.engine.common.misc.Active;
import com.lk.engine.common.script.Enviroment;
import com.lk.engine.common.script.Evaluator;
import com.lk.engine.soccer.elements.players.Player;

public class Unmount extends InstructionTarget {
	public Unmount(final String name, final String state) {
		super(name, state);
	}

	@Override
	public Active execute(final Evaluator evaluator, final Enviroment enviroment) {
		final String state = getTarget(enviroment);
		final List<Player<?>> players = enviroment.getPlayers(getElement(enviroment));

		if (players.isEmpty()) {
			StateMachineOwner owner = enviroment.getTeam(getElement(enviroment));
			if (owner != null)
				owner.getFSM().changeTo(state, getOnExit(), null);
		} else {
			for (Player<?> p : players) {
				p.getFSM().changeTo(state, getOnExit(), null);/**/
			}
		}

		return Active.No;
	}

	@Override
	public String toString() {
		return "WalkTo " + super.toString();
	}
}
