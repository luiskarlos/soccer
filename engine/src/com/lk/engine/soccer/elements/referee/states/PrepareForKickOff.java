/**
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.soccer.elements.referee.states;

import com.google.gwt.core.client.GWT;
import com.lk.engine.common.fsm.State;
import com.lk.engine.common.fsm.StateAdapter;
import com.lk.engine.common.fsm.StateMachine;
import com.lk.engine.soccer.elements.referee.Referee;

public class PrepareForKickOff extends StateAdapter {
	public static final String NAME = "referee.prepareForKickOff";

	public PrepareForKickOff() {
		super(NAME);
	}

	@Override
	public void enter(StateMachine stateMachine) {
		final Referee referee = stateMachine.getOwner();
    GWT.debugger();
		referee.setGameOff();
		referee.prepareForKickOff();
	}
	
	@Override
	public State.Status execute(final StateMachine stateMachine, final Object data) {
		final Referee referee = stateMachine.getOwner();
		if (referee.areTeamsReadyForKickoff()) {
			stateMachine.changeTo(SuperviseGame.NAME);
		}
		return State.Status.INTERRUPTIBLE;
	}

}