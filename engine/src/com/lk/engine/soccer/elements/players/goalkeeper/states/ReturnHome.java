/**
 * 
//------------------------- ReturnHome: ----------------------------------
//
//  In this state the goalkeeper simply returns back to the center of
//  the goal region before changing state back to TendGoal
//------------------------------------------------------------------------
 * 
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.soccer.elements.players.goalkeeper.states;

import com.lk.engine.common.fsm.State;
import com.lk.engine.common.fsm.StateAdapter;
import com.lk.engine.common.fsm.StateMachine;
import com.lk.engine.soccer.elements.players.Player;

public class ReturnHome extends StateAdapter {
	public static final String NAME = "ReturnHome";
	
	public ReturnHome() {
		super(NAME);
	}

	@Override
	public void enter(final StateMachine stateMachine) {
		final Player<?> player = stateMachine.getOwner();
		player.steering().arriveOn();
	}

	@Override
	public State.Status execute(final StateMachine stateMachine, final Object data) {
		final Player<?> player = stateMachine.getOwner();
		player.steering().setTarget(player.homeRegion().center());

		// if close enough to home or the opponents get control over the ball,
		// change state to tend goal
		if (player.inHomeRegion() || !player.team().inControl()) {
			stateMachine.changeTo(TendGoal.NAME);
		}
		return State.Status.INTERRUPTIBLE;
	}

	@Override
	public void exit(final StateMachine stateMachine) {
		final Player<?> player = stateMachine.getOwner();
		player.steering().arriveOff();
	}
}