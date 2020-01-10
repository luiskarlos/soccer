/**
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.soccer.elements.referee.states;

import com.lk.engine.common.fsm.State;
import com.lk.engine.common.fsm.StateAdapter;
import com.lk.engine.common.fsm.StateMachine;
import com.lk.engine.soccer.elements.referee.Referee;

public class KickOff extends StateAdapter {
	public static final String NAME = "referee.kickOff";

	public KickOff() {
		super(NAME);
	}

	@Override
	public State.Status execute(final StateMachine stateMachine, final Object data) {
		final Referee referee = stateMachine.getOwner();
		referee.setGameOn();
		return State.Status.INTERRUPTIBLE;
	}
}