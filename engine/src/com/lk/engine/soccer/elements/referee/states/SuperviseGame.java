package com.lk.engine.soccer.elements.referee.states;

import com.lk.engine.common.fsm.State;
import com.lk.engine.common.fsm.StateAdapter;
import com.lk.engine.common.fsm.StateMachine;
import com.lk.engine.soccer.elements.referee.Referee;

public class SuperviseGame extends StateAdapter {
	
	public static final String NAME = "referee.superviseGame";

	public SuperviseGame() {
		super(NAME);
	}
	
	@Override
  public void enter(StateMachine stateMachine) {
		final Referee referee = stateMachine.getOwner();
		referee.setGameOn();
  }

	@Override
	public State.Status execute(final StateMachine stateMachine, final Object data) {
		final Referee referee = stateMachine.getOwner();
    //TODO: check time
		//TODO: in the future check faults or outsides
		if (referee.isGoal()) {
			stateMachine.changeTo(PrepareForKickOff.NAME);
		}
		return State.Status.INTERRUPTIBLE;
	}

	@Override
  public void exit(StateMachine stateMachine) {
		final Referee referee = stateMachine.getOwner();
		referee.setGameOff();
  }

}