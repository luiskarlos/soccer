package com.lk.engine.common.fsm;

import com.lk.engine.soccer.injector.States;
import com.lk.engine.soccer.script.Evaluator;
import com.lk.engine.soccer.script.Executable;

public class StateMachineSpy extends StateMachine {

	public StateMachineSpy(final StateMachineOwner owner, final States states) {
	  super(owner, states);
  }

	@Override
  public void changeTo(Class<? extends State> newState) {
	  super.changeTo(newState);
  }

	@Override
  public void changeTo(Class<? extends State> newState, Evaluator evaluator, Executable onExit, Object data) {
	  super.changeTo(newState, evaluator, onExit, data);
  }
}
