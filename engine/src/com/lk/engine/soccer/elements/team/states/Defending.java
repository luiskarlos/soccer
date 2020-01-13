/**
 */
package com.lk.engine.soccer.elements.team.states;

import com.lk.engine.common.fsm.State;
import com.lk.engine.common.fsm.StateMachine;
import com.lk.engine.soccer.elements.team.Team;

public class Defending implements State {
	public Defending() {
	}

	@Override
	public void enter(final StateMachine stateMachine) {
		final Team team = stateMachine.getOwner();
		team.defend();
	}

	@Override
	public void execute(final StateMachine stateMachine, final Object data) {
		final Team team = stateMachine.getOwner();
		// if in control change states
		if (team.inControl()) {
			stateMachine.changeTo(Attacking.class);
			return;
		}
	}

	@Override
	public void exit(final StateMachine stateMachine) {
	}
}
