/**
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.soccer.elements.team.states;

import com.lk.engine.common.fsm.State;
import com.lk.engine.common.fsm.StateAdapter;
import com.lk.engine.common.fsm.StateMachine;
import com.lk.engine.soccer.elements.team.Team;

public class Attacking extends StateAdapter {
	public static final String NAME = "Attacking";
	
	public Attacking() {
		super(NAME);
	}

	@Override
	public void enter(final StateMachine stateMachine) {
		final Team team = stateMachine.getOwner();
		team.attack();
	}

	@Override
	public State.Status execute(final StateMachine stateMachine, final Object data) {
		final Team team = stateMachine.getOwner();
		// if this team is no longer in control change states
		if (!team.inControl()) {
			stateMachine.changeTo(Defending.NAME);
			return State.Status.INTERRUPTIBLE;
		}

		// calculate the best position for any supporting ATTACKER to move to
		team.determineBestSupportingPosition();
		return State.Status.INTERRUPTIBLE;
	}

	@Override
	public void exit(final StateMachine stateMachine) {
		final Team team = stateMachine.getOwner();
		// there is no supporting player for defense
		team.setSupportingPlayer(null);
	}

}
