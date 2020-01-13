/**
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.soccer.elements.team.states;

import com.lk.engine.common.fsm.State;
import com.lk.engine.common.fsm.StateMachine;
import com.lk.engine.soccer.elements.team.Team;

public class Attacking implements State {
	public Attacking() {
	}

	@Override
	public void enter(final StateMachine stateMachine) {
		final Team team = stateMachine.getOwner();
		team.attack();
	}

	@Override
	public void execute(final StateMachine stateMachine, final Object data) {
		final Team team = stateMachine.getOwner();
		// if this team is no longer in control change states
		if (!team.inControl()) {
			stateMachine.changeTo(Defending.class);
			return;
		}

		// calculate the best position for any supporting ATTACKER to move to
		team.determineBestSupportingPosition();
	}

	@Override
	public void exit(final StateMachine stateMachine) {
		final Team team = stateMachine.getOwner();
		// there is no supporting player for defense
		team.setSupportingPlayer(null);
	}
}
